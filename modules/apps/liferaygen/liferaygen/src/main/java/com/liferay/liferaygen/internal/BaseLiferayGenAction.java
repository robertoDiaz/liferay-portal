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

package com.liferay.liferaygen.internal;

import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.internal.config.constants.LiferayGenConfigConstants;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Collections;
import java.util.Map;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public abstract class BaseLiferayGenAction implements LiferayGenAction {

	public void configure(Map<String, Object> parameters) {
		_parameters = parameters;
	}

	@Override
	public void destroy() {
	}

	public abstract String doGetDescription();

	public abstract Map<String, Object> doGetParametersDefaultValues();

	public abstract Map<String, String> doGetParametersDescription();

	public String getDescription() {
		String description = doGetDescription();

		if (Validator.isNull(description)) {
			return StringPool.BLANK;
		}

		return description;
	}

	@Override
	public Criterion getEntityFilter() {
		return null;
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return null;
	}

	@Override
	public String getEntityModelPK() {
		return null;
	}

	@Override
	public String getEntityProperties() {
		return null;
	}

	public Criterion getFilterByGroupId() {
		if (!hasScopeByGroupId()) {
			return null;
		}

		long groupId = GetterUtil.getLong(
			_parameters.get(LiferayGenConfigConstants.GROUP_ID));

		if (groupId == 0) {
			return null;
		}

		return RestrictionsFactoryUtil.eq("groupId", groupId);
	}

	public final Map<String, Object> getParametersDefaultValues() {
		return _getUnmodifiableMap(doGetParametersDefaultValues());
	}

	public final Map<String, String> getParametersDescription() {
		return _getUnmodifiableMap(doGetParametersDescription());
	}

	public boolean hasScopeByGroupId() {
		Class<? extends ClassedModel> entityModel = getEntityModel();

		if (entityModel == null) {
			return true;
		}

		return GroupedModel.class.isAssignableFrom(entityModel);
	}

	@Override
	public void init() {
	}

	@Override
	public void run() {
		try {
			doRun();
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected abstract void doRun();

	private <T> Map<String, T> _getUnmodifiableMap(Map<String, T> map) {
		if (map == null) {
			return Collections.emptyMap();
		}

		return Collections.unmodifiableMap(map);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BaseLiferayGenAction.class);

	private Map<String, Object> _parameters;

}