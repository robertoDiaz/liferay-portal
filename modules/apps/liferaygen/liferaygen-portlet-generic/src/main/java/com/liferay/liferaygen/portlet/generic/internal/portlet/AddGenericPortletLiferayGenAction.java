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

import aQute.bnd.annotation.component.Activate;

import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.portlet.api.BaseAddPortletLiferayGenAction;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.Portal;

import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletPreferences;

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
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.portlet.generic.internal.portlet.AddGenericPortletLiferayGenAction",
	service = LiferayGenAction.class
)
public class AddGenericPortletLiferayGenAction
	extends BaseAddPortletLiferayGenAction {

	@Activate
	public void activate() {
		liferayGenValueGenerator = new LiferayGenValueGenerator(
			_companyLocalService, _liferayGenQueryHandler, _portal,
			_portletLocalService);

		initUpdateLayoutActionMethod();
	}

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
					StringBundler.concat(
						"Portlet to be created. In case of specify a list of ",
						"portletIds, a random one will be selected from list. ",
						"If parameter is empty, a random one will be selected ",
						"from all existing portlets"));
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

	@Override
	public String getName() {
		return "AddGenericPortletLiferayGenAction";
	}

	@Override
	protected void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception {
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}