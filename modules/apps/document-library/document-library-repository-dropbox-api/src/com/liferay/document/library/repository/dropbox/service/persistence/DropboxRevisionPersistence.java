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

import com.liferay.portal.service.persistence.BasePersistence;

/**
 * The persistence interface for the dropbox revision service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.document.library.repository.dropbox.service.persistence.impl.DropboxRevisionPersistenceImpl
 * @see DropboxRevisionUtil
 * @generated
 */
@ProviderType
public interface DropboxRevisionPersistence extends BasePersistence<DropboxRevision> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link DropboxRevisionUtil} to access the dropbox revision persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	* Returns all the dropbox revisions where uuid = &#63;.
	*
	* @param uuid the uuid
	* @return the matching dropbox revisions
	*/
	public java.util.List<DropboxRevision> findByUuid(java.lang.String uuid);

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
	public java.util.List<DropboxRevision> findByUuid(java.lang.String uuid,
		int start, int end);

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
	public java.util.List<DropboxRevision> findByUuid(java.lang.String uuid,
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator);

	/**
	* Returns the first dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public DropboxRevision findByUuid_First(java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Returns the first dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public DropboxRevision fetchByUuid_First(java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator);

	/**
	* Returns the last dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public DropboxRevision findByUuid_Last(java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Returns the last dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public DropboxRevision fetchByUuid_Last(java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator);

	/**
	* Returns the dropbox revisions before and after the current dropbox revision in the ordered set where uuid = &#63;.
	*
	* @param revisionId the primary key of the current dropbox revision
	* @param uuid the uuid
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the previous, current, and next dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	*/
	public DropboxRevision[] findByUuid_PrevAndNext(long revisionId,
		java.lang.String uuid,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Removes all the dropbox revisions where uuid = &#63; from the database.
	*
	* @param uuid the uuid
	*/
	public void removeByUuid(java.lang.String uuid);

	/**
	* Returns the number of dropbox revisions where uuid = &#63;.
	*
	* @param uuid the uuid
	* @return the number of matching dropbox revisions
	*/
	public int countByUuid(java.lang.String uuid);

	/**
	* Returns all the dropbox revisions where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @return the matching dropbox revisions
	*/
	public java.util.List<DropboxRevision> findByR_E(long repositoryId,
		long entryId);

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
	public java.util.List<DropboxRevision> findByR_E(long repositoryId,
		long entryId, int start, int end);

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
	public java.util.List<DropboxRevision> findByR_E(long repositoryId,
		long entryId, int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator);

	/**
	* Returns the first dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public DropboxRevision findByR_E_First(long repositoryId, long entryId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Returns the first dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the first matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public DropboxRevision fetchByR_E_First(long repositoryId, long entryId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator);

	/**
	* Returns the last dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public DropboxRevision findByR_E_Last(long repositoryId, long entryId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Returns the last dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	* @return the last matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public DropboxRevision fetchByR_E_Last(long repositoryId, long entryId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator);

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
	public DropboxRevision[] findByR_E_PrevAndNext(long revisionId,
		long repositoryId, long entryId,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Removes all the dropbox revisions where repositoryId = &#63; and entryId = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	*/
	public void removeByR_E(long repositoryId, long entryId);

	/**
	* Returns the number of dropbox revisions where repositoryId = &#63; and entryId = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @return the number of matching dropbox revisions
	*/
	public int countByR_E(long repositoryId, long entryId);

	/**
	* Returns the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchRevisionException} if it could not be found.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @return the matching dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	*/
	public DropboxRevision findByR_E_R(long repositoryId, long entryId,
		java.lang.String rev)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Returns the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @return the matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public DropboxRevision fetchByR_E_R(long repositoryId, long entryId,
		java.lang.String rev);

	/**
	* Returns the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @param retrieveFromCache whether to use the finder cache
	* @return the matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	*/
	public DropboxRevision fetchByR_E_R(long repositoryId, long entryId,
		java.lang.String rev, boolean retrieveFromCache);

	/**
	* Removes the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; from the database.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @return the dropbox revision that was removed
	*/
	public DropboxRevision removeByR_E_R(long repositoryId, long entryId,
		java.lang.String rev)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Returns the number of dropbox revisions where repositoryId = &#63; and entryId = &#63; and rev = &#63;.
	*
	* @param repositoryId the repository ID
	* @param entryId the entry ID
	* @param rev the rev
	* @return the number of matching dropbox revisions
	*/
	public int countByR_E_R(long repositoryId, long entryId,
		java.lang.String rev);

	/**
	* Caches the dropbox revision in the entity cache if it is enabled.
	*
	* @param dropboxRevision the dropbox revision
	*/
	public void cacheResult(DropboxRevision dropboxRevision);

	/**
	* Caches the dropbox revisions in the entity cache if it is enabled.
	*
	* @param dropboxRevisions the dropbox revisions
	*/
	public void cacheResult(java.util.List<DropboxRevision> dropboxRevisions);

	/**
	* Creates a new dropbox revision with the primary key. Does not add the dropbox revision to the database.
	*
	* @param revisionId the primary key for the new dropbox revision
	* @return the new dropbox revision
	*/
	public DropboxRevision create(long revisionId);

	/**
	* Removes the dropbox revision with the primary key from the database. Also notifies the appropriate model listeners.
	*
	* @param revisionId the primary key of the dropbox revision
	* @return the dropbox revision that was removed
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	*/
	public DropboxRevision remove(long revisionId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	public DropboxRevision updateImpl(DropboxRevision dropboxRevision);

	/**
	* Returns the dropbox revision with the primary key or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchRevisionException} if it could not be found.
	*
	* @param revisionId the primary key of the dropbox revision
	* @return the dropbox revision
	* @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	*/
	public DropboxRevision findByPrimaryKey(long revisionId)
		throws com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;

	/**
	* Returns the dropbox revision with the primary key or returns <code>null</code> if it could not be found.
	*
	* @param revisionId the primary key of the dropbox revision
	* @return the dropbox revision, or <code>null</code> if a dropbox revision with the primary key could not be found
	*/
	public DropboxRevision fetchByPrimaryKey(long revisionId);

	@Override
	public java.util.Map<java.io.Serializable, DropboxRevision> fetchByPrimaryKeys(
		java.util.Set<java.io.Serializable> primaryKeys);

	/**
	* Returns all the dropbox revisions.
	*
	* @return the dropbox revisions
	*/
	public java.util.List<DropboxRevision> findAll();

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
	public java.util.List<DropboxRevision> findAll(int start, int end);

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
	public java.util.List<DropboxRevision> findAll(int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<DropboxRevision> orderByComparator);

	/**
	* Removes all the dropbox revisions from the database.
	*/
	public void removeAll();

	/**
	* Returns the number of dropbox revisions.
	*
	* @return the number of dropbox revisions
	*/
	public int countAll();

	@Override
	public java.util.Set<java.lang.String> getBadColumnNames();
}