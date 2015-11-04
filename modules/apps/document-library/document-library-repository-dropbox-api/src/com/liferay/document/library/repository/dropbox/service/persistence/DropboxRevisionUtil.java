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

import com.liferay.document.library.repository.dropbox.model.DropboxRevision;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.service.ServiceContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

import java.util.List;

/**
 * The persistence utility for the dropbox revision service. This utility wraps {@link com.liferay.document.library.repository.dropbox.service.persistence.impl.DropboxRevisionPersistenceImpl} and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DropboxRevisionPersistence
 * @see com.liferay.document.library.repository.dropbox.service.persistence.impl.DropboxRevisionPersistenceImpl
 * @generated
 */
@ProviderType
public class DropboxRevisionUtil {
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
	public static void clearCache(DropboxRevision dropboxRevision) {
		getPersistence().clearCache(dropboxRevision);
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
	public static List<DropboxRevision> findWithDynamicQuery(
		DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<DropboxRevision> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {
		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<DropboxRevision> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<DropboxRevision> orderByComparator) {
		return getPersistence()
				   .findWithDynamicQuery(dynamicQuery, start, end,
			orderByComparator);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel)
	 */
	public static DropboxRevision update(DropboxRevision dropboxRevision) {
		return getPersistence().update(dropboxRevision);
	}

	/**
	 * @see com.liferay.portal.service.persistence.BasePersistence#update(com.liferay.portal.model.BaseModel, ServiceContext)
	 */
	public static DropboxRevision update(DropboxRevision dropboxRevision,
		ServiceContext serviceContext) {
		return getPersistence().update(dropboxRevision, serviceContext);
	}

	/**
	* Returns all the dropbox revisions where uuid = &#63;.
	*
	* @param uuid the uuid
	* @return the matching dropbox revisions
	*/
	public static List<DropboxRevision> findByUuid(java.lang.String uuid) {
		return getPersistence().findByUuid(uuid);
	}

	/**
	* Returns a range of all the dropbox revisions where uuid = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param uuid the uuid
	* @param start the lower bound of the range of dropbox revisions
	* @param end the upper bound of the range of dropbox revisions (not inclusive)
	* @return the range of matching dropbox revisions
	*/
	public static List<DropboxRevision> findByUuid(java.lang.String uuid,
		int start, int end) {
		return getPersistence().findByUuid(uuid, start, end);
	}

	/**
	* Returns an ordered range of all the dropbox revisions where uuid = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param uuid the uuid
	* @param start the lower bound of the range of dropbox revisions
	* @param end the upper bound of the range of dropbox revisions (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching dropbox revisions
	*/
	public static List<DropboxRevision> findByUuid(java.lang.String uuid,
		int start, int end, OrderByComparator<DropboxRevision> orderByComparator) {
		return getPersistence().findByUuid(uuid, start, end, orderByComparator);
	}

	/**
	* Returns the first dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public static DropboxRevision findByUuid_First(java.lang.String uuid,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence().findByUuid_First(uuid, orderByComparator);
	}

	/**
	* Returns the first dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public static DropboxRevision fetchByUuid_First(java.lang.String uuid,
		OrderByComparator<DropboxRevision> orderByComparator) {
		return getPersistence().fetchByUuid_First(uuid, orderByComparator);
	}

	/**
	* Returns the last dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public static DropboxRevision findByUuid_Last(java.lang.String uuid,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence().findByUuid_Last(uuid, orderByComparator);
	}

	/**
	* Returns the last dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public static DropboxRevision fetchByUuid_Last(java.lang.String uuid,
		OrderByComparator<DropboxRevision> orderByComparator) {
		return getPersistence().fetchByUuid_Last(uuid, orderByComparator);
	}

	/**
	* Returns the dropbox revisions before and after the current dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param revisionId the primary key of the current dropbox revision
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	*/
	public static DropboxRevision[] findByUuid_PrevAndNext(long revisionId,
		java.lang.String uuid,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence()
				   .findByUuid_PrevAndNext(revisionId, uuid, orderByComparator);
	}

	/**
	* Removes all the dropbox revisions where uuid = &#63; from the database.
	*
	* @param uuid the uuid
	*/
	public static void removeByUuid(java.lang.String uuid) {
		getPersistence().removeByUuid(uuid);
	}

	/**
	* Returns the number of dropbox revisions where uuid = &#63;.
	*
	* @param uuid the uuid
	* @return the number of matching dropbox revisions
	*/
	public static int countByUuid(java.lang.String uuid) {
		return getPersistence().countByUuid(uuid);
	}

	/**
	* Returns all the dropbox revisions where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @return the matching dropbox revisions
	*/
	public static List<DropboxRevision> findByR_E(long repositoryId,
		long entryId) {
		return getPersistence().findByR_E(repositoryId, entryId);
	}

	/**
	* Returns a range of all the dropbox revisions where repositoryId = &#63; and entryId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param start the lower bound of the range of dropbox revisions
	* @param end the upper bound of the range of dropbox revisions (not inclusive)
	* @return the range of matching dropbox revisions
	*/
	public static List<DropboxRevision> findByR_E(long repositoryId,
		long entryId, int start, int end) {
		return getPersistence().findByR_E(repositoryId, entryId, start, end);
	}

	/**
	* Returns an ordered range of all the dropbox revisions where repositoryId = &#63; and entryId = &#63;.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param start the lower bound of the range of dropbox revisions
	* @param end the upper bound of the range of dropbox revisions (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of matching dropbox revisions
	*/
	public static List<DropboxRevision> findByR_E(long repositoryId,
		long entryId, int start, int end,
		OrderByComparator<DropboxRevision> orderByComparator) {
		return getPersistence()
				   .findByR_E(repositoryId, entryId, start, end,
			orderByComparator);
	}

	/**
	* Returns the first dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public static DropboxRevision findByR_E_First(long repositoryId,
		long entryId, OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence()
				   .findByR_E_First(repositoryId, entryId, orderByComparator);
	}

	/**
	* Returns the first dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public static DropboxRevision fetchByR_E_First(long repositoryId,
		long entryId, OrderByComparator<DropboxRevision> orderByComparator) {
		return getPersistence()
				   .fetchByR_E_First(repositoryId, entryId, orderByComparator);
	}

	/**
	* Returns the last dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public static DropboxRevision findByR_E_Last(long repositoryId,
		long entryId, OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence()
				   .findByR_E_Last(repositoryId, entryId, orderByComparator);
	}

	/**
	* Returns the last dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public static DropboxRevision fetchByR_E_Last(long repositoryId,
		long entryId, OrderByComparator<DropboxRevision> orderByComparator) {
		return getPersistence()
				   .fetchByR_E_Last(repositoryId, entryId, orderByComparator);
	}

	/**
	* Returns the dropbox revisions before and after the current dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param revisionId the primary key of the current dropbox revision
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	*/
	public static DropboxRevision[] findByR_E_PrevAndNext(long revisionId,
		long repositoryId, long entryId,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence()
				   .findByR_E_PrevAndNext(revisionId, repositoryId, entryId,
			orderByComparator);
	}

	/**
	* Removes all the dropbox revisions where repositoryId = &#63; and entryId = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	*/
	public static void removeByR_E(long repositoryId, long entryId) {
		getPersistence().removeByR_E(repositoryId, entryId);
	}

	/**
	* Returns the number of dropbox revisions where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @return the number of matching dropbox revisions
	*/
	public static int countByR_E(long repositoryId, long entryId) {
		return getPersistence().countByR_E(repositoryId, entryId);
	}

	/**
	* Returns the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchRevisionException} if it could not be found.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @return the matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public static DropboxRevision findByR_E_R(long repositoryId, long entryId,
		java.lang.String rev)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence().findByR_E_R(repositoryId, entryId, rev);
	}

	/**
	* Returns the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @return the matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public static DropboxRevision fetchByR_E_R(long repositoryId, long entryId,
		java.lang.String rev) {
		return getPersistence().fetchByR_E_R(repositoryId, entryId, rev);
	}

	/**
	* Returns the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public static DropboxRevision fetchByR_E_R(long repositoryId, long entryId,
		java.lang.String rev, boolean retrieveFromCache) {
		return getPersistence()
				   .fetchByR_E_R(repositoryId, entryId, rev, retrieveFromCache);
	}

	/**
	* Removes the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @return the dropbox revision that was removed
	*/
	public static DropboxRevision removeByR_E_R(long repositoryId,
		long entryId, java.lang.String rev)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence().removeByR_E_R(repositoryId, entryId, rev);
	}

	/**
	* Returns the number of dropbox revisions where repositoryId = &#63; and entryId = &#63; and rev = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @return the number of matching dropbox revisions
	*/
	public static int countByR_E_R(long repositoryId, long entryId,
		java.lang.String rev) {
		return getPersistence().countByR_E_R(repositoryId, entryId, rev);
	}

	/**
	* Caches the dropbox revision in the entity cache if it is enabled.
	*
	* @param dropboxRevision the dropbox revision
	*/
	public static void cacheResult(DropboxRevision dropboxRevision) {
		getPersistence().cacheResult(dropboxRevision);
	}

	/**
	* Caches the dropbox revisions in the entity cache if it is enabled.
	*
	* @param dropboxRevisions the dropbox revisions
	*/
	public static void cacheResult(List<DropboxRevision> dropboxRevisions) {
		getPersistence().cacheResult(dropboxRevisions);
	}

	/**
	* Creates a new dropbox revision with the primary key. Does not add the dropbox revision to the database.
	*
	* @param revisionId the primary key for the new dropbox revision
	* @return the new dropbox revision
	*/
	public static DropboxRevision create(long revisionId) {
		return getPersistence().create(revisionId);
	}

	/**
	* Removes the dropbox revision with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param revisionId the primary key of the dropbox revision
	* @return the dropbox revision that was removed
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	*/
	public static DropboxRevision remove(long revisionId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence().remove(revisionId);
	}

	public static DropboxRevision updateImpl(DropboxRevision dropboxRevision) {
		return getPersistence().updateImpl(dropboxRevision);
	}

	/**
	* Returns the dropbox revision with the primary key or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchRevisionException} if it could not be found.
	*
	* @param revisionId the primary key of the dropbox revision
	* @return the dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	*/
	public static DropboxRevision findByPrimaryKey(long revisionId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException {
		return getPersistence().findByPrimaryKey(revisionId);
	}

	/**
	* Returns the dropbox revision with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param revisionId the primary key of the dropbox revision
	* @return the dropbox revision, or <code>null</code> if a dropbox revision with the primary key could not be found
	*/
	public static DropboxRevision fetchByPrimaryKey(long revisionId) {
		return getPersistence().fetchByPrimaryKey(revisionId);
	}

	public static java.util.Map<java.io.Serializable, DropboxRevision> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys) {
		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	* Returns all the dropbox revisions.
	*
	* @return the dropbox revisions
	*/
	public static List<DropboxRevision> findAll() {
		return getPersistence().findAll();
	}

	/**
	* Returns a range of all the dropbox revisions.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of dropbox revisions
	* @param end the upper bound of the range of dropbox revisions (not inclusive)
	* @return the range of dropbox revisions
	*/
	public static List<DropboxRevision> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

	/**
	* Returns an ordered range of all the dropbox revisions.
	*
	* <p>
	* Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link DropboxRevisionModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	* </p>
	*
	* @param start the lower bound of the range of dropbox revisions
	* @param end the upper bound of the range of dropbox revisions (not inclusive)
	* @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	* @return the ordered range of dropbox revisions
	*/
	public static List<DropboxRevision> findAll(int start, int end,
		OrderByComparator<DropboxRevision> orderByComparator) {
		return getPersistence().findAll(start, end, orderByComparator);
	}

	/**
	* Removes all the dropbox revisions from the database.
	*/
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	* Returns the number of dropbox revisions.
	*
	* @return the number of dropbox revisions
	*/
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static java.util.Set<java.lang.String> getBadColumnNames() {
		return getPersistence().getBadColumnNames();
	}

	public static DropboxRevisionPersistence getPersistence() {
		return _serviceTracker.getService();
	}

	/**
	 * @deprecated As of 6.2.0
	 */
	@Deprecated
	public void setPersistence(DropboxRevisionPersistence persistence) {
	}

	private static ServiceTracker<DropboxRevisionPersistence, DropboxRevisionPersistence> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(DropboxRevisionUtil.class);

		_serviceTracker = new ServiceTracker<DropboxRevisionPersistence, DropboxRevisionPersistence>(bundle.getBundleContext(),
				DropboxRevisionPersistence.class, null);

		_serviceTracker.open();
	}
}