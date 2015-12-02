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
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
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
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Roberto Díaz
 */
public abstract class BaseBlogsEntryImageTestCase {

	@Test
	public void testAddImageFromBytes() throws Exception {
		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Assert.assertEquals("image1.jpg", imageFileEntry.getTitle());
	}

	@Test
	public void testAddImageFromIds() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
				user.getUserId(), "image1.jpg", serviceContext);

		ImageSelector imageSelector = new ImageSelector(
			initialFileEntry.getFileEntryId(), StringPool.BLANK,
			StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Assert.assertEquals(initialFileEntry, imageFileEntry);
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testImageFromBytesDeletedWhenDeletingBlogsEntry()
		throws Exception {

		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		BlogsEntryLocalServiceUtil.deleteEntry(blogsEntry);

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testImageFromBytesDeletedWhenUpdatingBlogsEntryWithEmptyImageSelector()
		throws Exception {

		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		ImageSelector imageSelector = new ImageSelector(
			0, StringPool.BLANK, StringPool.BLANK);

		blogsEntry = updateBlogsEntry(blogsEntry.getEntryId(), imageSelector);

		Assert.assertEquals(0, getImageFileEntry(blogsEntry));

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test
	public void testImageFromBytesNotUpdatedWhenUpdatingBlogsEntryWithNullImageSelector()
		throws Exception {

		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		ImageSelector imageSelector = null;

		blogsEntry = updateBlogsEntry(blogsEntry.getEntryId(), imageSelector);

		Assert.assertEquals(
			imageFileEntry.getFileEntryId(), getImageFileEntry(blogsEntry));

		Folder folder = BlogsEntryLocalServiceUtil.addAttachmentsFolder(
			user.getUserId(), group.getGroupId());

		PortletFileRepositoryUtil.getPortletFileEntry(
			group.getGroupId(), folder.getFolderId(), "image1.jpg");

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test
	public void testImageFromBytesStoredInBlogsRepository() throws Exception {
		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Repository repository = RepositoryLocalServiceUtil.getRepository(
			imageFileEntry.getRepositoryId());

		Assert.assertEquals(BlogsConstants.SERVICE_NAME, repository.getName());
	}

	@Test
	public void testImageFromBytesStoredInInvisibleImageFolder()
		throws Exception {

		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Folder imageFolder = imageFileEntry.getFolder();

		Assert.assertNotEquals(
			BlogsConstants.SERVICE_NAME, imageFolder.getName());
	}

	@Test
	public void testImageFromIdNotDeletedWhenDeletingBlogsEntry()
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
				user.getUserId(), "image1.jpg", serviceContext);

		ImageSelector imageSelector = new ImageSelector(
			initialFileEntry.getFileEntryId(), StringPool.BLANK,
			StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		BlogsEntryLocalServiceUtil.deleteEntry(blogsEntry);

		PortletFileRepositoryUtil.getPortletFileEntry(
				imageFileEntry.getFileEntryId());
	}

	@Test
	public void testImageFromIdNotDeletedWhenUpdatingBlogsEntryWithEmptyImageSelector()
		throws Exception {

		ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(
						group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
				user.getUserId(), "image1.jpg", serviceContext);

		ImageSelector imageSelector = new ImageSelector(
				initialFileEntry.getFileEntryId(), StringPool.BLANK,
				StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		imageSelector = new ImageSelector(
			0, StringPool.BLANK, StringPool.BLANK);

		blogsEntry = updateBlogsEntry(blogsEntry.getEntryId(), imageSelector);

		Assert.assertEquals(0, getImageFileEntry(blogsEntry));

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test
	public void testImageFromIdNotUpdatedWhenUpdatingBlogsEntryWithNullImageSelector()
		throws Exception {

		ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(
						group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
				user.getUserId(), "image1.jpg", serviceContext);

		ImageSelector imageSelector = new ImageSelector(
				initialFileEntry.getFileEntryId(), StringPool.BLANK,
				StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		blogsEntry = updateBlogsEntry(blogsEntry.getEntryId(), null);

		Assert.assertEquals(
			imageFileEntry.getFileEntryId(), getImageFileEntry(blogsEntry));
	}

	@Test
	public void testImageFromIdRemainsSameRepository() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
			user.getUserId(), "image1.jpg", serviceContext);

		Repository initialRepository = RepositoryLocalServiceUtil.getRepository(
			initialFileEntry.getRepositoryId());

		ImageSelector imageSelector = new ImageSelector(
			initialFileEntry.getFileEntryId(), StringPool.BLANK,
			StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Repository repository = RepositoryLocalServiceUtil.getRepository(
			imageFileEntry.getRepositoryId());

		Assert.assertEquals(initialRepository.getName(), repository.getName());
	}

	@Test
	public void testImageFromIdRemainsInSameFolder() throws Exception {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
			user.getUserId(), "image1.jpg", serviceContext);

		Folder initalFolder = PortletFileRepositoryUtil.getPortletFolder(
			initialFileEntry.getFolderId());

		ImageSelector imageSelector = new ImageSelector(
			initialFileEntry.getFileEntryId(), StringPool.BLANK,
			StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Folder imageFolder = imageFileEntry.getFolder();

		Assert.assertNotEquals(
			initalFolder.getName(), imageFolder.getName());
	}

	@Test(expected = NoSuchFileEntryException.class)
	public void testPreviousImageFromBytesDeletedWhenUpdatingImage()
		throws Exception {

		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		updateBlogsEntryPassingImageSelectorWithBytes(
			blogsEntry.getEntryId(), "image2.jpg");

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test
	public void testPreviousImageFromIdNotDeletedWhenUpdatingImage()
		throws Exception {

		ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(
						group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
				user.getUserId(), "image1.jpg", serviceContext);

		ImageSelector imageSelector = new ImageSelector(
				initialFileEntry.getFileEntryId(), StringPool.BLANK,
				StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		updateBlogsEntryPassingImageSelectorWithBytes(
			blogsEntry.getEntryId(), "image2.jpg");

		PortletFileRepositoryUtil.getPortletFileEntry(
			imageFileEntry.getFileEntryId());
	}

	@Test
	public void testUpdateImageFromBytesWithImageFromBytes() throws Exception {
		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		blogsEntry = updateBlogsEntryPassingImageSelectorWithBytes(
			blogsEntry.getEntryId(), "image2.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Assert.assertEquals("image2.jpg", imageFileEntry.getTitle());
	}

	@Test
	public void testUpdateImageFromBytesWithImageFromId() throws Exception {
		BlogsEntry blogsEntry = addBlogsEntryPassingImageSelectorWithBytes(
			"image1.jpg");

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group.getGroupId(), user.getUserId());

		FileEntry fileEntry = getTempFileEntry(
			user.getUserId(), "image2.jpg", serviceContext);

		ImageSelector imageSelector = new ImageSelector(
			fileEntry.getFileEntryId(), StringPool.BLANK, getImageCropRegion());

		blogsEntry = updateBlogsEntry(blogsEntry.getEntryId(), imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Assert.assertEquals("image2.jpg", imageFileEntry.getTitle());
	}

	@Test
	public void testUpdateImageFromIdWithImageFromBytes() throws Exception {
		ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(
						group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
				user.getUserId(), "image1.jpg", serviceContext);

		ImageSelector imageSelector = new ImageSelector(
				initialFileEntry.getFileEntryId(), StringPool.BLANK,
				StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		blogsEntry = updateBlogsEntryPassingImageSelectorWithBytes(
			blogsEntry.getEntryId(), "image2.jpg");

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Assert.assertEquals("image2.jpg", imageFileEntry.getTitle());
	}

	@Test
	public void testUpdateImageFromIdWithImageFromId() throws Exception {
		ServiceContext serviceContext =
				ServiceContextTestUtil.getServiceContext(
						group.getGroupId(), user.getUserId());

		FileEntry initialFileEntry = getTempFileEntry(
				user.getUserId(), "image1.jpg", serviceContext);

		ImageSelector imageSelector = new ImageSelector(
				initialFileEntry.getFileEntryId(), StringPool.BLANK,
				StringPool.BLANK);

		BlogsEntry blogsEntry = addBlogsEntry(imageSelector);

		FileEntry fileEntry = getTempFileEntry(
				user.getUserId(), "image2.jpg", serviceContext);

		imageSelector = new ImageSelector(
			fileEntry.getFileEntryId(), StringPool.BLANK, getImageCropRegion());

		blogsEntry = updateBlogsEntry(blogsEntry.getEntryId(), imageSelector);

		FileEntry imageFileEntry =
			PortletFileRepositoryUtil.getPortletFileEntry(
				getImageFileEntry(blogsEntry));

		Assert.assertEquals("image2.jpg", imageFileEntry.getTitle());
	}

	protected abstract BlogsEntry addBlogsEntry(ImageSelector imageSelector)
		throws Exception;

	protected BlogsEntry addBlogsEntryPassingImageSelectorWithBytes(
			String imageTitle)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group.getGroupId(), user.getUserId());

		FileEntry fileEntry = getTempFileEntry(
			user.getUserId(), imageTitle, serviceContext);

		byte[] bytes = FileUtil.getBytes(fileEntry.getContentStream());

		ImageSelector imageSelector = new ImageSelector(
			bytes, fileEntry.getTitle(), fileEntry.getMimeType(),
			StringPool.BLANK, getImageCropRegion());

		return addBlogsEntry(imageSelector);
	}

	protected FileEntry getFileEntry(
			long userId, String title, ServiceContext serviceContext)
		throws PortalException {

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"com/liferay/portal/util/dependencies/test.jpg");

		return PortletFileRepositoryUtil.addPortletFileEntry(
			serviceContext.getScopeGroupId(), userId,
			BlogsEntry.class.getName(), 0, StringUtil.randomString(),
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, inputStream, title,
			MimeTypesUtil.getContentType(title), false);
	}

	protected abstract String getImageCropRegion();

	protected abstract long getImageFileEntry(BlogsEntry blogsEntry);

	protected FileEntry getTempFileEntry(
			long userId, String title, ServiceContext serviceContext)
		throws PortalException {

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream(
			"com/liferay/portal/util/dependencies/test.jpg");

		return TempFileEntryUtil.addTempFileEntry(
			serviceContext.getScopeGroupId(), userId,
			BlogsEntry.class.getName(), title, inputStream,
			MimeTypesUtil.getContentType(title));
	}

	protected abstract BlogsEntry updateBlogsEntry(
			long blogsEntryId, ImageSelector imageSelector)
		throws Exception;

	protected BlogsEntry updateBlogsEntryPassingImageSelectorWithBytes(
			long blogsEntryId, String coverImageTitle)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group.getGroupId(), user.getUserId());

		FileEntry fileEntry = getTempFileEntry(
			user.getUserId(), coverImageTitle, serviceContext);

		ImageSelector imageSelector = new ImageSelector(
			fileEntry.getFileEntryId(), StringPool.BLANK, StringPool.BLANK);

		return updateBlogsEntry(blogsEntryId, imageSelector);
	}

	@DeleteAfterTestRun
	protected Group group;

	protected User user;

}