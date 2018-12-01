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

package com.liferay.document.library.kernel.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.kernel.model.DLPreview;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ReferenceRegistry;

import java.util.List;

/**
 * The persistence utility for the document library preview service. This utility wraps {@link com.liferay.portlet.documentlibrary.service.persistence.impl.DLPreviewPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLPreviewPersistence
 * @see com.liferay.portlet.documentlibrary.service.persistence.impl.DLPreviewPersistenceImpl
 * @generated
 */
@ProviderType
public class DLPreviewUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(DLPreview dlPreview) {
		getPersistence().clearCache(dlPreview);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<DLPreview> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<DLPreview> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<DLPreview> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<DLPreview> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static DLPreview update(DLPreview dlPreview) {
		return getPersistence().update(dlPreview);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static DLPreview update(DLPreview dlPreview,
		ServiceContext serviceContext) {
		return getPersistence().update(dlPreview, serviceContext);
	}

	/**
	* Returns all the document library previews where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @return the matching document library previews
	*/
	public static List<DLPreview> findByFileEntryId(long fileEntryId) {
		return getPersistence().findByFileEntryId(fileEntryId);
	}

	/**
	* Returns a range of all the document library previews where fileEntryId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param fileEntryId the file entry ID
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @return the range of matching document library previews
	*/
	public static List<DLPreview> findByFileEntryId(long fileEntryId,
		int start, int end) {
		return getPersistence().findByFileEntryId(fileEntryId, start, end);
	}

	/**
	* Returns an ordered range of all the document library previews where fileEntryId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param fileEntryId the file entry ID
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching document library previews
	*/
	public static List<DLPreview> findByFileEntryId(long fileEntryId,
		int start, int end, OrderByComparator<DLPreview> orderByComparator) {
		return getPersistence()
				   .findByFileEntryId(fileEntryId, start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the document library previews where fileEntryId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param fileEntryId the file entry ID
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching document library previews
	*/
	public static List<DLPreview> findByFileEntryId(long fileEntryId,
		int start, int end, OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findByFileEntryId(fileEntryId, start, end,
			orderByComparator, retrieveFromCache);
	}

	/**
	* Returns the first document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching document library preview
	* @throws NoSuchPreviewException if a matching document library preview could not be found
	*/
	public static DLPreview findByFileEntryId_First(long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator)
		throws com.liferay.document.library.kernel.exception.NoSuchPreviewException {
		return getPersistence()
				   .findByFileEntryId_First(fileEntryId, orderByComparator);
	}

	/**
	* Returns the first document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching document library preview, or <code>null</code> if a matching document library preview could not be found
	*/
	public static DLPreview fetchByFileEntryId_First(long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator) {
		return getPersistence()
				   .fetchByFileEntryId_First(fileEntryId, orderByComparator);
	}

	/**
	* Returns the last document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching document library preview
	* @throws NoSuchPreviewException if a matching document library preview could not be found
	*/
	public static DLPreview findByFileEntryId_Last(long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator)
		throws com.liferay.document.library.kernel.exception.NoSuchPreviewException {
		return getPersistence()
				   .findByFileEntryId_Last(fileEntryId, orderByComparator);
	}

	/**
	* Returns the last document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching document library preview, or <code>null</code> if a matching document library preview could not be found
	*/
	public static DLPreview fetchByFileEntryId_Last(long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator) {
		return getPersistence()
				   .fetchByFileEntryId_Last(fileEntryId, orderByComparator);
	}

	/**
	* Returns the document library previews before and after the current document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param filePreviewId the primary key of the current document library preview
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next document library preview
	* @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	*/
	public static DLPreview[] findByFileEntryId_PrevAndNext(
		long filePreviewId, long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator)
		throws com.liferay.document.library.kernel.exception.NoSuchPreviewException {
		return getPersistence()
				   .findByFileEntryId_PrevAndNext(filePreviewId, fileEntryId,
			orderByComparator);
	}

	/**
	* Removes all the document library previews where fileEntryId = &#63; from the database.
	*
	* @param fileEntryId the file entry ID
	*/
	public static void removeByFileEntryId(long fileEntryId) {
		getPersistence().removeByFileEntryId(fileEntryId);
	}

	/**
	* Returns the number of document library previews where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @return the number of matching document library previews
	*/
	public static int countByFileEntryId(long fileEntryId) {
		return getPersistence().countByFileEntryId(fileEntryId);
	}

	/**
	* Returns all the document library previews where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @return the matching document library previews
	*/
	public static List<DLPreview> findByFileVersionId(long fileVersionId) {
		return getPersistence().findByFileVersionId(fileVersionId);
	}

	/**
	* Returns a range of all the document library previews where fileVersionId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param fileVersionId the file version ID
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @return the range of matching document library previews
	*/
	public static List<DLPreview> findByFileVersionId(long fileVersionId,
		int start, int end) {
		return getPersistence().findByFileVersionId(fileVersionId, start, end);
	}

	/**
	* Returns an ordered range of all the document library previews where fileVersionId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param fileVersionId the file version ID
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching document library previews
	*/
	public static List<DLPreview> findByFileVersionId(long fileVersionId,
		int start, int end, OrderByComparator<DLPreview> orderByComparator) {
		return getPersistence()
				   .findByFileVersionId(fileVersionId, start, end,
			orderByComparator);
	}

	/**
	* Returns an ordered range of all the document library previews where fileVersionId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param fileVersionId the file version ID
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of matching document library previews
	*/
	public static List<DLPreview> findByFileVersionId(long fileVersionId,
		int start, int end, OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findByFileVersionId(fileVersionId, start, end,
			orderByComparator, retrieveFromCache);
	}

	/**
	* Returns the first document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching document library preview
	* @throws NoSuchPreviewException if a matching document library preview could not be found
	*/
	public static DLPreview findByFileVersionId_First(long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator)
		throws com.liferay.document.library.kernel.exception.NoSuchPreviewException {
		return getPersistence()
				   .findByFileVersionId_First(fileVersionId, orderByComparator);
	}

	/**
	* Returns the first document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching document library preview, or <code>null</code> if a matching document library preview could not be found
	*/
	public static DLPreview fetchByFileVersionId_First(long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator) {
		return getPersistence()
				   .fetchByFileVersionId_First(fileVersionId, orderByComparator);
	}

	/**
	* Returns the last document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching document library preview
	* @throws NoSuchPreviewException if a matching document library preview could not be found
	*/
	public static DLPreview findByFileVersionId_Last(long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator)
		throws com.liferay.document.library.kernel.exception.NoSuchPreviewException {
		return getPersistence()
				   .findByFileVersionId_Last(fileVersionId, orderByComparator);
	}

	/**
	* Returns the last document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching document library preview, or <code>null</code> if a matching document library preview could not be found
	*/
	public static DLPreview fetchByFileVersionId_Last(long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator) {
		return getPersistence()
				   .fetchByFileVersionId_Last(fileVersionId, orderByComparator);
	}

	/**
	* Returns the document library previews before and after the current document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param filePreviewId the primary key of the current document library preview
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next document library preview
	* @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	*/
	public static DLPreview[] findByFileVersionId_PrevAndNext(
		long filePreviewId, long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator)
		throws com.liferay.document.library.kernel.exception.NoSuchPreviewException {
		return getPersistence()
				   .findByFileVersionId_PrevAndNext(filePreviewId,
			fileVersionId, orderByComparator);
	}

	/**
	* Removes all the document library previews where fileVersionId = &#63; from the database.
	*
	* @param fileVersionId the file version ID
	*/
	public static void removeByFileVersionId(long fileVersionId) {
		getPersistence().removeByFileVersionId(fileVersionId);
	}

	/**
	* Returns the number of document library previews where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @return the number of matching document library previews
	*/
	public static int countByFileVersionId(long fileVersionId) {
		return getPersistence().countByFileVersionId(fileVersionId);
	}

	/**
	* Caches the document library preview in the entity cache if it is enabled.
	*
	* @param dlPreview the document library preview
	*/
	public static void cacheResult(DLPreview dlPreview) {
		getPersistence().cacheResult(dlPreview);
	}

	/**
	* Caches the document library previews in the entity cache if it is enabled.
	*
	* @param dlPreviews the document library previews
	*/
	public static void cacheResult(List<DLPreview> dlPreviews) {
		getPersistence().cacheResult(dlPreviews);
	}

	/**
	* Creates a new document library preview with the primary key. Does not add the document library preview to the database.
	*
	* @param filePreviewId the primary key for the new document library preview
	* @return the new document library preview
	*/
	public static DLPreview create(long filePreviewId) {
		return getPersistence().create(filePreviewId);
	}

	/**
	* Removes the document library preview with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview that was removed
	* @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	*/
	public static DLPreview remove(long filePreviewId)
		throws com.liferay.document.library.kernel.exception.NoSuchPreviewException {
		return getPersistence().remove(filePreviewId);
	}

	public static DLPreview updateImpl(DLPreview dlPreview) {
		return getPersistence().updateImpl(dlPreview);
	}

	/**
	* Returns the document library preview with the primary key or throws a {@link NoSuchPreviewException} if it could not be found.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview
	* @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	*/
	public static DLPreview findByPrimaryKey(long filePreviewId)
		throws com.liferay.document.library.kernel.exception.NoSuchPreviewException {
		return getPersistence().findByPrimaryKey(filePreviewId);
	}

	/**
	* Returns the document library preview with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview, or <code>null</code> if a document library preview with the primary key could not be found
	*/
	public static DLPreview fetchByPrimaryKey(long filePreviewId) {
		return getPersistence().fetchByPrimaryKey(filePreviewId);
	}

	public static java.util.Map<java.io.Serializable, DLPreview> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the document library previews.
	*
	* @return the document library previews
	*/
	public static List<DLPreview> findAll() {
		return getPersistence().findAll();
	}

	/**
	* Returns a range of all the document library previews.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @return the range of document library previews
	*/
	public static List<DLPreview> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	* Returns an ordered range of all the document library previews.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of document library previews
	*/
	public static List<DLPreview> findAll(int start, int end,
		OrderByComparator<DLPreview> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Returns an ordered range of all the document library previews.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DLPreviewModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of document library previews
	* @param end the upper bound of the range of document library previews (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @param retrieveFromCache whether to retrieve from the finder cache
	* @return the ordered range of document library previews
	*/
	public static List<DLPreview> findAll(int start, int end,
		OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache) {
		return getPersistence()
				   .findAll(start, end, orderByComparator, retrieveFromCache);
	}

	/**
	* Removes all the document library previews from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of document library previews.
	*
	* @return the number of document library previews
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static DLPreviewPersistence getPersistence() {
		if (_persistence == null) {
			_persistence = (DLPreviewPersistence)PortalBeanLocatorUtil.locate(DLPreviewPersistence.class.getName());

			ReferenceRegistry.registerReference(DLPreviewUtil.class,
				"_persistence");
		}

		return _persistence;
	}

	private static DLPreviewPersistence _persistence;
}