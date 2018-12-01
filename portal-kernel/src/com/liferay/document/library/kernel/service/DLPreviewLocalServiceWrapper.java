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

package com.liferay.document.library.kernel.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link DLPreviewLocalService}.
 *
 * @author Brian Wing Shun Chan
 * @see DLPreviewLocalService
 * @generated
 */
@ProviderType
public class DLPreviewLocalServiceWrapper implements DLPreviewLocalService,
	ServiceWrapper<DLPreviewLocalService> {
	public DLPreviewLocalServiceWrapper(
		DLPreviewLocalService dlPreviewLocalService) {
		_dlPreviewLocalService = dlPreviewLocalService;
	}

	/**
	* Adds the document library preview to the database. Also notifies the appropriate model listeners.
	*
	* @param dlPreview the document library preview
	* @return the document library preview that was added
	*/
	@Override
	public com.liferay.document.library.kernel.model.DLPreview addDLPreview(
		com.liferay.document.library.kernel.model.DLPreview dlPreview) {
		return _dlPreviewLocalService.addDLPreview(dlPreview);
	}

	/**
	* NOTE FOR DEVELOPERS:
	*
	* Never reference this class directly. Always use {@link DLPreviewLocalServiceUtil} to access the document library preview local service.
	*/
	@Override
	public com.liferay.document.library.kernel.model.DLPreview addDLPreview(
		long fileEntryId, long fileVersionId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlPreviewLocalService.addDLPreview(fileEntryId, fileVersionId);
	}

	@Override
	public com.liferay.document.library.kernel.model.DLPreview addDLPreview(
		long fileEntryId, long fileVersionId, String status)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlPreviewLocalService.addDLPreview(fileEntryId, fileVersionId,
			status);
	}

	/**
	* Creates a new document library preview with the primary key. Does not add the document library preview to the database.
	*
	* @param filePreviewId the primary key for the new document library preview
	* @return the new document library preview
	*/
	@Override
	public com.liferay.document.library.kernel.model.DLPreview createDLPreview(
		long filePreviewId) {
		return _dlPreviewLocalService.createDLPreview(filePreviewId);
	}

	/**
	* Deletes the document library preview from the database. Also notifies the appropriate model listeners.
	*
	* @param dlPreview the document library preview
	* @return the document library preview that was removed
	*/
	@Override
	public com.liferay.document.library.kernel.model.DLPreview deleteDLPreview(
		com.liferay.document.library.kernel.model.DLPreview dlPreview) {
		return _dlPreviewLocalService.deleteDLPreview(dlPreview);
	}

	/**
	* Deletes the document library preview with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview that was removed
	* @throws PortalException if a document library preview with the primary key could not be found
	*/
	@Override
	public com.liferay.document.library.kernel.model.DLPreview deleteDLPreview(
		long filePreviewId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlPreviewLocalService.deleteDLPreview(filePreviewId);
	}

	/**
	* @throws PortalException
	*/
	@Override
	public com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
		com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlPreviewLocalService.deletePersistedModel(persistedModel);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return _dlPreviewLocalService.dynamicQuery();
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
		return _dlPreviewLocalService.dynamicQuery(dynamicQuery);
	}

	/**
	* Performs a dynamic query on the database and returns a range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.documentlibrary.model.impl.DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _dlPreviewLocalService.dynamicQuery(dynamicQuery, start, end);
	}

	/**
	* Performs a dynamic query on the database and returns an ordered range of the matching rows.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.documentlibrary.model.impl.DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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
		return _dlPreviewLocalService.dynamicQuery(dynamicQuery, start, end,
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
		return _dlPreviewLocalService.dynamicQueryCount(dynamicQuery);
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
		return _dlPreviewLocalService.dynamicQueryCount(dynamicQuery, projection);
	}

	@Override
	public com.liferay.document.library.kernel.model.DLPreview fetchDLPreview(
		com.liferay.portal.kernel.repository.model.FileVersion fileVersion) {
		return _dlPreviewLocalService.fetchDLPreview(fileVersion);
	}

	@Override
	public com.liferay.document.library.kernel.model.DLPreview fetchDLPreview(
		long filePreviewId) {
		return _dlPreviewLocalService.fetchDLPreview(filePreviewId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return _dlPreviewLocalService.getActionableDynamicQuery();
	}

	/**
	* Returns the document library preview with the primary key.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview
	* @throws PortalException if a document library preview with the primary key could not be found
	*/
	@Override
	public com.liferay.document.library.kernel.model.DLPreview getDLPreview(
		long filePreviewId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlPreviewLocalService.getDLPreview(filePreviewId);
	}

	/**
	* Returns a range of all the document library previews.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.liferay.portlet.documentlibrary.model.impl.DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @return the range of document library previews
	*/
	@Override
	public java.util.List<com.liferay.document.library.kernel.model.DLPreview> getDLPreviews(
		int start, int end) {
		return _dlPreviewLocalService.getDLPreviews(start, end);
	}

	/**
	* Returns the number of document library previews.
	*
	* @return the number of document library previews
	*/
	@Override
	public int getDLPreviewsCount() {
		return _dlPreviewLocalService.getDLPreviewsCount();
	}

	@Override
	public String getDLPreviewStatus(
		com.liferay.portal.kernel.repository.model.FileVersion fileVersion) {
		return _dlPreviewLocalService.getDLPreviewStatus(fileVersion);
	}

	@Override
	public String getDLPreviewStatus(long fileEntryId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlPreviewLocalService.getDLPreviewStatus(fileEntryId);
	}

	@Override
	public com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		return _dlPreviewLocalService.getIndexableActionableDynamicQuery();
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public String getOSGiServiceIdentifier() {
		return _dlPreviewLocalService.getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return _dlPreviewLocalService.getPersistedModel(primaryKeyObj);
	}

	/**
	* Updates the document library preview in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param dlPreview the document library preview
	* @return the document library preview that was updated
	*/
	@Override
	public com.liferay.document.library.kernel.model.DLPreview updateDLPreview(
		com.liferay.document.library.kernel.model.DLPreview dlPreview) {
		return _dlPreviewLocalService.updateDLPreview(dlPreview);
	}

	@Override
	public com.liferay.document.library.kernel.model.DLPreview updateDLPreview(
		long dlPreviewId, long fileEntryId, long fileVersionId, String status) {
		return _dlPreviewLocalService.updateDLPreview(dlPreviewId, fileEntryId,
			fileVersionId, status);
	}

	@Override
	public DLPreviewLocalService getWrappedService() {
		return _dlPreviewLocalService;
	}

	@Override
	public void setWrappedService(DLPreviewLocalService dlPreviewLocalService) {
		_dlPreviewLocalService = dlPreviewLocalService;
	}

	private DLPreviewLocalService _dlPreviewLocalService;
}