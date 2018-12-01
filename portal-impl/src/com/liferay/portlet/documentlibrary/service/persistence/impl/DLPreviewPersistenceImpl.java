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

package com.liferay.portlet.documentlibrary.service.persistence.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.kernel.exception.NoSuchPreviewException;
import com.liferay.document.library.kernel.model.DLPreview;
import com.liferay.document.library.kernel.service.persistence.DLPreviewPersistence;

import com.liferay.petra.string.StringBundler;

import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;

import com.liferay.portlet.documentlibrary.model.impl.DLPreviewImpl;
import com.liferay.portlet.documentlibrary.model.impl.DLPreviewModelImpl;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence implementation for the document library preview service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLPreviewPersistence
 * @see com.liferay.document.library.kernel.service.persistence.DLPreviewUtil
 * @generated
 */
@ProviderType
public class DLPreviewPersistenceImpl extends BasePersistenceImpl<DLPreview>
	implements DLPreviewPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link DLPreviewUtil} to access the document library preview persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = DLPreviewImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, DLPreviewImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, DLPreviewImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_FILEENTRYID =
		new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, DLPreviewImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByFileEntryId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEENTRYID =
		new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, DLPreviewImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByFileEntryId",
			new String[] { Long.class.getName() },
			DLPreviewModelImpl.FILEENTRYID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_FILEENTRYID = new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByFileEntryId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the document library previews where fileEntryId = &#63;.
	 *
	 * @param fileEntryId the file entry ID
	 * @return the matching document library previews
	 */
	@Override
	public List<DLPreview> findByFileEntryId(long fileEntryId) {
		return findByFileEntryId(fileEntryId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
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
	@Override
	public List<DLPreview> findByFileEntryId(long fileEntryId, int start,
		int end) {
		return findByFileEntryId(fileEntryId, start, end, null);
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
	@Override
	public List<DLPreview> findByFileEntryId(long fileEntryId, int start,
		int end, OrderByComparator<DLPreview> orderByComparator) {
		return findByFileEntryId(fileEntryId, start, end, orderByComparator,
			true);
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
	@Override
	public List<DLPreview> findByFileEntryId(long fileEntryId, int start,
		int end, OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEENTRYID;
			finderArgs = new Object[] { fileEntryId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_FILEENTRYID;
			finderArgs = new Object[] { fileEntryId, start, end, orderByComparator };
		}

		List<DLPreview> list = null;

		if (retrieveFromCache) {
			list = (List<DLPreview>)FinderCacheUtil.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DLPreview dlPreview : list) {
					if ((fileEntryId != dlPreview.getFileEntryId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_DLPREVIEW_WHERE);

			query.append(_FINDER_COLUMN_FILEENTRYID_FILEENTRYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DLPreviewModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(fileEntryId);

				if (!pagination) {
					list = (List<DLPreview>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DLPreview>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first document library preview in the ordered set where fileEntryId = &#63;.
	 *
	 * @param fileEntryId the file entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library preview
	 * @throws NoSuchPreviewException if a matching document library preview could not be found
	 */
	@Override
	public DLPreview findByFileEntryId_First(long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException {
		DLPreview dlPreview = fetchByFileEntryId_First(fileEntryId,
				orderByComparator);

		if (dlPreview != null) {
			return dlPreview;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("fileEntryId=");
		msg.append(fileEntryId);

		msg.append("}");

		throw new NoSuchPreviewException(msg.toString());
	}

	/**
	 * Returns the first document library preview in the ordered set where fileEntryId = &#63;.
	 *
	 * @param fileEntryId the file entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library preview, or <code>null</code> if a matching document library preview could not be found
	 */
	@Override
	public DLPreview fetchByFileEntryId_First(long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator) {
		List<DLPreview> list = findByFileEntryId(fileEntryId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last document library preview in the ordered set where fileEntryId = &#63;.
	 *
	 * @param fileEntryId the file entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library preview
	 * @throws NoSuchPreviewException if a matching document library preview could not be found
	 */
	@Override
	public DLPreview findByFileEntryId_Last(long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException {
		DLPreview dlPreview = fetchByFileEntryId_Last(fileEntryId,
				orderByComparator);

		if (dlPreview != null) {
			return dlPreview;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("fileEntryId=");
		msg.append(fileEntryId);

		msg.append("}");

		throw new NoSuchPreviewException(msg.toString());
	}

	/**
	 * Returns the last document library preview in the ordered set where fileEntryId = &#63;.
	 *
	 * @param fileEntryId the file entry ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library preview, or <code>null</code> if a matching document library preview could not be found
	 */
	@Override
	public DLPreview fetchByFileEntryId_Last(long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator) {
		int count = countByFileEntryId(fileEntryId);

		if (count == 0) {
			return null;
		}

		List<DLPreview> list = findByFileEntryId(fileEntryId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DLPreview[] findByFileEntryId_PrevAndNext(long filePreviewId,
		long fileEntryId, OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException {
		DLPreview dlPreview = findByPrimaryKey(filePreviewId);

		Session session = null;

		try {
			session = openSession();

			DLPreview[] array = new DLPreviewImpl[3];

			array[0] = getByFileEntryId_PrevAndNext(session, dlPreview,
					fileEntryId, orderByComparator, true);

			array[1] = dlPreview;

			array[2] = getByFileEntryId_PrevAndNext(session, dlPreview,
					fileEntryId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DLPreview getByFileEntryId_PrevAndNext(Session session,
		DLPreview dlPreview, long fileEntryId,
		OrderByComparator<DLPreview> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(4 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DLPREVIEW_WHERE);

		query.append(_FINDER_COLUMN_FILEENTRYID_FILEENTRYID_2);

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
			query.append(DLPreviewModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(fileEntryId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dlPreview);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DLPreview> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the document library previews where fileEntryId = &#63; from the database.
	 *
	 * @param fileEntryId the file entry ID
	 */
	@Override
	public void removeByFileEntryId(long fileEntryId) {
		for (DLPreview dlPreview : findByFileEntryId(fileEntryId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dlPreview);
		}
	}

	/**
	 * Returns the number of document library previews where fileEntryId = &#63;.
	 *
	 * @param fileEntryId the file entry ID
	 * @return the number of matching document library previews
	 */
	@Override
	public int countByFileEntryId(long fileEntryId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_FILEENTRYID;

		Object[] finderArgs = new Object[] { fileEntryId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_DLPREVIEW_WHERE);

			query.append(_FINDER_COLUMN_FILEENTRYID_FILEENTRYID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(fileEntryId);

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

	private static final String _FINDER_COLUMN_FILEENTRYID_FILEENTRYID_2 = "dlPreview.fileEntryId = ?";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_FILEVERSIONID =
		new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, DLPreviewImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByFileVersionId",
			new String[] {
				Long.class.getName(),
				
			Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEVERSIONID =
		new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, DLPreviewImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByFileVersionId",
			new String[] { Long.class.getName() },
			DLPreviewModelImpl.FILEVERSIONID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_FILEVERSIONID = new FinderPath(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByFileVersionId",
			new String[] { Long.class.getName() });

	/**
	 * Returns all the document library previews where fileVersionId = &#63;.
	 *
	 * @param fileVersionId the file version ID
	 * @return the matching document library previews
	 */
	@Override
	public List<DLPreview> findByFileVersionId(long fileVersionId) {
		return findByFileVersionId(fileVersionId, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
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
	@Override
	public List<DLPreview> findByFileVersionId(long fileVersionId, int start,
		int end) {
		return findByFileVersionId(fileVersionId, start, end, null);
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
	@Override
	public List<DLPreview> findByFileVersionId(long fileVersionId, int start,
		int end, OrderByComparator<DLPreview> orderByComparator) {
		return findByFileVersionId(fileVersionId, start, end,
			orderByComparator, true);
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
	@Override
	public List<DLPreview> findByFileVersionId(long fileVersionId, int start,
		int end, OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache) {
		boolean pagination = true;
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			pagination = false;
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEVERSIONID;
			finderArgs = new Object[] { fileVersionId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_FILEVERSIONID;
			finderArgs = new Object[] {
					fileVersionId,
					
					start, end, orderByComparator
				};
		}

		List<DLPreview> list = null;

		if (retrieveFromCache) {
			list = (List<DLPreview>)FinderCacheUtil.getResult(finderPath,
					finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (DLPreview dlPreview : list) {
					if ((fileVersionId != dlPreview.getFileVersionId())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 2));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_DLPREVIEW_WHERE);

			query.append(_FINDER_COLUMN_FILEVERSIONID_FILEVERSIONID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}
			else
			 if (pagination) {
				query.append(DLPreviewModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(fileVersionId);

				if (!pagination) {
					list = (List<DLPreview>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DLPreview>)QueryUtil.list(q, getDialect(),
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
	 * Returns the first document library preview in the ordered set where fileVersionId = &#63;.
	 *
	 * @param fileVersionId the file version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library preview
	 * @throws NoSuchPreviewException if a matching document library preview could not be found
	 */
	@Override
	public DLPreview findByFileVersionId_First(long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException {
		DLPreview dlPreview = fetchByFileVersionId_First(fileVersionId,
				orderByComparator);

		if (dlPreview != null) {
			return dlPreview;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("fileVersionId=");
		msg.append(fileVersionId);

		msg.append("}");

		throw new NoSuchPreviewException(msg.toString());
	}

	/**
	 * Returns the first document library preview in the ordered set where fileVersionId = &#63;.
	 *
	 * @param fileVersionId the file version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching document library preview, or <code>null</code> if a matching document library preview could not be found
	 */
	@Override
	public DLPreview fetchByFileVersionId_First(long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator) {
		List<DLPreview> list = findByFileVersionId(fileVersionId, 0, 1,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last document library preview in the ordered set where fileVersionId = &#63;.
	 *
	 * @param fileVersionId the file version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library preview
	 * @throws NoSuchPreviewException if a matching document library preview could not be found
	 */
	@Override
	public DLPreview findByFileVersionId_Last(long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException {
		DLPreview dlPreview = fetchByFileVersionId_Last(fileVersionId,
				orderByComparator);

		if (dlPreview != null) {
			return dlPreview;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("fileVersionId=");
		msg.append(fileVersionId);

		msg.append("}");

		throw new NoSuchPreviewException(msg.toString());
	}

	/**
	 * Returns the last document library preview in the ordered set where fileVersionId = &#63;.
	 *
	 * @param fileVersionId the file version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching document library preview, or <code>null</code> if a matching document library preview could not be found
	 */
	@Override
	public DLPreview fetchByFileVersionId_Last(long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator) {
		int count = countByFileVersionId(fileVersionId);

		if (count == 0) {
			return null;
		}

		List<DLPreview> list = findByFileVersionId(fileVersionId, count - 1,
				count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
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
	@Override
	public DLPreview[] findByFileVersionId_PrevAndNext(long filePreviewId,
		long fileVersionId, OrderByComparator<DLPreview> orderByComparator)
		throws NoSuchPreviewException {
		DLPreview dlPreview = findByPrimaryKey(filePreviewId);

		Session session = null;

		try {
			session = openSession();

			DLPreview[] array = new DLPreviewImpl[3];

			array[0] = getByFileVersionId_PrevAndNext(session, dlPreview,
					fileVersionId, orderByComparator, true);

			array[1] = dlPreview;

			array[2] = getByFileVersionId_PrevAndNext(session, dlPreview,
					fileVersionId, orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected DLPreview getByFileVersionId_PrevAndNext(Session session,
		DLPreview dlPreview, long fileVersionId,
		OrderByComparator<DLPreview> orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(4 +
					(orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_DLPREVIEW_WHERE);

		query.append(_FINDER_COLUMN_FILEVERSIONID_FILEVERSIONID_2);

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
			query.append(DLPreviewModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(fileVersionId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(dlPreview);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<DLPreview> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the document library previews where fileVersionId = &#63; from the database.
	 *
	 * @param fileVersionId the file version ID
	 */
	@Override
	public void removeByFileVersionId(long fileVersionId) {
		for (DLPreview dlPreview : findByFileVersionId(fileVersionId,
				QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {
			remove(dlPreview);
		}
	}

	/**
	 * Returns the number of document library previews where fileVersionId = &#63;.
	 *
	 * @param fileVersionId the file version ID
	 * @return the number of matching document library previews
	 */
	@Override
	public int countByFileVersionId(long fileVersionId) {
		FinderPath finderPath = FINDER_PATH_COUNT_BY_FILEVERSIONID;

		Object[] finderArgs = new Object[] { fileVersionId };

		Long count = (Long)FinderCacheUtil.getResult(finderPath, finderArgs,
				this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_DLPREVIEW_WHERE);

			query.append(_FINDER_COLUMN_FILEVERSIONID_FILEVERSIONID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(fileVersionId);

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

	private static final String _FINDER_COLUMN_FILEVERSIONID_FILEVERSIONID_2 = "dlPreview.fileVersionId = ?";

	public DLPreviewPersistenceImpl() {
		setModelClass(DLPreview.class);
	}

	/**
	 * Caches the document library preview in the entity cache if it is enabled.
	 *
	 * @param dlPreview the document library preview
	 */
	@Override
	public void cacheResult(DLPreview dlPreview) {
		EntityCacheUtil.putResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewImpl.class, dlPreview.getPrimaryKey(), dlPreview);

		dlPreview.resetOriginalValues();
	}

	/**
	 * Caches the document library previews in the entity cache if it is enabled.
	 *
	 * @param dlPreviews the document library previews
	 */
	@Override
	public void cacheResult(List<DLPreview> dlPreviews) {
		for (DLPreview dlPreview : dlPreviews) {
			if (EntityCacheUtil.getResult(
						DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
						DLPreviewImpl.class, dlPreview.getPrimaryKey()) == null) {
				cacheResult(dlPreview);
			}
			else {
				dlPreview.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all document library previews.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		EntityCacheUtil.clearCache(DLPreviewImpl.class);

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the document library preview.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(DLPreview dlPreview) {
		EntityCacheUtil.removeResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewImpl.class, dlPreview.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<DLPreview> dlPreviews) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (DLPreview dlPreview : dlPreviews) {
			EntityCacheUtil.removeResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
				DLPreviewImpl.class, dlPreview.getPrimaryKey());
		}
	}

	/**
	 * Creates a new document library preview with the primary key. Does not add the document library preview to the database.
	 *
	 * @param filePreviewId the primary key for the new document library preview
	 * @return the new document library preview
	 */
	@Override
	public DLPreview create(long filePreviewId) {
		DLPreview dlPreview = new DLPreviewImpl();

		dlPreview.setNew(true);
		dlPreview.setPrimaryKey(filePreviewId);

		return dlPreview;
	}

	/**
	 * Removes the document library preview with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param filePreviewId the primary key of the document library preview
	 * @return the document library preview that was removed
	 * @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	 */
	@Override
	public DLPreview remove(long filePreviewId) throws NoSuchPreviewException {
		return remove((Serializable)filePreviewId);
	}

	/**
	 * Removes the document library preview with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the document library preview
	 * @return the document library preview that was removed
	 * @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	 */
	@Override
	public DLPreview remove(Serializable primaryKey)
		throws NoSuchPreviewException {
		Session session = null;

		try {
			session = openSession();

			DLPreview dlPreview = (DLPreview)session.get(DLPreviewImpl.class,
					primaryKey);

			if (dlPreview == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchPreviewException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(dlPreview);
		}
		catch (NoSuchPreviewException nsee) {
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
	protected DLPreview removeImpl(DLPreview dlPreview) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(dlPreview)) {
				dlPreview = (DLPreview)session.get(DLPreviewImpl.class,
						dlPreview.getPrimaryKeyObj());
			}

			if (dlPreview != null) {
				session.delete(dlPreview);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		if (dlPreview != null) {
			clearCache(dlPreview);
		}

		return dlPreview;
	}

	@Override
	public DLPreview updateImpl(DLPreview dlPreview) {
		boolean isNew = dlPreview.isNew();

		if (!(dlPreview instanceof DLPreviewModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(dlPreview.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(dlPreview);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in dlPreview proxy " +
					invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom DLPreview implementation " +
				dlPreview.getClass());
		}

		DLPreviewModelImpl dlPreviewModelImpl = (DLPreviewModelImpl)dlPreview;

		Session session = null;

		try {
			session = openSession();

			if (dlPreview.isNew()) {
				session.save(dlPreview);

				dlPreview.setNew(false);
			}
			else {
				dlPreview = (DLPreview)session.merge(dlPreview);
			}
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!DLPreviewModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else
		 if (isNew) {
			Object[] args = new Object[] { dlPreviewModelImpl.getFileEntryId() };

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_FILEENTRYID, args);
			FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEENTRYID,
				args);

			args = new Object[] { dlPreviewModelImpl.getFileVersionId() };

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_FILEVERSIONID,
				args);
			FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEVERSIONID,
				args);

			FinderCacheUtil.removeResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY);
			FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL,
				FINDER_ARGS_EMPTY);
		}

		else {
			if ((dlPreviewModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEENTRYID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dlPreviewModelImpl.getOriginalFileEntryId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_FILEENTRYID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEENTRYID,
					args);

				args = new Object[] { dlPreviewModelImpl.getFileEntryId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_FILEENTRYID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEENTRYID,
					args);
			}

			if ((dlPreviewModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEVERSIONID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						dlPreviewModelImpl.getOriginalFileVersionId()
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_FILEVERSIONID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEVERSIONID,
					args);

				args = new Object[] { dlPreviewModelImpl.getFileVersionId() };

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_FILEVERSIONID,
					args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_FILEVERSIONID,
					args);
			}
		}

		EntityCacheUtil.putResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
			DLPreviewImpl.class, dlPreview.getPrimaryKey(), dlPreview, false);

		dlPreview.resetOriginalValues();

		return dlPreview;
	}

	/**
	 * Returns the document library preview with the primary key or throws a {@link com.liferay.portal.kernel.exception.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the document library preview
	 * @return the document library preview
	 * @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	 */
	@Override
	public DLPreview findByPrimaryKey(Serializable primaryKey)
		throws NoSuchPreviewException {
		DLPreview dlPreview = fetchByPrimaryKey(primaryKey);

		if (dlPreview == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchPreviewException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				primaryKey);
		}

		return dlPreview;
	}

	/**
	 * Returns the document library preview with the primary key or throws a {@link NoSuchPreviewException} if it could not be found.
	 *
	 * @param filePreviewId the primary key of the document library preview
	 * @return the document library preview
	 * @throws NoSuchPreviewException if a document library preview with the primary key could not be found
	 */
	@Override
	public DLPreview findByPrimaryKey(long filePreviewId)
		throws NoSuchPreviewException {
		return findByPrimaryKey((Serializable)filePreviewId);
	}

	/**
	 * Returns the document library preview with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the document library preview
	 * @return the document library preview, or <code>null</code> if a document library preview with the primary key could not be found
	 */
	@Override
	public DLPreview fetchByPrimaryKey(Serializable primaryKey) {
		Serializable serializable = EntityCacheUtil.getResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
				DLPreviewImpl.class, primaryKey);

		if (serializable == nullModel) {
			return null;
		}

		DLPreview dlPreview = (DLPreview)serializable;

		if (dlPreview == null) {
			Session session = null;

			try {
				session = openSession();

				dlPreview = (DLPreview)session.get(DLPreviewImpl.class,
						primaryKey);

				if (dlPreview != null) {
					cacheResult(dlPreview);
				}
				else {
					EntityCacheUtil.putResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
						DLPreviewImpl.class, primaryKey, nullModel);
				}
			}
			catch (Exception e) {
				EntityCacheUtil.removeResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
					DLPreviewImpl.class, primaryKey);

				throw processException(e);
			}
			finally {
				closeSession(session);
			}
		}

		return dlPreview;
	}

	/**
	 * Returns the document library preview with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param filePreviewId the primary key of the document library preview
	 * @return the document library preview, or <code>null</code> if a document library preview with the primary key could not be found
	 */
	@Override
	public DLPreview fetchByPrimaryKey(long filePreviewId) {
		return fetchByPrimaryKey((Serializable)filePreviewId);
	}

	@Override
	public Map<Serializable, DLPreview> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {
		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, DLPreview> map = new HashMap<Serializable, DLPreview>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			DLPreview dlPreview = fetchByPrimaryKey(primaryKey);

			if (dlPreview != null) {
				map.put(primaryKey, dlPreview);
			}

			return map;
		}

		Set<Serializable> uncachedPrimaryKeys = null;

		for (Serializable primaryKey : primaryKeys) {
			Serializable serializable = EntityCacheUtil.getResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
					DLPreviewImpl.class, primaryKey);

			if (serializable != nullModel) {
				if (serializable == null) {
					if (uncachedPrimaryKeys == null) {
						uncachedPrimaryKeys = new HashSet<Serializable>();
					}

					uncachedPrimaryKeys.add(primaryKey);
				}
				else {
					map.put(primaryKey, (DLPreview)serializable);
				}
			}
		}

		if (uncachedPrimaryKeys == null) {
			return map;
		}

		StringBundler query = new StringBundler((uncachedPrimaryKeys.size() * 2) +
				1);

		query.append(_SQL_SELECT_DLPREVIEW_WHERE_PKS_IN);

		for (Serializable primaryKey : uncachedPrimaryKeys) {
			query.append((long)primaryKey);

			query.append(",");
		}

		query.setIndex(query.index() - 1);

		query.append(")");

		String sql = query.toString();

		Session session = null;

		try {
			session = openSession();

			Query q = session.createQuery(sql);

			for (DLPreview dlPreview : (List<DLPreview>)q.list()) {
				map.put(dlPreview.getPrimaryKeyObj(), dlPreview);

				cacheResult(dlPreview);

				uncachedPrimaryKeys.remove(dlPreview.getPrimaryKeyObj());
			}

			for (Serializable primaryKey : uncachedPrimaryKeys) {
				EntityCacheUtil.putResult(DLPreviewModelImpl.ENTITY_CACHE_ENABLED,
					DLPreviewImpl.class, primaryKey, nullModel);
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
	 * Returns all the document library previews.
	 *
	 * @return the document library previews
	 */
	@Override
	public List<DLPreview> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<DLPreview> findAll(int start, int end) {
		return findAll(start, end, null);
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
	@Override
	public List<DLPreview> findAll(int start, int end,
		OrderByComparator<DLPreview> orderByComparator) {
		return findAll(start, end, orderByComparator, true);
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
	@Override
	public List<DLPreview> findAll(int start, int end,
		OrderByComparator<DLPreview> orderByComparator,
		boolean retrieveFromCache) {
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

		List<DLPreview> list = null;

		if (retrieveFromCache) {
			list = (List<DLPreview>)FinderCacheUtil.getResult(finderPath,
					finderArgs, this);
		}

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 2));

				query.append(_SQL_SELECT_DLPREVIEW);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_DLPREVIEW;

				if (pagination) {
					sql = sql.concat(DLPreviewModelImpl.ORDER_BY_JPQL);
				}
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (!pagination) {
					list = (List<DLPreview>)QueryUtil.list(q, getDialect(),
							start, end, false);

					Collections.sort(list);

					list = Collections.unmodifiableList(list);
				}
				else {
					list = (List<DLPreview>)QueryUtil.list(q, getDialect(),
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
	 * Removes all the document library previews from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (DLPreview dlPreview : findAll()) {
			remove(dlPreview);
		}
	}

	/**
	 * Returns the number of document library previews.
	 *
	 * @return the number of document library previews
	 */
	@Override
	public int countAll() {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_DLPREVIEW);

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
	protected Map<String, Integer> getTableColumnsMap() {
		return DLPreviewModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the document library preview persistence.
	 */
	public void afterPropertiesSet() {
	}

	public void destroy() {
		EntityCacheUtil.removeCache(DLPreviewImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	private static final String _SQL_SELECT_DLPREVIEW = "SELECT dlPreview FROM DLPreview dlPreview";
	private static final String _SQL_SELECT_DLPREVIEW_WHERE_PKS_IN = "SELECT dlPreview FROM DLPreview dlPreview WHERE filePreviewId IN (";
	private static final String _SQL_SELECT_DLPREVIEW_WHERE = "SELECT dlPreview FROM DLPreview dlPreview WHERE ";
	private static final String _SQL_COUNT_DLPREVIEW = "SELECT COUNT(dlPreview) FROM DLPreview dlPreview";
	private static final String _SQL_COUNT_DLPREVIEW_WHERE = "SELECT COUNT(dlPreview) FROM DLPreview dlPreview WHERE ";
	private static final String _ORDER_BY_ENTITY_ALIAS = "dlPreview.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No DLPreview exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No DLPreview exists with the key {";
	private static final Log _log = LogFactoryUtil.getLog(DLPreviewPersistenceImpl.class);
}