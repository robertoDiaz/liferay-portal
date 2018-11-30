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

package com.liferay.liferaygen.dummy.internal;

import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.User;

import java.util.Arrays;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.dummy.internal.LiferayGenActionDummy",
	service = LiferayGenAction.class
)
public class LiferayGenActionDummy extends BaseLiferayGenAction {

	@Override
	public String doGetDescription() {
		return "Dummy action only for tests purposes";
	}

	@Override
	public Map<String, Object> doGetParametersDefaultValues() {
		return null;
	}

	@Override
	public Map<String, String> doGetParametersDescription() {
		return null;
	}

	public Class<? extends ClassedModel> getEntityModel() {
		return User.class;
	}

	public String getEntityProperties() {
		return "userId,contactId";
	}

	@Override
	public String getName() {
		return "LiferayGenActionDummy";
	}

	protected void doRun() {
		_log.error("start doRun");

		Map<String, Object> parameters = getParameters();

		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			Object value = entry.getValue();

			Class<?> clazz = value.getClass();

			if (clazz.isArray()) {
				value = Arrays.toString((Object[])value);
			}

			_log.error(entry.getKey() + ": " + value);
		}

		_log.error("end doRun");
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayGenActionDummy.class);

}