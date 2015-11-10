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

package com.liferay.blogs.web.upgrade.v1_0_0;

import com.liferay.portal.kernel.dao.db.DBProcessContext;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.upgrade.UpgradeException;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.PortletPreferencesLocalService;
import com.liferay.portal.util.PortletKeys;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Iván Zaera
 */
public class UpgradeEnableViewCount implements UpgradeStep {

	public UpgradeEnableViewCount(
		PortletPreferencesLocalService portletPreferencesLocalService) {

		_portletPreferencesLocalService = portletPreferencesLocalService;
	}

	@Override
	public void upgrade(DBProcessContext dbProcessContext)
		throws UpgradeException {

		ActionableDynamicQuery actionableDynamicQuery =
			_portletPreferencesLocalService.getActionableDynamicQuery();

		actionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					Property portletIdProperty = PropertyFactoryUtil.forName(
						"portletId");

					dynamicQuery.add(portletIdProperty.eq("33"));

					Property ownerTypeProperty = PropertyFactoryUtil.forName(
						"ownerType");

					dynamicQuery.add(
						ownerTypeProperty.eq(
							PortletKeys.PREFS_OWNER_TYPE_LAYOUT));
				}
			});

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod
					<PortletPreferences>() {

				@Override
				public void performAction(PortletPreferences portletPreferences)
					throws PortalException {

					String preferences = portletPreferences.getPreferences();

					Pattern pattern = Pattern.compile(
						"<.*portlet-preferences.*/>");

					Matcher matcher = pattern.matcher(preferences);

					if (matcher.matches()) {
						preferences =
							"<portlet-preferences><preference><name>" +
								"enableViewCount</name><value>true</value>" +
								"</preference></portlet-preferences>";
					}
					else {
						preferences = preferences.replaceAll(
							"<portlet-preferences>",
							"<portlet-preferences><preference><name>" +
								"enableViewCount</name><value>true</value>" +
								"</preference>");
					}

					portletPreferences.setPreferences(preferences);

					_portletPreferencesLocalService.updatePortletPreferences(
						portletPreferences);
				}
			}
		);

		try {
			actionableDynamicQuery.performActions();
		}
		catch (PortalException e) {
			e.printStackTrace();
		}
	}

	private final PortletPreferencesLocalService
		_portletPreferencesLocalService;

}