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
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletConfigFactoryUtil;
import com.liferay.portlet.asset.model.AssetEntry;

import java.util.ArrayList;
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

import static com.liferay.liferaygen.util.ValueGenerator.getRandomIntegerFromRange;
public class AddPortletAssetPublisherManual
	extends AddPortletAssetPublisherBase {

	public void destroy() {
		assetEntryMapCache = null;
	}

	@Override
	public String doGetDescription() {
		return "Adds a random manual Asset Publisher in a random layout in a " +
			"random position. Portlet configuration will be random. Layout " +
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

	public void init() {
		List<Object[]> allVisibleAssetEntries =
			(List<Object[]>)QueryUtil.executeEntityModelQuery(
				AssetEntry.class.getName(), "entryId,classNameId,groupId",
				RestrictionsFactoryUtil.eq("visible", true));

		Map<Long, List<Object[]>> assetEntryMapCacheAux =
			new ConcurrentHashMap<Long, List<Object[]>>();

		for (Object[] assetEntry : allVisibleAssetEntries) {
			Long groupId = (Long)assetEntry[2];

			List<Object[]> groupAssetEntries = assetEntryMapCacheAux.get(
				groupId);

			if (groupAssetEntries == null) {
				groupAssetEntries = new ArrayList<Object[]>();

				assetEntryMapCacheAux.put(groupId, groupAssetEntries);
			}

			groupAssetEntries.add(assetEntry);
		}

		assetEntryMapCache = assetEntryMapCacheAux;
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

		/* set selection style to manual */
		actionRequest.setParameter(Constants.CMD, "selection-style");
		setPreferencesParameter(actionRequest, "selectionStyle", "manual");

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

		try {
			/* update defaultAssetPublisher */
			actionRequest.setParameter(Constants.CMD, Constants.UPDATE);
			actionRequest.setParameter(
				"defaultAssetPublisher",
				Boolean.toString(ValueGenerator.getBoolean()));

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

		portletPreferences = getPortletPreferences(layout, portlet);
		actionRequest.setPreferences(portletPreferences);

		List<Object[]> groupAssetEntries = assetEntryMapCache.get(
			layout.getGroupId());

		int numElements = getRandomIntegerFromRange(
			1, MAX_ASSET_PUBLISHER_ELEMENTS);

		List<Object[]> assetEntryList =
			(List<Object[]>)ValueGenerator.getRandomObjectsFromList(
				groupAssetEntries, numElements);

		for (Object[] assetEntry : assetEntryList) {
			String assetEntryId = String.valueOf((Long)assetEntry[0]);
			String assetEntryType = PortalUtil.getClassName(
				(Long)assetEntry[1]);

			actionRequest.setParameter(Constants.CMD, "add-selection");
			actionRequest.setParameter("assetEntryId", assetEntryId);
			actionRequest.setParameter("assetEntryType", assetEntryType);
			actionRequest.setParameter("assetEntryOrder", String.valueOf(-1));

			configurationAction.processAction(
				portletConfig, actionRequest, actionResponse);

			portletPreferences = getPortletPreferences(layout, portlet);
			actionRequest.setPreferences(portletPreferences);
		}
	}

	private static final int MAX_ASSET_PUBLISHER_ELEMENTS = 200;

	private static Log _log = LogFactoryUtil.getLog(
		AddPortletAssetPublisherManual.class);

	private static Map<Long, List<Object[]>> assetEntryMapCache = null;

}