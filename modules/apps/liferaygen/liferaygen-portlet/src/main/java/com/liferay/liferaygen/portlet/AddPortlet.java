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
import com.liferay.liferaygen.constants.ConfigConstants;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTemplate;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletPreferences;
public class AddPortlet extends BasePortlet {

	@Override
	public String doGetDescription() {
		return "Adds a random portlet in a random layout in a random " +
			"position. Portlet and Layout can be specified in parameters";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("portletId", null);
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
					"portletId",
					"Portlet to be created. In case of specify a list of " +
					"portletIds, a random one will be selected from list. " +
					"If parameter is empty, a random one will be selected " +
					"from all existing portlets");
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
	protected void doRun() {
		long groupId = -1;
		Layout layout = null;
		String portletId = null;
		String columnId = null;
		int columnPos = -1;

		try {
			groupId = GetterUtil.getLong(
				_parameters.get(ConfigConstants.GROUP_ID));
			layout = (Layout) _parameters.get(ActionConfig.TARGET);
			Object portletIdObj = (Object) _parameters.get("portletId");

			validateLayout(groupId, layout);

			LayoutTypePortlet layoutTypePortlet =
				(LayoutTypePortlet)layout.getLayoutType();

			LayoutTemplate layoutTemplate =
				layoutTypePortlet.getLayoutTemplate();

			Locale locale = ValueGenerator.getRandomObjectFromList(
				ListUtil.fromArray(LanguageUtil.getAvailableLocales()));

			columnId = ValueGenerator.getRandomObjectFromList(
					layoutTemplate.getColumns());

			int columnSize = getColumnSize(layout, columnId);

			columnPos = ValueGenerator.getRandomIntegerFromRange(0, columnSize);

			if (portletIdObj != null) {
				portletId = portletIdObj.toString();

				portletId = ValueGenerator.getRandomObjectFromArray(
					StringUtil.split(portletId));

				Portlet portlet = PortletLocalServiceUtil.getPortletById(
					layout.getCompanyId(), portletId);

				if (!ValueGenerator.canAddPortlet(portlet, layout)) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							"Portlet " + portletId + " cannot be added to " +
								"layoutId=" + layout.getLayoutId());
					}

					return;
				}
			}

			if (Validator.isNull(portletId)) {
				Portlet portlet = ValueGenerator.getRandomPortlet(layout);

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
			layout = LayoutLocalServiceUtil.getLayout(layout.getPlid());

			Portlet portlet = getColumnPortlet(layout, columnId, columnPos);

			if ((portlet == null) ||
				!portlet.getRootPortlet().getPortletId().equals(portletId)) {

				_log.error(
					"Error retrieving created portlet with parameters " +
					"groupId=" + groupId + " layoutId=" + layout.getLayoutId() +
					" plid=" + layout.getPlid() + " portletId=" + portletId +
					" columnId=" + columnId + " columnPos=" + columnPos);
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
				"Error executing AddPortlet with parameters groupId=" +
				groupId + " layoutId=" + layoutId + " plid=" + plid +
				" portletId=" + portletId + " columnId=" + columnId +
				" columnPos=" + columnPos + " - " + t.getMessage(), t);
		}
	}

	protected void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception {
	}

	private static Log _log = LogFactoryUtil.getLog(AddPortlet.class);

}