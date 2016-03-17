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

package com.liferay.blogs.web.upload;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.ResourcePermissionCheckerUtil;
import com.liferay.portal.kernel.upload.BaseUploadHandler;
import com.liferay.portlet.blogs.service.permission.BlogsPermission;

/**
 * @author Sergio González
 * @author Adolfo Pérez
 */
public abstract class BaseBlogsUploadHandler extends BaseUploadHandler {

	public BaseBlogsUploadHandler(
		long maxFileSize, String[] validExtensions) {

		_maxFileSize = maxFileSize;
		_validExtensions = validExtensions;
	}

	@Override
	protected void checkPermission(
			long groupId, long folderId, PermissionChecker permissionChecker)
		throws PortalException {

		boolean containsResourcePermission =
			ResourcePermissionCheckerUtil.containsResourcePermission(
				permissionChecker, BlogsPermission.RESOURCE_NAME, groupId,
				ActionKeys.ADD_ENTRY);

		if (!containsResourcePermission) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, BlogsPermission.RESOURCE_NAME, groupId,
				ActionKeys.ADD_ENTRY);
		}
	}

	@Override
	protected long getMaxFileSize() {
		return _maxFileSize;
	}

	@Override
	protected String[] getValidExtensions() {
		return _validExtensions;
	}

	@Override
	protected String getParameterName() {
		return "imageSelectorFileName";
	}

	private long _maxFileSize;
	private String[] _validExtensions;

}
