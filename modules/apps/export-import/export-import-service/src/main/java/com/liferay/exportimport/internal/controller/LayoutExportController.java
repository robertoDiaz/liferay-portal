/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.controller;

import com.liferay.asset.link.model.adapter.StagedAssetLink;
import com.liferay.exportimport.constants.ExportImportConstants;
import com.liferay.exportimport.controller.PortletExportController;
import com.liferay.exportimport.internal.lar.PermissionExporter;
import com.liferay.exportimport.kernel.controller.ExportController;
import com.liferay.exportimport.kernel.controller.ExportImportController;
import com.liferay.exportimport.kernel.lar.ExportImportDateUtil;
import com.liferay.exportimport.kernel.lar.ExportImportHelper;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.PortletDataContextFactory;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleManager;
import com.liferay.exportimport.kernel.lifecycle.constants.ExportImportLifecycleConstants;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.lar.DeletionSystemEventExporter;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalService;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskThreadLocal;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.model.LayoutSetPrototype;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutPrototypeLocalService;
import com.liferay.portal.kernel.service.LayoutSetLocalService;
import com.liferay.portal.kernel.service.LayoutSetPrototypeLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.DateRange;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.zip.ZipWriter;
import com.liferay.portal.model.adapter.util.ModelAdapterUtil;
import com.liferay.segments.model.SegmentsExperience;
import com.liferay.site.model.adapter.StagedGroup;

import java.io.File;
import java.io.Serializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang.time.StopWatch;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Joel Kozikowski
 * @author Charles May
 * @author Raymond Augé
 * @author Jorge Ferrer
 * @author Bruno Farache
 * @author Karthik Sudarshan
 * @author Zsigmond Rab
 * @author Douglas Wong
 * @author Máté Thurzó
 */
@Component(
	property = "model.class.name=com.liferay.portal.kernel.model.Layout",
	service = ExportImportController.class
)
public class LayoutExportController implements ExportController {

	@Override
	public File export(ExportImportConfiguration exportImportConfiguration)
		throws Exception {

		PortletDataContext portletDataContext = null;

		try {
			ExportImportThreadLocal.setLayoutExportInProcess(true);

			portletDataContext = getPortletDataContext(
				exportImportConfiguration);

			_exportImportLifecycleManager.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.EVENT_LAYOUT_EXPORT_STARTED,
				getProcessFlag(),
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				_portletDataContextFactory.clonePortletDataContext(
					portletDataContext));

			File file = doExport(portletDataContext);

			ExportImportThreadLocal.setLayoutExportInProcess(false);

			_exportImportLifecycleManager.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.EVENT_LAYOUT_EXPORT_SUCCEEDED,
				getProcessFlag(),
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				_portletDataContextFactory.clonePortletDataContext(
					portletDataContext));

