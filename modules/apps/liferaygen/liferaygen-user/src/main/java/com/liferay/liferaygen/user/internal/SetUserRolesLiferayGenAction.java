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

package com.liferay.liferaygen.user.internal;

import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.util.LiferayGenParameterHandler;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.List;
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
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.user.internal.SetUserRolesLiferayGenAction",
	service = LiferayGenAction.class
)
public class SetUserRolesLiferayGenAction extends BaseLiferayGenAction {

	@Override
	public String doGetDescription() {
		return "Add roles to user. Role/roles can be random or specified in " +
			"parameters";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put(LiferayGenActionConfig.RELATED_TARGETS, null);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					LiferayGenActionConfig.RELATED_TARGETS,
					StringBundler.concat(
						"Roles to be asigned. Three options: (1) empty = ",
						"random regular role (2) percentage = random ",
						"selection of regular roles (3) list = list of roles ",
						"to add"));
				put(LiferayGenActionConfig.TARGET, "User to update");
			}
		};
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return User.class;
	}

	@Override
	public String getEntityProperties() {
		return "userId";
	}

	@Override
	public String getName() {
		return "SetUserRolesLiferayGenAction";
	}

	@Override
	protected void doRun() {
		Map<String, Object> parameters = getParameters();

		long companyId = (Long)parameters.get(
			LiferayGenConfigConstants.COMPANY_ID);

		long userId = (Long)parameters.get(LiferayGenActionConfig.TARGET);

		String targetRoles = (String)parameters.get(
			LiferayGenActionConfig.RELATED_TARGETS);

		List<Role> roles = new ArrayList<>();

		try {
			LiferayGenValueGenerator liferayGenValueGenerator =
				new LiferayGenValueGenerator(
					_companyLocalService, _liferayGenQueryHandler, _portal,
					_portletLocalService);

			if (Validator.isNull(targetRoles)) {
				roles = _roleLocalService.getRoles(
					companyId, RoleConstants.TYPES_REGULAR);

				roles.add(
					liferayGenValueGenerator.getRandomObjectFromList(roles));
			}
			else if (targetRoles.endsWith("%")) {
				roles = _roleLocalService.getRoles(
					companyId, RoleConstants.TYPES_REGULAR);

				int percentage =
					_liferayGenParameterHandler.getParamAsIntegerPercentage(
						parameters, LiferayGenActionConfig.RELATED_TARGETS);

				Double rolesToSelect = roles.size() * (percentage / 100.0);

				roles = liferayGenValueGenerator.getRandomObjectsFromList(
					roles, rolesToSelect.longValue());
			}
			else {
				String[] roleNames = StringUtil.split(targetRoles);

				for (String roleName : roleNames) {
					Role role = _roleLocalService.fetchRole(
						companyId, roleName);

					if (role != null) {
						roles.add(role);
					}
				}
			}

			_roleLocalService.addUserRoles(userId, roles);
		}
		catch (Exception e) {
			String message = e.getMessage();

			//multi-threading error

			if (message.contains(
					"org.springframework.dao.DuplicateKeyException")) {

				return;
			}

			_log.error(e, e);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SetUserRolesLiferayGenAction.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenParameterHandler _liferayGenParameterHandler;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private RoleLocalService _roleLocalService;

}