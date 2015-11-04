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

package com.liferay.document.library.repository.dropbox.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link DropboxEntryLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see DropboxEntryLocalService
 * @generated
 */
@ProviderType
public class DropboxEntryLocalServiceWrapper implements DropboxEntryLocalService,
	ServiceWrapper<DropboxEntryLocalService> {
	public DropboxEntryLocalServiceWrapper(
		DropboxEntryLocalService dropboxEntryLocalService) {
		_dropboxEntryLocalService = dropboxEntryLocalService;
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry addDropboxEntry(
		long companyId, long groupId, long repositoryId, long userId,
		java.lang.String path, java.lang.String rev, java.lang.String name,
		java.lang.String description, java.lang.String changeLog, long size,
		com.liferay.document.library.repository.dropbox.constants.DropboxEntryType dropboxEntryType)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.addDropboxEntry(companyId, groupId,
			repositoryId, userId, path, rev, name, description, changeLog,
			size, dropboxEntryType);
	}

	/**
	* Adds the dropbox entry to the database. Also notifies the appropriate model listeners.
	*
	* @param dropboxEntry the dropbox entry
	* @return the dropbox entry that was added
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry addDropboxEntry(
		com.liferay.document.library.repository.dropbox.model.DropboxEntry dropboxEntry) {
		return _dropboxEntryLocalService.addDropboxEntry(dropboxEntry);
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry copyDropboxEntry(
		long companyId, long groupId, long repositoryId, long userId,
		java.lang.String fromPath, java.lang.String toPath,
		java.lang.String toRev)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.copyDropboxEntry(companyId, groupId,
			repositoryId, userId, fromPath, toPath, toRev);
	}

	/**
	* Creates a new dropbox entry with the primary key. Does not add the dropbox entry to the database.
	*
	* @param entryId the primary key for the new dropbox entry
	* @return the new dropbox entry
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry createDropboxEntry(
		long entryId) {
		return _dropboxEntryLocalService.createDropboxEntry(entryId);
	}

	/**
	* Deletes the dropbox entry from the database. Also notifies the appropriate model listeners.
	*
	* @param dropboxEntry the dropbox entry
	* @return the dropbox entry that was removed
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry deleteDropboxEntry(
		com.liferay.document.library.repository.dropbox.model.DropboxEntry dropboxEntry) {
		return _dropboxEntryLocalService.deleteDropboxEntry(dropboxEntry);
	}

	/**
	* Deletes the dropbox entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param entryId the primary key of the dropbox entry
	* @return the dropbox entry that was removed
	* @throws PortalException if a dropbox entry with the primary key could not be found
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry deleteDropboxEntry(
		long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.deleteDropboxEntry(entryId);
	}

	@Override
	public void deleteDropboxEntry(long repositoryId, java.lang.String path)
		throws com.liferay.portal.kernel.exception.PortalException {
		_dropboxEntryLocalService.deleteDropboxEntry(repositoryId, path);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.model.PersistedModel deletePersistedModel(
		com.liferay.portal.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _dropboxEntryLocalService.dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _dropboxEntryLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.document.library.repository.dropbox.model.impl.DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @return the range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return _dropboxEntryLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.document.library.repository.dropbox.model.impl.DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param dynamicQuery the dynamic query
	* @param start the lower bound of the range of model instances
	* @param end the upper bound of the range of model instances (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching rows
	*/
	@Override
	public <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return _dropboxEntryLocalService.dynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return _dropboxEntryLocalService.dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	@Override
	public long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return _dropboxEntryLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry fetchDropboxEntry(
		long entryId) {
		return _dropboxEntryLocalService.fetchDropboxEntry(entryId);
	}

	/**
	* Returns the dropbox entry matching the UUID and group.
	*
	* @param uuid the dropbox entry's UUID
	* @param groupId the primary key of the group
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry fetchDropboxEntryByUuidAndGroupId(
		java.lang.String uuid, long groupId) {
		return _dropboxEntryLocalService.fetchDropboxEntryByUuidAndGroupId(uuid,
			groupId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _dropboxEntryLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _dropboxEntryLocalService.getBeanIdentifier();
	}

	@Override
	public java.util.List<com.liferay.document.library.repository.dropbox.model.DropboxEntry> getDropboxEntries(
		long repositoryId, java.lang.String parentPath,
		com.liferay.document.library.repository.dropbox.constants.DropboxEntryType dropboxEntryType) {
		return _dropboxEntryLocalService.getDropboxEntries(repositoryId,
			parentPath, dropboxEntryType);
	}

	/**
	* Returns a range of all the dropbox entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.document.library.repository.dropbox.model.impl.DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @return the range of dropbox entries
	*/
	@Override
	public java.util.List<com.liferay.document.library.repository.dropbox.model.DropboxEntry> getDropboxEntries(
		int start, int end) {
		return _dropboxEntryLocalService.getDropboxEntries(start, end);
	}

	/**
	* Returns all the dropbox entries matching the UUID and company.
	*
	* @param uuid the UUID of the dropbox entries
	* @param companyId the primary key of the company
	* @return the matching dropbox entries, or an empty list if no matches were found
	*/
	@Override
	public java.util.List<com.liferay.document.library.repository.dropbox.model.DropboxEntry> getDropboxEntriesByUuidAndCompanyId(
		java.lang.String uuid, long companyId) {
		return _dropboxEntryLocalService.getDropboxEntriesByUuidAndCompanyId(uuid,
			companyId);
	}

	/**
	* Returns a range of dropbox entries matching the UUID and company.
	*
	* @param uuid the UUID of the dropbox entries
	* @param companyId the primary key of the company
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the range of matching dropbox entries, or an empty list if no matches were found
	*/
	@Override
	public java.util.List<com.liferay.document.library.repository.dropbox.model.DropboxEntry> getDropboxEntriesByUuidAndCompanyId(
		java.lang.String uuid, long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<com.liferay.document.library.repository.dropbox.model.DropboxEntry> orderByComparator) {
		return _dropboxEntryLocalService.getDropboxEntriesByUuidAndCompanyId(uuid,
			companyId, start, end, orderByComparator);
	}

	/**
	* Returns the number of dropbox entries.
	*
	* @return the number of dropbox entries
	*/
	@Override
	public int getDropboxEntriesCount() {
		return _dropboxEntryLocalService.getDropboxEntriesCount();
	}

	@Override
	public int getDropboxEntriesCount(long repositoryId,
		java.lang.String parentPath,
		com.liferay.document.library.repository.dropbox.constants.DropboxEntryType dropboxEntryType) {
		return _dropboxEntryLocalService.getDropboxEntriesCount(repositoryId,
			parentPath, dropboxEntryType);
	}

	/**
	* Returns the dropbox entry with the primary key.
	*
	* @param entryId the primary key of the dropbox entry
	* @return the dropbox entry
	* @throws PortalException if a dropbox entry with the primary key could not be found
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry getDropboxEntry(
		long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.getDropboxEntry(entryId);
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry getDropboxEntry(
		long repositoryId, java.lang.String path)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return _dropboxEntryLocalService.getDropboxEntry(repositoryId, path);
	}

	/**
	* Returns the dropbox entry matching the UUID and group.
	*
	* @param uuid the dropbox entry's UUID
	* @param groupId the primary key of the group
	* @return the matching dropbox entry
	* @throws PortalException if a matching dropbox entry could not be found
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry getDropboxEntryByUuidAndGroupId(
		java.lang.String uuid, long groupId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.getDropboxEntryByUuidAndGroupId(uuid,
			groupId);
	}

	@Override
	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.getPersistedModel(primaryKeyObj);
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry moveDropboxEntry(
		long companyId, long groupId, long repositoryId, long userId,
		java.lang.String fromPath, java.lang.String toPath, java.lang.String rev)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.moveDropboxEntry(companyId, groupId,
			repositoryId, userId, fromPath, toPath, rev);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_dropboxEntryLocalService.setBeanIdentifier(beanIdentifier);
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry synchronizeDropboxEntry(
		long companyId, long groupId, long repositoryId, long userId,
		java.lang.String path, java.lang.String rev, java.lang.String name,
		long size,
		com.liferay.document.library.repository.dropbox.constants.DropboxEntryType dropboxEntryType)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.synchronizeDropboxEntry(companyId,
			groupId, repositoryId, userId, path, rev, name, size,
			dropboxEntryType);
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry updateDropboxEntry(
		long companyId, long groupId, long repositoryId, long userId,
		java.lang.String path, java.lang.String rev, long size)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxEntryLocalService.updateDropboxEntry(companyId, groupId,
			repositoryId, userId, path, rev, size);
	}

	/**
	* Updates the dropbox entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param dropboxEntry the dropbox entry
	* @return the dropbox entry that was updated
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry updateDropboxEntry(
		com.liferay.document.library.repository.dropbox.model.DropboxEntry dropboxEntry) {
		return _dropboxEntryLocalService.updateDropboxEntry(dropboxEntry);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public DropboxEntryLocalService getWrappedDropboxEntryLocalService() {
		return _dropboxEntryLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedDropboxEntryLocalService(
		DropboxEntryLocalService dropboxEntryLocalService) {
		_dropboxEntryLocalService = dropboxEntryLocalService;
	}

	@Override
	public DropboxEntryLocalService getWrappedService() {
		return _dropboxEntryLocalService;
	}

	@Override
	public void setWrappedService(
		DropboxEntryLocalService dropboxEntryLocalService) {
		_dropboxEntryLocalService = dropboxEntryLocalService;
	}

	private DropboxEntryLocalService _dropboxEntryLocalService;
}