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

package com.liferay.document.library.repository.dropbox.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.repository.dropbox.exception.NoSuchEntryException;
import com.liferay.document.library.repository.dropbox.model.DropboxEntry;
import com.liferay.document.library.repository.dropbox.model.impl.DropboxEntryImpl;
import com.liferay.document.library.repository.dropbox.model.impl.DropboxEntryModelImpl;
import com.liferay.document.library.repository.dropbox.service.persistence.DropboxEntryPersistence;

import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the dropbox entry service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DropboxEntryPersistence
 * @see com.liferay.document.library.repository.dropbox.service.persistence.DropboxEntryUtil
 * @generated
 */
@ProviderType
public class DropboxEntryPersistenceImpl extends BasePersistenceImpl<DropboxEntry>
	implements DropboxEntryPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link DropboxEntryUtil} to access the dropbox entry persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = DropboxEntryImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] { String.class.getName() },
			DropboxEntryModelImpl.UUID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] { String.class.getName() });

	/**
	 * Returns all the dropbox entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching dropbox entries
	 */
	@Override
	public List<DropboxEntry> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<DropboxEntry> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
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
	@Override
	public List<DropboxEntry> findByUuid(String uuid, int start, int end,
		OrderByComparator<DropboxEntry> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID;
			finderArgs = new Object[] { uuid };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID;
			finderArgs = new Object[] { uuid, start, end, orderByComparator };
		}

		List<DropboxEntry> list = (List<DropboxEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DropboxEntry dropboxEntry : list) {
				if (!Validator.equals(uuid, dropboxEntry.getUuid())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DropboxEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				if (!pagination) {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first dropbox entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dropbox entry
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry findByUuid_First(String uuid,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByUuid_First(uuid, orderByComparator);

		if (dropboxEntry != null) {
			return dropboxEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first dropbox entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByUuid_First(String uuid,
		OrderByComparator<DropboxEntry> orderByComparator) {
		List<DropboxEntry> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last dropbox entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dropbox entry
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry findByUuid_Last(String uuid,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByUuid_Last(uuid, orderByComparator);

		if (dropboxEntry != null) {
			return dropboxEntry;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last dropbox entry in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByUuid_Last(String uuid,
		OrderByComparator<DropboxEntry> orderByComparator) {
		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<DropboxEntry> list = findByUuid(uuid, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxEntry[] findByUuid_PrevAndNext(long entryId, String uuid,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = findByPrimaryKey(entryId);

		Session session = null;

		try {
			session = openSession();

			DropboxEntry[] array = new DropboxEntryImpl[3];

			array[0] = getByUuid_PrevAndNext(session, dropboxEntry, uuid,
					orderByComparator, true);

			array[1] = dropboxEntry;

			array[2] = getByUuid_PrevAndNext(session, dropboxEntry, uuid,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DropboxEntry getByUuid_PrevAndNext(Session session,
		DropboxEntry dropboxEntry, String uuid,
		OrderByComparator<DropboxEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid == null) {
			query.append(_FINDER_COLUMN_UUID_UUID_1);
		}
		else if (uuid.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			query.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DropboxEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindUuid) {
			qPos.add(uuid);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dropboxEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DropboxEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dropbox entries where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (DropboxEntry dropboxEntry : findByUuid(uuid, QueryUtil.ALL_POS,
				QueryUtil.ALL_POS, null)) {
			remove(dropboxEntry);
		}
	}

	/**
	 * Returns the number of dropbox entries where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching dropbox entries
	 */
	@Override
	public int countByUuid(String uuid) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID;

		Object[] finderArgs = new Object[] { uuid };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_DROPBOXENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_1 = "dropboxEntry.uuid IS NULL";
	private static final String _FINDER_COLUMN_UUID_UUID_2 = "dropboxEntry.uuid = ?";
	private static final String _FINDER_COLUMN_UUID_UUID_3 = "(dropboxEntry.uuid IS NULL OR dropboxEntry.uuid = '')";
	public static final FinderPath FINDER_PATH_FETCH_BY_UUID_G = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByUUID_G",
			new String[] { String.class.getName(), Long.class.getName() },
			DropboxEntryModelImpl.UUID_COLUMN_BITMASK |
			DropboxEntryModelImpl.GROUPID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID_G = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G",
			new String[] { String.class.getName(), Long.class.getName() });

	/**
	 * Returns the dropbox entry where uuid = &#63; and groupId = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching dropbox entry
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry findByUUID_G(String uuid, long groupId)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByUUID_G(uuid, groupId);

		if (dropboxEntry == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("uuid=");
			msg.append(uuid);

			msg.append(", groupId=");
			msg.append(groupId);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchEntryException(msg.toString());
		}

		return dropboxEntry;
	}

	/**
	 * Returns the dropbox entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the dropbox entry where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByUUID_G(String uuid, long groupId,
		boolean retrieveFromCache) {
		Object[] finderArgs = new Object[] { uuid, groupId };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_UUID_G,
					finderArgs, this);
		}

		if (result instanceof DropboxEntry) {
			DropboxEntry dropboxEntry = (DropboxEntry)result;

			if (!Validator.equals(uuid, dropboxEntry.getUuid()) ||
					(groupId != dropboxEntry.getGroupId())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_G_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			query.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				qPos.add(groupId);

				List<DropboxEntry> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
						finderArgs, list);
				}
				else {
					DropboxEntry dropboxEntry = list.get(0);

					result = dropboxEntry;

					cacheResult(dropboxEntry);

					if ((dropboxEntry.getUuid() == null) ||
							!dropboxEntry.getUuid().equals(uuid) ||
							(dropboxEntry.getGroupId() != groupId)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
							finderArgs, dropboxEntry);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (DropboxEntry)result;
		}
	}

	/**
	 * Removes the dropbox entry where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the dropbox entry that was removed
	 */
	@Override
	public DropboxEntry removeByUUID_G(String uuid, long groupId)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = findByUUID_G(uuid, groupId);

		return remove(dropboxEntry);
	}

	/**
	 * Returns the number of dropbox entries where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching dropbox entries
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID_G;

		Object[] finderArgs = new Object[] { uuid, groupId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_DROPBOXENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_G_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			query.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				qPos.add(groupId);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_G_UUID_1 = "dropboxEntry.uuid IS NULL AND ";
	private static final String _FINDER_COLUMN_UUID_G_UUID_2 = "dropboxEntry.uuid = ? AND ";
	private static final String _FINDER_COLUMN_UUID_G_UUID_3 = "(dropboxEntry.uuid IS NULL OR dropboxEntry.uuid = '') AND ";
	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 = "dropboxEntry.groupId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID_C = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C =
		new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] { String.class.getName(), Long.class.getName() },
			DropboxEntryModelImpl.UUID_COLUMN_BITMASK |
			DropboxEntryModelImpl.COMPANYID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID_C = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] { String.class.getName(), Long.class.getName() });

	/**
	 * Returns all the dropbox entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching dropbox entries
	 */
	@Override
	public List<DropboxEntry> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(uuid, companyId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
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
	@Override
	public List<DropboxEntry> findByUuid_C(String uuid, long companyId,
		int start, int end) {
		return findByUuid_C(uuid, companyId, start, end, null);
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
	@Override
	public List<DropboxEntry> findByUuid_C(String uuid, long companyId,
		int start, int end, OrderByComparator<DropboxEntry> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C;
			finderArgs = new Object[] { uuid, companyId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID_C;
			finderArgs = new Object[] {
					uuid, companyId,
					
					start, end, orderByComparator
				};
		}

		List<DropboxEntry> list = (List<DropboxEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DropboxEntry dropboxEntry : list) {
				if (!Validator.equals(uuid, dropboxEntry.getUuid()) ||
						(companyId != dropboxEntry.getCompanyId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DropboxEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				qPos.add(companyId);

				if (!pagination) {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public DropboxEntry findByUuid_C_First(String uuid, long companyId,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByUuid_C_First(uuid, companyId,
				orderByComparator);

		if (dropboxEntry != null) {
			return dropboxEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(", companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByUuid_C_First(String uuid, long companyId,
		OrderByComparator<DropboxEntry> orderByComparator) {
		List<DropboxEntry> list = findByUuid_C(uuid, companyId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxEntry findByUuid_C_Last(String uuid, long companyId,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByUuid_C_Last(uuid, companyId,
				orderByComparator);

		if (dropboxEntry != null) {
			return dropboxEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(", companyId=");
		msg.append(companyId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last dropbox entry in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByUuid_C_Last(String uuid, long companyId,
		OrderByComparator<DropboxEntry> orderByComparator) {
		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<DropboxEntry> list = findByUuid_C(uuid, companyId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxEntry[] findByUuid_C_PrevAndNext(long entryId, String uuid,
		long companyId, OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = findByPrimaryKey(entryId);

		Session session = null;

		try {
			session = openSession();

			DropboxEntry[] array = new DropboxEntryImpl[3];

			array[0] = getByUuid_C_PrevAndNext(session, dropboxEntry, uuid,
					companyId, orderByComparator, true);

			array[1] = dropboxEntry;

			array[2] = getByUuid_C_PrevAndNext(session, dropboxEntry, uuid,
					companyId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DropboxEntry getByUuid_C_PrevAndNext(Session session,
		DropboxEntry dropboxEntry, String uuid, long companyId,
		OrderByComparator<DropboxEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

		boolean bindUuid = false;

		if (uuid == null) {
			query.append(_FINDER_COLUMN_UUID_C_UUID_1);
		}
		else if (uuid.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			query.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DropboxEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		if (bindUuid) {
			qPos.add(uuid);
		}

		qPos.add(companyId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dropboxEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DropboxEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dropbox entries where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (DropboxEntry dropboxEntry : findByUuid_C(uuid, companyId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dropboxEntry);
		}
	}

	/**
	 * Returns the number of dropbox entries where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching dropbox entries
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID_C;

		Object[] finderArgs = new Object[] { uuid, companyId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_DROPBOXENTRY_WHERE);

			boolean bindUuid = false;

			if (uuid == null) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_1);
			}
			else if (uuid.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				query.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			query.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				if (bindUuid) {
					qPos.add(uuid);
				}

				qPos.add(companyId);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_1 = "dropboxEntry.uuid IS NULL AND ";
	private static final String _FINDER_COLUMN_UUID_C_UUID_2 = "dropboxEntry.uuid = ? AND ";
	private static final String _FINDER_COLUMN_UUID_C_UUID_3 = "(dropboxEntry.uuid IS NULL OR dropboxEntry.uuid = '') AND ";
	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 = "dropboxEntry.companyId = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_R_P = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_ENTITY, "fetchByR_P",
			new String[] { Long.class.getName(), String.class.getName() },
			DropboxEntryModelImpl.REPOSITORYID_COLUMN_BITMASK |
			DropboxEntryModelImpl.PATH_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_R_P = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_P",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns the dropbox entry where repositoryId = &#63; and path = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	 *
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @return the matching dropbox entry
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry findByR_P(long repositoryId, String path)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByR_P(repositoryId, path);

		if (dropboxEntry == null) {
			StringBundler msg = new StringBundler(6);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("repositoryId=");
			msg.append(repositoryId);

			msg.append(", path=");
			msg.append(path);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchEntryException(msg.toString());
		}

		return dropboxEntry;
	}

	/**
	 * Returns the dropbox entry where repositoryId = &#63; and path = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByR_P(long repositoryId, String path) {
		return fetchByR_P(repositoryId, path, true);
	}

	/**
	 * Returns the dropbox entry where repositoryId = &#63; and path = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @param retrieveFromCache whether to use the finder cache
	 * @return the matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByR_P(long repositoryId, String path,
		boolean retrieveFromCache) {
		Object[] finderArgs = new Object[] { repositoryId, path };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_R_P,
					finderArgs, this);
		}

		if (result instanceof DropboxEntry) {
			DropboxEntry dropboxEntry = (DropboxEntry)result;

			if ((repositoryId != dropboxEntry.getRepositoryId()) ||
					!Validator.equals(path, dropboxEntry.getPath())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

			query.append(_FINDER_COLUMN_R_P_REPOSITORYID_2);

			boolean bindPath = false;

			if (path == null) {
				query.append(_FINDER_COLUMN_R_P_PATH_1);
			}
			else if (path.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_R_P_PATH_3);
			}
			else {
				bindPath = true;

				query.append(_FINDER_COLUMN_R_P_PATH_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				if (bindPath) {
					qPos.add(path);
				}

				List<DropboxEntry> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_P,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"DropboxEntryPersistenceImpl.fetchByR_P(long, String, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					DropboxEntry dropboxEntry = list.get(0);

					result = dropboxEntry;

					cacheResult(dropboxEntry);

					if ((dropboxEntry.getRepositoryId() != repositoryId) ||
							(dropboxEntry.getPath() == null) ||
							!dropboxEntry.getPath().equals(path)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_P,
							finderArgs, dropboxEntry);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_R_P,
					finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (DropboxEntry)result;
		}
	}

	/**
	 * Removes the dropbox entry where repositoryId = &#63; and path = &#63; from the database.
	 *
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @return the dropbox entry that was removed
	 */
	@Override
	public DropboxEntry removeByR_P(long repositoryId, String path)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = findByR_P(repositoryId, path);

		return remove(dropboxEntry);
	}

	/**
	 * Returns the number of dropbox entries where repositoryId = &#63; and path = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param path the path
	 * @return the number of matching dropbox entries
	 */
	@Override
	public int countByR_P(long repositoryId, String path) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_R_P;

		Object[] finderArgs = new Object[] { repositoryId, path };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_DROPBOXENTRY_WHERE);

			query.append(_FINDER_COLUMN_R_P_REPOSITORYID_2);

			boolean bindPath = false;

			if (path == null) {
				query.append(_FINDER_COLUMN_R_P_PATH_1);
			}
			else if (path.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_R_P_PATH_3);
			}
			else {
				bindPath = true;

				query.append(_FINDER_COLUMN_R_P_PATH_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				if (bindPath) {
					qPos.add(path);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_R_P_REPOSITORYID_2 = "dropboxEntry.repositoryId = ? AND ";
	private static final String _FINDER_COLUMN_R_P_PATH_1 = "dropboxEntry.path IS NULL";
	private static final String _FINDER_COLUMN_R_P_PATH_2 = "dropboxEntry.path = ?";
	private static final String _FINDER_COLUMN_R_P_PATH_3 = "(dropboxEntry.path IS NULL OR dropboxEntry.path = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_R_PP = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByR_PP",
			new String[] {
				Long.class.getName(), String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByR_PP",
			new String[] { Long.class.getName(), String.class.getName() },
			DropboxEntryModelImpl.REPOSITORYID_COLUMN_BITMASK |
			DropboxEntryModelImpl.PARENTPATH_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_R_PP = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_PP",
			new String[] { Long.class.getName(), String.class.getName() });

	/**
	 * Returns all the dropbox entries where repositoryId = &#63; and parentPath = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param parentPath the parent path
	 * @return the matching dropbox entries
	 */
	@Override
	public List<DropboxEntry> findByR_PP(long repositoryId, String parentPath) {
		return findByR_PP(repositoryId, parentPath, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
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
	@Override
	public List<DropboxEntry> findByR_PP(long repositoryId, String parentPath,
		int start, int end) {
		return findByR_PP(repositoryId, parentPath, start, end, null);
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
	@Override
	public List<DropboxEntry> findByR_PP(long repositoryId, String parentPath,
		int start, int end, OrderByComparator<DropboxEntry> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP;
			finderArgs = new Object[] { repositoryId, parentPath };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_R_PP;
			finderArgs = new Object[] {
					repositoryId, parentPath,
					
					start, end, orderByComparator
				};
		}

		List<DropboxEntry> list = (List<DropboxEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DropboxEntry dropboxEntry : list) {
				if ((repositoryId != dropboxEntry.getRepositoryId()) ||
						!Validator.equals(parentPath,
							dropboxEntry.getParentPath())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(4 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(4);
			}

			query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

			query.append(_FINDER_COLUMN_R_PP_REPOSITORYID_2);

			boolean bindParentPath = false;

			if (parentPath == null) {
				query.append(_FINDER_COLUMN_R_PP_PARENTPATH_1);
			}
			else if (parentPath.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_R_PP_PARENTPATH_3);
			}
			else {
				bindParentPath = true;

				query.append(_FINDER_COLUMN_R_PP_PARENTPATH_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DropboxEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				if (bindParentPath) {
					qPos.add(parentPath);
				}

				if (!pagination) {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public DropboxEntry findByR_PP_First(long repositoryId, String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByR_PP_First(repositoryId, parentPath,
				orderByComparator);

		if (dropboxEntry != null) {
			return dropboxEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("repositoryId=");
		msg.append(repositoryId);

		msg.append(", parentPath=");
		msg.append(parentPath);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the first dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param parentPath the parent path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByR_PP_First(long repositoryId, String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator) {
		List<DropboxEntry> list = findByR_PP(repositoryId, parentPath, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxEntry findByR_PP_Last(long repositoryId, String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByR_PP_Last(repositoryId, parentPath,
				orderByComparator);

		if (dropboxEntry != null) {
			return dropboxEntry;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("repositoryId=");
		msg.append(repositoryId);

		msg.append(", parentPath=");
		msg.append(parentPath);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
	}

	/**
	 * Returns the last dropbox entry in the ordered set where repositoryId = &#63; and parentPath = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param parentPath the parent path
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dropbox entry, or <code>null</code> if a matching dropbox entry could not be found
	 */
	@Override
	public DropboxEntry fetchByR_PP_Last(long repositoryId, String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator) {
		int count = countByR_PP(repositoryId, parentPath);

		if (count == 0) {
			return null;
		}

		List<DropboxEntry> list = findByR_PP(repositoryId, parentPath,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxEntry[] findByR_PP_PrevAndNext(long entryId,
		long repositoryId, String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = findByPrimaryKey(entryId);

		Session session = null;

		try {
			session = openSession();

			DropboxEntry[] array = new DropboxEntryImpl[3];

			array[0] = getByR_PP_PrevAndNext(session, dropboxEntry,
					repositoryId, parentPath, orderByComparator, true);

			array[1] = dropboxEntry;

			array[2] = getByR_PP_PrevAndNext(session, dropboxEntry,
					repositoryId, parentPath, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DropboxEntry getByR_PP_PrevAndNext(Session session,
		DropboxEntry dropboxEntry, long repositoryId, String parentPath,
		OrderByComparator<DropboxEntry> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

		query.append(_FINDER_COLUMN_R_PP_REPOSITORYID_2);

		boolean bindParentPath = false;

		if (parentPath == null) {
			query.append(_FINDER_COLUMN_R_PP_PARENTPATH_1);
		}
		else if (parentPath.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_R_PP_PARENTPATH_3);
		}
		else {
			bindParentPath = true;

			query.append(_FINDER_COLUMN_R_PP_PARENTPATH_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DropboxEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(repositoryId);

		if (bindParentPath) {
			qPos.add(parentPath);
		}

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dropboxEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DropboxEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dropbox entries where repositoryId = &#63; and parentPath = &#63; from the database.
	 *
	 * @param repositoryId the repository ID
	 * @param parentPath the parent path
	 */
	@Override
	public void removeByR_PP(long repositoryId, String parentPath) {
		for (DropboxEntry dropboxEntry : findByR_PP(repositoryId, parentPath,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dropboxEntry);
		}
	}

	/**
	 * Returns the number of dropbox entries where repositoryId = &#63; and parentPath = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param parentPath the parent path
	 * @return the number of matching dropbox entries
	 */
	@Override
	public int countByR_PP(long repositoryId, String parentPath) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_R_PP;

		Object[] finderArgs = new Object[] { repositoryId, parentPath };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_DROPBOXENTRY_WHERE);

			query.append(_FINDER_COLUMN_R_PP_REPOSITORYID_2);

			boolean bindParentPath = false;

			if (parentPath == null) {
				query.append(_FINDER_COLUMN_R_PP_PARENTPATH_1);
			}
			else if (parentPath.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_R_PP_PARENTPATH_3);
			}
			else {
				bindParentPath = true;

				query.append(_FINDER_COLUMN_R_PP_PARENTPATH_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				if (bindParentPath) {
					qPos.add(parentPath);
				}

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_R_PP_REPOSITORYID_2 = "dropboxEntry.repositoryId = ? AND ";
	private static final String _FINDER_COLUMN_R_PP_PARENTPATH_1 = "dropboxEntry.parentPath IS NULL";
	private static final String _FINDER_COLUMN_R_PP_PARENTPATH_2 = "dropboxEntry.parentPath = ?";
	private static final String _FINDER_COLUMN_R_PP_PARENTPATH_3 = "(dropboxEntry.parentPath IS NULL OR dropboxEntry.parentPath = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_R_PP_T = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByR_PP_T",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP_T =
		new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, DropboxEntryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByR_PP_T",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName()
			},
			DropboxEntryModelImpl.REPOSITORYID_COLUMN_BITMASK |
			DropboxEntryModelImpl.PARENTPATH_COLUMN_BITMASK |
			DropboxEntryModelImpl.TYPE_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_R_PP_T = new FinderPath(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_PP_T",
			new String[] {
				Long.class.getName(), String.class.getName(),
				Integer.class.getName()
			});

	/**
	 * Returns all the dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param parentPath the parent path
	 * @param type the type
	 * @return the matching dropbox entries
	 */
	@Override
	public List<DropboxEntry> findByR_PP_T(long repositoryId,
		String parentPath, int type) {
		return findByR_PP_T(repositoryId, parentPath, type, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
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
	@Override
	public List<DropboxEntry> findByR_PP_T(long repositoryId,
		String parentPath, int type, int start, int end) {
		return findByR_PP_T(repositoryId, parentPath, type, start, end, null);
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
	@Override
	public List<DropboxEntry> findByR_PP_T(long repositoryId,
		String parentPath, int type, int start, int end,
		OrderByComparator<DropboxEntry> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP_T;
			finderArgs = new Object[] { repositoryId, parentPath, type };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_R_PP_T;
			finderArgs = new Object[] {
					repositoryId, parentPath, type,
					
					start, end, orderByComparator
				};
		}

		List<DropboxEntry> list = (List<DropboxEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DropboxEntry dropboxEntry : list) {
				if ((repositoryId != dropboxEntry.getRepositoryId()) ||
						!Validator.equals(parentPath,
							dropboxEntry.getParentPath()) ||
						(type != dropboxEntry.getType())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(5 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(5);
			}

			query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

			query.append(_FINDER_COLUMN_R_PP_T_REPOSITORYID_2);

			boolean bindParentPath = false;

			if (parentPath == null) {
				query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_1);
			}
			else if (parentPath.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_3);
			}
			else {
				bindParentPath = true;

				query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_2);
			}

			query.append(_FINDER_COLUMN_R_PP_T_TYPE_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DropboxEntryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				if (bindParentPath) {
					qPos.add(parentPath);
				}

				qPos.add(type);

				if (!pagination) {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
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
	@Override
	public DropboxEntry findByR_PP_T_First(long repositoryId,
		String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByR_PP_T_First(repositoryId,
				parentPath, type, orderByComparator);

		if (dropboxEntry != null) {
			return dropboxEntry;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("repositoryId=");
		msg.append(repositoryId);

		msg.append(", parentPath=");
		msg.append(parentPath);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
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
	@Override
	public DropboxEntry fetchByR_PP_T_First(long repositoryId,
		String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator) {
		List<DropboxEntry> list = findByR_PP_T(repositoryId, parentPath, type,
				0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxEntry findByR_PP_T_Last(long repositoryId, String parentPath,
		int type, OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByR_PP_T_Last(repositoryId,
				parentPath, type, orderByComparator);

		if (dropboxEntry != null) {
			return dropboxEntry;
		}

		StringBundler msg = new StringBundler(8);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("repositoryId=");
		msg.append(repositoryId);

		msg.append(", parentPath=");
		msg.append(parentPath);

		msg.append(", type=");
		msg.append(type);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchEntryException(msg.toString());
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
	@Override
	public DropboxEntry fetchByR_PP_T_Last(long repositoryId,
		String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator) {
		int count = countByR_PP_T(repositoryId, parentPath, type);

		if (count == 0) {
			return null;
		}

		List<DropboxEntry> list = findByR_PP_T(repositoryId, parentPath, type,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxEntry[] findByR_PP_T_PrevAndNext(long entryId,
		long repositoryId, String parentPath, int type,
		OrderByComparator<DropboxEntry> orderByComparator)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = findByPrimaryKey(entryId);

		Session session = null;

		try {
			session = openSession();

			DropboxEntry[] array = new DropboxEntryImpl[3];

			array[0] = getByR_PP_T_PrevAndNext(session, dropboxEntry,
					repositoryId, parentPath, type, orderByComparator, true);

			array[1] = dropboxEntry;

			array[2] = getByR_PP_T_PrevAndNext(session, dropboxEntry,
					repositoryId, parentPath, type, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DropboxEntry getByR_PP_T_PrevAndNext(Session session,
		DropboxEntry dropboxEntry, long repositoryId, String parentPath,
		int type, OrderByComparator<DropboxEntry> orderByComparator,
		boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DROPBOXENTRY_WHERE);

		query.append(_FINDER_COLUMN_R_PP_T_REPOSITORYID_2);

		boolean bindParentPath = false;

		if (parentPath == null) {
			query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_1);
		}
		else if (parentPath.equals(StringPool.BLANK)) {
			query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_3);
		}
		else {
			bindParentPath = true;

			query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_2);
		}

		query.append(_FINDER_COLUMN_R_PP_T_TYPE_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			query.append(DropboxEntryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(repositoryId);

		if (bindParentPath) {
			qPos.add(parentPath);
		}

		qPos.add(type);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dropboxEntry);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DropboxEntry> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63; from the database.
	 *
	 * @param repositoryId the repository ID
	 * @param parentPath the parent path
	 * @param type the type
	 */
	@Override
	public void removeByR_PP_T(long repositoryId, String parentPath, int type) {
		for (DropboxEntry dropboxEntry : findByR_PP_T(repositoryId, parentPath,
				type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dropboxEntry);
		}
	}

	/**
	 * Returns the number of dropbox entries where repositoryId = &#63; and parentPath = &#63; and type = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param parentPath the parent path
	 * @param type the type
	 * @return the number of matching dropbox entries
	 */
	@Override
	public int countByR_PP_T(long repositoryId, String parentPath, int type) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_R_PP_T;

		Object[] finderArgs = new Object[] { repositoryId, parentPath, type };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_DROPBOXENTRY_WHERE);

			query.append(_FINDER_COLUMN_R_PP_T_REPOSITORYID_2);

			boolean bindParentPath = false;

			if (parentPath == null) {
				query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_1);
			}
			else if (parentPath.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_3);
			}
			else {
				bindParentPath = true;

				query.append(_FINDER_COLUMN_R_PP_T_PARENTPATH_2);
			}

			query.append(_FINDER_COLUMN_R_PP_T_TYPE_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				if (bindParentPath) {
					qPos.add(parentPath);
				}

				qPos.add(type);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(finderPath, finderArgs, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_R_PP_T_REPOSITORYID_2 = "dropboxEntry.repositoryId = ? AND ";
	private static final String _FINDER_COLUMN_R_PP_T_PARENTPATH_1 = "dropboxEntry.parentPath IS NULL AND ";
	private static final String _FINDER_COLUMN_R_PP_T_PARENTPATH_2 = "dropboxEntry.parentPath = ? AND ";
	private static final String _FINDER_COLUMN_R_PP_T_PARENTPATH_3 = "(dropboxEntry.parentPath IS NULL OR dropboxEntry.parentPath = '') AND ";
	private static final String _FINDER_COLUMN_R_PP_T_TYPE_2 = "dropboxEntry.type = ?";

	public DropboxEntryPersistenceImpl() {
		setModelClass(DropboxEntry.class);
	}

	/**
	 * Caches the dropbox entry in the entity cache if it is enabled.
	 *
	 * @param dropboxEntry the dropbox entry
	 */
	@Override
	public void cacheResult(DropboxEntry dropboxEntry) {
		EntityCacheUtil.putResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryImpl.class, dropboxEntry.getPrimaryKey(), dropboxEntry);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G,
			new Object[] { dropboxEntry.getUuid(), dropboxEntry.getGroupId() },
			dropboxEntry);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_P,
			new Object[] { dropboxEntry.getRepositoryId(), dropboxEntry.getPath() },
			dropboxEntry);

		dropboxEntry.resetOriginalValues();
	}

	/**
	 * Caches the dropbox entries in the entity cache if it is enabled.
	 *
	 * @param dropboxEntries the dropbox entries
	 */
	@Override
	public void cacheResult(List<DropboxEntry> dropboxEntries) {
		for (DropboxEntry dropboxEntry : dropboxEntries) {
			if (EntityCacheUtil.getResult(
						DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
						DropboxEntryImpl.class, dropboxEntry.getPrimaryKey()) == null) {
				cacheResult(dropboxEntry);
			}
			else {
				dropboxEntry.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all dropbox entries.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		EntityCacheUtil.clearCache(DropboxEntryImpl.class);

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the dropbox entry.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DropboxEntry dropboxEntry) {
		EntityCacheUtil.removeResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryImpl.class, dropboxEntry.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache((DropboxEntryModelImpl)dropboxEntry);
	}

	@Override
	public void clearCache(List<DropboxEntry> dropboxEntries) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (DropboxEntry dropboxEntry : dropboxEntries) {
			EntityCacheUtil.removeResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
				DropboxEntryImpl.class, dropboxEntry.getPrimaryKey());

			clearUniqueFindersCache((DropboxEntryModelImpl)dropboxEntry);
		}
	}

	protected void cacheUniqueFindersCache(
		DropboxEntryModelImpl dropboxEntryModelImpl, boolean isNew) {
		if (isNew) {
			Object[] args = new Object[] {
					dropboxEntryModelImpl.getUuid(),
					dropboxEntryModelImpl.getGroupId()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_UUID_G, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G, args,
				dropboxEntryModelImpl);

			args = new Object[] {
					dropboxEntryModelImpl.getRepositoryId(),
					dropboxEntryModelImpl.getPath()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_R_P, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_P, args,
				dropboxEntryModelImpl);
		}
		else {
			if ((dropboxEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_UUID_G.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxEntryModelImpl.getUuid(),
						dropboxEntryModelImpl.getGroupId()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_UUID_G, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_UUID_G, args,
					dropboxEntryModelImpl);
			}

			if ((dropboxEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_R_P.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxEntryModelImpl.getRepositoryId(),
						dropboxEntryModelImpl.getPath()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_R_P, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_P, args,
					dropboxEntryModelImpl);
			}
		}
	}

	protected void clearUniqueFindersCache(
		DropboxEntryModelImpl dropboxEntryModelImpl) {
		Object[] args = new Object[] {
				dropboxEntryModelImpl.getUuid(),
				dropboxEntryModelImpl.getGroupId()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_G, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G, args);

		if ((dropboxEntryModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_UUID_G.getColumnBitmask()) != 0) {
			args = new Object[] {
					dropboxEntryModelImpl.getOriginalUuid(),
					dropboxEntryModelImpl.getOriginalGroupId()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_G, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_UUID_G, args);
		}

		args = new Object[] {
				dropboxEntryModelImpl.getRepositoryId(),
				dropboxEntryModelImpl.getPath()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_P, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_R_P, args);

		if ((dropboxEntryModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_R_P.getColumnBitmask()) != 0) {
			args = new Object[] {
					dropboxEntryModelImpl.getOriginalRepositoryId(),
					dropboxEntryModelImpl.getOriginalPath()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_P, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_R_P, args);
		}
	}

	/**
	 * Creates a new dropbox entry with the primary key. Does not add the dropbox entry to the database.
	 *
	 * @param entryId the primary key for the new dropbox entry
	 * @return the new dropbox entry
	 */
	@Override
	public DropboxEntry create(long entryId) {
		DropboxEntry dropboxEntry = new DropboxEntryImpl();

		dropboxEntry.setNew(true);
		dropboxEntry.setPrimaryKey(entryId);

		String uuid = PortalUUIDUtil.generate();

		dropboxEntry.setUuid(uuid);

		return dropboxEntry;
	}

	/**
	 * Removes the dropbox entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param entryId the primary key of the dropbox entry
	 * @return the dropbox entry that was removed
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	 */
	@Override
	public DropboxEntry remove(long entryId) throws NoSuchEntryException {
		return remove((Serializable)entryId);
	}

	/**
	 * Removes the dropbox entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the dropbox entry
	 * @return the dropbox entry that was removed
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	 */
	@Override
	public DropboxEntry remove(Serializable primaryKey)
		throws NoSuchEntryException {
		Session session = null;

		try {
			session = openSession();

			DropboxEntry dropboxEntry = (DropboxEntry)session.get(DropboxEntryImpl.class,
					primaryKey);

			if (dropboxEntry == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(dropboxEntry);
		}
		catch (NoSuchEntryException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected DropboxEntry removeImpl(DropboxEntry dropboxEntry) {
		dropboxEntry = toUnwrappedModel(dropboxEntry);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(dropboxEntry)) {
				dropboxEntry = (DropboxEntry)session.get(DropboxEntryImpl.class,
						dropboxEntry.getPrimaryKeyObj());
			}

			if (dropboxEntry != null) {
				session.delete(dropboxEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (dropboxEntry != null) {
			clearCache(dropboxEntry);
		}

		return dropboxEntry;
	}

	@Override
	public DropboxEntry updateImpl(DropboxEntry dropboxEntry) {
		dropboxEntry = toUnwrappedModel(dropboxEntry);

		boolean isNew = dropboxEntry.isNew();

		DropboxEntryModelImpl dropboxEntryModelImpl = (DropboxEntryModelImpl)dropboxEntry;

		if (Validator.isNull(dropboxEntry.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			dropboxEntry.setUuid(uuid);
		}

		ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (dropboxEntry.getCreateDate() == null)) {
			if (serviceContext == null) {
				dropboxEntry.setCreateDate(now);
			}
			else {
				dropboxEntry.setCreateDate(serviceContext.getCreateDate(now));
			}
		}

		if (!dropboxEntryModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				dropboxEntry.setModifiedDate(now);
			}
			else {
				dropboxEntry.setModifiedDate(serviceContext.getModifiedDate(now));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (dropboxEntry.isNew()) {
				session.save(dropboxEntry);

				dropboxEntry.setNew(false);
			}
			else {
				dropboxEntry = (DropboxEntry)session.merge(dropboxEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !DropboxEntryModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((dropboxEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxEntryModelImpl.getOriginalUuid()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);

				args = new Object[] { dropboxEntryModelImpl.getUuid() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);
			}

			if ((dropboxEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxEntryModelImpl.getOriginalUuid(),
						dropboxEntryModelImpl.getOriginalCompanyId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C,
					args);

				args = new Object[] {
						dropboxEntryModelImpl.getUuid(),
						dropboxEntryModelImpl.getCompanyId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID_C, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID_C,
					args);
			}

			if ((dropboxEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxEntryModelImpl.getOriginalRepositoryId(),
						dropboxEntryModelImpl.getOriginalParentPath()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_PP, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP,
					args);

				args = new Object[] {
						dropboxEntryModelImpl.getRepositoryId(),
						dropboxEntryModelImpl.getParentPath()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_PP, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP,
					args);
			}

			if ((dropboxEntryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP_T.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxEntryModelImpl.getOriginalRepositoryId(),
						dropboxEntryModelImpl.getOriginalParentPath(),
						dropboxEntryModelImpl.getOriginalType()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_PP_T, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP_T,
					args);

				args = new Object[] {
						dropboxEntryModelImpl.getRepositoryId(),
						dropboxEntryModelImpl.getParentPath(),
						dropboxEntryModelImpl.getType()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_PP_T, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_PP_T,
					args);
			}
		}

		EntityCacheUtil.putResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
			DropboxEntryImpl.class, dropboxEntry.getPrimaryKey(), dropboxEntry,
			false);

		clearUniqueFindersCache(dropboxEntryModelImpl);
		cacheUniqueFindersCache(dropboxEntryModelImpl, isNew);

		dropboxEntry.resetOriginalValues();

		return dropboxEntry;
	}

	protected DropboxEntry toUnwrappedModel(DropboxEntry dropboxEntry) {
		if (dropboxEntry instanceof DropboxEntryImpl) {
			return dropboxEntry;
		}

		DropboxEntryImpl dropboxEntryImpl = new DropboxEntryImpl();

		dropboxEntryImpl.setNew(dropboxEntry.isNew());
		dropboxEntryImpl.setPrimaryKey(dropboxEntry.getPrimaryKey());

		dropboxEntryImpl.setUuid(dropboxEntry.getUuid());
		dropboxEntryImpl.setEntryId(dropboxEntry.getEntryId());
		dropboxEntryImpl.setGroupId(dropboxEntry.getGroupId());
		dropboxEntryImpl.setCompanyId(dropboxEntry.getCompanyId());
		dropboxEntryImpl.setRepositoryId(dropboxEntry.getRepositoryId());
		dropboxEntryImpl.setUserId(dropboxEntry.getUserId());
		dropboxEntryImpl.setUserName(dropboxEntry.getUserName());
		dropboxEntryImpl.setCreateDate(dropboxEntry.getCreateDate());
		dropboxEntryImpl.setModifiedDate(dropboxEntry.getModifiedDate());
		dropboxEntryImpl.setChangeLog(dropboxEntry.getChangeLog());
		dropboxEntryImpl.setDescription(dropboxEntry.getDescription());
		dropboxEntryImpl.setName(dropboxEntry.getName());
		dropboxEntryImpl.setPath(dropboxEntry.getPath());
		dropboxEntryImpl.setParentPath(dropboxEntry.getParentPath());
		dropboxEntryImpl.setRev(dropboxEntry.getRev());
		dropboxEntryImpl.setSize(dropboxEntry.getSize());
		dropboxEntryImpl.setType(dropboxEntry.getType());

		return dropboxEntryImpl;
	}

	/**
	 * Returns the dropbox entry with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the dropbox entry
	 * @return the dropbox entry
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	 */
	@Override
	public DropboxEntry findByPrimaryKey(Serializable primaryKey)
		throws NoSuchEntryException {
		DropboxEntry dropboxEntry = fetchByPrimaryKey(primaryKey);

		if (dropboxEntry == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchEntryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return dropboxEntry;
	}

	/**
	 * Returns the dropbox entry with the primary key or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchEntryException} if it could not be found.
	 *
	 * @param entryId the primary key of the dropbox entry
	 * @return the dropbox entry
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchEntryException if a dropbox entry with the primary key could not be found
	 */
	@Override
	public DropboxEntry findByPrimaryKey(long entryId)
		throws NoSuchEntryException {
		return findByPrimaryKey((Serializable)entryId);
	}

	/**
	 * Returns the dropbox entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the dropbox entry
	 * @return the dropbox entry, or <code>null</code> if a dropbox entry with the primary key could not be found
	 */
	@Override
	public DropboxEntry fetchByPrimaryKey(Serializable primaryKey) {
		DropboxEntry dropboxEntry = (DropboxEntry)EntityCacheUtil.getResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
				DropboxEntryImpl.class, primaryKey);

		if (dropboxEntry == _nullDropboxEntry) {
			return null;
		}

		if (dropboxEntry == null) {
			Session session = null;

			try {
				session = openSession();

				dropboxEntry = (DropboxEntry)session.get(DropboxEntryImpl.class,
						primaryKey);

				if (dropboxEntry != null) {
					cacheResult(dropboxEntry);
				}
				else {
					EntityCacheUtil.putResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
						DropboxEntryImpl.class, primaryKey, _nullDropboxEntry);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
					DropboxEntryImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return dropboxEntry;
	}

	/**
	 * Returns the dropbox entry with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param entryId the primary key of the dropbox entry
	 * @return the dropbox entry, or <code>null</code> if a dropbox entry with the primary key could not be found
	 */
	@Override
	public DropboxEntry fetchByPrimaryKey(long entryId) {
		return fetchByPrimaryKey((Serializable)entryId);
	}

	@Override
	public Map<Serializable, DropboxEntry> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, DropboxEntry> map = new HashMap<Serializable, DropboxEntry>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			DropboxEntry dropboxEntry = fetchByPrimaryKey(primaryKey);

			if (dropboxEntry != null) {
				map.put(primaryKey, dropboxEntry);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			DropboxEntry dropboxEntry = (DropboxEntry)EntityCacheUtil.getResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
					DropboxEntryImpl.class, primaryKey);

			if (dropboxEntry == null) {
				if (uncachedPrimaryKeys == null) {
					uncachedPrimaryKeys = new HashSet<Serializable>();
				}

				uncachedPrimaryKeys.add(primaryKey);
			}
			else {
				map.put(primaryKey, dropboxEntry);
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_DROPBOXENTRY_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append(String.valueOf(primaryKey));

			query.append(StringPool.COMMA);
		}

		query.setIndex(query.index() - 1);

		query.append(StringPool.CLOSE_PARENTHESIS);

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (DropboxEntry dropboxEntry : (List<DropboxEntry>)q.list()) {
				map.put(dropboxEntry.getPrimaryKeyObj(), dropboxEntry);

				cacheResult(dropboxEntry);

				uncachedPrimaryKeys.remove(dropboxEntry.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				EntityCacheUtil.putResult(DropboxEntryModelImpl.ENTITY_CACHE_ENABLED,
					DropboxEntryImpl.class, primaryKey, _nullDropboxEntry);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the dropbox entries.
	 *
	 * @return the dropbox entries
	 */
	@Override
	public List<DropboxEntry> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<DropboxEntry> findAll(int start, int end) {
		return findAll(start, end, null);
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
	@Override
	public List<DropboxEntry> findAll(int start, int end,
		OrderByComparator<DropboxEntry> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<DropboxEntry> list = (List<DropboxEntry>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_DROPBOXENTRY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_DROPBOXENTRY;

				if (pagination) {
					sql = sql.concat(DropboxEntryModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DropboxEntry>)QueryUtil.list(q, getDialect(),
							start, end);
				}

				cacheResult(list);

				FinderCacheUtil.putResult(finderPath, finderArgs, list);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(finderPath, finderArgs);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the dropbox entries from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (DropboxEntry dropboxEntry : findAll()) {
			remove(dropboxEntry);
		}
	}

	/**
	 * Returns the number of dropbox entries.
	 *
	 * @return the number of dropbox entries
	 */
	@Override
	public int countAll() {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_DROPBOXENTRY);

				count = (Long)q.uniqueResult();

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return DropboxEntryModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the dropbox entry persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		EntityCacheUtil.removeCache(DropboxEntryImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_DROPBOXENTRY = "SELECT dropboxEntry FROM DropboxEntry dropboxEntry";
	private static final String _SQL_SELECT_DROPBOXENTRY_WHERE_PKS_IN = "SELECT dropboxEntry FROM DropboxEntry dropboxEntry WHERE entryId IN (";
	private static final String _SQL_SELECT_DROPBOXENTRY_WHERE = "SELECT dropboxEntry FROM DropboxEntry dropboxEntry WHERE ";
	private static final String _SQL_COUNT_DROPBOXENTRY = "SELECT COUNT(dropboxEntry) FROM DropboxEntry dropboxEntry";
	private static final String _SQL_COUNT_DROPBOXENTRY_WHERE = "SELECT COUNT(dropboxEntry) FROM DropboxEntry dropboxEntry WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "dropboxEntry.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No DropboxEntry exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No DropboxEntry exists with the key {";
	private static final Log _log = LogFactoryUtil.getLog(DropboxEntryPersistenceImpl.class);
	private static final Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"uuid", "path", "size", "type"
			});
	private static final DropboxEntry _nullDropboxEntry = new DropboxEntryImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<DropboxEntry> toCacheModel() {
				return _nullDropboxEntryCacheModel;
			}
		};

	private static final CacheModel<DropboxEntry> _nullDropboxEntryCacheModel = new CacheModel<DropboxEntry>() {
			@Override
			public DropboxEntry toEntityModel() {
				return _nullDropboxEntry;
			}
		};
}