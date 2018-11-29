/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.liferaygen.layout.internal;

import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.util.LiferayGenParameterHandler;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.mobile.device.rules.model.MDRAction;
import com.liferay.mobile.device.rules.model.MDRRuleGroupInstance;
import com.liferay.mobile.device.rules.service.MDRActionLocalService;
import com.liferay.mobile.device.rules.service.MDRRuleGroupInstanceLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.model.LayoutPrototype;
import com.liferay.portal.kernel.model.LayoutTemplate;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.PortletPreferencesIds;
import com.liferay.portal.kernel.model.Theme;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.LayoutPrototypeLocalService;
import com.liferay.portal.kernel.service.LayoutTemplateLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.PortletPreferencesLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.layout.internal.CreateLayoutLiferayGenAction",
	service = LiferayGenAction.class
)
public class CreateLayoutLiferayGenAction extends BaseLiferayGenAction {

	@Override
	public String doGetDescription() {
		return "Creates a random layout (it will be randomly private/public)";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("nonDefaultParentLayoutRatio", 0L);
				put("nonPortletLayoutTypeRatio", 0L);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					"nonDefaultParentLayoutRatio",
					"Probability percentage of creating a layout with a " +
						"random parent layout");
				put(
					"nonPortletLayoutTypeRatio",
					"Probability percentage of creating a layout of non " +
						"portlet type");
				put(LiferayGenActionConfig.TARGET, "Parent layout");
			}
		};
	}

	@Override
	public Criterion getEntityFilter() {
		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		conjunction.add(RestrictionsFactoryUtil.eq("type", "portlet"));
		conjunction.add(
			RestrictionsFactoryUtil.eq("layoutPrototypeLinkEnabled", false));

		return conjunction;
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return Layout.class;
	}

	@Override
	public String getEntityProperties() {
		return "plid";
	}

	@Override
	public boolean hasScopeByGroupId() {
		return true;
	}

	protected Object[] addLayout(
			LiferayGenValueGenerator liferayGenValueGenerator, long userId,
			String languageId, long companyId, long groupId,
			boolean privateLayout, long parentLayoutId, boolean hidden,
			String layoutType, Locale[] locales, ServiceContext serviceContext)
		throws Exception {

		Map<Locale, String> nameMap =
			liferayGenValueGenerator.getRandomValuesLocalizationMap(
				locales,
				liferayGenValueGenerator.getRandomIntegerFromRange(12, 20));

		Map<Locale, String> titleMap =
			liferayGenValueGenerator.getRandomValuesLocalizationMap(
				locales,
				liferayGenValueGenerator.getRandomIntegerFromRange(12, 20));

		Map<Locale, String> descriptionMap =
			liferayGenValueGenerator.getRandomValuesLocalizationMap(
				locales,
				liferayGenValueGenerator.getRandomIntegerFromRange(20, 65));

		Map<Locale, String> keywordsMap =
			liferayGenValueGenerator.getRandomValuesLocalizationMap(locales, 0);

		Map<Locale, String> robotsMap =
			liferayGenValueGenerator.getRandomValuesLocalizationMap(locales, 0);

		//TODO create a real friendlyURLMap
		Map<Locale, String> friendlyURLMap = new HashMap<>();

		long layoutPrototypeId = 0;

		Layout layout = null;

		UnicodeProperties layoutTypeSettingsProperties = null;

		String oldFriendlyURL = StringPool.BLANK;

		// Add layout

		boolean inheritFromParentLayoutId =
			liferayGenValueGenerator.getBoolean();

		if (inheritFromParentLayoutId && (parentLayoutId > 0)) {
			Layout parentLayout = _layoutLocalService.getLayout(
				groupId, privateLayout, parentLayoutId);

			//TODO update call with TypeSettings instead of ""
			layout = _layoutLocalService.addLayout(
				userId, groupId, privateLayout, parentLayoutId, nameMap,
				titleMap, parentLayout.getDescriptionMap(),
				parentLayout.getKeywordsMap(), parentLayout.getRobotsMap(),
				parentLayout.getType(), StringPool.BLANK, hidden,
				friendlyURLMap, serviceContext);

			_layoutLocalService.updateLayout(
				layout.getGroupId(), layout.isPrivateLayout(),
				layout.getLayoutId(), parentLayout.getTypeSettings());

			inheritMobileRuleGroups(layout, serviceContext);

			if (parentLayout.isTypePortlet()) {
				copyPreferences(
					serviceContext.getRequest(), companyId, userId, languageId,
					layout, parentLayout);

				copyLookAndFeel(layout, parentLayout);
			}
		}
		else if (layoutPrototypeId > 0) {
			LayoutPrototype layoutPrototype =
				_layoutPrototypeLocalService.getLayoutPrototype(
					layoutPrototypeId);

			String layoutPrototypeLinkEnabled = null;

			if (Validator.isNotNull(layoutPrototypeLinkEnabled)) {
				serviceContext.setAttribute(
					"layoutPrototypeLinkEnabled", layoutPrototypeLinkEnabled);
			}

			serviceContext.setAttribute(
				"layoutPrototypeUuid", layoutPrototype.getUuid());

			//TODO update call with TypeSettings instead of ""
			layout = _layoutLocalService.addLayout(
				userId, groupId, privateLayout, parentLayoutId, nameMap,
				titleMap, descriptionMap, keywordsMap, robotsMap, layoutType,
				StringPool.BLANK, hidden, friendlyURLMap, serviceContext);
		}
		else {
			//TODO update call with TypeSettings instead of ""

			layout = _layoutLocalService.addLayout(
				userId, groupId, privateLayout, parentLayoutId, nameMap,
				titleMap, descriptionMap, keywordsMap, robotsMap, layoutType,
				StringPool.BLANK, hidden, friendlyURLMap, serviceContext);
		}

		UnicodeProperties formTypeSettingsProperties = new UnicodeProperties();

		if (LayoutConstants.TYPE_LINK_TO_LAYOUT.equals(layoutType)) {
			List<Layout> layouts = _layoutLocalService.getLayouts(
				groupId, privateLayout, LayoutConstants.TYPE_PORTLET);

			if (!layouts.isEmpty()) {
				Layout randomLayout =
					liferayGenValueGenerator.getRandomObjectFromList(layouts);

				formTypeSettingsProperties.put(
					"linkToLayoutId=" + randomLayout.getLayoutId());
				formTypeSettingsProperties.put(
					"groupId=" + randomLayout.getGroupId());

				formTypeSettingsProperties.put("show-alternate-links=true");
				formTypeSettingsProperties.put("layoutUpdateable=true");
				formTypeSettingsProperties.put(
					"privateLayout=" + randomLayout.isPrivateLayout());
			}

			layoutTypeSettingsProperties = layout.getTypeSettingsProperties();

			layout.setTypeSettingsProperties(formTypeSettingsProperties);

			layoutTypeSettingsProperties.putAll(
				layout.getTypeSettingsProperties());
		}
		else if (LayoutConstants.TYPE_PORTLET.equals(layoutType)) {
			LayoutTypePortlet layoutTypePortlet = null;

			Theme theme = null;

			layoutTypePortlet = (LayoutTypePortlet)layout.getLayoutType();

			theme = layout.getTheme();

			String layoutTemplateId = PropsUtil.get(
				PropsKeys.DEFAULT_LAYOUT_TEMPLATE_ID);

			List<LayoutTemplate> layoutTemplates =
				_layoutTemplateLocalService.getLayoutTemplates(
					theme.getThemeId());

			LayoutTemplate layoutTemplate =
				liferayGenValueGenerator.getRandomObjectFromList(
					layoutTemplates);

			while (StringUtil.equals(
						"freeform", layoutTemplate.getLayoutTemplateId())) {

				layoutTemplate =
					liferayGenValueGenerator.getRandomObjectFromList(
						layoutTemplates);
			}

			if (layoutTemplate != null) {
				layoutTemplateId = layoutTemplate.getLayoutTemplateId();
			}

			layoutTypePortlet.setLayoutTemplateId(userId, layoutTemplateId);
		}

		_layoutLocalService.updateLayout(
			groupId, privateLayout, layout.getLayoutId(),
			layout.getTypeSettings());

		return new Object[] {layout, oldFriendlyURL};
	}

	protected void copyLookAndFeel(Layout targetLayout, Layout sourceLayout)
		throws Exception {

		_layoutLocalService.updateLookAndFeel(
			targetLayout.getGroupId(), targetLayout.isPrivateLayout(),
			targetLayout.getLayoutId(), sourceLayout.getThemeId(),
			sourceLayout.getColorSchemeId(), sourceLayout.getCss());
	}

	protected void copyPreferences(
			HttpServletRequest request, long companyId, long userId,
			String languageId, Layout targetLayout, Layout sourceLayout)
		throws Exception {

		LayoutTypePortlet sourceLayoutTypePortlet =
			(LayoutTypePortlet)sourceLayout.getLayoutType();

		List<String> sourcePortletIds = sourceLayoutTypePortlet.getPortletIds();

		for (String sourcePortletId : sourcePortletIds) {

			// Copy preference

			PortletPreferencesIds portletPreferencesIds =
				PortletPreferencesFactoryUtil.getPortletPreferencesIds(
					request, targetLayout, sourcePortletId);

			_portletPreferencesLocalService.getPreferences(
				portletPreferencesIds);

			PortletPreferencesIds sourcePortletPreferencesIds =
				PortletPreferencesFactoryUtil.getPortletPreferencesIds(
					request, sourceLayout, sourcePortletId);

			PortletPreferences sourcePreferences =
				_portletPreferencesLocalService.getPreferences(
					sourcePortletPreferencesIds);

			_portletPreferencesLocalService.updatePreferences(
				portletPreferencesIds.getOwnerId(),
				portletPreferencesIds.getOwnerType(),
				portletPreferencesIds.getPlid(),
				portletPreferencesIds.getPortletId(), sourcePreferences);

			// Copy portlet setup

			PortletPreferences targetPreferences =
				_portletPreferencesLocalService.getPreferences(
					companyId, PortletKeys.PREFS_OWNER_ID_DEFAULT,
					PortletKeys.PREFS_OWNER_TYPE_LAYOUT, targetLayout.getPlid(),
					sourcePortletId);

			sourcePreferences = _portletPreferencesLocalService.getPreferences(
				companyId, PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, sourceLayout.getPlid(),
				sourcePortletId);

			_portletPreferencesLocalService.updatePreferences(
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, targetLayout.getPlid(),
				sourcePortletId, sourcePreferences);

			updateLayoutScopes(
				userId, sourceLayout, targetLayout, sourcePreferences,
				targetPreferences, sourcePortletId, languageId);
		}
	}

	@Override
	protected void doRun() {
		Map<String, Object> parameters = getParameters();

		long companyId = (Long)parameters.get(
			LiferayGenConfigConstants.COMPANY_ID);

		long groupId = (Long)parameters.get(LiferayGenConfigConstants.GROUP_ID);

		Locale[] locales = (Locale[])parameters.get(
			LiferayGenConfigConstants.LOCALES);

		int nonDefaultParentLayoutRatio =
			_liferayGenParameterHandler.getParamAsIntegerPercentage(
				parameters, "nonDefaultParentLayoutRatio");

		long parentPlid = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
		long parentLayoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;

		LiferayGenValueGenerator liferayGenValueGenerator =
			new LiferayGenValueGenerator(
				_companyLocalService, _liferayGenQueryHandler, _portal,
				_portletLocalService);

		if ((nonDefaultParentLayoutRatio > 0) &&
			liferayGenValueGenerator.getBoolean(nonDefaultParentLayoutRatio)) {

			parentPlid = MapUtil.getLong(
				parameters, LiferayGenActionConfig.TARGET,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);
		}

		int nonPortletLayoutTypeRatio =
			_liferayGenParameterHandler.getParamAsIntegerPercentage(
				parameters, "nonPortletLayoutTypeRatio");

		String layoutType = LayoutConstants.TYPE_PORTLET;

		if ((nonPortletLayoutTypeRatio > 0) &&
			liferayGenValueGenerator.getBoolean(nonPortletLayoutTypeRatio)) {

			layoutType = LayoutConstants.TYPE_LINK_TO_LAYOUT;
		}

		boolean privateLayout = liferayGenValueGenerator.getBoolean();

		boolean hidden = liferayGenValueGenerator.getBoolean(5);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		try {
			if (parentPlid != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
				Layout layout = _layoutLocalService.getLayout(parentPlid);

				privateLayout = layout.isPrivateLayout();
				parentLayoutId = layout.getLayoutId();
			}

			long userId = liferayGenValueGenerator.getRandomUserIdFromCache();

			User user = _userLocalService.getUser(userId);

			String languageId = user.getLanguageId();

			addLayout(
				liferayGenValueGenerator, userId, languageId, companyId,
				groupId, privateLayout, parentLayoutId, hidden, layoutType,
				locales, serviceContext);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected void inheritMobileRuleGroups(
			Layout layout, ServiceContext serviceContext)
		throws PortalException {

		List<MDRRuleGroupInstance> parentMDRRuleGroupInstances =
			_mdrRuleGroupInstanceLocalService.getRuleGroupInstances(
				Layout.class.getName(), layout.getParentPlid());

		for (MDRRuleGroupInstance parentMDRRuleGroupInstance :
				parentMDRRuleGroupInstances) {

			MDRRuleGroupInstance mdrRuleGroupInstance =
				_mdrRuleGroupInstanceLocalService.addRuleGroupInstance(
					layout.getGroupId(), Layout.class.getName(),
					layout.getPlid(),
					parentMDRRuleGroupInstance.getRuleGroupId(),
					parentMDRRuleGroupInstance.getPriority(), serviceContext);

			List<MDRAction> parentMDRActions =
				_mdrActionLocalService.getActions(
					parentMDRRuleGroupInstance.getRuleGroupInstanceId());

			for (MDRAction mdrAction : parentMDRActions) {
				_mdrActionLocalService.addAction(
					mdrRuleGroupInstance.getRuleGroupInstanceId(),
					mdrAction.getNameMap(), mdrAction.getDescriptionMap(),
					mdrAction.getType(), mdrAction.getTypeSettings(),
					serviceContext);
			}
		}
	}

	protected void updateLayoutScopes(
			long userId, Layout sourceLayout, Layout targetLayout,
			PortletPreferences sourcePreferences,
			PortletPreferences targetPreferences, String sourcePortletId,
			String languageId)
		throws Exception {

		String scopeType = GetterUtil.getString(
			sourcePreferences.getValue("lfrScopeType", null));

		if (Validator.isNull(scopeType) || !scopeType.equals("layout")) {
			return;
		}

		Layout targetScopeLayout =
			_layoutLocalService.getLayoutByUuidAndGroupId(
				targetLayout.getUuid(), targetLayout.getGroupId(),
				targetLayout.isPrivateLayout());

		if (!targetScopeLayout.hasScopeGroup()) {
			//TODO adjust this
			_groupLocalService.addGroup(
				userId, GroupConstants.DEFAULT_PARENT_GROUP_ID,
				Layout.class.getName(), targetLayout.getPlid(),
				targetLayout.getGroupId(), new HashMap<>(), new HashMap<>(), 0,
				true, 0, null, false, true, true, null);
		}

		String portletTitle = _portal.getPortletTitle(
			sourcePortletId, languageId);

		String newPortletTitle = _portal.getNewPortletTitle(
			portletTitle, String.valueOf(sourceLayout.getLayoutId()),
			targetLayout.getName(languageId));

		targetPreferences.setValue(
			"groupId", String.valueOf(targetLayout.getGroupId()));
		targetPreferences.setValue("lfrScopeType", "layout");
		targetPreferences.setValue(
			"lfrScopeLayoutUuid", targetLayout.getUuid());
		targetPreferences.setValue(
			"portletSetupTitle_" + languageId, newPortletTitle);
		targetPreferences.setValue(
			"portletSetupUseCustomTitle", StringPool.TRUE);

		targetPreferences.store();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CreateLayoutLiferayGenAction.class);

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LayoutPrototypeLocalService _layoutPrototypeLocalService;

	@Reference
	private LayoutTemplateLocalService _layoutTemplateLocalService;

	@Reference
	private LiferayGenParameterHandler _liferayGenParameterHandler;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private MDRActionLocalService _mdrActionLocalService;

	@Reference
	private MDRRuleGroupInstanceLocalService _mdrRuleGroupInstanceLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private PortletPreferencesLocalService _portletPreferencesLocalService;

	@Reference
	private UserLocalService _userLocalService;

}