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

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * Provides the local service utility for DLPreview. This utility wraps
 * {@link com.liferay.portlet.documentlibrary.service.impl.DLPreviewLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see DLPreviewLocalService
 * @see com.liferay.portlet.documentlibrary.service.base.DLPreviewLocalServiceBaseImpl
 * @see com.liferay.portlet.documentlibrary.service.impl.DLPreviewLocalServiceImpl
 * @generated
 */
@ProviderType
public class DLPreviewLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portlet.documentlibrary.service.impl.DLPreviewLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	* Adds the document library preview to the database. Also notifies the appropriate model listeners.
	*
	* @param dlPreview the document library preview
	* @return the document library preview that was added
	*/
	public static com.liferay.document.library.kernel.model.DLPreview addDLPreview(
		com.liferay.document.library.kernel.model.DLPreview dlPreview) {
		return getService().addDLPreview(dlPreview);
	}

	/**
	* Creates a new document library preview with the primary key. Does not add the document library preview to the database.
	*
	* @param filePreviewId the primary key for the new document library preview
	* @return the new document library preview
	*/
	public static com.liferay.document.library.kernel.model.DLPreview createDLPreview(
		long filePreviewId) {
		return getService().createDLPreview(filePreviewId);
	}

	/**
	* Deletes the document library preview from the database. Also notifies the appropriate model listeners.
	*
	* @param dlPreview the document library preview
	* @return the document library preview that was removed
	*/
	public static com.liferay.document.library.kernel.model.DLPreview deleteDLPreview(
		com.liferay.document.library.kernel.model.DLPreview dlPreview) {
		return getService().deleteDLPreview(dlPreview);
	}

	/**
	* Deletes the document library preview with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview that was removed
	* @throws PortalException if a document library preview with the primary key could not be found
	*/
	public static com.liferay.document.library.kernel.model.DLPreview deleteDLPreview(
		long filePreviewId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().deleteDLPreview(filePreviewId);
	}

	/**
	* @throws PortalException
	*/
	public static com.liferay.portal.kernel.model.PersistedModel deletePersistedModel(
		com.liferay.portal.kernel.model.PersistedModel persistedModel)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().deletePersistedModel(persistedModel);
	}

	public static com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery() {
		return getService().dynamicQuery();
	}

	/**
	* Performs a dynamic query on the database and returns the matching rows.
	*
	* @param dynamicQuery the dynamic query
	* @return the matching rows
	*/
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return getService().dynamicQuery(dynamicQuery);
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
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) {
		return getService().dynamicQuery(dynamicQuery, start, end);
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
	public static <T> java.util.List<T> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end,
		com.liferay.portal.kernel.util.OrderByComparator<T> orderByComparator) {
		return getService()
				   .dynamicQuery(dynamicQuery, start, end, orderByComparator);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @return the number of rows matching the dynamic query
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) {
		return getService().dynamicQueryCount(dynamicQuery);
	}

	/**
	* Returns the number of rows matching the dynamic query.
	*
	* @param dynamicQuery the dynamic query
	* @param projection the projection to apply to the query
	* @return the number of rows matching the dynamic query
	*/
	public static long dynamicQueryCount(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery,
		com.liferay.portal.kernel.dao.orm.Projection projection) {
		return getService().dynamicQueryCount(dynamicQuery, projection);
	}

	public static com.liferay.document.library.kernel.model.DLPreview fetchDLPreview(
		long filePreviewId) {
		return getService().fetchDLPreview(filePreviewId);
	}

	public static com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery getActionableDynamicQuery() {
		return getService().getActionableDynamicQuery();
	}

	/**
	* Returns the document library preview with the primary key.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview
	* @throws PortalException if a document library preview with the primary key could not be found
	*/
	public static com.liferay.document.library.kernel.model.DLPreview getDLPreview(
		long filePreviewId)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getDLPreview(filePreviewId);
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
	public static java.util.List<com.liferay.document.library.kernel.model.DLPreview> getDLPreviews(
		int start, int end) {
		return getService().getDLPreviews(start, end);
	}

	/**
	* Returns the number of document library previews.
	*
	* @return the number of document library previews
	*/
	public static int getDLPreviewsCount() {
		return getService().getDLPreviewsCount();
	}

	public static com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery getIndexableActionableDynamicQuery() {
		return getService().getIndexableActionableDynamicQuery();
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static com.liferay.portal.kernel.model.PersistedModel getPersistedModel(
		java.io.Serializable primaryKeyObj)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService().getPersistedModel(primaryKeyObj);
	}

	/**
	* Updates the document library preview in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	*
	* @param dlPreview the document library preview
	* @return the document library preview that was updated
	*/
	public static com.liferay.document.library.kernel.model.DLPreview updateDLPreview(
		com.liferay.document.library.kernel.model.DLPreview dlPreview) {
		return getService().updateDLPreview(dlPreview);
	}

	public static DLPreviewLocalService getService() {
		if (_service == null) {
			_service = (DLPreviewLocalService)PortalBeanLocatorUtil.locate(DLPreviewLocalService.class.getName());

			ReferenceRegistry.registerReference(DLPreviewLocalServiceUtil.class,
				"_service");
		}

		return _service;
	}

	private static DLPreviewLocalService _service;
}