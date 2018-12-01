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

import com.liferay.document.library.kernel.exception.NoSuchPreviewException;
import com.liferay.document.library.kernel.model.DLPreview;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

/**
 * The persistence interface for the document library preview service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portlet.documentlibrary.service.persistence.impl.DLPreviewPersistenceImpl
 * @see DLPreviewUtil
 * @generated
 */
@ProviderType
public interface DLPreviewPersistence extends BasePersistence<DLPreview> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link DLPreviewUtil} to access the document library preview persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the document library previews where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @return the matching document library previews
	*/
	public java.util.List<DLPreview> findByFileEntryId(long fileEntryId);

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
	public java.util.List<DLPreview> findByFileEntryId(long fileEntryId,
		int start, int end);

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
	public java.util.List<DLPreview> findByFileEntryId(long fileEntryId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator);

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
	public java.util.List<DLPreview> findByFileEntryId(long fileEntryId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching document library preview
	* @throws NoSuchPreviewException if a matching document library preview could not be found
	*/
	public DLPreview findByFileEntryId_First(long fileEntryId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException;

	/**
	* Returns the first document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching document library preview, or <code>null</code> if a matching document library preview could not be found
	*/
	public DLPreview fetchByFileEntryId_First(long fileEntryId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator);

	/**
	* Returns the last document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching document library preview
	* @throws NoSuchPreviewException if a matching document library preview could not be found
	*/
	public DLPreview findByFileEntryId_Last(long fileEntryId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException;

	/**
	* Returns the last document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching document library preview, or <code>null</code> if a matching document library preview could not be found
	*/
	public DLPreview fetchByFileEntryId_Last(long fileEntryId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator);

	/**
	* Returns the document library previews before and after the current document library preview in the ordered set where fileEntryId = &#63;.
	*
	* @param filePreviewId the primary key of the current document library preview
	* @param fileEntryId the file entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next document library preview
	* @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	*/
	public DLPreview[] findByFileEntryId_PrevAndNext(long filePreviewId,
		long fileEntryId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException;

	/**
	* Removes all the document library previews where fileEntryId = &#63; from the database.
	*
	* @param fileEntryId the file entry ID
	*/
	public void removeByFileEntryId(long fileEntryId);

	/**
	* Returns the number of document library previews where fileEntryId = &#63;.
	*
	* @param fileEntryId the file entry ID
	* @return the number of matching document library previews
	*/
	public int countByFileEntryId(long fileEntryId);

	/**
	* Returns all the document library previews where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @return the matching document library previews
	*/
	public java.util.List<DLPreview> findByFileVersionId(long fileVersionId);

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
	public java.util.List<DLPreview> findByFileVersionId(long fileVersionId,
		int start, int end);

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
	public java.util.List<DLPreview> findByFileVersionId(long fileVersionId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator);

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
	public java.util.List<DLPreview> findByFileVersionId(long fileVersionId,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Returns the first document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching document library preview
	* @throws NoSuchPreviewException if a matching document library preview could not be found
	*/
	public DLPreview findByFileVersionId_First(long fileVersionId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException;

	/**
	* Returns the first document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching document library preview, or <code>null</code> if a matching document library preview could not be found
	*/
	public DLPreview fetchByFileVersionId_First(long fileVersionId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator);

	/**
	* Returns the last document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching document library preview
	* @throws NoSuchPreviewException if a matching document library preview could not be found
	*/
	public DLPreview findByFileVersionId_Last(long fileVersionId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException;

	/**
	* Returns the last document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching document library preview, or <code>null</code> if a matching document library preview could not be found
	*/
	public DLPreview fetchByFileVersionId_Last(long fileVersionId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator);

	/**
	* Returns the document library previews before and after the current document library preview in the ordered set where fileVersionId = &#63;.
	*
	* @param filePreviewId the primary key of the current document library preview
	* @param fileVersionId the file version ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next document library preview
	* @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	*/
	public DLPreview[] findByFileVersionId_PrevAndNext(long filePreviewId,
		long fileVersionId,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException;

	/**
	* Removes all the document library previews where fileVersionId = &#63; from the database.
	*
	* @param fileVersionId the file version ID
	*/
	public void removeByFileVersionId(long fileVersionId);

	/**
	* Returns the number of document library previews where fileVersionId = &#63;.
	*
	* @param fileVersionId the file version ID
	* @return the number of matching document library previews
	*/
	public int countByFileVersionId(long fileVersionId);

	/**
	* Caches the document library preview in the entity cache if it is enabled.
	*
	* @param dlPreview the document library preview
	*/
	public void cacheResult(DLPreview dlPreview);

	/**
	* Caches the document library previews in the entity cache if it is enabled.
	*
	* @param dlPreviews the document library previews
	*/
	public void cacheResult(java.util.List<DLPreview> dlPreviews);

	/**
	* Creates a new document library preview with the primary key. Does not add the document library preview to the database.
	*
	* @param filePreviewId the primary key for the new document library preview
	* @return the new document library preview
	*/
	public DLPreview create(long filePreviewId);

	/**
	* Removes the document library preview with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview that was removed
	* @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	*/
	public DLPreview remove(long filePreviewId) throws NoSuchPreviewException;

	public DLPreview updateImpl(DLPreview dlPreview);

	/**
	* Returns the document library preview with the primary key or throws a {@link NoSuchPreviewException} if it could not be found.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview
	* @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	*/
	public DLPreview findByPrimaryKey(long filePreviewId)
		throws NoSuchPreviewException;

	/**
	* Returns the document library preview with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param filePreviewId the primary key of the document library preview
	* @return the document library preview, or <code>null</code> if a document library preview with the primary key could not be found
	*/
	public DLPreview fetchByPrimaryKey(long filePreviewId);

	@Override
	public java.util.Map<java.io.Serializable, DLPreview> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the document library previews.
	*
	* @return the document library previews
	*/
	public java.util.List<DLPreview> findAll();

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
	public java.util.List<DLPreview> findAll(int start, int end);

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
	public java.util.List<DLPreview> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator);

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
	public java.util.List<DLPreview> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache);

	/**
	* Removes all the document library previews from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of document library previews.
	*
	* @return the number of document library previews
	*/
	public int countAll();
}