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

package com.liferay.liferaygen.web.internal.portlet;

import com.liferay.liferaygen.constants.LiferaygenPortletKeys;
import com.liferay.liferaygen.util.LiferayGenConfigHandler;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.liferaygen.web.internal.DefaultLiferayGenExecutor;
import com.liferay.liferaygen.web.internal.util.LiferayGenExecutorHandler;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.portlet.Portlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.css-class-wrapper=portlet-blogs",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.icon=/liferaygen/icons/icon.png",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.preferences-unique-per-layout=false",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.scopeable=true",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Liferay Gen",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.mvc-command-names-default-views=/lifearygen/view",
		"javax.portlet.init-param.portlet-title-based-navigation=true",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.name=" + LiferaygenPortletKeys.LIFERAYGEN_ADMIN,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=administrator",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class LiferaygenAdminPortlet extends MVCPortlet {

	public static final String PARAM_CONFIGURATION = "configuration";

	public void generate(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		_portal.copyRequestParameters(actionRequest, actionResponse);

		try {
			String configurationText = ParamUtil.getString(
				actionRequest, PARAM_CONFIGURATION);

			Map<String, Object> configuration =
				_liferaygenConfigHandler.getConfiguration(configurationText);

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
		catch (Throwable t) {
			StringWriter sw = new StringWriter();
			t.printStackTrace(new PrintWriter(sw));
			actionRequest.setAttribute("errorMessage", sw.toString());
		}
	}

	@Reference
	private Portal _portal;

	@Reference
	LiferayGenConfigHandler _liferaygenConfigHandler;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private LiferayGenExecutorHandler _liferayGenExecutorHandler;

	@Reference
	private PortletLocalService _portletLocalService;

}