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

import com.liferay.document.library.repository.dropbox.constants.DropboxEntryType;
import com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;
import com.liferay.document.library.repository.dropbox.model.DropboxEntry;
import com.liferay.document.library.repository.dropbox.service.base.DropboxEntryLocalServiceBaseImpl;
import com.liferay.portal.NoSuchRepositoryEntryException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.RepositoryEntry;

import java.util.List;

/**
 * The implementation of the dropbox entry local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.document.library.repository.dropbox.service.DropboxEntryLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DropboxEntryLocalServiceBaseImpl
 * @see com.liferay.document.library.repository.dropbox.service.DropboxEntryLocalServiceUtil
 */
@ProviderType
public class DropboxEntryLocalServiceImpl
	extends DropboxEntryLocalServiceBaseImpl {

	@Override
	public DropboxEntry addDropboxEntry(
			long companyId, long groupId, long repositoryId, long userId,
			String path, String rev, String name, String description,
			String changeLog, long size, DropboxEntryType dropboxEntryType)
		throws PortalException {

		RepositoryEntry repositoryEntry =
			repositoryEntryLocalService.getRepositoryEntry(
				userId, groupId, repositoryId, path);

		DropboxEntry dropboxEntry = createDropboxEntry(
			repositoryEntry.getRepositoryEntryId());

		dropboxEntry.setCompanyId(companyId);
		dropboxEntry.setGroupId(groupId);
		dropboxEntry.setRepositoryId(repositoryId);
		dropboxEntry.setUserId(userId);
		dropboxEntry.setParentPath(getParentPath(path));
		dropboxEntry.setPath(path);
		dropboxEntry.setRev(rev);
		dropboxEntry.setName(name);
		dropboxEntry.setDescription(description);
		dropboxEntry.setChangeLog(changeLog);
		dropboxEntry.setSize(size);
		dropboxEntry.setType(dropboxEntryType.getType());

		dropboxEntry = addDropboxEntry(dropboxEntry);

		dropboxRevisionLocalService.addDropboxRevision(
			repositoryId, dropboxEntry.getEntryId(), path, rev, size);

		return dropboxEntry;
	}

	@Override
	public DropboxEntry copyDropboxEntry(
			long companyId, long groupId, long repositoryId, long userId,
			String fromPath, String toPath, String toRev)
		throws PortalException {

		RepositoryEntry repositoryEntry =
			repositoryEntryLocalService.getRepositoryEntry(
				userId, groupId, repositoryId, fromPath);

		DropboxEntry fromDropboxEntry = dropboxEntryPersistence.findByR_P(
			repositoryId, fromPath);

		return addDropboxEntry(
			fromDropboxEntry.getCompanyId(), fromDropboxEntry.getGroupId(),
			fromDropboxEntry.getRepositoryId(), userId, toPath, toRev,
			fromDropboxEntry.getName(), fromDropboxEntry.getDescription(),
			fromDropboxEntry.getChangeLog(), fromDropboxEntry.getSize(),
			DropboxEntryType.fromType(fromDropboxEntry.getType()));
	}

	@Override
	public void deleteDropboxEntry(long repositoryId, String path)
		throws PortalException {

		try {
			repositoryEntryLocalService.deleteRepositoryEntry(
				repositoryId, path);
		}
		catch (NoSuchRepositoryEntryException nsree) {
		}

		DropboxEntry dropboxEntry = dropboxEntryPersistence.fetchByR_P(
			repositoryId, path);

		if (dropboxEntry == null) {
			return;
		}

		dropboxEntryPersistence.remove(dropboxEntry);

		dropboxRevisionLocalService.deleteDropboxRevisions(
			repositoryId, dropboxEntry.getEntryId());
	}

	@Override
	public List<DropboxEntry> getDropboxEntries(
		long repositoryId, String parentPath,
		DropboxEntryType dropboxEntryType) {

		if (dropboxEntryType == DropboxEntryType.ANY) {
			return dropboxEntryPersistence.findByR_PP(repositoryId, parentPath);
		}

		return dropboxEntryPersistence.findByR_PP_T(
			repositoryId, parentPath, dropboxEntryType.getType());
	}

	@Override
	public int getDropboxEntriesCount(
		long repositoryId, String parentPath,
		DropboxEntryType dropboxEntryType) {

		if (dropboxEntryType == DropboxEntryType.ANY) {
			return dropboxEntryPersistence.countByR_PP(
				repositoryId, parentPath);
		}

		return dropboxEntryPersistence.countByR_PP_T(
			repositoryId, parentPath, dropboxEntryType.getType());
	}

	@Override
	public DropboxEntry getDropboxEntry(long repositoryId, String path)
		throws NoSuchEntryException {

		return dropboxEntryPersistence.findByR_P(repositoryId, path);
	}

	@Override
	public DropboxEntry moveDropboxEntry(
			long companyId, long groupId, long repositoryId, long userId,
			String fromPath, String toPath, String rev)
		throws PortalException {

		RepositoryEntry repositoryEntry =
			repositoryEntryLocalService.getRepositoryEntry(
				userId, groupId, repositoryId, fromPath);

		repositoryEntry.setMappedId(toPath);

		repositoryEntryLocalService.updateRepositoryEntry(repositoryEntry);

		DropboxEntry dropboxEntry = dropboxEntryPersistence.findByR_P(
			repositoryId, fromPath);

		dropboxEntry.setParentPath(getParentPath(toPath));
		dropboxEntry.setPath(toPath);
		dropboxEntry.setRev(rev);

		dropboxEntry = dropboxEntryPersistence.update(dropboxEntry);

		dropboxRevisionLocalService.addDropboxRevision(
			repositoryId, dropboxEntry.getEntryId(), toPath, rev,
			dropboxEntry.getSize());

		return dropboxEntry;
	}

	@Override
	public DropboxEntry synchronizeDropboxEntry(
			long companyId, long groupId, long repositoryId, long userId,
			String path, String rev, String name, long size,
			DropboxEntryType dropboxEntryType)
		throws PortalException {

		DropboxEntry dropboxEntry = dropboxEntryPersistence.fetchByR_P(
			repositoryId, path);

		if (dropboxEntry == null) {
			return addDropboxEntry(
				companyId, groupId, repositoryId, userId, path, rev, name,
				StringPool.BLANK, StringPool.BLANK, size, dropboxEntryType);
		}

		return updateDropboxEntry(
			companyId, groupId, repositoryId, userId, path, rev, size);
	}

	@Override
	public DropboxEntry updateDropboxEntry(
			long companyId, long groupId, long repositoryId, long userId,
			String path, String rev, long size)
		throws PortalException {

		DropboxEntry dropboxEntry = dropboxEntryPersistence.findByR_P(
			repositoryId, path);

		dropboxEntry.setRev(rev);
		dropboxEntry.setSize(size);

		dropboxEntry = dropboxEntryPersistence.update(dropboxEntry);

		dropboxRevisionLocalService.addDropboxRevision(
			repositoryId, dropboxEntry.getEntryId(), path, rev, size);

		return dropboxEntry;
	}

	protected String getParentPath(String path) {
		if (path.equals(StringPool.SLASH)) {
			return null;
		}

		int i = path.lastIndexOf(CharPool.SLASH);

		if (i == 0) {
			return StringPool.SLASH;
		}

		return path.substring(0, i);
	}

	private static final String _REPOSITORY_CLASS_NAME =
		"com.liferay.document.library.repository.dropbox.DropboxRepository";

}