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

package com.liferay.document.library.web.upload;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.BaseUploadHandler;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.documentlibrary.service.permission.DLFolderPermission;

import java.io.InputStream;

/**
 * @author Roberto Díaz
 * @author Sergio González
 */
public class FileEntryDLUploadHandler extends BaseUploadHandler {

	private long _maxFileSize;
	private String[] _validExtensions;

	private FileEntryDLUploadHandler() {
	}

	public FileEntryDLUploadHandler(
		long maxFileSize, String[] validExtensions) {

		_maxFileSize = maxFileSize;
		_validExtensions = validExtensions;
	}

	@Override
	protected FileEntry addFileEntry(
			long userId, long groupId, long folderId, String fileName,
			String contentType, InputStream inputStream, long size,
			ServiceContext serviceContext)
		throws PortalException {

		return DLAppServiceUtil.addFileEntry(
			groupId, folderId, fileName, contentType, fileName,
			StringPool.BLANK, StringPool.BLANK, inputStream, size,
			serviceContext);
	}

	@Override
	protected void checkPermission(
			long groupId, long folderId, PermissionChecker permissionChecker)
		throws PortalException {

		DLFolderPermission.check(
			permissionChecker, groupId, folderId, ActionKeys.ADD_DOCUMENT);
	}

	@Override
	protected FileEntry fetchFileEntry(
			long userId, long groupId, long folderId, String fileName)
		throws PortalException {

		try {
			return DLAppServiceUtil.getFileEntry(groupId, folderId, fileName);
		}
		catch (PortalException pe) {
			return null;
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
	protected long getFolderId(UploadPortletRequest uploadPortletRequest) {
		return ParamUtil.getLong(uploadPortletRequest, "folderId");
	}

	@Override
	protected String getParameterName() {
		return "imageSelectorFileName";
	}

	@Override
	protected ServiceContext getServiceContext(
			UploadPortletRequest uploadPortletRequest)
		throws PortalException {

		return ServiceContextFactory.getInstance(
			DLFileEntry.class.getName(), uploadPortletRequest);
	}

	@Override
	protected String getURL(FileEntry fileEntry, ThemeDisplay themeDisplay) {
		try {
			return DLUtil.getPreviewURL(
				fileEntry, fileEntry.getLatestFileVersion(), themeDisplay,
				StringPool.BLANK);
		}
		catch (PortalException pe) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to get URL for file entry " +
						fileEntry.getFileEntryId());
			}
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FileEntryDLUploadHandler.class);

}