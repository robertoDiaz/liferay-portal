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

package com.liferay.liferaygen.web.internal.portlet.action;

import com.liferay.liferaygen.constants.LiferayGenPortletKeys;
import com.liferay.liferaygen.util.LiferayGenConfigHandler;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.liferaygen.web.internal.DefaultLiferayGenExecutor;
import com.liferay.liferaygen.web.internal.util.LiferayGenExecutorHandler;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

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
	property = {
		"javax.portlet.name=" + LiferayGenPortletKeys.LIFERAY_GEN_ADMIN,
		"mvc.command.name=/liferaygen/edit_liferaygen_action"
	},
	service = MVCActionCommand.class
)
public class EditLiferayGenActionMVCActionCommand extends BaseMVCActionCommand {

	public static final String PARAM_CONFIGURATION = "configuration";

	@Override
	protected void doProcessAction(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		_portal.copyRequestParameters(actionRequest, actionResponse);

		try {
			String configurationText = ParamUtil.getString(
				actionRequest, PARAM_CONFIGURATION);

			Map<String, Object> configuration =
				_liferayGenConfigHandler.getConfiguration(configurationText);

			HttpServletRequest httpServletRequest =
				_portal.getHttpServletRequest(actionRequest);

			LiferayGenValueGenerator liferayGenValueGenerator =
				new LiferayGenValueGenerator(
					_companyLocalService, _liferayGenQueryHandler, _portal,
					_portletLocalService);

			liferayGenValueGenerator.setServletContext(
				httpServletRequest.getServletContext());

			DefaultLiferayGenExecutor defaultLiferayGenExecutor =
				new DefaultLiferayGenExecutor(
					configuration, _liferayGenExecutorHandler,
					liferayGenValueGenerator);

			defaultLiferayGenExecutor.run();
		}
		catch (Exception e) {
			actionRequest.setAttribute("errorMessage", e.toString());
		}
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenConfigHandler _liferayGenConfigHandler;

	@Reference
	private LiferayGenExecutorHandler _liferayGenExecutorHandler;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}