			return file;
		}
		catch (Throwable throwable) {
			ExportImportThreadLocal.setLayoutExportInProcess(false);

			_exportImportLifecycleManager.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.EVENT_LAYOUT_EXPORT_FAILED,
				getProcessFlag(),
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				_portletDataContextFactory.clonePortletDataContext(
					portletDataContext),
				throwable);

			throw throwable;
		}
	}

	protected File doExport(PortletDataContext portletDataContext)
		throws Exception {

		_exportSite(portletDataContext);

		_exportMultiSites(portletDataContext);

		ZipWriter zipWriter = portletDataContext.getZipWriter();

		return zipWriter.getFile();
	}

	protected PortletDataContext getPortletDataContext(
			ExportImportConfiguration exportImportConfiguration)
		throws Exception {

		Map<String, Serializable> settingsMap =
			exportImportConfiguration.getSettingsMap();

		long sourceGroupId = MapUtil.getLong(settingsMap, "sourceGroupId");

		Group group = _groupLocalService.getGroup(sourceGroupId);

		Map<String, String[]> parameterMap =
			(Map<String, String[]>)settingsMap.get("parameterMap");
		DateRange dateRange = ExportImportDateUtil.getDateRange(
			exportImportConfiguration);
		ZipWriter zipWriter = _exportImportHelper.getLayoutSetZipWriter(
			sourceGroupId);

		PortletDataContext portletDataContext =
			_portletDataContextFactory.createExportPortletDataContext(
				group.getCompanyId(), sourceGroupId, parameterMap,
				dateRange.getStartDate(), dateRange.getEndDate(), zipWriter);

		boolean privateLayout = MapUtil.getBoolean(
			settingsMap, "privateLayout");
		long[] layoutIds = GetterUtil.getLongValues(
			settingsMap.get("layoutIds"));

		String cmd = MapUtil.getString(parameterMap, Constants.CMD);

		if (ArrayUtil.contains(layoutIds, 0) &&
			!Objects.equals(cmd, Constants.EXPORT) &&
			!Objects.equals(cmd, Constants.PUBLISH_TO_LIVE) &&
			!Objects.equals(cmd, Constants.PUBLISH_TO_REMOTE)) {

			layoutIds = _exportImportHelper.getAllLayoutIds(
				sourceGroupId, privateLayout);
		}

		portletDataContext.setExportImportProcessId(
			String.valueOf(
				exportImportConfiguration.getExportImportConfigurationId()));
		portletDataContext.setPrivateLayout(privateLayout);
		portletDataContext.setLayoutIds(layoutIds);

		return portletDataContext;
	}

	protected int getProcessFlag() {
		if (ExportImportThreadLocal.isLayoutStagingInProcess()) {
			return ExportImportLifecycleConstants.
				PROCESS_FLAG_LAYOUT_STAGING_IN_PROCESS;
		}

		return ExportImportLifecycleConstants.
			PROCESS_FLAG_LAYOUT_EXPORT_IN_PROCESS;
	}

	private void _exportMultiSites(PortletDataContext portletDataContext)
		throws Exception {

		if (!FeatureFlagManagerUtil.isEnabled(
				portletDataContext.getCompanyId(), "LPD-35914")) {

			return;
		}

		Map<String, String[]> parameterMap =
			portletDataContext.getParameterMap();

		String multiSitesGroupIds = MapUtil.getString(
			parameterMap, "multiSitesGroupIds");

		if (Validator.isNull(multiSitesGroupIds)) {
			return;
		}

		Map<String, String[]> originalParameterMap = new HashMap<>(
			parameterMap);

		long[] groupIds = GetterUtil.getLongValues(
			ArrayUtil.toStringArray(
				ListUtil.fromString(multiSitesGroupIds, ",")));

		long originalGroupId = portletDataContext.getGroupId();
		long originalScopeGroupId = portletDataContext.getScopeGroupId();
		long originalSourceGroupId = portletDataContext.getSourceGroupId();

		Element originalExportDataRootElement = portletDataContext.getExportDataRootElement();

		try {
			for (long groupId : groupIds) {
				portletDataContext.setGroupId(groupId);
				portletDataContext.setScopeGroupId(groupId);
				portletDataContext.setSourceGroupId(groupId);

				portletDataContext.setLayoutIds(
					_exportImportHelper.getAllLayoutIds(groupId, false));

				Set<String> primaryKeys = portletDataContext.getPrimaryKeys();

				primaryKeys.clear();

				Map<String, Map<?, ?>> newPrimaryKeysMaps =
					portletDataContext.getNewPrimaryKeysMaps();

				newPrimaryKeysMaps.clear();

				portletDataContext.setPortletId(null);
				portletDataContext.setPlid(0);
				portletDataContext.setPrivateLayout(false);

				parameterMap.clear();

				parameterMap.putAll(originalParameterMap);

				parameterMap.put(
					"COMMENTS", new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"DELETIONS", new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"LAYOUT_SET_PROTOTYPE_SETTINGS",
					new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"LAYOUT_SET_SETTINGS",
					new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"LOGO", new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"PERMISSIONS", new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"PORTLET_ARCHIVED_SETUPS_ALL",
					new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"PORTLET_CONFIGURATION_ALL",
					new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"PORTLET_DATA", new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"PORTLET_DATA_CONTROL_DEFAULT",
					new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"PORTLET_SETUP_ALL",
					new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"PORTLET_USER_PREFERENCES_ALL",
					new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"RATINGS", new String[] {Boolean.TRUE.toString()});
				parameterMap.put(
					"THEME_REFERENCE", new String[] {Boolean.TRUE.toString()});

				parameterMap.put(
					"currentMultiSitesGroupId", new String[] {String.valueOf(groupId)});

				List<Portlet> exportablePortlets =
					_exportImportHelper.getExportablePortlets(
						portletDataContext.getCompanyId(), false, groupId);

				for (Portlet portlet : exportablePortlets) {
					parameterMap.put(
						"PORTLET_DATA_" + portlet.getPortletId(),
						new String[] {Boolean.TRUE.toString()});
				}

				portletDataContext.setExportDataRootElement(null);

				_exportSite(portletDataContext);
			}
		}
		finally {
			portletDataContext.setGroupId(originalGroupId);
			portletDataContext.setScopeGroupId(originalScopeGroupId);
			portletDataContext.setSourceGroupId(originalSourceGroupId);

			portletDataContext.setExportDataRootElement(originalExportDataRootElement);

			parameterMap.clear();

			parameterMap.putAll(originalParameterMap);
		}
	}

	private void _exportSite(PortletDataContext portletDataContext)
		throws Exception {

		Map<String, String[]> parameterMap =
			portletDataContext.getParameterMap();

		boolean ignoreLastPublishDate = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.IGNORE_LAST_PUBLISH_DATE);

		boolean permissions = MapUtil.getBoolean(
			parameterMap, PortletDataHandlerKeys.PERMISSIONS);

		if (_log.isDebugEnabled()) {
			_log.debug("Export permissions " + permissions);
		}

		long companyId = portletDataContext.getCompanyId();

		long guestUserId = _userLocalService.getGuestUserId(companyId);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.popServiceContext();

		if (serviceContext == null) {
			serviceContext = new ServiceContext();
		}

		serviceContext.setCompanyId(companyId);
		serviceContext.setSignedIn(true);

		if (BackgroundTaskThreadLocal.hasBackgroundTask()) {
			BackgroundTask backgroundTask =
				_backgroundTaskLocalService.getBackgroundTask(
					BackgroundTaskThreadLocal.getBackgroundTaskId());

			serviceContext.setUserId(backgroundTask.getUserId());
		}
		else {
			serviceContext.setUserId(guestUserId);
		}

		serviceContext.setAttribute("exporting", Boolean.TRUE);

		long layoutSetBranchId = MapUtil.getLong(
			parameterMap, "layoutSetBranchId");

		serviceContext.setAttribute("layoutSetBranchId", layoutSetBranchId);

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		if (ignoreLastPublishDate) {
			portletDataContext.setEndDate(null);
			portletDataContext.setStartDate(null);
		}

		StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("root");

		portletDataContext.setExportDataRootElement(rootElement);

		Element headerElement = rootElement.addElement("header");

		headerElement.addAttribute(
			"available-locales",
			StringUtil.merge(
				_language.getAvailableLocales(
					portletDataContext.getScopeGroupId())));

		headerElement.addAttribute(
			"build-number", String.valueOf(ReleaseInfo.getBuildNumber()));

		if (_isCurrentMultiSite(portletDataContext)) {
			Group group = _groupLocalService.getGroup(portletDataContext.getGroupId());

			headerElement.addAttribute(
				"site-external-reference-code", group.getExternalReferenceCode());
		}

		headerElement.addAttribute(
			"schema-version",
			ExportImportConstants.EXPORT_IMPORT_SCHEMA_VERSION);

		headerElement.addAttribute("export-date", Time.getRFC822());

		if (portletDataContext.hasDateRange()) {
			headerElement.addAttribute(
				"start-date",
				String.valueOf(portletDataContext.getStartDate()));
			headerElement.addAttribute(
				"end-date", String.valueOf(portletDataContext.getEndDate()));
		}

		headerElement.addAttribute(
			"company-id", String.valueOf(portletDataContext.getCompanyId()));
		headerElement.addAttribute(
			"company-group-id",
			String.valueOf(portletDataContext.getCompanyGroupId()));

		Group group = _groupLocalService.fetchGroup(
			portletDataContext.getGroupId());

		headerElement.addAttribute(
			"group-friendly-url", group.getFriendlyURL());

		headerElement.addAttribute(
			"group-id", String.valueOf(portletDataContext.getGroupId()));
		headerElement.addAttribute(
			"user-personal-site-group-id",
			String.valueOf(portletDataContext.getUserPersonalSiteGroupId()));
		headerElement.addAttribute(
			"private-layout",
			String.valueOf(portletDataContext.isPrivateLayout()));

		String type = "layout-set";

		if (group.isLayoutPrototype()) {
			type = "layout-prototype";

			LayoutPrototype layoutPrototype =
				_layoutPrototypeLocalService.getLayoutPrototype(
					group.getClassPK());

			headerElement.addAttribute("type-uuid", layoutPrototype.getUuid());
		}
		else if (group.isLayoutSetPrototype()) {
			type = "layout-set-prototype";

			LayoutSetPrototype layoutSetPrototype =
				_layoutSetPrototypeLocalService.getLayoutSetPrototype(
					group.getClassPK());

			headerElement.addAttribute(
				"type-uuid", layoutSetPrototype.getUuid());
		}

		headerElement.addAttribute("type", type);

		if (_isMultiSitesExport(portletDataContext) && !_isCurrentMultiSite(portletDataContext)) {
			Element multiSitesElement = headerElement.addElement("multi-sites");

			long[] multiSitesGroupIds = _getMultiSitesGroupIds(portletDataContext);

			for (long multiSitesGroupId : multiSitesGroupIds) {
				Element multiSiteElement = multiSitesElement.addElement("multi-site");

				multiSiteElement.addAttribute("group-id", String.valueOf(multiSitesGroupId));

				Group multiSiteGroup = _groupLocalService.fetchGroup(multiSitesGroupId);

				if (multiSiteGroup != null) {
					multiSiteElement.addAttribute("name", multiSiteGroup.getNameCurrentValue());
					multiSiteElement.addAttribute("friendly-url", multiSiteGroup.getFriendlyURL());
					multiSiteElement.addAttribute("external-reference-code", multiSiteGroup.getExternalReferenceCode());
					multiSiteElement.addAttribute("uuid", multiSiteGroup.getUuid());
				}
			}
		}

		portletDataContext.setType(type);
		portletDataContext.setMissingReferencesElement(
			rootElement.addElement("missing-references"));

		rootElement.addElement("site-portlets");
		rootElement.addElement("site-services");

		// Export the group

		LayoutSet layoutSet = _layoutSetLocalService.getLayoutSet(
			portletDataContext.getGroupId(),
			portletDataContext.isPrivateLayout());

		String layoutSetPrototypeUuid = layoutSet.getLayoutSetPrototypeUuid();

		boolean layoutSetPrototypeSettings = MapUtil.getBoolean(
			portletDataContext.getParameterMap(),
			PortletDataHandlerKeys.LAYOUT_SET_PROTOTYPE_SETTINGS);

		if (Validator.isNotNull(layoutSetPrototypeUuid) &&
			layoutSetPrototypeSettings) {

			LayoutSetPrototype layoutSetPrototype =
				_layoutSetPrototypeLocalService.
					getLayoutSetPrototypeByUuidAndCompanyId(
						layoutSetPrototypeUuid, companyId);

			headerElement.addAttribute(
				"layout-set-prototype-uuid", layoutSetPrototypeUuid);

			headerElement.addAttribute(
				"layout-set-prototype-name",
				layoutSetPrototype.getName(LocaleUtil.getDefault()));
		}

		StagedGroup stagedGroup = ModelAdapterUtil.adapt(
			group, Group.class, StagedGroup.class);

		StagedModelDataHandlerUtil.exportStagedModel(
			portletDataContext, stagedGroup);

		// Export other models

		_portletExportController.exportAssetLinks(portletDataContext);
		_portletExportController.exportLocks(portletDataContext);

		if (Objects.equals(portletDataContext.getType(), "layout-set")) {
			portletDataContext.addDeletionSystemEventStagedModelTypes(
				new StagedModelType(SegmentsExperience.class, Layout.class));
			portletDataContext.addDeletionSystemEventStagedModelTypes(
				new StagedModelType(StagedAssetLink.class));
		}

		_deletionSystemEventExporter.exportDeletionSystemEvents(
			portletDataContext);

		if (permissions) {
			_permissionExporter.exportPortletDataPermissions(
				portletDataContext);
		}

		_exportImportHelper.writeManifestSummary(
			document, portletDataContext.getManifestSummary());

		if (_log.isInfoEnabled()) {
			_log.info("Exporting layouts takes " + stopWatch.getTime() + " ms");
		}

		portletDataContext.addZipEntry(
			portletDataContext.getManifestXmlFilePath(), document.formattedString());
	}

	private boolean _isCurrentMultiSite(PortletDataContext portletDataContext) {
		if (!FeatureFlagManagerUtil.isEnabled(
			portletDataContext.getCompanyId(), "LPD-35914")) {

			return false;
		}

		Map<String, String[]> parameterMap =
			portletDataContext.getParameterMap();

		long currentMultiSitesGroupId = MapUtil.getLong(
			parameterMap, "currentMultiSitesGroupId");

		if (Validator.isNull(currentMultiSitesGroupId)) {
			return false;
		}

		return true;
	}

	private long[] _getMultiSitesGroupIds(PortletDataContext portletDataContext) {
		if (!FeatureFlagManagerUtil.isEnabled(
			portletDataContext.getCompanyId(), "LPD-35914")) {

			return new long[] {};
		}

		Map<String, String[]> parameterMap =
			portletDataContext.getParameterMap();

		String multiSitesGroupIds = MapUtil.getString(
			parameterMap, "multiSitesGroupIds");

		if (Validator.isNull(multiSitesGroupIds)) {
			return new long[] {};
		}

		return GetterUtil.getLongValues(
			ArrayUtil.toStringArray(
				ListUtil.fromString(multiSitesGroupIds, ",")));
	}

	private boolean _isMultiSitesExport(PortletDataContext portletDataContext) {
		if (!FeatureFlagManagerUtil.isEnabled(
			portletDataContext.getCompanyId(), "LPD-35914")) {

			return false;
		}

		Map<String, String[]> parameterMap =
			portletDataContext.getParameterMap();

		String multiSitesGroupIds = MapUtil.getString(
			parameterMap, "multiSitesGroupIds");

		if (Validator.isNull(multiSitesGroupIds)) {
			return false;
		}

		long[] groupIds = GetterUtil.getLongValues(
			ArrayUtil.toStringArray(
				ListUtil.fromString(multiSitesGroupIds, ",")));

		if (groupIds.length == 0) {
			return  false;
		}

		return true;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutExportController.class);

	@Reference
	private BackgroundTaskLocalService _backgroundTaskLocalService;

	@Reference
	private DeletionSystemEventExporter _deletionSystemEventExporter;

	@Reference
	private ExportImportHelper _exportImportHelper;

	@Reference
	private ExportImportLifecycleManager _exportImportLifecycleManager;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private Language _language;

	@Reference
	private LayoutPrototypeLocalService _layoutPrototypeLocalService;

	@Reference
	private LayoutSetLocalService _layoutSetLocalService;

	@Reference
	private LayoutSetPrototypeLocalService _layoutSetPrototypeLocalService;

	private final PermissionExporter _permissionExporter =
		PermissionExporter.getInstance();

	@Reference
	private PortletDataContextFactory _portletDataContextFactory;

	@Reference
	private PortletExportController _portletExportController;

	@Reference
	private UserLocalService _userLocalService;

}