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

package com.liferay.liferaygen.web.internal.actions.user;

import com.liferay.liferaygen.config.ActionConfig;
import com.liferay.liferaygen.constants.ConfigConstants;
import com.liferay.liferaygen.impl.BaseAction;
import com.liferay.liferaygen.util.ParameterUtil;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleLocalServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
public class AddUserToRoles extends BaseAction {

	@Override
	public String doGetDescription() {
		return "Add roles to user. Role/roles can be random or specified " +
					"in parameters";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put(ActionConfig.RELATED_TARGETS, null);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					ActionConfig.RELATED_TARGETS,
					"Roles to be asigned. Three options: (1) empty=random " +
					"regular role (2) percentage=random selection of regular " +
					"roles (3) list=list of roles to add");
				put(ActionConfig.TARGET, "User to update");
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
	protected void doRun() {

		long companyId = (Long) _parameters.get(ConfigConstants.COMPANY_ID);

		long userId = (Long) _parameters.get(ActionConfig.TARGET);

		String roleTargets = (String) _parameters.get(
				ActionConfig.RELATED_TARGETS);

		List<Role> roles = new ArrayList<Role>();

		try {
			if (Validator.isNull(roleTargets)) {
				roles = RoleLocalServiceUtil.getRoles(
						companyId, RoleConstants.TYPES_REGULAR);

				roles.add(ValueGenerator.getRandomObjectFromList(roles));
			}
			else if (roleTargets.endsWith("%")) {
				roles = RoleLocalServiceUtil.getRoles(
						companyId, RoleConstants.TYPES_REGULAR);

				int percentage = ParameterUtil.getParamAsIntegerPercentage(
					_parameters, ActionConfig.RELATED_TARGETS);

				Double rolesToSelect = (roles.size() * (percentage / 100.0));

				roles = ValueGenerator.getRandomObjectsFromList(
					roles, rolesToSelect.longValue());
			}
			else {
				String[] roleNames = StringUtil.split(roleTargets);

				for (int i = 0; i < roleNames.length; i++) {
					Role role = RoleLocalServiceUtil.fetchRole(
							companyId, roleNames[i]);

					if (role != null) {
						roles.add(role);
					}
				}
			}

			RoleLocalServiceUtil.addUserRoles(userId, roles);
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

	private static Log _log = LogFactoryUtil.getLog(AddUserToRoles.class);

}