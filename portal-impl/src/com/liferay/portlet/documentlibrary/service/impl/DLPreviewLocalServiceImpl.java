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

package com.liferay.portlet.documentlibrary.service.impl;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLPreview;
import com.liferay.document.library.kernel.model.DLPreviewConstants;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.document.library.kernel.service.persistence.DLPreviewUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portlet.documentlibrary.service.base.DLPreviewLocalServiceBaseImpl;

import java.util.List;

/**
 * The implementation of the document library preview local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.document.library.kernel.service.DLPreviewLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @author Lianne Louie
 * @see DLPreviewLocalServiceBaseImpl
 * @see com.liferay.document.library.kernel.service.DLPreviewLocalServiceUtil
 */
public class DLPreviewLocalServiceImpl extends DLPreviewLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.liferay.document.library.kernel.service.DLPreviewLocalServiceUtil} to access the document library preview local service.
	 */
	public DLPreview addDLPreview(long fileEntryId, long fileVersionId)
		throws PortalException {

		return addDLPreview(
			fileEntryId, fileVersionId, DLPreviewConstants.STATUS_NOT_CREATED);
	}

	public DLPreview addDLPreview(
			long fileEntryId, long fileVersionId, String status)
		throws PortalException {

		long fileEncryptionId = counterLocalService.increment();

		DLPreview dlPreview = dlPreviewPersistence.create(fileEncryptionId);

		dlPreview.setFileEntryId(fileEntryId);
		dlPreview.setFileVersionId(fileVersionId);
		dlPreview.setStatus(status);

		DLFileEntry dlFileEntry = DLFileEntryLocalServiceUtil.getDLFileEntry(
			fileEntryId);

		long groupId = dlFileEntry.getGroupId();

		dlPreview.setGroupId(groupId);

		dlPreviewPersistence.update(dlPreview);

		return dlPreview;
	}

	public DLPreview fetchDLPreview(FileVersion fileVersion) {
		long fileVersionId = fileVersion.getFileVersionId();

		List<DLPreview> dlPreviewList = DLPreviewUtil.findByFileVersionId(
			fileVersionId);

		if (dlPreviewList.isEmpty()) {
			return null;
		}

		return dlPreviewList.get(0);
	}

	public String getDLPreviewStatus(FileVersion fileVersion) {
		long fileVersionId = fileVersion.getFileVersionId();

		List<DLPreview> dlPreviewList = DLPreviewUtil.findByFileVersionId(
			fileVersionId);

		if (dlPreviewList.isEmpty()) {
			return null;
		}

		DLPreview dlPreview = dlPreviewList.get(0);

		return dlPreview.getStatus();
	}

	public String getDLPreviewStatus(long fileEntryId) throws PortalException {
		DLFileEntry fileEntry = DLFileEntryLocalServiceUtil.getFileEntry(
			fileEntryId);

		FileVersion fileVersion = (FileVersion)fileEntry.getLatestFileVersion(
			true);

		return getDLPreviewStatus(fileVersion);
	}

	public DLPreview updateDLPreview(
		long dlPreviewId, long fileEntryId, long fileVersionId, String status) {

		DLPreview dlPreview = DLPreviewUtil.fetchByPrimaryKey(dlPreviewId);

		dlPreview.setFileEntryId(fileEntryId);
		dlPreview.setFileVersionId(fileVersionId);
		dlPreview.setStatus(status);

		dlPreviewPersistence.update(dlPreview);

		return dlPreview;
	}

}