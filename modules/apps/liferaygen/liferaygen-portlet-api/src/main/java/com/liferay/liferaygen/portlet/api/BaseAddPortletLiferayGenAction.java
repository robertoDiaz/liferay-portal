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

package com.liferay.liferaygen.portlet.api;

import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTemplate;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public abstract class BaseAddPortletLiferayGenAction
	extends BasePortletLiferayGenAction {

	public abstract void activate();

	@Reference(unbind = "-")
	public void setLayoutLocalService(LayoutLocalService layoutLocalService) {
		_layoutLocalService = layoutLocalService;
	}

	@Reference(unbind = "-")
	public void setPortletLocalService(
		PortletLocalService portletLocalService) {

		_portletLocalService = portletLocalService;
	}

	@Override
	protected void doRun() {
		long groupId = -1;

		Layout layout = null;

		String portletId = null;

		String columnId = null;

		int columnPos = -1;

		try {
			groupId = GetterUtil.getLong(
				parameters.get(LiferayGenConfigConstants.GROUP_ID));
			layout = (Layout)parameters.get(LiferayGenActionConfig.TARGET);
			Object portletIdObj = parameters.get("portletId");

			validateLayout(groupId, layout);

			LayoutTypePortlet layoutTypePortlet =
				(LayoutTypePortlet)layout.getLayoutType();

			LayoutTemplate layoutTemplate =
				layoutTypePortlet.getLayoutTemplate();

			Locale locale = liferayGenValueGenerator.getRandomObjectFromList(
				ListUtil.fromCollection(LanguageUtil.getAvailableLocales()));

			columnId = liferayGenValueGenerator.getRandomObjectFromList(
				layoutTemplate.getColumns());

			int columnSize = getColumnSize(layout, columnId);

			columnPos = liferayGenValueGenerator.getRandomIntegerFromRange(
				0, columnSize);

			if (portletIdObj != null) {
				portletId = portletIdObj.toString();

				portletId = liferayGenValueGenerator.getRandomObjectFromArray(
					StringUtil.split(portletId));

				Portlet portlet = _portletLocalService.getPortletById(
					layout.getCompanyId(), portletId);

				if (!liferayGenValueGenerator.canAddPortlet(portlet, layout)) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							StringBundler.concat(
								"Portlet ", portletId, " cannot be added to ",
								"layoutId = ", layout.getLayoutId()));
					}

					return;
				}
			}

			if (Validator.isNull(portletId)) {
				Portlet portlet = liferayGenValueGenerator.getRandomPortlet(
					layout);

				portletId = portlet.getPortletId();
			}

			User user = getRandomUserWithPermissions(layout, portletId);

			if (user == null) {
				return;
			}

			String cmd = Constants.ADD;

			updateLayoutAction(
				layout, user, locale, cmd, portletId, columnId, columnPos);

			//refresh objects from database

			layout = _layoutLocalService.getLayout(layout.getPlid());

			Portlet portlet = getColumnPortlet(layout, columnId, columnPos);

			if ((portlet == null) ||
				!StringUtil.equals(portlet.getRootPortletId(), portletId)) {

				_log.error(
					StringBundler.concat(
						"Error retrieving created portlet with parameters ",
						"groupId = ", groupId, " layoutId = ",
						layout.getLayoutId(), " plid = ", layout.getPlid(),
						" portletId = ", portletId, " columnId=", columnId,
						" columnPos=", columnPos));
			}

			PortletPreferences portletPreferences = getPortletPreferences(
				layout, portlet);

			doUpdatePortletPreferences(
				user, layout, portlet, portletPreferences);
		}
		catch (Throwable t) {
			long layoutId = -1;
			long plid = -1;

			if (layout != null) {
				layoutId = layout.getLayoutId();
				plid = layout.getPlid();
			}

			_log.error(
				StringBundler.concat(
					"Error executing AddPortlet with parameters groupId = ",
					groupId, " layoutId = ", layoutId, " plid = ", plid,
					" portletId = ", portletId, " columnId = ", columnId,
					" columnPos = ", columnPos, " - ", t.getMessage()),
				t);
		}
	}

	protected abstract void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception;

	private static final Log _log = LogFactoryUtil.getLog(
		BaseAddPortletLiferayGenAction.class);

	private LayoutLocalService _layoutLocalService;
	private PortletLocalService _portletLocalService;

}