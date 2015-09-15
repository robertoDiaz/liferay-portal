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

package com.liferay.portlet.blogs.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Repository;
import com.liferay.portal.model.User;
import com.liferay.portal.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.service.RepositoryLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.MainServletTestRule;
import com.liferay.portlet.blogs.constants.BlogsConstants;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;

import java.io.InputStream;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public class SmallImageTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), MainServletTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
		_user = TestPropsValues.getUser();
	}

	@Test
	public void testAddOriginalSmallImage() throws Exception {
		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			_user.getUserId(), _group.getGroupId());

		int initialFolderFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				_group.getGroupId(), folder.getFolderId());

		addBlogsEntry("image.jpg");

		int finalPortletFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				_group.getGroupId(), folder.getFolderId());

		Assert.assertEquals(
			initialFolderFileEntriesCount + 1, finalPortletFileEntriesCount);

		PortletFileRepositoryUtil.getPortletFileEntry(
			_group.getGroupId(), folder.getFolderId(), "image.jpg");
	}

	@Test
	public void testAddOriginalSmallImageWhenUpdatingBlogEntry()
		throws Exception {

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			_user.getUserId(), _group.getGroupId());

		int initialFolderFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				_group.getGroupId(), folder.getFolderId());

		BlogsEntry entry = addBlogsEntry("image.jpg");

		updateBlogsEntry(entry.getEntryId(), "image2.jpg");

		int finalPortletFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				_group.getGroupId(), folder.getFolderId());

		Assert.assertEquals(
			initialFolderFileEntriesCount + 2, finalPortletFileEntriesCount);

		PortletFileRepositoryUtil.getPortletFileEntry(
			_group.getGroupId(), folder.getFolderId(), "image2.jpg");
	}

	@Test
	public void testAddSmallImage() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		Assert.assertEquals("image.jpg", smallImageFileEntry.getTitle());
	}

	@Test
	public void testOriginalSmallImageNotDeletedWhenEmptySmallImageSelector()
		throws Exception {

		BlogsEntry entry = addBlogsEntry("image.jpg");

		ImageSelector smallImageSelector = new ImageSelector(0);

		updateBlogsEntry(entry.getEntryId(), smallImageSelector);

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			_user.getUserId(), _group.getGroupId());

		PortletFileRepositoryUtil.getPortletFileEntry(
			_group.getGroupId(), folder.getFolderId(), "image.jpg");
	}

	@Test
	public void testOriginalSmallImageNotDeletedWhenNullSmallImageSelector()
		throws Exception {

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			_user.getUserId(), _group.getGroupId());

		int initialFolderFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				_group.getGroupId(), folder.getFolderId());

		BlogsEntry entry = addBlogsEntry("image.jpg");

		ImageSelector smallImageSelector = null;

		updateBlogsEntry(entry.getEntryId(), smallImageSelector);

		int finalPortletFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				_group.getGroupId(), folder.getFolderId());

		Assert.assertEquals(
			initialFolderFileEntriesCount + 1, finalPortletFileEntriesCount);

		PortletFileRepositoryUtil.getPortletFileEntry(
			_group.getGroupId(), folder.getFolderId(), "image.jpg");
	}

	@Test
	public void testOriginalSmallImageStoredInBlogsRepository()
		throws Exception {

		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		Repository repository = RepositoryLocalServiceUtil.getRepository(
			smallImageFileEntry.getRepositoryId());

		Assert.assertEquals(BlogsConstants.SERVICE_NAME, repository.getName());
	}

	@Test
	public void testPreviousOriginalSmallImageNotDeletedWhenChangingSmallImage()
		throws Exception {

		BlogsEntry entry = addBlogsEntry("image.jpg");

		updateBlogsEntry(entry.getEntryId(), "image2.jpg");

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			_user.getUserId(), _group.getGroupId());

		PortletFileRepositoryUtil.getPortletFileEntry(
			_group.getGroupId(), folder.getFolderId(), "image.jpg");
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testPreviousSmallImageDeletedWhenChangingSmallImage()
		throws Exception {

		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		updateBlogsEntry(entry.getEntryId(), "image2.jpg");

		PortletFileRepositoryUtil.getPortletFileEntry(
			smallImageFileEntry.getFileEntryId());
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testSmallImageDeletedWhenDeletingEntry() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		BlogsEntryLocalServiceUtil.deleteEntry(entry);

		PortletFileRepositoryUtil.getPortletFileEntry(
			smallImageFileEntry.getFileEntryId());
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testSmallImageDeletedWhenEmptySmallImageSelector()
		throws Exception {

		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		ImageSelector smallImageSelector = new ImageSelector(0);

		entry = updateBlogsEntry(entry.getEntryId(), smallImageSelector);

		Assert.assertEquals(0, entry.getSmallImageFileEntryId());

		PortletFileRepositoryUtil.getPortletFileEntry(
			smallImageFileEntry.getFileEntryId());
	}

	@Test
	public void testSmallImageNotChangedWhenNullSmallImageSelector()
		throws Exception {

		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		ImageSelector smallImageSelector = null;

		entry = updateBlogsEntry(entry.getEntryId(), smallImageSelector);

		Assert.assertEquals(
			smallImageFileEntry.getFileEntryId(),
			entry.getSmallImageFileEntryId());

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			_user.getUserId(), _group.getGroupId());

		PortletFileRepositoryUtil.getPortletFileEntry(
			_group.getGroupId(), folder.getFolderId(), "image.jpg");

		PortletFileRepositoryUtil.getPortletFileEntry(
			smallImageFileEntry.getFileEntryId());
	}

	@Test
	public void testSmallImageStoredInBlogsRepository() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		Repository repository = RepositoryLocalServiceUtil.getRepository(
			smallImageFileEntry.getRepositoryId());

		Assert.assertEquals(BlogsConstants.SERVICE_NAME, repository.getName());
	}

	@Test
	public void testSmallImageStoredInSmallImageFolder() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		Folder smallImageFolder = smallImageFileEntry.getFolder();

		Assert.assertNotEquals(
			BlogsConstants.SERVICE_NAME, smallImageFolder.getName());
	}

	@Test
	public void testUpdateSmallImage() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		entry = updateBlogsEntry(entry.getEntryId(), "image2.jpg");

		FileEntry smallImageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

		Assert.assertEquals("image2.jpg", smallImageFileEntry.getTitle());
	}

	protected BlogsEntry addBlogsEntry(String smallImageTitle)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId());

		FileEntry fileEntry = getTempFileEntry(
			_user.getUserId(), smallImageTitle, serviceContext);

		ImageSelector coverImageSelector = null;
		ImageSelector smallImageSelector = new ImageSelector(
			fileEntry.getFileEntryId());

		return BlogsEntryLocalServiceUtil.addEntry(
			_user.getUserId(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), new Date(), true, true,
			new String[0], StringPool.BLANK, coverImageSelector,
			smallImageSelector, serviceContext);
	}

	protected FileEntry getTempFileEntry(
			long userId, String title, ServiceContext serviceContext)
		throws PortalException {

		ClassLoader classLoader = getClass().getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"com/liferay/portal/util/dependencies/test.jpg");

		return TempFileEntryUtil.addTempFileEntry(
			serviceContext.getScopeGroupId(), userId,
			BlogsEntry.class.getName(), title, inputStream,
			MimeTypesUtil.getContentType(title));
	}

	protected BlogsEntry updateBlogsEntry(
			long entryId, ImageSelector smallImageSelector)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId());

		ImageSelector coverImageSelector = null;

		return BlogsEntryLocalServiceUtil.updateEntry(
			_user.getUserId(), entryId, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), new Date(), true, true,
			new String[0], StringPool.BLANK, coverImageSelector,
			smallImageSelector, serviceContext);
	}

	protected BlogsEntry updateBlogsEntry(long entryId, String smallImageTitle)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _user.getUserId());

		FileEntry fileEntry = getTempFileEntry(
			_user.getUserId(), smallImageTitle, serviceContext);

		ImageSelector smallImageSelector = new ImageSelector(
			fileEntry.getFileEntryId());

		return updateBlogsEntry(entryId, smallImageSelector);
	}

	@DeleteAfterTestRun
	private Group _group;

	private User _user;

}