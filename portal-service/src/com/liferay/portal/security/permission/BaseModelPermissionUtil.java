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

package com.liferay.portal.security.permission;

import com.liferay.registry.collections.ServiceTrackerCollections;
import com.liferay.registry.collections.ServiceTrackerMap;

/**
 * @author Roberto Díaz
 */
public class BaseModelPermissionUtil {

	public static Boolean containsBaseModelPermission(
		PermissionChecker permissionChecker, long groupId, String className,
		long classPK, String actionId) {

		BaseModelPermission baseModelPermission = _serviceTrackerMap.getService(
			className);

		if (baseModelPermission == null) {
			return null;
		}

		try {
			baseModelPermission.checkBaseModel(
				permissionChecker, groupId, classPK, actionId);
		}
		catch (Exception e) {
			return false;
		}

		return true;
	}

	private static final ServiceTrackerMap<String, BaseModelPermission>
		_serviceTrackerMap = ServiceTrackerCollections.singleValueMap(
			BaseModelPermission.class, "model.class.name");

	static {
		_serviceTrackerMap.open();
	}

}