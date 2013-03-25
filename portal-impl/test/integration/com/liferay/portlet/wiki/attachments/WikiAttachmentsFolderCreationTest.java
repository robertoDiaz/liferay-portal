/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.wiki.attachments;

import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.test.MainServletExecutionTestListener;
import com.liferay.portal.test.TransactionalCallbackAwareExecutionTestListener;
import com.liferay.portal.util.GroupTestUtil;
import com.liferay.portal.util.TestPropsValues;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil;
import com.liferay.portlet.wiki.model.WikiNode;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.portlet.wiki.util.WikiTestUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Roberto Díaz
 */
@ExecutionTestListeners(
	listeners = {
		EnvironmentExecutionTestListener.class,
		TransactionalCallbackAwareExecutionTestListener.class
	})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
@Transactional
public class WikiAttachmentsFolderCreationTest {

	@Before
	public void setUp() throws Exception {
		FinderCacheUtil.clearCache();

		_group = GroupTestUtil.addGroup();
	}

	@After
	public void tearDown() {
		_group = null;
		_node  =null;
		_page = null;
	}

	@Test
	public void testAddWikiNode() throws Exception {
		int expectedFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		addWikiNode();
		int currentFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		Assert.assertNotNull(_node);
		Assert.assertEquals(expectedFolderCount, currentFolderCount);
	}

	@Test
	public void testAddWikiPage() throws Exception {
		int expectedFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		addWikiPage();
		int currentFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		Assert.assertNotNull(_page);
		Assert.assertEquals(expectedFolderCount, currentFolderCount);
	}

	@Test
	public void testAddWikiPageAttachments() throws Exception {
		int initialFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		int newRepositoryFolder = 1;
		int newNodeFolder = 1;
		int newPageFolder = 1;

		int newFolders = newRepositoryFolder + newNodeFolder + newPageFolder;

		int firstFolderCount = assertAttachFileToPage1InNode1FromGroup1(
			initialFolderCount + newFolders);

		int totalNewFolders = newFolders;

		int secondFolderCount = assertAttachFileToPage2InNode1FromGroup1(
			firstFolderCount + newPageFolder);

		totalNewFolders += newPageFolder;

		newFolders = newNodeFolder + newPageFolder;

		int thirdFolderCount = assertAttachFileToPage2InNode2FromGroup1(
			secondFolderCount + newFolders);

		totalNewFolders += newFolders;

		newFolders = newRepositoryFolder + newNodeFolder + newPageFolder;

		int finalFolderCount = assertAttachFileToPage1InNode1FromGroup2(
			thirdFolderCount + newFolders );

		totalNewFolders += newFolders;

		Assert.assertEquals(
			initialFolderCount + totalNewFolders, finalFolderCount);
	}

	@Test
	public void testAddWikiPageSecondAttachment() throws Exception {
		int originalFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		addWikiPageAttachment();

		int firstFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		addWikiPageAttachment();

		int finalFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		Assert.assertNotSame(originalFolderCount, firstFolderCount);
		Assert.assertEquals(firstFolderCount, finalFolderCount);
	}

	@Test
	public void testMoveToTrashWikiPageWithAttachments() throws Exception {
		addWikiPageAttachment();

		WikiPageLocalServiceUtil.movePageToTrash(
			TestPropsValues.getUserId(), _page);

		WikiPage wikiPage =
			WikiPageLocalServiceUtil.getWikiPage(_page.getPageId());

		Assert.assertEquals(
			WorkflowConstants.STATUS_IN_TRASH, wikiPage.getStatus());
	}

	@Test
	public void testMoveToTrashWikiPageWithoutAttachments() throws Exception {
		addWikiPage();

		WikiPageLocalServiceUtil.movePageToTrash(
			TestPropsValues.getUserId(), _page);

		WikiPage wikiPage =
			WikiPageLocalServiceUtil.getWikiPage(_page.getPageId());

		Assert.assertEquals(
			WorkflowConstants.STATUS_IN_TRASH, wikiPage.getStatus());
	}

	@Test
	public void testDeleteWikiPageWithAttachments() throws Exception {
		addWikiPageAttachment();

		int expectedWikiPageCount =
			(WikiPageLocalServiceUtil.getPagesCount(_node.getNodeId()) - 1);

		WikiPageLocalServiceUtil.deletePage(
			_page.getNodeId(), _page.getTitle());

		int finalWikiPageCount = WikiPageLocalServiceUtil.getPagesCount(
			_node.getNodeId());

		Assert.assertEquals(expectedWikiPageCount, finalWikiPageCount);
	}

