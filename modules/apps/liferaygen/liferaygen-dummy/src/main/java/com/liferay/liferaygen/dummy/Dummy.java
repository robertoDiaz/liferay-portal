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

package com.liferay.liferaygen.web.internal.actions;

import com.liferay.liferaygen.impl.BaseAction;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.User;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
public class Dummy extends BaseAction {

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

	protected void doRun() {
		_log.error("start doRun");

		for (Entry<String, Object> entry : _parameters.entrySet()) {
			Object value = entry.getValue();

			if (value.getClass().isArray()) {
				value = Arrays.toString((Object[])value);
			}

			_log.error(entry.getKey() + ": " + value);
		}

		_log.error("end doRun");
	}

	private static Log _log = LogFactoryUtil.getLog(Dummy.class);

}