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

package com.liferay.liferaygen.portlet.generic.internal.portlet;

import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.portlet.api.BasePortletLiferayGenAction;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTemplate;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

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
	properties = {
		"liferaygen.action.class.name=com.liferay.liferaygen.portlet.generic.internal.portlet.DeletePortletLiferayGenAction"
	},
	service = LiferayGenAction.class
)
public class DeletePortletLiferayGenAction extends BasePortletLiferayGenAction {

	@Override
	public String doGetDescription() {
		return "Deletes a random portlet in a random layout in a random " +
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
					"Portlet to be created. If parameter is empty, a random " +
						"one will be selected from layout portlets");
				put(
					"layoutId",
					"Force adding portlet to the layout with this layoutId, " +
						"instead of using target parameter");
				put("plid", "Same as layoutId");
				put(
					LiferayGenActionConfig.TARGET,
					"Layout to use during delete action");
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

		liferayGenValueGenerator = new LiferayGenValueGenerator(
			_companyLocalService, _liferayGenQueryHandler, _portal,
			_portletLocalService);

		try {
			groupId = GetterUtil.getLong(
				parameters.get(LiferayGenConfigConstants.GROUP_ID));
			layout = (Layout)parameters.get(LiferayGenActionConfig.TARGET);
			Object portletIdObj = (Object)parameters.get("portletId");

			validateLayout(groupId, layout);

			LayoutTypePortlet layoutTypePortlet =
				(LayoutTypePortlet)layout.getLayoutType();

			LayoutTemplate layoutTemplate =
				layoutTypePortlet.getLayoutTemplate();

			if (portletIdObj != null) {
				portletId = portletIdObj.toString();
			}

			if (Validator.isNull(portletId)) {
				columnId = liferayGenValueGenerator.getRandomObjectFromList(
					layoutTemplate.getColumns());

				int columnSize = getColumnSize(layout, columnId);

				columnPos = liferayGenValueGenerator.getRandomIntegerFromRange(
					0, columnSize);

				Portlet portlet = getColumnPortlet(layout, columnId, columnPos);

				if (portlet == null) {
					if (_log.isDebugEnabled()) {
						_log.debug(
							StringBundler.concat(
								"No portlet was found with parameters groupId ",
								"= ", groupId, " layoutId = ",
								layout.getLayoutId(), " plid = ",
								layout.getPlid(), " portletId = ", portletId,
								" columnId = ", columnId, " columnPos = ",
								columnPos));
					}

					return;
				}

				portletId = portlet.getPortletId();
			}

			User user = getRandomUserWithPermissions(layout, portletId);

			if (user == null) {
				return;
			}

			Locale locale = liferayGenValueGenerator.getRandomObjectFromList(
				ListUtil.fromCollection(LanguageUtil.getAvailableLocales()));

			String cmd = Constants.DELETE;

			updateLayoutAction(layout, user, locale, cmd, portletId, null, -1);

			//refresh objects from database
			layout = _layoutLocalService.getLayout(layout.getPlid());

			Portlet portlet = getColumnPortlet(layout, columnId, columnPos);

			if ((portlet != null) &&
				StringUtil.equals(portlet.getRootPortletId(), portletId)) {

				_log.error(
					StringBundler.concat(
						"Error deleting portlet with parameters groupId = ",
						groupId, " layoutId = ", layout.getLayoutId(),
						" plid = ", layout.getPlid(), " portletId = ",
						portletId, " columnId = ", columnId, " columnPos = ",
						columnPos));
			}
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
					"Error executing DeletePortletLiferayGenAction with",
					"parameters groupId = ", groupId, " layoutId = ", layoutId,
					" plid = ", plid, " portletId=", portletId, " columnId=",
					columnId, " columnPos = ", columnPos, " - ",
					t.getMessage()),
				t);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DeletePortletLiferayGenAction.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LayoutLocalService _layoutLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}