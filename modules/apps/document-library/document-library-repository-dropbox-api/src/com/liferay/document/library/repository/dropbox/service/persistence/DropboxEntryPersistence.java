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

package com.liferay.document.library.repository.dropbox.service.persistence;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.repository.dropbox.model.DropboxEntry;

import com.liferay.portal.service.persistence.BasePersistence;

/**
 * The persistence interface for the dropbox entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.document.library.repository.dropbox.service.persistence.impl.DropboxEntryPersistenceImpl
 * @see DropboxEntryUtil
 * @generated
 */
@ProviderType
public interface DropboxEntryPersistence extends BasePersistence<DropboxEntry> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link DropboxEntryUtil} to access the dropbox entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the dropbox entries where uuid = &#63;.
	*
	* @param uuid the uuid
	* @return the matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByUuid(java.lang.String uuid);

	/**
	* Returns a range of all the dropbox entries where uuid = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param uuid the uuid
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @return the range of matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByUuid(java.lang.String uuid,
		int start, int end);

	/**
	* Returns an ordered range of all the dropbox entries where uuid = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param uuid the uuid
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByUuid(java.lang.String uuid,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the first dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByUuid_First(java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the first dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByUuid_First(java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the last dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByUuid_Last(java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the last dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByUuid_Last(java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the dropbox entries before and after the current dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param entryId the primary key of the current dropbox entry
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public DropboxEntry[] findByUuid_PrevAndNext(long entryId,
		java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Removes all the dropbox entries where uuid = &#63; from the database.
	*
	* @param uuid the uuid
	*/
	public void removeByUuid(java.lang.String uuid);

	/**
	* Returns the number of dropbox entries where uuid = &#63;.
	*
	* @param uuid the uuid
	* @return the number of matching dropbox entries
	*/
	public int countByUuid(java.lang.String uuid);

	/**
	* Returns the dropbox entry where uuid = &#63; and groupId = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @return the matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByUUID_G(java.lang.String uuid, long groupId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the dropbox entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByUUID_G(java.lang.String uuid, long groupId);

	/**
	* Returns the dropbox entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByUUID_G(java.lang.String uuid, long groupId,
		boolean retrieveFromCache);

	/**
	* Removes the dropbox entry where uuid = &#63; and groupId = &#63; from the database.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @return the dropbox entry that was removed
	*/
	public DropboxEntry removeByUUID_G(java.lang.String uuid, long groupId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the number of dropbox entries where uuid = &#63; and groupId = &#63;.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @return the number of matching dropbox entries
	*/
	public int countByUUID_G(java.lang.String uuid, long groupId);

	/**
	* Returns all the dropbox entries where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @return the matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByUuid_C(java.lang.String uuid,
		long companyId);

	/**
	* Returns a range of all the dropbox entries where uuid = &#63; and companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @return the range of matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByUuid_C(java.lang.String uuid,
		long companyId, int start, int end);

	/**
	* Returns an ordered range of all the dropbox entries where uuid = &#63; and companyId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByUuid_C(java.lang.String uuid,
		long companyId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the first dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByUuid_C_First(java.lang.String uuid,
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the first dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByUuid_C_First(java.lang.String uuid,
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the last dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByUuid_C_Last(java.lang.String uuid,
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the last dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByUuid_C_Last(java.lang.String uuid,
		long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the dropbox entries before and after the current dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param entryId the primary key of the current dropbox entry
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public DropboxEntry[] findByUuid_C_PrevAndNext(long entryId,
		java.lang.String uuid, long companyId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Removes all the dropbox entries where uuid = &#63; and companyId = &#63; from the database.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	*/
	public void removeByUuid_C(java.lang.String uuid, long companyId);

	/**
	* Returns the number of dropbox entries where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @return the number of matching dropbox entries
	*/
	public int countByUuid_C(java.lang.String uuid, long companyId);

	/**
	* Returns the dropbox entry where repositoryId = &#63; and path = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @return the matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByR_P(long repositoryId, java.lang.String path)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the dropbox entry where repositoryId = &#63; and path = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByR_P(long repositoryId, java.lang.String path);

	/**
	* Returns the dropbox entry where repositoryId = &#63; and path = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByR_P(long repositoryId, java.lang.String path,
		boolean retrieveFromCache);

	/**
	* Removes the dropbox entry where repositoryId = &#63; and path = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @return the dropbox entry that was removed
	*/
	public DropboxEntry removeByR_P(long repositoryId, java.lang.String path)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the number of dropbox entries where repositoryId = &#63; and path = &#63;.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @return the number of matching dropbox entries
	*/
	public int countByR_P(long repositoryId, java.lang.String path);

	/**
	* Returns all the dropbox entries where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @return the matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByR_PP(long repositoryId,
		java.lang.String parentPath);

	/**
	* Returns a range of all the dropbox entries where repositoryId = &#63; and parentPath = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @return the range of matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByR_PP(long repositoryId,
		java.lang.String parentPath, int start, int end);

	/**
	* Returns an ordered range of all the dropbox entries where repositoryId = &#63; and parentPath = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByR_PP(long repositoryId,
		java.lang.String parentPath, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the first dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByR_PP_First(long repositoryId,
		java.lang.String parentPath,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the first dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByR_PP_First(long repositoryId,
		java.lang.String parentPath,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the last dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByR_PP_Last(long repositoryId,
		java.lang.String parentPath,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the last dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByR_PP_Last(long repositoryId,
		java.lang.String parentPath,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the dropbox entries before and after the current dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param entryId the primary key of the current dropbox entry
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public DropboxEntry[] findByR_PP_PrevAndNext(long entryId,
		long repositoryId, java.lang.String parentPath,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Removes all the dropbox entries where repositoryId = &#63; and parentPath = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	*/
	public void removeByR_PP(long repositoryId, java.lang.String parentPath);

	/**
	* Returns the number of dropbox entries where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @return the number of matching dropbox entries
	*/
	public int countByR_PP(long repositoryId, java.lang.String parentPath);

	/**
	* Returns all the dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @return the matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByR_PP_T(long repositoryId,
		java.lang.String parentPath, int type);

	/**
	* Returns a range of all the dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @return the range of matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByR_PP_T(long repositoryId,
		java.lang.String parentPath, int type, int start, int end);

	/**
	* Returns an ordered range of all the dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching dropbox entries
	*/
	public java.util.List<DropboxEntry> findByR_PP_T(long repositoryId,
		java.lang.String parentPath, int type, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the first dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByR_PP_T_First(long repositoryId,
		java.lang.String parentPath, int type,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the first dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByR_PP_T_First(long repositoryId,
		java.lang.String parentPath, int type,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the last dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public DropboxEntry findByR_PP_T_Last(long repositoryId,
		java.lang.String parentPath, int type,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the last dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public DropboxEntry fetchByR_PP_T_Last(long repositoryId,
		java.lang.String parentPath, int type,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Returns the dropbox entries before and after the current dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param entryId the primary key of the current dropbox entry
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public DropboxEntry[] findByR_PP_T_PrevAndNext(long entryId,
		long repositoryId, java.lang.String parentPath, int type,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Removes all the dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	*/
	public void removeByR_PP_T(long repositoryId, java.lang.String parentPath,
		int type);

	/**
	* Returns the number of dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @return the number of matching dropbox entries
	*/
	public int countByR_PP_T(long repositoryId, java.lang.String parentPath,
		int type);

	/**
	* Caches the dropbox entry in the entity cache if it is enabled.
	*
	* @param dropboxEntry the dropbox entry
	*/
	public void cacheResult(DropboxEntry dropboxEntry);

	/**
	* Caches the dropbox entries in the entity cache if it is enabled.
	*
	* @param dropboxEntries the dropbox entries
	*/
	public void cacheResult(java.util.List<DropboxEntry> dropboxEntries);

	/**
	* Creates a new dropbox entry with the primary key. Does not add the dropbox entry to the database.
	*
	* @param entryId the primary key for the new dropbox entry
	* @return the new dropbox entry
	*/
	public DropboxEntry create(long entryId);

	/**
	* Removes the dropbox entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param entryId the primary key of the dropbox entry
	* @return the dropbox entry that was removed
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public DropboxEntry remove(long entryId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	public DropboxEntry updateImpl(DropboxEntry dropboxEntry);

	/**
	* Returns the dropbox entry with the primary key or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	*
	* @param entryId the primary key of the dropbox entry
	* @return the dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public DropboxEntry findByPrimaryKey(long entryId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;

	/**
	* Returns the dropbox entry with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param entryId the primary key of the dropbox entry
	* @return the dropbox entry, or <code>null</code> if a dropbox entry with the primary key could not be found
	*/
	public DropboxEntry fetchByPrimaryKey(long entryId);

	@Override
	public java.util.Map<java.io.Serializable, DropboxEntry> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the dropbox entries.
	*
	* @return the dropbox entries
	*/
	public java.util.List<DropboxEntry> findAll();

	/**
	* Returns a range of all the dropbox entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @return the range of dropbox entries
	*/
	public java.util.List<DropboxEntry> findAll(int start, int end);

	/**
	* Returns an ordered range of all the dropbox entries.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxEntryModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of dropbox entries
	* @param end the upper bound of the range of dropbox entries (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of dropbox entries
	*/
	public java.util.List<DropboxEntry> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxEntry> orderByComparator);

	/**
	* Removes all the dropbox entries from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of dropbox entries.
	*
	* @return the number of dropbox entries
	*/
	public int countAll();

	@Override
	public java.util.Set<java.lang.String> getBadColumnNames();
}