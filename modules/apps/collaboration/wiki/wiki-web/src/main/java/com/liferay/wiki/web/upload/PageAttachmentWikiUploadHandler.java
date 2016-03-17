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

package com.liferay.wiki.web.upload;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.upload.BaseUploadHandler;
import com.liferay.portal.util.PropsValues;
import com.liferay.wiki.model.WikiPage;
import com.liferay.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.wiki.service.WikiPageServiceUtil;
import com.liferay.wiki.service.permission.WikiNodePermissionChecker;

import java.io.InputStream;

/**
 * @author Roberto Díaz
 */
public class PageAttachmentWikiUploadHandler extends BaseUploadHandler {

	private long _maxFileSize;
	private String[] _validExtensions;

	public PageAttachmentWikiUploadHandler(
		long classPK, long maxFileSize, String[] validExtensions) {

		_classPK = classPK;
		_maxFileSize = maxFileSize;
		_validExtensions = validExtensions;
	}

	private PageAttachmentWikiUploadHandler() {
		this(0, 0, null);
	}

	@Override
	protected FileEntry addFileEntry(
			long userId, long groupId, long folderId, String fileName,
			String contentType, InputStream inputStream, long size,
			ServiceContext serviceContext)
		throws PortalException {

		WikiPage page = WikiPageLocalServiceUtil.getPage(_classPK);

		return WikiPageServiceUtil.addPageAttachment(
			page.getNodeId(), page.getTitle(), fileName, inputStream,
			contentType);
	}

	@Override
	protected void checkPermission(
			long groupId, long folderId, PermissionChecker permissionChecker)
		throws PortalException {

		WikiPage page = WikiPageLocalServiceUtil.getPage(_classPK);

		WikiNodePermissionChecker.check(
			permissionChecker, page.getNodeId(), ActionKeys.ADD_ATTACHMENT);
	}

	@Override
	protected FileEntry fetchFileEntry(
			long userId, long groupId, long folderId, String fileName)
		throws PortalException {

		try {
			WikiPage page = WikiPageLocalServiceUtil.getPage(_classPK);

			Folder folder = page.addAttachmentsFolder();

			return PortletFileRepositoryUtil.getPortletFileEntry(
				groupId, folder.getFolderId(), fileName);
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
	protected String getParameterName() {
		return "imageSelectorFileName";
	}

	private final long _classPK;

}