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

package com.liferay.liferaygen.web.internal.actions.portlet;

import com.liferay.liferaygen.config.ActionConfig;
import com.liferay.liferaygen.util.QueryUtil;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.ClassName;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletConfigFactoryUtil;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetRendererFactory;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.util.RSSUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;

import javax.servlet.ServletContext;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.mock.web.portlet.MockActionResponse;
public class AddPortletAssetPublisherDynamic
	extends AddPortletAssetPublisherBase {

	@Override
	public void destroy() {
		assetRendererClassNameIdsPerGroupIdCache = null;
	}

	@Override
	public String doGetDescription() {
		return "Adds a random dynamic Asset Publisher in a random layout in " +
			"a random position. Portlet configuration will be random. Layout " +
			"can be specified in parameters";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("layoutId", null);
				put("plid", null);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					"layoutId",
					"Force adding portlet to the layout with this layoutId, " +
					"instead of using target parameter");
				put("plid", "Same as layoutId");
				put(ActionConfig.TARGET, "Layout to use during add action");
			}
		};
	}

	@Override
	public void init() {
		assetRendererClassNameIdsPerGroupIdCache =
			new ConcurrentHashMap<Long, List<Long>>();
	}

	@Override
	protected void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception {

		super.doUpdatePortletPreferences(
			user, layout, portlet, portletPreferences);

		portletPreferences = getPortletPreferences(layout, portlet);

		ConfigurationAction configurationAction =
			portlet.getConfigurationActionInstance();

		Locale locale = getLayoutDefaultLocale(layout);

		MockHttpServletRequest request =
			ValueGenerator.getMockHttpServletRequest(layout, user, locale);

		MockActionRequest actionRequest = ValueGenerator.getMockActionRequest(
			request, portlet, portletPreferences);

		MockActionResponse actionResponse = new MockActionResponse();

		PortletConfig portletConfig = PortletConfigFactoryUtil.create(
			portlet, (ServletContext)request.getAttribute(WebKeys.CTX));

		/* set selection style to dynamic */
		actionRequest.setParameter(Constants.CMD, "selection-style");
		setPreferencesParameter(actionRequest, "selectionStyle", "dynamic");

		configurationAction.processAction(
			portletConfig, actionRequest, actionResponse);

		portletPreferences = getPortletPreferences(layout, portlet);
		actionRequest.setPreferences(portletPreferences);

		/* set scope */
		actionRequest.setParameter(Constants.CMD, "select-scope");
		setPreferencesParameter(
			actionRequest, "scopeIds", "Group_" + layout.getGroupId());

		configurationAction.processAction(
			portletConfig, actionRequest, actionResponse);

		portletPreferences = getPortletPreferences(layout, portlet);
		actionRequest.setPreferences(portletPreferences);

		List<Long> availableClassNameIds = getAssetRendererClassNameIds(
			layout.getGroupId());

		/* getSelectedClassNameIds method selects with following percentages:
		 * 20% = zero (=any) asset types
		 * 30% = one random asset type
		 * 30% = JournalArticle asset type
		 * 20% = more than one random asset type
		 */
		long numberOfIdsToSelect = getNumberOfIdsToSelect(
				availableClassNameIds);

		List<Long> selectedClassNameIds = getSelectedClassNameIds(
				availableClassNameIds, numberOfIdsToSelect);

		String anyAssetType = String.valueOf(numberOfIdsToSelect == 0);
		String classNameIds = StringUtil.merge(selectedClassNameIds);

		/* update defaultAssetPublisher */
		actionRequest.setParameter(Constants.CMD, Constants.UPDATE);
		actionRequest.setParameter(
			"defaultAssetPublisher",
			Boolean.toString(ValueGenerator.getBoolean()));

		setPreferencesParameter(actionRequest, "anyAssetType", anyAssetType);
		setPreferencesParameter(actionRequest, "classNameIds", classNameIds);

		if (selectedClassNameIds.size() == 1) {
			setAssetClassTypeParameters(
				actionRequest, selectedClassNameIds.get(0), layout, locale);
		}

		setPreferencesParameter(
			actionRequest, "orderByColumn1",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {
					"title","createDate","modifiedDate","publishDate",
					"expirationDate","priority","viewCount","ratings"}));

		setPreferencesParameter(
			actionRequest, "orderByType1",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {"ASC","DESC"}));

		setPreferencesParameter(
			actionRequest, "orderByColumn2",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {
					"title","createDate","modifiedDate","publishDate",
					"expirationDate","priority","viewCount","ratings"}));

		setPreferencesParameter(
			actionRequest, "orderByType2",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {"ASC","DESC"}));

		setPreferencesParameter(
			actionRequest, "enableRss",
			Boolean.toString(ValueGenerator.getBoolean()));

		setPreferencesParameter(
			actionRequest, "rssName", ValueGenerator.getLowerCaseWord(10));

		setPreferencesParameter(
			actionRequest, "rssDelta",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {
					"1","2","3","4","5","10","15","20","25","30","40","50","60",
					"70","80","90","100"}));

		setPreferencesParameter(
			actionRequest, "rssDisplayStyle",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {
					RSSUtil.DISPLAY_STYLE_ABSTRACT,
					RSSUtil.DISPLAY_STYLE_TITLE}));

		setPreferencesParameter(
			actionRequest, "rssFormat",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {"rss10", "rss20", "atom10"}));

		try {
			configurationAction.processAction(
				portletConfig, actionRequest, actionResponse);
		}
		catch (PrincipalException pe) {
			if (_log.isWarnEnabled()) {
				String warn = pe.getMessage();

				if (Validator.isNull(warn)) {
					warn = pe.getClass().getName();
				}

				_log.warn(warn);
			}
		}
	}

	private static String getClassName(
		AssetRendererFactory assetRendererFactory) {

		Class<?> clazz = assetRendererFactory.getClass();

		String className = clazz.getName();

		int pos = className.lastIndexOf(StringPool.PERIOD);

		return className.substring(pos + 1);
	}

	private List<Long> getAssetRendererClassNameIds(long groupId)
		throws PortalException, SystemException {

		if ((assetRendererClassNameIdsPerGroupIdCache != null) &&
			assetRendererClassNameIdsPerGroupIdCache.containsKey(groupId)) {

			return assetRendererClassNameIdsPerGroupIdCache.get(groupId);
		}

		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();
		conjunction.add(RestrictionsFactoryUtil.eq("groupId", groupId));
		conjunction.add(RestrictionsFactoryUtil.eq("visible", true));

		List<Long> availableClassNameIds =
			(List<Long>)QueryUtil.executeEntityModelQuery(
				AssetEntry.class.getName(), "distinct(classNameId)",
				conjunction);

		List<Long> assetRendererClassNameIds = new ArrayList<Long>();

		for (Long classNameId : availableClassNameIds) {
			ClassName className = ClassNameLocalServiceUtil.getClassName(
				classNameId);

			AssetRendererFactory assetRendererFactory =
				AssetRendererFactoryRegistryUtil.
					getAssetRendererFactoryByClassName(
						className.getClassName());

			if (assetRendererFactory != null) {
				assetRendererClassNameIds.add((Long)classNameId);
			}
		}

		if (assetRendererClassNameIdsPerGroupIdCache != null) {
			assetRendererClassNameIdsPerGroupIdCache.put(
				groupId, assetRendererClassNameIds);
		}

		return assetRendererClassNameIds;
	}

	private List<Long> getAvailableClassTypeIds(
			AssetRendererFactory assetRendererFactory, long companyId,
			long groupId, Locale locale)
		throws Exception {

		Company company = CompanyLocalServiceUtil.getCompany(companyId);

		long[] groupIds = {company.getGroup().getGroupId(), groupId};

		Map<Long, String> classTypes = assetRendererFactory.getClassTypes(
				groupIds, locale);

		if ((classTypes == null) || classTypes.isEmpty()) {
			return Collections.emptyList();
		}

		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();
		conjunction.add(
			RestrictionsFactoryUtil.eq(
				"classNameId", assetRendererFactory.getClassNameId()));

		conjunction.add(
			RestrictionsFactoryUtil.in("classTypeId", classTypes.keySet()));

		return (List<Long>)QueryUtil.executeEntityModelQuery(
				AssetEntry.class.getName(), "distinct(classTypeId)",
				conjunction);
	}

	private <T> long getNumberOfIdsToSelect(Collection<T> availableIds) {
		long mode = ValueGenerator.getRandomLongFromRange(0, 4);

		if (mode == 0) {

			// Select zero asset types = 20%

			return 0;
		}
		else if (mode != 1) {

			// Select one asset type = 60%

			return 1;
		}
		else {

			// Select more than one asset type = 20%

			long min = Math.min(2, availableIds.size());
			long max = Math.max(min, availableIds.size());

			return ValueGenerator.getRandomLongFromRange(min, max);
		}
	}

	private List<Long> getSelectedClassNameIds(
			List<Long> availableClassNameIds, long numberOfIdsToSelect) {

		if (numberOfIdsToSelect == 0) {
			return availableClassNameIds;
		}

		if (numberOfIdsToSelect == 1) {
			Long randomClassNameId;

			if (ValueGenerator.getBoolean()) {

				// When numberOfClassNameIds==1, select JournalArticle = 50%

				randomClassNameId = PortalUtil.getClassNameId(
					JournalArticle.class);
			}
			else {
				randomClassNameId = ValueGenerator.getRandomObjectFromList(
					availableClassNameIds);
			}

			return Collections.singletonList(randomClassNameId);
		}

		return ValueGenerator.getRandomObjectsFromList(
			availableClassNameIds, numberOfIdsToSelect);
	}

	private void setAssetClassTypeParameters(
			MockActionRequest actionRequest, long classNameId, Layout layout,
			Locale locale)
		throws Exception {

		String className = PortalUtil.getClassName(classNameId);

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				className);

		if (assetRendererFactory == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(className + " has no assetRendererFactory");
			}

			return;
		}

		List<Long> availableClassTypeIds = getAvailableClassTypeIds(
				assetRendererFactory, layout.getCompanyId(),
				layout.getGroupId(), locale);

		if (availableClassTypeIds.isEmpty()) {
			return;
		}

		List<Long> selectedClassTypeIds;

		long numberOfIdsToSelect = getNumberOfIdsToSelect(
				availableClassTypeIds);

		if (numberOfIdsToSelect == 0) {
			selectedClassTypeIds = availableClassTypeIds;
		}
		else {
			selectedClassTypeIds = ValueGenerator.getRandomObjectsFromList(
				availableClassTypeIds, numberOfIdsToSelect);
		}

		String anyClassType = String.valueOf(numberOfIdsToSelect == 0);
		String classTypeIds = StringUtil.merge(selectedClassTypeIds);

		String assetRendererFactoryName = getClassName(assetRendererFactory);

		setPreferencesParameter(
			actionRequest, "anyClassType" + assetRendererFactoryName,
			anyClassType);
		setPreferencesParameter(
			actionRequest, "classTypeIds" + assetRendererFactoryName,
			classTypeIds);
	}

	private static Log _log = LogFactoryUtil.getLog(
		AddPortletAssetPublisherDynamic.class);

	private static Map<Long, List<Long>>
		assetRendererClassNameIdsPerGroupIdCache = null;

}