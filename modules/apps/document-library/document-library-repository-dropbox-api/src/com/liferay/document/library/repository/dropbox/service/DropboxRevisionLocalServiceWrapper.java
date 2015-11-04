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
 * Provides a wrapper for {@link DropboxRevisionLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see DropboxRevisionLocalService
 * @generated
 */
@ProviderType
public class DropboxRevisionLocalServiceWrapper
	implements DropboxRevisionLocalService,
		ServiceWrapper<DropboxRevisionLocalService> {
	public DropboxRevisionLocalServiceWrapper(
		DropboxRevisionLocalService dropboxRevisionLocalService) {
		_dropboxRevisionLocalService = dropboxRevisionLocalService;
	}

	/**
	* Adds the dropbox revision to the database. Also notifies the appropriate model listeners.
	*
	* @param dropboxRevision the dropbox revision
	* @return the dropbox revision that was added
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision addDropboxRevision(
		com.liferay.document.library.repository.dropbox.model.DropboxRevision dropboxRevision) {
		return _dropboxRevisionLocalService.addDropboxRevision(dropboxRevision);
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision addDropboxRevision(
		long repositoryId, long entryId, java.lang.String path,
		java.lang.String rev, long size) {
		return _dropboxRevisionLocalService.addDropboxRevision(repositoryId,
			entryId, path, rev, size);
	}

	/**
	* Creates a new dropbox revision with the primary key. Does not add the dropbox revision to the database.
	*
	* @param revisionId the primary key for the new dropbox revision
	* @return the new dropbox revision
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision createDropboxRevision(
		long revisionId) {
		return _dropboxRevisionLocalService.createDropboxRevision(revisionId);
	}

	/**
	* Deletes the dropbox revision from the database. Also notifies the appropriate model listeners.
	*
	* @param dropboxRevision the dropbox revision
	* @return the dropbox revision that was removed
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision deleteDropboxRevision(
		com.liferay.document.library.repository.dropbox.model.DropboxRevision dropboxRevision) {
		return _dropboxRevisionLocalService.deleteDropboxRevision(dropboxRevision);
	}

	/**
	* Deletes the dropbox revision with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param revisionId the primary key of the dropbox revision
	* @return the dropbox revision that was removed
	* @throws PortalException if a dropbox revision with the primary key could not be found
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision deleteDropboxRevision(
		long revisionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxRevisionLocalService.deleteDropboxRevision(revisionId);
	}

	@Override
	public void deleteDropboxRevisions(long repositoryId, long entryId) {
		_dropboxRevisionLocalService.deleteDropboxRevisions(repositoryId,
			entryId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.model.PersistedModel deletePersistedModel(
		com.liferay.portal.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxRevisionLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _dropboxRevisionLocalService.dynamicQuery();
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
		return _dropboxRevisionLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.document.library.repository.dropbox.model.impl.DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _dropboxRevisionLocalService.dynamicQuery(dynamicQuery, start,
			end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.document.library.repository.dropbox.model.impl.DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _dropboxRevisionLocalService.dynamicQuery(dynamicQuery, start,
			end, orderByComparator);
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
		return _dropboxRevisionLocalService.dynamicQueryCount(dynamicQuery);
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
		return _dropboxRevisionLocalService.dynamicQueryCount(dynamicQuery,
			projection);
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision fetchDropboxRevision(
		long revisionId) {
		return _dropboxRevisionLocalService.fetchDropboxRevision(revisionId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _dropboxRevisionLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	@Override
	public java.lang.String getBeanIdentifier() {
		return _dropboxRevisionLocalService.getBeanIdentifier();
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision getDropboxRevision(
		long repositoryId, long entryId, java.lang.String rev)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return _dropboxRevisionLocalService.getDropboxRevision(repositoryId,
			entryId, rev);
	}

	/**
	* Returns the dropbox revision with the primary key.
	*
	* @param revisionId the primary key of the dropbox revision
	* @return the dropbox revision
	* @throws PortalException if a dropbox revision with the primary key could not be found
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision getDropboxRevision(
		long revisionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxRevisionLocalService.getDropboxRevision(revisionId);
	}

	@Override
	public java.util.List<com.liferay.document.library.repository.dropbox.model.DropboxRevision> getDropboxRevisions(
		long repositoryId, long entryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxRevisionLocalService.getDropboxRevisions(repositoryId,
			entryId);
	}

	/**
	* Returns a range of all the dropbox revisions.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.document.library.repository.dropbox.model.impl.DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of dropbox revisions
	* @param end the upper bound of the range of dropbox revisions (not inclusive)
	* @return the range of dropbox revisions
	*/
	@Override
	public java.util.List<com.liferay.document.library.repository.dropbox.model.DropboxRevision> getDropboxRevisions(
		int start, int end) {
		return _dropboxRevisionLocalService.getDropboxRevisions(start, end);
	}

	/**
	* Returns the number of dropbox revisions.
	*
	* @return the number of dropbox revisions
	*/
	@Override
	public int getDropboxRevisionsCount() {
		return _dropboxRevisionLocalService.getDropboxRevisionsCount();
	}

	@Override
	public com.liferay.portal.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dropboxRevisionLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	@Override
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_dropboxRevisionLocalService.setBeanIdentifier(beanIdentifier);
	}

	/**
	* Updates the dropbox revision in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param dropboxRevision the dropbox revision
	* @return the dropbox revision that was updated
	*/
	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision updateDropboxRevision(
		com.liferay.document.library.repository.dropbox.model.DropboxRevision dropboxRevision) {
		return _dropboxRevisionLocalService.updateDropboxRevision(dropboxRevision);
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedService}
	 */
	@Deprecated
	public DropboxRevisionLocalService getWrappedDropboxRevisionLocalService() {
		return _dropboxRevisionLocalService;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #setWrappedService}
	 */
	@Deprecated
	public void setWrappedDropboxRevisionLocalService(
		DropboxRevisionLocalService dropboxRevisionLocalService) {
		_dropboxRevisionLocalService = dropboxRevisionLocalService;
	}

	@Override
	public DropboxRevisionLocalService getWrappedService() {
		return _dropboxRevisionLocalService;
	}

	@Override
	public void setWrappedService(
		DropboxRevisionLocalService dropboxRevisionLocalService) {
		_dropboxRevisionLocalService = dropboxRevisionLocalService;
	}

	private DropboxRevisionLocalService _dropboxRevisionLocalService;
}