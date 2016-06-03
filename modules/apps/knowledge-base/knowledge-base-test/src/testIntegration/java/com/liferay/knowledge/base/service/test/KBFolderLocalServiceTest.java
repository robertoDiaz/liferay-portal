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

package com.liferay.knowledge.base.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.knowledge.base.constants.KBFolderConstants;
import com.liferay.knowledge.base.model.KBArticle;
import com.liferay.knowledge.base.model.KBFolder;
import com.liferay.knowledge.base.service.KBArticleLocalServiceUtil;
import com.liferay.knowledge.base.service.KBFolderLocalServiceUtil;
import com.liferay.knowledge.base.util.comparator.KBModelTitleComparator;
import com.liferay.portal.kernel.dao.orm.QueryDefinition;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Roberto Díaz
 */
@RunWith(Arquillian.class)
@Sync
public class KBFolderLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
		_user = TestPropsValues.getUser();

		_kbFolder = addKbFolder(KBFolderConstants.DEFAULT_PARENT_FOLDER_ID);
	}

	@Test
	public void testGetKBFolderAndKBArticlesCount() throws Exception {
		addKBArticle(KBFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		QueryDefinition<DLFileEntry> queryDefinition = new QueryDefinition<>();

		queryDefinition.setStatus(WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(
			2,
			KBFolderLocalServiceUtil.getKBFolderAndKBArticlesCount(
				_group.getGroupId(), KBFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				queryDefinition));
	}

	@Test
	public void testGetKBFolderAndKBArticlesCountFilteredByDraftStatus()
		throws Exception {

		addKBArticle(KBFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		QueryDefinition<DLFileEntry> queryDefinition = new QueryDefinition<>();

		queryDefinition.setStatus(WorkflowConstants.STATUS_DRAFT);

		Assert.assertEquals(
			1,
			KBFolderLocalServiceUtil.getKBFolderAndKBArticlesCount(
				_group.getGroupId(), KBFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				queryDefinition));
	}

	@Test
	public void testGetKBFolderAndKBArticlesCountInFolder() throws Exception {
		addKBArticle(_kbFolder.getKbFolderId());
		addKBArticle(_kbFolder.getKbFolderId());

		addKbFolder(_kbFolder.getKbFolderId());

		QueryDefinition<DLFileEntry> queryDefinition = new QueryDefinition<>();

		queryDefinition.setStatus(WorkflowConstants.STATUS_APPROVED);

		Assert.assertEquals(
			3,
			KBFolderLocalServiceUtil.getKBFolderAndKBArticlesCount(
				_group.getGroupId(), _kbFolder.getKbFolderId(),
				queryDefinition));
	}

	@Test
	public void testGetKBFolderAndKBArticlesFilteredByDraftStatus()
		throws Exception {

		QueryDefinition<DLFileEntry> queryDefinition = new QueryDefinition<>();

		queryDefinition.setStatus(WorkflowConstants.STATUS_DRAFT);
		queryDefinition.setOrderByComparator(
			new KBModelTitleComparator<DLFileEntry>(false, true));

		addKBArticle(KBFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		List<Object> kbFolderAndArticles =
			KBFolderLocalServiceUtil.getKBFolderAndKBArticles(
				_group.getGroupId(), KBFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				queryDefinition);

		KBFolder currentkbFolder = (KBFolder)kbFolderAndArticles.get(0);

		Assert.assertEquals(1, kbFolderAndArticles.size());
		Assert.assertEquals(
			_kbFolder.getKbFolderId(), currentkbFolder.getKbFolderId());
	}

	@Test
	public void testGetKBFolderAndKBArticlesInFolder() throws Exception {
		QueryDefinition<DLFileEntry> queryDefinition = new QueryDefinition<>();

		queryDefinition.setStatus(WorkflowConstants.STATUS_APPROVED);
		queryDefinition.setOrderByComparator(
			new KBModelTitleComparator<DLFileEntry>(false, true));

		KBArticle kbArticle1 = addKBArticle(_kbFolder.getKbFolderId());
		KBArticle kbArticle2 = addKBArticle(_kbFolder.getKbFolderId());

		KBFolder kbFolder = addKbFolder(_kbFolder.getKbFolderId());

		List<Object> kbFolderAndArticles =
			KBFolderLocalServiceUtil.getKBFolderAndKBArticles(
				_group.getGroupId(), KBFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				queryDefinition);

		KBFolder currentkbFolder = (KBFolder)kbFolderAndArticles.get(0);
		KBArticle currentkbArticle1 = (KBArticle)kbFolderAndArticles.get(1);
		KBArticle currentkbArticle2 = (KBArticle)kbFolderAndArticles.get(2);

		Assert.assertEquals(
			kbFolder.getKbFolderId(), currentkbFolder.getKbFolderId());
		Assert.assertEquals(
			kbArticle1.getKbArticleId(), currentkbArticle1.getKbArticleId());
		Assert.assertEquals(
			kbArticle2.getKbArticleId(), currentkbArticle2.getKbArticleId());
	}

	@Test
	public void testGetKBFolderAndKBArticles() throws Exception {
		QueryDefinition<DLFileEntry> queryDefinition = new QueryDefinition<>();

		queryDefinition.setStatus(WorkflowConstants.STATUS_APPROVED);
		queryDefinition.setOrderByComparator(
			new KBModelTitleComparator<DLFileEntry>(false, true));

		KBArticle kbArticle = addKBArticle(
			KBFolderConstants.DEFAULT_PARENT_FOLDER_ID);

		List<Object> kbFolderAndArticles =
			KBFolderLocalServiceUtil.getKBFolderAndKBArticles(
				_group.getGroupId(), KBFolderConstants.DEFAULT_PARENT_FOLDER_ID,
				queryDefinition);

		KBFolder currentkbFolder = (KBFolder)kbFolderAndArticles.get(0);
		KBArticle currentkbArticle = (KBArticle)kbFolderAndArticles.get(1);

		Assert.assertEquals(
			_kbFolder.getKbFolderId(), currentkbFolder.getKbFolderId());
		Assert.assertEquals(
			kbArticle.getKbArticleId(), currentkbArticle.getKbArticleId());
	}

	protected KBArticle addKBArticle(long parentKbFolderId) throws Exception {
		String title = RandomTestUtil.randomString();

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group, _user.getUserId());

		return KBArticleLocalServiceUtil.addKBArticle(
			_user.getUserId(),
			PortalUtil.getClassNameId(KBFolderConstants.getClassName()),
			parentKbFolderId, title, title, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), null, new String[0], new String[0],
			serviceContext);
	}

	protected KBFolder addKbFolder(long kbFolderId)
		throws com.liferay.portal.kernel.exception.PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group, _user.getUserId());

		return KBFolderLocalServiceUtil.addKBFolder(
			_user.getUserId(), _group.getGroupId(),
			PortalUtil.getClassNameId(KBFolderConstants.getClassName()),
			kbFolderId, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), serviceContext);
	}

	@DeleteAfterTestRun
	private Group _group;

	private KBFolder _kbFolder;
	private User _user;

}