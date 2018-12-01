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

package com.liferay.portlet.documentlibrary.service.persistence.test;

import com.liferay.document.library.kernel.exception.NoSuchPreviewException;
import com.liferay.document.library.kernel.model.DLPreview;
import com.liferay.document.library.kernel.service.DLPreviewLocalServiceUtil;
import com.liferay.document.library.kernel.service.persistence.DLPreviewPersistence;
import com.liferay.document.library.kernel.service.persistence.DLPreviewUtil;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @generated
 */
public class DLPreviewPersistenceTest {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new AggregateTestRule(new LiferayIntegrationTestRule(),
			PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(Propagation.REQUIRED));

	@Before
	public void setUp() {
		_persistence = DLPreviewUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<DLPreview> iterator = _dlPreviews.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		DLPreview dlPreview = _persistence.create(pk);

		Assert.assertNotNull(dlPreview);

		Assert.assertEquals(dlPreview.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		DLPreview newDLPreview = addDLPreview();

		_persistence.remove(newDLPreview);

		DLPreview existingDLPreview = _persistence.fetchByPrimaryKey(newDLPreview.getPrimaryKey());

		Assert.assertNull(existingDLPreview);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addDLPreview();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		DLPreview newDLPreview = _persistence.create(pk);

		newDLPreview.setGroupId(RandomTestUtil.nextLong());

		newDLPreview.setFileEntryId(RandomTestUtil.nextLong());

		newDLPreview.setFileVersionId(RandomTestUtil.nextLong());

		newDLPreview.setStatus(RandomTestUtil.randomString());

		_dlPreviews.add(_persistence.update(newDLPreview));

		DLPreview existingDLPreview = _persistence.findByPrimaryKey(newDLPreview.getPrimaryKey());

		Assert.assertEquals(existingDLPreview.getFilePreviewId(),
			newDLPreview.getFilePreviewId());
		Assert.assertEquals(existingDLPreview.getGroupId(),
			newDLPreview.getGroupId());
		Assert.assertEquals(existingDLPreview.getFileEntryId(),
			newDLPreview.getFileEntryId());
		Assert.assertEquals(existingDLPreview.getFileVersionId(),
			newDLPreview.getFileVersionId());
		Assert.assertEquals(existingDLPreview.getStatus(),
			newDLPreview.getStatus());
	}

	@Test
	public void testCountByFileEntryId() throws Exception {
		_persistence.countByFileEntryId(RandomTestUtil.nextLong());

		_persistence.countByFileEntryId(0L);
	}

	@Test
	public void testCountByFileVersionId() throws Exception {
		_persistence.countByFileVersionId(RandomTestUtil.nextLong());

		_persistence.countByFileVersionId(0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		DLPreview newDLPreview = addDLPreview();

		DLPreview existingDLPreview = _persistence.findByPrimaryKey(newDLPreview.getPrimaryKey());

		Assert.assertEquals(existingDLPreview, newDLPreview);
	}

	@Test(expected = NoSuchPreviewException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			getOrderByComparator());
	}

	protected OrderByComparator<DLPreview> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create("DLPreview",
			"filePreviewId", true, "groupId", true, "fileEntryId", true,
			"fileVersionId", true, "status", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		DLPreview newDLPreview = addDLPreview();

		DLPreview existingDLPreview = _persistence.fetchByPrimaryKey(newDLPreview.getPrimaryKey());

		Assert.assertEquals(existingDLPreview, newDLPreview);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		DLPreview missingDLPreview = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingDLPreview);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {
		DLPreview newDLPreview1 = addDLPreview();
		DLPreview newDLPreview2 = addDLPreview();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newDLPreview1.getPrimaryKey());
		primaryKeys.add(newDLPreview2.getPrimaryKey());

		Map<Serializable, DLPreview> dlPreviews = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, dlPreviews.size());
		Assert.assertEquals(newDLPreview1,
			dlPreviews.get(newDLPreview1.getPrimaryKey()));
		Assert.assertEquals(newDLPreview2,
			dlPreviews.get(newDLPreview2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {
		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, DLPreview> dlPreviews = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(dlPreviews.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {
		DLPreview newDLPreview = addDLPreview();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newDLPreview.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, DLPreview> dlPreviews = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, dlPreviews.size());
		Assert.assertEquals(newDLPreview,
			dlPreviews.get(newDLPreview.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys()
		throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, DLPreview> dlPreviews = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(dlPreviews.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey()
		throws Exception {
		DLPreview newDLPreview = addDLPreview();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newDLPreview.getPrimaryKey());

		Map<Serializable, DLPreview> dlPreviews = _persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, dlPreviews.size());
		Assert.assertEquals(newDLPreview,
			dlPreviews.get(newDLPreview.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery = DLPreviewLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(new ActionableDynamicQuery.PerformActionMethod<DLPreview>() {
				@Override
				public void performAction(DLPreview dlPreview) {
					Assert.assertNotNull(dlPreview);

					count.increment();
				}
			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		DLPreview newDLPreview = addDLPreview();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLPreview.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("filePreviewId",
				newDLPreview.getFilePreviewId()));

		List<DLPreview> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		DLPreview existingDLPreview = result.get(0);

		Assert.assertEquals(existingDLPreview, newDLPreview);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLPreview.class,
				_dynamicQueryClassLoader);

		dynamicQuery.add(RestrictionsFactoryUtil.eq("filePreviewId",
				RandomTestUtil.nextLong()));

		List<DLPreview> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		DLPreview newDLPreview = addDLPreview();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLPreview.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"filePreviewId"));

		Object newFilePreviewId = newDLPreview.getFilePreviewId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("filePreviewId",
				new Object[] { newFilePreviewId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingFilePreviewId = result.get(0);

		Assert.assertEquals(existingFilePreviewId, newFilePreviewId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(DLPreview.class,
				_dynamicQueryClassLoader);

		dynamicQuery.setProjection(ProjectionFactoryUtil.property(
				"filePreviewId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("filePreviewId",
				new Object[] { RandomTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	protected DLPreview addDLPreview() throws Exception {
		long pk = RandomTestUtil.nextLong();

		DLPreview dlPreview = _persistence.create(pk);

		dlPreview.setGroupId(RandomTestUtil.nextLong());

		dlPreview.setFileEntryId(RandomTestUtil.nextLong());

		dlPreview.setFileVersionId(RandomTestUtil.nextLong());

		dlPreview.setStatus(RandomTestUtil.randomString());

		_dlPreviews.add(_persistence.update(dlPreview));

		return dlPreview;
	}

	private List<DLPreview> _dlPreviews = new ArrayList<DLPreview>();
	private DLPreviewPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;
}