	@Test
	public void testDeleteWikiPageWithoutAttachments() throws Exception {
		addWikiPage();

		int expectedWikiPageCount =
			(WikiPageLocalServiceUtil.getPagesCount(_node.getNodeId()) - 1);

		WikiPageLocalServiceUtil.deletePage(
			_page.getNodeId(), _page.getTitle());

		int finalWikiPageCount = WikiPageLocalServiceUtil.getPagesCount(
			_node.getNodeId());

		Assert.assertEquals(expectedWikiPageCount, finalWikiPageCount);
	}


	@Test
	public void testCountDLFoldersWhenDeletingWikiPageWithAttachments()
		throws Exception {

		int initialFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		addWikiPageAttachment();

		int firstFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		int newRepositoryFolder = 1;
		int newNodeFolder = 1;
		int newPageFolder = 1;

		int newFolders = newRepositoryFolder + newNodeFolder + newPageFolder;

		int expectedFolders = initialFolderCount + newFolders;

		Assert.assertEquals(expectedFolders, firstFolderCount);

		WikiPageLocalServiceUtil.deletePage(
			_page.getNodeId(), _page.getTitle());

		int finalFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		Assert.assertEquals(
			expectedFolders - newPageFolder, finalFolderCount);
	}

	@Test
	public void testCountDLFoldersWhenDeletingWikiPageWithoutAttachments()
		throws Exception {

		int intialFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		addWikiPage();

		int firstFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		Assert.assertEquals(intialFolderCount, firstFolderCount);

		WikiPageLocalServiceUtil.deletePage(
			_page.getNodeId(), _page.getTitle());

		int finalFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		Assert.assertEquals(intialFolderCount, finalFolderCount);
	}

	public void testExportWikiPageWithAttachments() throws Exception {
		//TODO
	}

	public void testExportWikiPageWithoutAttachments() throws Exception {
	 	//TODO
	}

	public void testImportWikiPageWithAttachments() throws Exception {
		//TODO
	}

	public void testImportWikiPageWithoutAttachments() throws Exception {
		//TODO
	}

	public void testCountDLFoldersWhenImportingWikiPageWithoutAttachments()
		throws Exception {

		//TODO
	}

	protected void addWikiNode() throws Exception {
		if (_group == null) {
			_group = GroupTestUtil.addGroup();
		}

		_node = WikiTestUtil.addNode(
			TestPropsValues.getUserId(), _group.getGroupId(),
			ServiceTestUtil.randomString(), ServiceTestUtil.randomString(50));
	}

	protected void addWikiPage() throws Exception {
		if (_node == null) {
			addWikiNode();
		}

		_page =  WikiTestUtil.addPage(
			_node.getUserId(), _group.getGroupId(), _node.getNodeId(),
			ServiceTestUtil.randomString(), true);
	}

	protected void addWikiPageAttachment() throws Exception {
		if (_page == null) {
			addWikiPage();
		}

		WikiTestUtil.addWikiAttachment(
			_page.getUserId(), _page.getNodeId(), _page.getTitle(), getClass());
	}

	protected int assertAttachFile(int expectedFolderCount) throws Exception {
		addWikiPageAttachment();

		int currentFolderCount = DLFolderLocalServiceUtil.getDLFoldersCount();

		Assert.assertEquals(expectedFolderCount, currentFolderCount );

		return currentFolderCount;
	}

	protected int assertAttachFileToPage1InNode1FromGroup1(
			int expectedFolderCount)
		throws Exception {

		return assertAttachFile(expectedFolderCount);
	}

	protected int assertAttachFileToPage1InNode1FromGroup2(
			int expectedFolderCount)
		throws Exception {

		_group = null;
		_page = null;
		_node = null;

		return assertAttachFile(expectedFolderCount);
	}

	protected int assertAttachFileToPage2InNode1FromGroup1(
			int expectedFolderCount)
		throws Exception {

		_page = null;

		return assertAttachFile(expectedFolderCount);
	}

	protected int assertAttachFileToPage2InNode2FromGroup1(
			int expectedFolderCount)
		throws Exception {

		_page = null;
		_node = null;

		return assertAttachFile(expectedFolderCount);
	}

	private Group _group;
	private WikiNode _node;
	private WikiPage _page;

}