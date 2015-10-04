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

package com.liferay.document.library.repository.dropbox.service.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;
import com.liferay.document.library.repository.dropbox.model.DropboxRevision;
import com.liferay.document.library.repository.dropbox.service.base.DropboxRevisionLocalServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.Date;
import java.util.List;

/**
 * The implementation of the dropbox revision local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.document.library.repository.dropbox.service.DropboxRevisionLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DropboxRevisionLocalServiceBaseImpl
 * @see com.liferay.document.library.repository.dropbox.service.DropboxRevisionLocalServiceUtil
 */
@ProviderType
public class DropboxRevisionLocalServiceImpl
	extends DropboxRevisionLocalServiceBaseImpl {

	@Override
	public DropboxRevision addDropboxRevision(
		long repositoryId, long entryId, String path, String rev, long size) {

		DropboxRevision dropboxRevision = createDropboxRevision(
			counterLocalService.increment());

		dropboxRevision.setRepositoryId(repositoryId);
		dropboxRevision.setEntryId(entryId);
		dropboxRevision.setPath(path);
		dropboxRevision.setRev(rev);
		dropboxRevision.setSize(size);
		dropboxRevision.setCreateDate(new Date());

		return dropboxRevisionPersistence.update(dropboxRevision);
	}

	@Override
	public void deleteDropboxRevisions(long repositoryId, long entryId) {
		dropboxRevisionPersistence.removeByR_E(repositoryId, entryId);
	}

	@Override
	public DropboxRevision getDropboxRevision(
			long repositoryId, long entryId, String rev)
		throws NoSuchRevisionException {

		return dropboxRevisionPersistence.findByR_E_R(
			repositoryId, entryId, rev);
	}

	@Override
	public List<DropboxRevision> getDropboxRevisions(
			long repositoryId, long entryId)
		throws PortalException {

		return dropboxRevisionPersistence.findByR_E(repositoryId, entryId);
	}

}