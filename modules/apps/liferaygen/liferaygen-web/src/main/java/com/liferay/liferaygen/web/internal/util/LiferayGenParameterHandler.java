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

package com.liferay.liferaygen.web.internal.util;

import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.web.internal.config.constants.LiferayGenConfigConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(immediate = true, service = LiferayGenParameterHandler.class)
public class LiferayGenParameterHandler {

	public void cleanupIntegerParameters(Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();

			if (value instanceof Integer) {
				Integer integer = (Integer)value;

				map.put(entry.getKey(), integer.longValue());
			}
		}
	}

	public Map<String, Object> getBackedParameters(
		LiferayGenAction liferayGenAction, Map<String, Object> configuration,
		Map<String, Object> parameters) {

		if (configuration == null) {
			configuration = Collections.emptyMap();
		}

		if (parameters == null) {
			parameters = Collections.emptyMap();
		}

		Map<String, Object> effectiveParameters = new HashMap<>(
			liferayGenAction.getParametersDefaultValues());

		Set<String> keys = new HashSet<>(effectiveParameters.keySet());

		keys.addAll(parameters.keySet());

		Map<String, String> parametersDescription =
			liferayGenAction.getParametersDescription();

		keys.removeAll(parametersDescription.keySet());

		if (_log.isWarnEnabled()) {
			for (String key : keys) {
				Class<? extends LiferayGenAction> clazz =
					liferayGenAction.getClass();

				_log.warn(
					StringBundler.concat(
						"parameter ", key,
						" is not documented for liferayGenAction ",
						clazz.getName()));
			}
		}

		effectiveParameters.putAll(configuration);

		effectiveParameters.remove(LiferayGenConfigConstants.ACTIONS);

		effectiveParameters.putAll(parameters);

		cleanupIntegerParameters(effectiveParameters);

		return effectiveParameters;
	}

	public int getParamAsIntegerPercentage(
		Map<String, Object> parameters, String parameter) {

		if (!parameters.containsKey(parameter)) {
			throw new IllegalArgumentException(
				"missing parameter " + parameter);
		}

		String parameterValue = String.valueOf(parameters.get(parameter));

		if (parameterValue.endsWith("%")) {
			parameterValue = parameterValue.substring(
				0, parameterValue.length() - 1);
		}

		int percentageValue = GetterUtil.getInteger(parameterValue);

		if (percentageValue < 0) {
			return 0;
		}

		if (percentageValue > 100) {
			return 100;
		}

		return percentageValue;
	}

	protected static List<Long> getGroupIds(Map<String, Object> parameters) {
		List list = (List)parameters.get(LiferayGenConfigConstants.GROUP_IDS);

		List<Long> groupIds = new ArrayList<>();

		if (list == null) {
			list = Collections.emptyList();
		}

		for (Object o : list) {
			if (o instanceof Number) {
				groupIds.add(((Number)o).longValue());
			}
			else {
				String str = o.toString();

				groupIds.add(GetterUtil.getLong(str));
			}
		}

		if (groupIds.isEmpty()) {
			Long groupId = (Long)parameters.get(
				LiferayGenConfigConstants.GROUP_ID);

			return Collections.singletonList(groupId);
		}

		return groupIds;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayGenParameterHandler.class);

}