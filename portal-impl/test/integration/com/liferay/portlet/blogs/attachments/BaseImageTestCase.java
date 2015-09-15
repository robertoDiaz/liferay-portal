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

package com.liferay.portlet.blogs.attachments;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.TempFileEntryUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Repository;
import com.liferay.portal.model.User;
import com.liferay.portal.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.service.RepositoryLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.blogs.constants.BlogsConstants;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public abstract class BaseImageTestCase {

	@Test
	public void testAddImage() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		Assert.assertEquals("image.jpg", imageFileEntry.getTitle());
	}

	@Test
	public void testAddOriginalImage() throws Exception {
		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			user.getUserId(), group.getGroupId());

		int initialFolderFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folder.getFolderId());

		addBlogsEntry("image.jpg");

		int finalPortletFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folder.getFolderId());

		Assert.assertEquals(
			initialFolderFileEntriesCount + 1, finalPortletFileEntriesCount);

		PortletFileRepositoryUtil.getPortletFileEntry(
			group.getGroupId(), folder.getFolderId(), "image.jpg");
	}

	@Test
	public void testAddOriginalImageWhenUpdatingBlogEntry() throws Exception {
		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			user.getUserId(), group.getGroupId());

		int initialFolderFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folder.getFolderId());

		BlogsEntry entry = addBlogsEntry("image.jpg");

		updateBlogsEntry(entry.getEntryId(), "image2.jpg");

		int finalPortletFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folder.getFolderId());

		Assert.assertEquals(
			initialFolderFileEntriesCount + 2, finalPortletFileEntriesCount);

		PortletFileRepositoryUtil.getPortletFileEntry(
			group.getGroupId(), folder.getFolderId(), "image2.jpg");
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testImageDeletedWhenDeletingEntry() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		BlogsEntryLocalServiceUtil.deleteEntry(entry);

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testImageDeletedWhenEmptyImageSelector() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		ImageSelector imageSelector = new ImageSelector(
			0, StringPool.BLANK, StringPool.BLANK);

		entry = updateBlogsEntry(entry.getEntryId(), imageSelector);

		Assert.assertEquals(0, getFileEntryId(entry));

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test
	public void testImageNotChangedWhenNullImageSelector() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		ImageSelector imageSelector = null;

		entry = updateBlogsEntry(entry.getEntryId(), imageSelector);

		Assert.assertEquals(
			imageFileEntry.getFileEntryId(), getFileEntryId(entry));

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			user.getUserId(), group.getGroupId());

		PortletFileRepositoryUtil.getPortletFileEntry(
			group.getGroupId(), folder.getFolderId(), "image.jpg");

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test
	public void testImageStoredInBlogsRepository() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		Repository repository = RepositoryLocalServiceUtil.getRepository(
			imageFileEntry.getRepositoryId());

		Assert.assertEquals(BlogsConstants.SERVICE_NAME, repository.getName());
	}

	@Test
	public void testImageStoredInImageFolder() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		Folder imageFolder = imageFileEntry.getFolder();

		Assert.assertNotEquals(
			BlogsConstants.SERVICE_NAME, imageFolder.getName());
	}

	@Test
	public void testOriginalImageNotDeletedWhenEmptyImageSelector()
		throws Exception {

		BlogsEntry entry = addBlogsEntry("image.jpg");

		ImageSelector imageSelector = new ImageSelector(
			0, StringPool.BLANK, StringPool.BLANK);

		updateBlogsEntry(entry.getEntryId(), imageSelector);

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			user.getUserId(), group.getGroupId());

		PortletFileRepositoryUtil.getPortletFileEntry(
			group.getGroupId(), folder.getFolderId(), "image.jpg");
	}

	@Test
	public void testOriginalImageNotDeletedWhenNullImageSelector()
		throws Exception {

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			user.getUserId(), group.getGroupId());

		int initialFolderFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folder.getFolderId());

		BlogsEntry entry = addBlogsEntry("image.jpg");

		ImageSelector imageSelector = null;

		updateBlogsEntry(entry.getEntryId(), imageSelector);

		int finalPortletFileEntriesCount =
			PortletFileRepositoryUtil.getPortletFileEntriesCount(
				group.getGroupId(), folder.getFolderId());

		Assert.assertEquals(
			initialFolderFileEntriesCount + 1, finalPortletFileEntriesCount);

		PortletFileRepositoryUtil.getPortletFileEntry(
			group.getGroupId(), folder.getFolderId(), "image.jpg");
	}

	@Test
	public void testOriginalImageStoredInBlogsRepository() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		Repository repository = RepositoryLocalServiceUtil.getRepository(
			imageFileEntry.getRepositoryId());

		Assert.assertEquals(BlogsConstants.SERVICE_NAME, repository.getName());
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testPreviousImageDeletedWhenChangingImage() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		updateBlogsEntry(entry.getEntryId(), "image2.jpg");

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test
	public void testPreviousOriginalImageNotDeletedWhenChangingImage()
		throws Exception {

		BlogsEntry entry = addBlogsEntry("image.jpg");

		updateBlogsEntry(entry.getEntryId(), "image2.jpg");

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			user.getUserId(), group.getGroupId());

		PortletFileRepositoryUtil.getPortletFileEntry(
			group.getGroupId(), folder.getFolderId(), "image.jpg");
	}

	@Test
	public void testUpdateImage() throws Exception {
		BlogsEntry entry = addBlogsEntry("image.jpg");

		entry = updateBlogsEntry(entry.getEntryId(), "image2.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getFileEntryId(entry));

		Assert.assertEquals("image2.jpg", imageFileEntry.getTitle());
	}

	protected abstract BlogsEntry addBlogsEntry(String ImageTitle)
		throws Exception;

	protected abstract long getFileEntryId(BlogsEntry entry);

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

	protected abstract BlogsEntry updateBlogsEntry(
			long entryId, ImageSelector imageSelector)
		throws Exception;

	protected abstract BlogsEntry updateBlogsEntry(
			long entryId, String imageTitle)
		throws Exception;

	@DeleteAfterTestRun
	protected Group group;

	protected User user;

}