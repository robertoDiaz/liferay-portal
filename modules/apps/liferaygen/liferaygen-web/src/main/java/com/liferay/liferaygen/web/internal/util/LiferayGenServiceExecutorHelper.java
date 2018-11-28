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

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.service.PersistedModelLocalService;

import java.lang.reflect.Method;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(immediate = true, service = LiferayGenServiceExecutorHelper.class)
public class LiferayGenServiceExecutorHelper {

	public Object executeServiceMethod(
		PersistedModelLocalService persistedModelLocalService,
		String methodName, Class<?> parameterType, Object... arg) {

		Class<?>[] parameterTypes = null;

		if (parameterType != null) {
			parameterTypes = new Class<?>[] {parameterType};
		}

		return executeServiceMethod(
			persistedModelLocalService, methodName, parameterTypes, arg);
	}

	public Object executeServiceMethod(
		PersistedModelLocalService persistedModelLocalService,
		String methodName, Class<?>[] parameterTypes, Object... arg) {

		try {
			Method method = getLocalServiceMethod(
				persistedModelLocalService, methodName, parameterTypes);

			if (method == null) {
				return null;
			}

			if (arg == null) {
				return method.invoke(persistedModelLocalService);
			}

			return method.invoke(persistedModelLocalService, arg);
		}
		catch (Exception e) {
			if (e instanceof NoSuchMethodException) {
				throw new RuntimeException(
					StringBundler.concat(
						"executeMethod: ", methodName, " method not found for ",
						persistedModelLocalService, e));
			}

			String cause = StringPool.BLANK;

			Throwable rootException = e.getCause();

			if (rootException != null) {
				cause = " (root cause: " + rootException.getMessage() + ")";
			}

			throw new RuntimeException(
				StringBundler.concat(
					"executeMethod: ", methodName, " method for ",
					persistedModelLocalService, cause, e));
		}
	}

	protected static Method getLocalServiceMethod(
			PersistedModelLocalService persistedModelLocalService,
			String methodName, Class<?>... parameterTypes)
		throws Exception {

		Class<?> classLocalService = persistedModelLocalService.getClass();

		return classLocalService.getMethod(methodName, parameterTypes);
	}

}