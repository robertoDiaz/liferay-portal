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

package com.liferay.liferaygen.asset.internal.portlet;

import aQute.bnd.annotation.component.Activate;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.mock.web.portlet.MockActionResponse;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.asset.internal.portlet.AddManualAssetPublisherPortletLiferayGenAction",
	service = LiferayGenAction.class
)
public class AddManualAssetPublisherPortletLiferayGenAction
	extends BaseAssetPublisherAddPortletLiferayGenAction {

	@Activate
	public void activate() {
		liferayGenValueGenerator = new LiferayGenValueGenerator(
			_companyLocalService, _liferayGenQueryHandler, _portal,
			_portletLocalService);

		initUpdateLayoutActionMethod();
	}

	public void destroy() {
		_assetEntryMapCache = null;
	}

	@Override
	public String doGetDescription() {
		return StringBundler.concat(
			"Adds a random manual Asset Publisher in a random layout in a ",
			"random position. Portlet configuration will be random. Layout ",
			"can be specified in parameters");
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
				put(
					LiferayGenActionConfig.TARGET,
					"Layout to use during add action");
			}
		};
	}

	public void init() {
		List<Object[]> allVisibleAssetEntries =
			(List<Object[]>)_liferayGenQueryHandler.executeEntityModelQuery(
				AssetEntry.class.getName(), "entryId, classNameId, groupId",
				RestrictionsFactoryUtil.eq("visible", true));

		Map<Long, List<Object[]>> assetEntryMapCacheAux =
			new ConcurrentHashMap<>();

		for (Object[] assetEntry : allVisibleAssetEntries) {
			Long groupId = (Long)assetEntry[2];

			List<Object[]> groupAssetEntries = assetEntryMapCacheAux.get(
				groupId);

			if (groupAssetEntries == null) {
				groupAssetEntries = new ArrayList<>();

				assetEntryMapCacheAux.put(groupId, groupAssetEntries);
			}

			groupAssetEntries.add(assetEntry);
		}

		_assetEntryMapCache = assetEntryMapCacheAux;
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
			liferayGenValueGenerator.getMockHttpServletRequest(
				layout, user, locale);

		MockActionRequest actionRequest =
			liferayGenValueGenerator.getMockActionRequest(
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
				Boolean.toString(liferayGenValueGenerator.getBoolean()));

			configurationAction.processAction(
				portletConfig, actionRequest, actionResponse);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				String warn = e.getMessage();

				if (Validator.isNull(warn)) {
					Class<? extends Exception> clazz = e.getClass();

					warn = clazz.getName();
				}

				_log.warn(warn);
			}
		}

		portletPreferences = getPortletPreferences(layout, portlet);

		actionRequest.setPreferences(portletPreferences);

		List<Object[]> groupAssetEntries = _assetEntryMapCache.get(
			layout.getGroupId());

		int numElements = liferayGenValueGenerator.getRandomIntegerFromRange(
			1, _MAX_ASSET_PUBLISHER_ELEMENTS);

		List<Object[]> assetEntryList =
			liferayGenValueGenerator.getRandomObjectsFromList(
				groupAssetEntries, numElements);

		for (Object[] assetEntry : assetEntryList) {
			String assetEntryId = String.valueOf((Long)assetEntry[0]);
			String assetEntryType = _portal.getClassName((Long)assetEntry[1]);

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

	private static final int _MAX_ASSET_PUBLISHER_ELEMENTS = 200;

	private static final Log _log = LogFactoryUtil.getLog(
		AddManualAssetPublisherPortletLiferayGenAction.class);

	private static Map<Long, List<Object[]>> _assetEntryMapCache;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}