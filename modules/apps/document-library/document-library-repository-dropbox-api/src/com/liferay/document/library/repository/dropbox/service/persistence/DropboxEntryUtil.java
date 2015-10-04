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

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

import java.util.List;

/**
 * The persistence utility for the dropbox entry service. This utility wraps {@link com.liferay.document.library.repository.dropbox.service.persistence.impl.DropboxEntryPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DropboxEntryPersistence
 * @see com.liferay.document.library.repository.dropbox.service.persistence.impl.DropboxEntryPersistenceImpl
 * @generated
 */
@ProviderType
public class DropboxEntryUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#clearCache(com.liferay.portal.model.BaseModel)
	 */
	public static void clearCache(DropboxEntry dropboxEntry) {
		getPersistence().clearCache(dropboxEntry);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<DropboxEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<DropboxEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<DropboxEntry> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel)
	 */
	public static DropboxEntry update(DropboxEntry dropboxEntry) {
		return getPersistence().update(dropboxEntry);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel, ServiceContext)
	 */
	public static DropboxEntry update(DropboxEntry dropboxEntry,
		ServiceContext serviceContext) {
		return getPersistence().update(dropboxEntry, serviceContext);
	}

	/**
	* Returns all the dropbox entries where uuid = &#63;.
	*
	* @param uuid the uuid
	* @return the matching dropbox entries
	*/
	public static List<DropboxEntry> findByUuid(java.lang.String uuid) {
		return getPersistence().findByUuid(uuid);
	}

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
	public static List<DropboxEntry> findByUuid(java.lang.String uuid,
		int start, int end) {
		return getPersistence().findByUuid(uuid, start, end);
	}

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
	public static List<DropboxEntry> findByUuid(java.lang.String uuid,
		int start, int end, OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	* Returns the first dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public static DropboxEntry findByUuid_First(java.lang.String uuid,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	* Returns the first dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByUuid_First(java.lang.String uuid,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	* Returns the last dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public static DropboxEntry findByUuid_Last(java.lang.String uuid,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	* Returns the last dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByUuid_Last(java.lang.String uuid,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	* Returns the dropbox entries before and after the current dropbox entry in the ordered set where uuid = &#63;.
	*
	* @param entryId the primary key of the current dropbox entry
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public static DropboxEntry[] findByUuid_PrevAndNext(long entryId,
		java.lang.String uuid, OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByUuid_PrevAndNext(entryId, uuid, orderByComparator);
	}

	/**
	* Removes all the dropbox entries where uuid = &#63; from the database.
	*
	* @param uuid the uuid
	*/
	public static void removeByUuid(java.lang.String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	* Returns the number of dropbox entries where uuid = &#63;.
	*
	* @param uuid the uuid
	* @return the number of matching dropbox entries
	*/
	public static int countByUuid(java.lang.String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	* Returns the dropbox entry where uuid = &#63; and groupId = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @return the matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public static DropboxEntry findByUUID_G(java.lang.String uuid, long groupId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence().findByUUID_G(uuid, groupId);
	}

	/**
	* Returns the dropbox entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByUUID_G(java.lang.String uuid, long groupId) {
		return getPersistence().fetchByUUID_G(uuid, groupId);
	}

	/**
	* Returns the dropbox entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByUUID_G(java.lang.String uuid,
		long groupId, boolean retrieveFromCache) {
		return getPersistence().fetchByUUID_G(uuid, groupId, retrieveFromCache);
	}

	/**
	* Removes the dropbox entry where uuid = &#63; and groupId = &#63; from the database.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @return the dropbox entry that was removed
	*/
	public static DropboxEntry removeByUUID_G(java.lang.String uuid,
		long groupId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence().removeByUUID_G(uuid, groupId);
	}

	/**
	* Returns the number of dropbox entries where uuid = &#63; and groupId = &#63;.
	*
	* @param uuid the uuid
	* @param groupId the group ID
	* @return the number of matching dropbox entries
	*/
	public static int countByUUID_G(java.lang.String uuid, long groupId) {
		return getPersistence().countByUUID_G(uuid, groupId);
	}

	/**
	* Returns all the dropbox entries where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @return the matching dropbox entries
	*/
	public static List<DropboxEntry> findByUuid_C(java.lang.String uuid,
		long companyId) {
		return getPersistence().findByUuid_C(uuid, companyId);
	}

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
	public static List<DropboxEntry> findByUuid_C(java.lang.String uuid,
		long companyId, int start, int end) {
		return getPersistence().findByUuid_C(uuid, companyId, start, end);
	}

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
	public static List<DropboxEntry> findByUuid_C(java.lang.String uuid,
		long companyId, int start, int end,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .findByUuid_C(uuid, companyId, start, end, orderByComparator);
	}

	/**
	* Returns the first dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public static DropboxEntry findByUuid_C_First(java.lang.String uuid,
		long companyId, OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByUuid_C_First(uuid, companyId, orderByComparator);
	}

	/**
	* Returns the first dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByUuid_C_First(java.lang.String uuid,
		long companyId, OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .fetchByUuid_C_First(uuid, companyId, orderByComparator);
	}

	/**
	* Returns the last dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public static DropboxEntry findByUuid_C_Last(java.lang.String uuid,
		long companyId, OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByUuid_C_Last(uuid, companyId, orderByComparator);
	}

	/**
	* Returns the last dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByUuid_C_Last(java.lang.String uuid,
		long companyId, OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .fetchByUuid_C_Last(uuid, companyId, orderByComparator);
	}

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
	public static DropboxEntry[] findByUuid_C_PrevAndNext(long entryId,
		java.lang.String uuid, long companyId,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByUuid_C_PrevAndNext(entryId, uuid, companyId,
			orderByComparator);
	}

	/**
	* Removes all the dropbox entries where uuid = &#63; and companyId = &#63; from the database.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	*/
	public static void removeByUuid_C(java.lang.String uuid, long companyId) {
		getPersistence().removeByUuid_C(uuid, companyId);
	}

	/**
	* Returns the number of dropbox entries where uuid = &#63; and companyId = &#63;.
	*
	* @param uuid the uuid
	* @param companyId the company ID
	* @return the number of matching dropbox entries
	*/
	public static int countByUuid_C(java.lang.String uuid, long companyId) {
		return getPersistence().countByUuid_C(uuid, companyId);
	}

	/**
	* Returns the dropbox entry where repositoryId = &#63; and path = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @return the matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public static DropboxEntry findByR_P(long repositoryId,
		java.lang.String path)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence().findByR_P(repositoryId, path);
	}

	/**
	* Returns the dropbox entry where repositoryId = &#63; and path = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByR_P(long repositoryId,
		java.lang.String path) {
		return getPersistence().fetchByR_P(repositoryId, path);
	}

	/**
	* Returns the dropbox entry where repositoryId = &#63; and path = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByR_P(long repositoryId,
		java.lang.String path, boolean retrieveFromCache) {
		return getPersistence().fetchByR_P(repositoryId, path, retrieveFromCache);
	}

	/**
	* Removes the dropbox entry where repositoryId = &#63; and path = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @return the dropbox entry that was removed
	*/
	public static DropboxEntry removeByR_P(long repositoryId,
		java.lang.String path)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence().removeByR_P(repositoryId, path);
	}

	/**
	* Returns the number of dropbox entries where repositoryId = &#63; and path = &#63;.
	*
	* @param repositoryId the repository ID
	* @param path the path
	* @return the number of matching dropbox entries
	*/
	public static int countByR_P(long repositoryId, java.lang.String path) {
		return getPersistence().countByR_P(repositoryId, path);
	}

	/**
	* Returns all the dropbox entries where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @return the matching dropbox entries
	*/
	public static List<DropboxEntry> findByR_PP(long repositoryId,
		java.lang.String parentPath) {
		return getPersistence().findByR_PP(repositoryId, parentPath);
	}

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
	public static List<DropboxEntry> findByR_PP(long repositoryId,
		java.lang.String parentPath, int start, int end) {
		return getPersistence().findByR_PP(repositoryId, parentPath, start, end);
	}

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
	public static List<DropboxEntry> findByR_PP(long repositoryId,
		java.lang.String parentPath, int start, int end,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .findByR_PP(repositoryId, parentPath, start, end,
			orderByComparator);
	}

	/**
	* Returns the first dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public static DropboxEntry findByR_PP_First(long repositoryId,
		java.lang.String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByR_PP_First(repositoryId, parentPath, orderByComparator);
	}

	/**
	* Returns the first dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByR_PP_First(long repositoryId,
		java.lang.String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .fetchByR_PP_First(repositoryId, parentPath,
			orderByComparator);
	}

	/**
	* Returns the last dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	*/
	public static DropboxEntry findByR_PP_Last(long repositoryId,
		java.lang.String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByR_PP_Last(repositoryId, parentPath, orderByComparator);
	}

	/**
	* Returns the last dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByR_PP_Last(long repositoryId,
		java.lang.String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .fetchByR_PP_Last(repositoryId, parentPath, orderByComparator);
	}

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
	public static DropboxEntry[] findByR_PP_PrevAndNext(long entryId,
		long repositoryId, java.lang.String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByR_PP_PrevAndNext(entryId, repositoryId, parentPath,
			orderByComparator);
	}

	/**
	* Removes all the dropbox entries where repositoryId = &#63; and parentPath = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	*/
	public static void removeByR_PP(long repositoryId,
		java.lang.String parentPath) {
		getPersistence().removeByR_PP(repositoryId, parentPath);
	}

	/**
	* Returns the number of dropbox entries where repositoryId = &#63; and parentPath = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @return the number of matching dropbox entries
	*/
	public static int countByR_PP(long repositoryId, java.lang.String parentPath) {
		return getPersistence().countByR_PP(repositoryId, parentPath);
	}

	/**
	* Returns all the dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @return the matching dropbox entries
	*/
	public static List<DropboxEntry> findByR_PP_T(long repositoryId,
		java.lang.String parentPath, int type) {
		return getPersistence().findByR_PP_T(repositoryId, parentPath, type);
	}

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
	public static List<DropboxEntry> findByR_PP_T(long repositoryId,
		java.lang.String parentPath, int type, int start, int end) {
		return getPersistence()
				   .findByR_PP_T(repositoryId, parentPath, type, start, end);
	}

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
	public static List<DropboxEntry> findByR_PP_T(long repositoryId,
		java.lang.String parentPath, int type, int start, int end,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .findByR_PP_T(repositoryId, parentPath, type, start, end,
			orderByComparator);
	}

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
	public static DropboxEntry findByR_PP_T_First(long repositoryId,
		java.lang.String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByR_PP_T_First(repositoryId, parentPath, type,
			orderByComparator);
	}

	/**
	* Returns the first dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByR_PP_T_First(long repositoryId,
		java.lang.String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .fetchByR_PP_T_First(repositoryId, parentPath, type,
			orderByComparator);
	}

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
	public static DropboxEntry findByR_PP_T_Last(long repositoryId,
		java.lang.String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByR_PP_T_Last(repositoryId, parentPath, type,
			orderByComparator);
	}

	/**
	* Returns the last dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	*/
	public static DropboxEntry fetchByR_PP_T_Last(long repositoryId,
		java.lang.String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence()
				   .fetchByR_PP_T_Last(repositoryId, parentPath, type,
			orderByComparator);
	}

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
	public static DropboxEntry[] findByR_PP_T_PrevAndNext(long entryId,
		long repositoryId, java.lang.String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence()
				   .findByR_PP_T_PrevAndNext(entryId, repositoryId, parentPath,
			type, orderByComparator);
	}

	/**
	* Removes all the dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	*/
	public static void removeByR_PP_T(long repositoryId,
		java.lang.String parentPath, int type) {
		getPersistence().removeByR_PP_T(repositoryId, parentPath, type);
	}

	/**
	* Returns the number of dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	*
	* @param repositoryId the repository ID
	* @param parentPath the parent path
	* @param type the type
	* @return the number of matching dropbox entries
	*/
	public static int countByR_PP_T(long repositoryId,
		java.lang.String parentPath, int type) {
		return getPersistence().countByR_PP_T(repositoryId, parentPath, type);
	}

	/**
	* Caches the dropbox entry in the entity cache if it is enabled.
	*
	* @param dropboxEntry the dropbox entry
	*/
	public static void cacheResult(DropboxEntry dropboxEntry) {
		getPersistence().cacheResult(dropboxEntry);
	}

	/**
	* Caches the dropbox entries in the entity cache if it is enabled.
	*
	* @param dropboxEntries the dropbox entries
	*/
	public static void cacheResult(List<DropboxEntry> dropboxEntries) {
		getPersistence().cacheResult(dropboxEntries);
	}

	/**
	* Creates a new dropbox entry with the primary key. Does not add the dropbox entry to the database.
	*
	* @param entryId the primary key for the new dropbox entry
	* @return the new dropbox entry
	*/
	public static DropboxEntry create(long entryId) {
		return getPersistence().create(entryId);
	}

	/**
	* Removes the dropbox entry with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param entryId the primary key of the dropbox entry
	* @return the dropbox entry that was removed
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public static DropboxEntry remove(long entryId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence().remove(entryId);
	}

	public static DropboxEntry updateImpl(DropboxEntry dropboxEntry) {
		return getPersistence().updateImpl(dropboxEntry);
	}

	/**
	* Returns the dropbox entry with the primary key or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	*
	* @param entryId the primary key of the dropbox entry
	* @return the dropbox entry
	* @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	*/
	public static DropboxEntry findByPrimaryKey(long entryId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException {
		return getPersistence().findByPrimaryKey(entryId);
	}

	/**
	* Returns the dropbox entry with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param entryId the primary key of the dropbox entry
	* @return the dropbox entry, or <code>null</code> if a dropbox entry with the primary key could not be found
	*/
	public static DropboxEntry fetchByPrimaryKey(long entryId) {
		return getPersistence().fetchByPrimaryKey(entryId);
	}

	public static java.util.Map<java.io.Serializable, DropboxEntry> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the dropbox entries.
	*
	* @return the dropbox entries
	*/
	public static List<DropboxEntry> findAll() {
		return getPersistence().findAll();
	}

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
	public static List<DropboxEntry> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

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
	public static List<DropboxEntry> findAll(int start, int end,
		OrderByComparator<DropboxEntry> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Removes all the dropbox entries from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of dropbox entries.
	*
	* @return the number of dropbox entries
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static java.util.Set<java.lang.String> getBadColumnNames() {
		return getPersistence().getBadColumnNames();
	}

	public static DropboxEntryPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	@Deprecated
	public void setPersistence(DropboxEntryPersistence persistence) {
	}

	private static ServiceTracker<DropboxEntryPersistence, DropboxEntryPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(DropboxEntryUtil.class);

		_serviceTracker = new ServiceTracker<DropboxEntryPersistence, DropboxEntryPersistence>(bundle.getBundleContext(),
				DropboxEntryPersistence.class, null);

		_serviceTracker.open();
	}
}