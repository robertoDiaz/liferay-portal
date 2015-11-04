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

import com.liferay.document.library.repository.dropbox.exception.NoSuchRevisionException;
import com.liferay.document.library.repository.dropbox.model.DropboxRevision;
import com.liferay.document.library.repository.dropbox.model.impl.DropboxRevisionImpl;
import com.liferay.document.library.repository.dropbox.model.impl.DropboxRevisionModelImpl;
import com.liferay.document.library.repository.dropbox.service.persistence.DropboxRevisionPersistence;

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
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import java.io.Serializable;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the dropbox revision service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DropboxRevisionPersistence
 * @see com.liferay.document.library.repository.dropbox.service.persistence.DropboxRevisionUtil
 * @generated
 */
@ProviderType
public class DropboxRevisionPersistenceImpl extends BasePersistenceImpl<DropboxRevision>
	implements DropboxRevisionPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link DropboxRevisionUtil} to access the dropbox revision persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = DropboxRevisionImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED,
			DropboxRevisionImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED,
			DropboxRevisionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_UUID = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED,
			DropboxRevisionImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByUuid",
			new String[] {
				String.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED,
			DropboxRevisionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] { String.class.getName() },
			DropboxRevisionModelImpl.UUID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_UUID = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] { String.class.getName() });

	/**
	 * Returns all the dropbox revisions where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching dropbox revisions
	 */
	@Override
	public List<DropboxRevision> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<DropboxRevision> findByUuid(String uuid, int start, int end) {
		return findByUuid(uuid, start, end, null);
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
	@Override
	public List<DropboxRevision> findByUuid(String uuid, int start, int end,
		OrderByComparator<DropboxRevision> orderByComparator) {
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

		List<DropboxRevision> list = (List<DropboxRevision>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DropboxRevision dropboxRevision : list) {
				if (!Validator.equals(uuid, dropboxRevision.getUuid())) {
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

			query.append(_SQL_SELECT_DROPBOXREVISION_WHERE);

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
				query.append(DropboxRevisionModelImpl.ORDER_BY_JPQL);
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
					list = (List<DropboxRevision>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DropboxRevision>)QueryUtil.list(q,
							getDialect(), start, end);
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
	 * Returns the first dropbox revision in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dropbox revision
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision findByUuid_First(String uuid,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = fetchByUuid_First(uuid,
				orderByComparator);

		if (dropboxRevision != null) {
			return dropboxRevision;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchRevisionException(msg.toString());
	}

	/**
	 * Returns the first dropbox revision in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision fetchByUuid_First(String uuid,
		OrderByComparator<DropboxRevision> orderByComparator) {
		List<DropboxRevision> list = findByUuid(uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last dropbox revision in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dropbox revision
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision findByUuid_Last(String uuid,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = fetchByUuid_Last(uuid,
				orderByComparator);

		if (dropboxRevision != null) {
			return dropboxRevision;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("uuid=");
		msg.append(uuid);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchRevisionException(msg.toString());
	}

	/**
	 * Returns the last dropbox revision in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision fetchByUuid_Last(String uuid,
		OrderByComparator<DropboxRevision> orderByComparator) {
		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<DropboxRevision> list = findByUuid(uuid, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxRevision[] findByUuid_PrevAndNext(long revisionId,
		String uuid, OrderByComparator<DropboxRevision> orderByComparator)
		throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = findByPrimaryKey(revisionId);

		Session session = null;

		try {
			session = openSession();

			DropboxRevision[] array = new DropboxRevisionImpl[3];

			array[0] = getByUuid_PrevAndNext(session, dropboxRevision, uuid,
					orderByComparator, true);

			array[1] = dropboxRevision;

			array[2] = getByUuid_PrevAndNext(session, dropboxRevision, uuid,
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

	protected DropboxRevision getByUuid_PrevAndNext(Session session,
		DropboxRevision dropboxRevision, String uuid,
		OrderByComparator<DropboxRevision> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DROPBOXREVISION_WHERE);

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
			query.append(DropboxRevisionModelImpl.ORDER_BY_JPQL);
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
			Object[] values = orderByComparator.getOrderByConditionValues(dropboxRevision);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DropboxRevision> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dropbox revisions where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (DropboxRevision dropboxRevision : findByUuid(uuid,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dropboxRevision);
		}
	}

	/**
	 * Returns the number of dropbox revisions where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching dropbox revisions
	 */
	@Override
	public int countByUuid(String uuid) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_UUID;

		Object[] finderArgs = new Object[] { uuid };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_DROPBOXREVISION_WHERE);

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

	private static final String _FINDER_COLUMN_UUID_UUID_1 = "dropboxRevision.uuid IS NULL";
	private static final String _FINDER_COLUMN_UUID_UUID_2 = "dropboxRevision.uuid = ?";
	private static final String _FINDER_COLUMN_UUID_UUID_3 = "(dropboxRevision.uuid IS NULL OR dropboxRevision.uuid = '')";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_R_E = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED,
			DropboxRevisionImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByR_E",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_E = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED,
			DropboxRevisionImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByR_E",
			new String[] { Long.class.getName(), Long.class.getName() },
			DropboxRevisionModelImpl.REPOSITORYID_COLUMN_BITMASK |
			DropboxRevisionModelImpl.ENTRYID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_R_E = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_E",
			new String[] { Long.class.getName(), Long.class.getName() });

	/**
	 * Returns all the dropbox revisions where repositoryId = &#63; and entryId = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @return the matching dropbox revisions
	 */
	@Override
	public List<DropboxRevision> findByR_E(long repositoryId, long entryId) {
		return findByR_E(repositoryId, entryId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
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
	@Override
	public List<DropboxRevision> findByR_E(long repositoryId, long entryId,
		int start, int end) {
		return findByR_E(repositoryId, entryId, start, end, null);
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
	@Override
	public List<DropboxRevision> findByR_E(long repositoryId, long entryId,
		int start, int end, OrderByComparator<DropboxRevision> orderByComparator) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_E;
			finderArgs = new Object[] { repositoryId, entryId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_R_E;
			finderArgs = new Object[] {
					repositoryId, entryId,
					
					start, end, orderByComparator
				};
		}

		List<DropboxRevision> list = (List<DropboxRevision>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (DropboxRevision dropboxRevision : list) {
				if ((repositoryId != dropboxRevision.getRepositoryId()) ||
						(entryId != dropboxRevision.getEntryId())) {
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

			query.append(_SQL_SELECT_DROPBOXREVISION_WHERE);

			query.append(_FINDER_COLUMN_R_E_REPOSITORYID_2);

			query.append(_FINDER_COLUMN_R_E_ENTRYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DropboxRevisionModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				qPos.add(entryId);

				if (!pagination) {
					list = (List<DropboxRevision>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DropboxRevision>)QueryUtil.list(q,
							getDialect(), start, end);
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
	 * Returns the first dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dropbox revision
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision findByR_E_First(long repositoryId, long entryId,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = fetchByR_E_First(repositoryId,
				entryId, orderByComparator);

		if (dropboxRevision != null) {
			return dropboxRevision;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("repositoryId=");
		msg.append(repositoryId);

		msg.append(", entryId=");
		msg.append(entryId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchRevisionException(msg.toString());
	}

	/**
	 * Returns the first dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision fetchByR_E_First(long repositoryId, long entryId,
		OrderByComparator<DropboxRevision> orderByComparator) {
		List<DropboxRevision> list = findByR_E(repositoryId, entryId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxRevision findByR_E_Last(long repositoryId, long entryId,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = fetchByR_E_Last(repositoryId,
				entryId, orderByComparator);

		if (dropboxRevision != null) {
			return dropboxRevision;
		}

		StringBundler msg = new StringBundler(6);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("repositoryId=");
		msg.append(repositoryId);

		msg.append(", entryId=");
		msg.append(entryId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchRevisionException(msg.toString());
	}

	/**
	 * Returns the last dropbox revision in the ordered set where repositoryId = &#63; and entryId = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision fetchByR_E_Last(long repositoryId, long entryId,
		OrderByComparator<DropboxRevision> orderByComparator) {
		int count = countByR_E(repositoryId, entryId);

		if (count == 0) {
			return null;
		}

		List<DropboxRevision> list = findByR_E(repositoryId, entryId,
				count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DropboxRevision[] findByR_E_PrevAndNext(long revisionId,
		long repositoryId, long entryId,
		OrderByComparator<DropboxRevision> orderByComparator)
		throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = findByPrimaryKey(revisionId);

		Session session = null;

		try {
			session = openSession();

			DropboxRevision[] array = new DropboxRevisionImpl[3];

			array[0] = getByR_E_PrevAndNext(session, dropboxRevision,
					repositoryId, entryId, orderByComparator, true);

			array[1] = dropboxRevision;

			array[2] = getByR_E_PrevAndNext(session, dropboxRevision,
					repositoryId, entryId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DropboxRevision getByR_E_PrevAndNext(Session session,
		DropboxRevision dropboxRevision, long repositoryId, long entryId,
		OrderByComparator<DropboxRevision> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DROPBOXREVISION_WHERE);

		query.append(_FINDER_COLUMN_R_E_REPOSITORYID_2);

		query.append(_FINDER_COLUMN_R_E_ENTRYID_2);

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
			query.append(DropboxRevisionModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(repositoryId);

		qPos.add(entryId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dropboxRevision);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DropboxRevision> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the dropbox revisions where repositoryId = &#63; and entryId = &#63; from the database.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 */
	@Override
	public void removeByR_E(long repositoryId, long entryId) {
		for (DropboxRevision dropboxRevision : findByR_E(repositoryId, entryId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dropboxRevision);
		}
	}

	/**
	 * Returns the number of dropbox revisions where repositoryId = &#63; and entryId = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @return the number of matching dropbox revisions
	 */
	@Override
	public int countByR_E(long repositoryId, long entryId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_R_E;

		Object[] finderArgs = new Object[] { repositoryId, entryId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(3);

			query.append(_SQL_COUNT_DROPBOXREVISION_WHERE);

			query.append(_FINDER_COLUMN_R_E_REPOSITORYID_2);

			query.append(_FINDER_COLUMN_R_E_ENTRYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				qPos.add(entryId);

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

	private static final String _FINDER_COLUMN_R_E_REPOSITORYID_2 = "dropboxRevision.repositoryId = ? AND ";
	private static final String _FINDER_COLUMN_R_E_ENTRYID_2 = "dropboxRevision.entryId = ?";
	public static final FinderPath FINDER_PATH_FETCH_BY_R_E_R = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED,
			DropboxRevisionImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByR_E_R",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			},
			DropboxRevisionModelImpl.REPOSITORYID_COLUMN_BITMASK |
			DropboxRevisionModelImpl.ENTRYID_COLUMN_BITMASK |
			DropboxRevisionModelImpl.REV_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_R_E_R = new FinderPath(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByR_E_R",
			new String[] {
				Long.class.getName(), Long.class.getName(),
				String.class.getName()
			});

	/**
	 * Returns the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchRevisionException} if it could not be found.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @param rev the rev
	 * @return the matching dropbox revision
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision findByR_E_R(long repositoryId, long entryId,
		String rev) throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = fetchByR_E_R(repositoryId, entryId,
				rev);

		if (dropboxRevision == null) {
			StringBundler msg = new StringBundler(8);

			msg.append(_NO_SUCH_ENTITY_WITH_KEY);

			msg.append("repositoryId=");
			msg.append(repositoryId);

			msg.append(", entryId=");
			msg.append(entryId);

			msg.append(", rev=");
			msg.append(rev);

			msg.append(StringPool.CLOSE_CURLY_BRACE);

			if (_log.isWarnEnabled()) {
				_log.warn(msg.toString());
			}

			throw new NoSuchRevisionException(msg.toString());
		}

		return dropboxRevision;
	}

	/**
	 * Returns the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @param rev the rev
	 * @return the matching dropbox revision, or <code>null</code> if a matching dropbox revision could not be found
	 */
	@Override
	public DropboxRevision fetchByR_E_R(long repositoryId, long entryId,
		String rev) {
		return fetchByR_E_R(repositoryId, entryId, rev, true);
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
	@Override
	public DropboxRevision fetchByR_E_R(long repositoryId, long entryId,
		String rev, boolean retrieveFromCache) {
		Object[] finderArgs = new Object[] { repositoryId, entryId, rev };

		Object result = null;

		if (retrieveFromCache) {
			result = FinderCacheUtil.getResult(FINDER_PATH_FETCH_BY_R_E_R,
					finderArgs, this);
		}

		if (result instanceof DropboxRevision) {
			DropboxRevision dropboxRevision = (DropboxRevision)result;

			if ((repositoryId != dropboxRevision.getRepositoryId()) ||
					(entryId != dropboxRevision.getEntryId()) ||
					!Validator.equals(rev, dropboxRevision.getRev())) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler query = new StringBundler(5);

			query.append(_SQL_SELECT_DROPBOXREVISION_WHERE);

			query.append(_FINDER_COLUMN_R_E_R_REPOSITORYID_2);

			query.append(_FINDER_COLUMN_R_E_R_ENTRYID_2);

			boolean bindRev = false;

			if (rev == null) {
				query.append(_FINDER_COLUMN_R_E_R_REV_1);
			}
			else if (rev.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_R_E_R_REV_3);
			}
			else {
				bindRev = true;

				query.append(_FINDER_COLUMN_R_E_R_REV_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				qPos.add(entryId);

				if (bindRev) {
					qPos.add(rev);
				}

				List<DropboxRevision> list = q.list();

				if (list.isEmpty()) {
					FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_E_R,
						finderArgs, list);
				}
				else {
					if ((list.size() > 1) && _log.isWarnEnabled()) {
						_log.warn(
							"DropboxRevisionPersistenceImpl.fetchByR_E_R(long, long, String, boolean) with parameters (" +
							StringUtil.merge(finderArgs) +
							") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
					}

					DropboxRevision dropboxRevision = list.get(0);

					result = dropboxRevision;

					cacheResult(dropboxRevision);

					if ((dropboxRevision.getRepositoryId() != repositoryId) ||
							(dropboxRevision.getEntryId() != entryId) ||
							(dropboxRevision.getRev() == null) ||
							!dropboxRevision.getRev().equals(rev)) {
						FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_E_R,
							finderArgs, dropboxRevision);
					}
				}
			}
			catch (Exception e) {
				FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_R_E_R,
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
			return (DropboxRevision)result;
		}
	}

	/**
	 * Removes the dropbox revision where repositoryId = &#63; and entryId = &#63; and rev = &#63; from the database.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @param rev the rev
	 * @return the dropbox revision that was removed
	 */
	@Override
	public DropboxRevision removeByR_E_R(long repositoryId, long entryId,
		String rev) throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = findByR_E_R(repositoryId, entryId, rev);

		return remove(dropboxRevision);
	}

	/**
	 * Returns the number of dropbox revisions where repositoryId = &#63; and entryId = &#63; and rev = &#63;.
	 *
	 * @param repositoryId the repository ID
	 * @param entryId the entry ID
	 * @param rev the rev
	 * @return the number of matching dropbox revisions
	 */
	@Override
	public int countByR_E_R(long repositoryId, long entryId, String rev) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_R_E_R;

		Object[] finderArgs = new Object[] { repositoryId, entryId, rev };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(4);

			query.append(_SQL_COUNT_DROPBOXREVISION_WHERE);

			query.append(_FINDER_COLUMN_R_E_R_REPOSITORYID_2);

			query.append(_FINDER_COLUMN_R_E_R_ENTRYID_2);

			boolean bindRev = false;

			if (rev == null) {
				query.append(_FINDER_COLUMN_R_E_R_REV_1);
			}
			else if (rev.equals(StringPool.BLANK)) {
				query.append(_FINDER_COLUMN_R_E_R_REV_3);
			}
			else {
				bindRev = true;

				query.append(_FINDER_COLUMN_R_E_R_REV_2);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(repositoryId);

				qPos.add(entryId);

				if (bindRev) {
					qPos.add(rev);
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

	private static final String _FINDER_COLUMN_R_E_R_REPOSITORYID_2 = "dropboxRevision.repositoryId = ? AND ";
	private static final String _FINDER_COLUMN_R_E_R_ENTRYID_2 = "dropboxRevision.entryId = ? AND ";
	private static final String _FINDER_COLUMN_R_E_R_REV_1 = "dropboxRevision.rev IS NULL";
	private static final String _FINDER_COLUMN_R_E_R_REV_2 = "dropboxRevision.rev = ?";
	private static final String _FINDER_COLUMN_R_E_R_REV_3 = "(dropboxRevision.rev IS NULL OR dropboxRevision.rev = '')";

	public DropboxRevisionPersistenceImpl() {
		setModelClass(DropboxRevision.class);
	}

	/**
	 * Caches the dropbox revision in the entity cache if it is enabled.
	 *
	 * @param dropboxRevision the dropbox revision
	 */
	@Override
	public void cacheResult(DropboxRevision dropboxRevision) {
		EntityCacheUtil.putResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionImpl.class, dropboxRevision.getPrimaryKey(),
			dropboxRevision);

		FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_E_R,
			new Object[] {
				dropboxRevision.getRepositoryId(), dropboxRevision.getEntryId(),
				dropboxRevision.getRev()
			}, dropboxRevision);

		dropboxRevision.resetOriginalValues();
	}

	/**
	 * Caches the dropbox revisions in the entity cache if it is enabled.
	 *
	 * @param dropboxRevisions the dropbox revisions
	 */
	@Override
	public void cacheResult(List<DropboxRevision> dropboxRevisions) {
		for (DropboxRevision dropboxRevision : dropboxRevisions) {
			if (EntityCacheUtil.getResult(
						DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
						DropboxRevisionImpl.class,
						dropboxRevision.getPrimaryKey()) == null) {
				cacheResult(dropboxRevision);
			}
			else {
				dropboxRevision.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all dropbox revisions.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		EntityCacheUtil.clearCache(DropboxRevisionImpl.class);

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the dropbox revision.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DropboxRevision dropboxRevision) {
		EntityCacheUtil.removeResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionImpl.class, dropboxRevision.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache((DropboxRevisionModelImpl)dropboxRevision);
	}

	@Override
	public void clearCache(List<DropboxRevision> dropboxRevisions) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (DropboxRevision dropboxRevision : dropboxRevisions) {
			EntityCacheUtil.removeResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
				DropboxRevisionImpl.class, dropboxRevision.getPrimaryKey());

			clearUniqueFindersCache((DropboxRevisionModelImpl)dropboxRevision);
		}
	}

	protected void cacheUniqueFindersCache(
		DropboxRevisionModelImpl dropboxRevisionModelImpl, boolean isNew) {
		if (isNew) {
			Object[] args = new Object[] {
					dropboxRevisionModelImpl.getRepositoryId(),
					dropboxRevisionModelImpl.getEntryId(),
					dropboxRevisionModelImpl.getRev()
				};

			FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_R_E_R, args,
				Long.valueOf(1));
			FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_E_R, args,
				dropboxRevisionModelImpl);
		}
		else {
			if ((dropboxRevisionModelImpl.getColumnBitmask() &
					FINDER_PATH_FETCH_BY_R_E_R.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxRevisionModelImpl.getRepositoryId(),
						dropboxRevisionModelImpl.getEntryId(),
						dropboxRevisionModelImpl.getRev()
					};

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_R_E_R, args,
					Long.valueOf(1));
				FinderCacheUtil.putResult(FINDER_PATH_FETCH_BY_R_E_R, args,
					dropboxRevisionModelImpl);
			}
		}
	}

	protected void clearUniqueFindersCache(
		DropboxRevisionModelImpl dropboxRevisionModelImpl) {
		Object[] args = new Object[] {
				dropboxRevisionModelImpl.getRepositoryId(),
				dropboxRevisionModelImpl.getEntryId(),
				dropboxRevisionModelImpl.getRev()
			};

		FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_E_R, args);
		FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_R_E_R, args);

		if ((dropboxRevisionModelImpl.getColumnBitmask() &
				FINDER_PATH_FETCH_BY_R_E_R.getColumnBitmask()) != 0) {
			args = new Object[] {
					dropboxRevisionModelImpl.getOriginalRepositoryId(),
					dropboxRevisionModelImpl.getOriginalEntryId(),
					dropboxRevisionModelImpl.getOriginalRev()
				};

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_E_R, args);
			FinderCacheUtil.removeResult(FINDER_PATH_FETCH_BY_R_E_R, args);
		}
	}

	/**
	 * Creates a new dropbox revision with the primary key. Does not add the dropbox revision to the database.
	 *
	 * @param revisionId the primary key for the new dropbox revision
	 * @return the new dropbox revision
	 */
	@Override
	public DropboxRevision create(long revisionId) {
		DropboxRevision dropboxRevision = new DropboxRevisionImpl();

		dropboxRevision.setNew(true);
		dropboxRevision.setPrimaryKey(revisionId);

		String uuid = PortalUUIDUtil.generate();

		dropboxRevision.setUuid(uuid);

		return dropboxRevision;
	}

	/**
	 * Removes the dropbox revision with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param revisionId the primary key of the dropbox revision
	 * @return the dropbox revision that was removed
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	 */
	@Override
	public DropboxRevision remove(long revisionId)
		throws NoSuchRevisionException {
		return remove((Serializable)revisionId);
	}

	/**
	 * Removes the dropbox revision with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the dropbox revision
	 * @return the dropbox revision that was removed
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	 */
	@Override
	public DropboxRevision remove(Serializable primaryKey)
		throws NoSuchRevisionException {
		Session session = null;

		try {
			session = openSession();

			DropboxRevision dropboxRevision = (DropboxRevision)session.get(DropboxRevisionImpl.class,
					primaryKey);

			if (dropboxRevision == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchRevisionException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(dropboxRevision);
		}
		catch (NoSuchRevisionException nsee) {
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
	protected DropboxRevision removeImpl(DropboxRevision dropboxRevision) {
		dropboxRevision = toUnwrappedModel(dropboxRevision);

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(dropboxRevision)) {
				dropboxRevision = (DropboxRevision)session.get(DropboxRevisionImpl.class,
						dropboxRevision.getPrimaryKeyObj());
			}

			if (dropboxRevision != null) {
				session.delete(dropboxRevision);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (dropboxRevision != null) {
			clearCache(dropboxRevision);
		}

		return dropboxRevision;
	}

	@Override
	public DropboxRevision updateImpl(DropboxRevision dropboxRevision) {
		dropboxRevision = toUnwrappedModel(dropboxRevision);

		boolean isNew = dropboxRevision.isNew();

		DropboxRevisionModelImpl dropboxRevisionModelImpl = (DropboxRevisionModelImpl)dropboxRevision;

		if (Validator.isNull(dropboxRevision.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			dropboxRevision.setUuid(uuid);
		}

		Session session = null;

		try {
			session = openSession();

			if (dropboxRevision.isNew()) {
				session.save(dropboxRevision);

				dropboxRevision.setNew(false);
			}
			else {
				dropboxRevision = (DropboxRevision)session.merge(dropboxRevision);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !DropboxRevisionModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((dropboxRevisionModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxRevisionModelImpl.getOriginalUuid()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);

				args = new Object[] { dropboxRevisionModelImpl.getUuid() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_UUID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_UUID,
					args);
			}

			if ((dropboxRevisionModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_E.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dropboxRevisionModelImpl.getOriginalRepositoryId(),
						dropboxRevisionModelImpl.getOriginalEntryId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_E, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_E,
					args);

				args = new Object[] {
						dropboxRevisionModelImpl.getRepositoryId(),
						dropboxRevisionModelImpl.getEntryId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_R_E, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_R_E,
					args);
			}
		}

		EntityCacheUtil.putResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
			DropboxRevisionImpl.class, dropboxRevision.getPrimaryKey(),
			dropboxRevision, false);

		clearUniqueFindersCache(dropboxRevisionModelImpl);
		cacheUniqueFindersCache(dropboxRevisionModelImpl, isNew);

		dropboxRevision.resetOriginalValues();

		return dropboxRevision;
	}

	protected DropboxRevision toUnwrappedModel(DropboxRevision dropboxRevision) {
		if (dropboxRevision instanceof DropboxRevisionImpl) {
			return dropboxRevision;
		}

		DropboxRevisionImpl dropboxRevisionImpl = new DropboxRevisionImpl();

		dropboxRevisionImpl.setNew(dropboxRevision.isNew());
		dropboxRevisionImpl.setPrimaryKey(dropboxRevision.getPrimaryKey());

		dropboxRevisionImpl.setUuid(dropboxRevision.getUuid());
		dropboxRevisionImpl.setRevisionId(dropboxRevision.getRevisionId());
		dropboxRevisionImpl.setCreateDate(dropboxRevision.getCreateDate());
		dropboxRevisionImpl.setEntryId(dropboxRevision.getEntryId());
		dropboxRevisionImpl.setPath(dropboxRevision.getPath());
		dropboxRevisionImpl.setRepositoryId(dropboxRevision.getRepositoryId());
		dropboxRevisionImpl.setRev(dropboxRevision.getRev());
		dropboxRevisionImpl.setSize(dropboxRevision.getSize());

		return dropboxRevisionImpl;
	}

	/**
	 * Returns the dropbox revision with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the dropbox revision
	 * @return the dropbox revision
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	 */
	@Override
	public DropboxRevision findByPrimaryKey(Serializable primaryKey)
		throws NoSuchRevisionException {
		DropboxRevision dropboxRevision = fetchByPrimaryKey(primaryKey);

		if (dropboxRevision == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchRevisionException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return dropboxRevision;
	}

	/**
	 * Returns the dropbox revision with the primary key or throws a {@link com.liferay.document.library.repository.dropbox.NoSuchRevisionException} if it could not be found.
	 *
	 * @param revisionId the primary key of the dropbox revision
	 * @return the dropbox revision
	 * @throws com.liferay.document.library.repository.dropbox.NoSuchRevisionException if a dropbox revision with the primary key could not be found
	 */
	@Override
	public DropboxRevision findByPrimaryKey(long revisionId)
		throws NoSuchRevisionException {
		return findByPrimaryKey((Serializable)revisionId);
	}

	/**
	 * Returns the dropbox revision with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the dropbox revision
	 * @return the dropbox revision, or <code>null</code> if a dropbox revision with the primary key could not be found
	 */
	@Override
	public DropboxRevision fetchByPrimaryKey(Serializable primaryKey) {
		DropboxRevision dropboxRevision = (DropboxRevision)EntityCacheUtil.getResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
				DropboxRevisionImpl.class, primaryKey);

		if (dropboxRevision == _nullDropboxRevision) {
			return null;
		}

		if (dropboxRevision == null) {
			Session session = null;

			try {
				session = openSession();

				dropboxRevision = (DropboxRevision)session.get(DropboxRevisionImpl.class,
						primaryKey);

				if (dropboxRevision != null) {
					cacheResult(dropboxRevision);
				}
				else {
					EntityCacheUtil.putResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
						DropboxRevisionImpl.class, primaryKey,
						_nullDropboxRevision);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
					DropboxRevisionImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return dropboxRevision;
	}

	/**
	 * Returns the dropbox revision with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param revisionId the primary key of the dropbox revision
	 * @return the dropbox revision, or <code>null</code> if a dropbox revision with the primary key could not be found
	 */
	@Override
	public DropboxRevision fetchByPrimaryKey(long revisionId) {
		return fetchByPrimaryKey((Serializable)revisionId);
	}

	@Override
	public Map<Serializable, DropboxRevision> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, DropboxRevision> map = new HashMap<Serializable, DropboxRevision>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			DropboxRevision dropboxRevision = fetchByPrimaryKey(primaryKey);

			if (dropboxRevision != null) {
				map.put(primaryKey, dropboxRevision);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			DropboxRevision dropboxRevision = (DropboxRevision)EntityCacheUtil.getResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
					DropboxRevisionImpl.class, primaryKey);

			if (dropboxRevision == null) {
				if (uncachedPrimaryKeys == null) {
					uncachedPrimaryKeys = new HashSet<Serializable>();
				}

				uncachedPrimaryKeys.add(primaryKey);
			}
			else {
				map.put(primaryKey, dropboxRevision);
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_DROPBOXREVISION_WHERE_PKS_IN);

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

			for (DropboxRevision dropboxRevision : (List<DropboxRevision>)q.list()) {
				map.put(dropboxRevision.getPrimaryKeyObj(), dropboxRevision);

				cacheResult(dropboxRevision);

				uncachedPrimaryKeys.remove(dropboxRevision.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				EntityCacheUtil.putResult(DropboxRevisionModelImpl.ENTITY_CACHE_ENABLED,
					DropboxRevisionImpl.class, primaryKey, _nullDropboxRevision);
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
	 * Returns all the dropbox revisions.
	 *
	 * @return the dropbox revisions
	 */
	@Override
	public List<DropboxRevision> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<DropboxRevision> findAll(int start, int end) {
		return findAll(start, end, null);
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
	@Override
	public List<DropboxRevision> findAll(int start, int end,
		OrderByComparator<DropboxRevision> orderByComparator) {
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

		List<DropboxRevision> list = (List<DropboxRevision>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_DROPBOXREVISION);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_DROPBOXREVISION;

				if (pagination) {
					sql = sql.concat(DropboxRevisionModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<DropboxRevision>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DropboxRevision>)QueryUtil.list(q,
							getDialect(), start, end);
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
	 * Removes all the dropbox revisions from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (DropboxRevision dropboxRevision : findAll()) {
			remove(dropboxRevision);
		}
	}

	/**
	 * Returns the number of dropbox revisions.
	 *
	 * @return the number of dropbox revisions
	 */
	@Override
	public int countAll() {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_DROPBOXREVISION);

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
		return DropboxRevisionModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the dropbox revision persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		EntityCacheUtil.removeCache(DropboxRevisionImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_DROPBOXREVISION = "SELECT dropboxRevision FROM DropboxRevision dropboxRevision";
	private static final String _SQL_SELECT_DROPBOXREVISION_WHERE_PKS_IN = "SELECT dropboxRevision FROM DropboxRevision dropboxRevision WHERE revisionId IN (";
	private static final String _SQL_SELECT_DROPBOXREVISION_WHERE = "SELECT dropboxRevision FROM DropboxRevision dropboxRevision WHERE ";
	private static final String _SQL_COUNT_DROPBOXREVISION = "SELECT COUNT(dropboxRevision) FROM DropboxRevision dropboxRevision";
	private static final String _SQL_COUNT_DROPBOXREVISION_WHERE = "SELECT COUNT(dropboxRevision) FROM DropboxRevision dropboxRevision WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "dropboxRevision.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No DropboxRevision exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No DropboxRevision exists with the key {";
	private static final Log _log = LogFactoryUtil.getLog(DropboxRevisionPersistenceImpl.class);
	private static final Set<String> _badColumnNames = SetUtil.fromArray(new String[] {
				"uuid", "path", "size"
			});
	private static final DropboxRevision _nullDropboxRevision = new DropboxRevisionImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<DropboxRevision> toCacheModel() {
				return _nullDropboxRevisionCacheModel;
			}
		};

	private static final CacheModel<DropboxRevision> _nullDropboxRevisionCacheModel =
		new CacheModel<DropboxRevision>() {
			@Override
			public DropboxRevision toEntityModel() {
				return _nullDropboxRevision;
			}
		};
}