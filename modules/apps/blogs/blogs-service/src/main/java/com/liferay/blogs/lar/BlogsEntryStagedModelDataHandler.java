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

package com.liferay.blogs.lar;

import com.liferay.blogs.exportimport.content.processor.BlogsEntryExportImportContentProcessor;
import com.liferay.exportimport.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.taglib.ui.ImageSelector;
import com.liferay.portal.kernel.trash.TrashHandler;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Image;
import com.liferay.portal.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.service.ImageLocalService;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalService;
import com.liferay.portlet.documentlibrary.lar.FileEntryUtil;
import com.liferay.portlet.exportimport.lar.ExportImportPathUtil;
import com.liferay.portlet.exportimport.lar.PortletDataContext;
import com.liferay.portlet.exportimport.lar.StagedModelDataHandler;
import com.liferay.portlet.exportimport.lar.StagedModelDataHandlerUtil;
import com.liferay.portlet.exportimport.lar.StagedModelModifiedDateComparator;

import java.io.InputStream;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Zsolt Berentey
 */
@Component(immediate = true, service = StagedModelDataHandler.class)
public class BlogsEntryStagedModelDataHandler
	extends BaseStagedModelDataHandler<BlogsEntry> {

	public static final String[] CLASS_NAMES = {BlogsEntry.class.getName()};

	@Override
	public void deleteStagedModel(BlogsEntry entry) throws PortalException {
		_blogsEntryLocalService.deleteEntry(entry);
	}

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException {

		BlogsEntry entry = fetchStagedModelByUuidAndGroupId(uuid, groupId);

		if (entry != null) {
			deleteStagedModel(entry);
		}
	}

	@Override
	public BlogsEntry fetchStagedModelByUuidAndGroupId(
		String uuid, long groupId) {

		return _blogsEntryLocalService.fetchBlogsEntryByUuidAndGroupId(
			uuid, groupId);
	}

	@Override
	public List<BlogsEntry> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		return _blogsEntryLocalService.getBlogsEntriesByUuidAndCompanyId(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new StagedModelModifiedDateComparator<BlogsEntry>());
	}

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	public String getDisplayName(BlogsEntry entry) {
		return entry.getTitle();
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, BlogsEntry entry)
		throws Exception {

		Element entryElement = portletDataContext.getExportDataElement(entry);

		if (entry.getCoverImageFileEntryId() != 0) {
			FileEntry fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getCoverImageFileEntryId());

			StagedModelDataHandlerUtil.exportReferenceStagedModel(
					portletDataContext, entry, fileEntry,
					PortletDataContext.REFERENCE_TYPE_WEAK);
		}

		if (entry.isSmallImage()) {
			Image smallImage = _imageLocalService.fetchImage(
				entry.getSmallImageId());

			if (Validator.isNotNull(entry.getSmallImageURL())) {
				String smallImageURL =
					_blogsEntryExportImportContentProcessor.
						replaceExportContentReferences(
							portletDataContext, entry,
							entry.getSmallImageURL() + StringPool.SPACE, true,
							true);

				entry.setSmallImageURL(smallImageURL);
			}
			else if (smallImage != null) {
				String smallImagePath = ExportImportPathUtil.getModelPath(
					entry,
					smallImage.getImageId() + StringPool.PERIOD +
						smallImage.getType());

				entryElement.addAttribute("small-image-path", smallImagePath);

				entry.setSmallImageType(smallImage.getType());

				portletDataContext.addZipEntry(
					smallImagePath, smallImage.getTextObj());
			}
		}

		if (entry.getSmallImageFileEntryId() != 0) {
			FileEntry fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(
				entry.getSmallImageFileEntryId());

			StagedModelDataHandlerUtil.exportReferenceStagedModel(
				portletDataContext, entry, fileEntry,
				PortletDataContext.REFERENCE_TYPE_WEAK);
		}

		String content =
			_blogsEntryExportImportContentProcessor.
				replaceExportContentReferences(
					portletDataContext, entry, entry.getContent(),
					portletDataContext.getBooleanParameter(
						"blogs", "referenced-content"),
					true);

		entry.setContent(content);

		portletDataContext.addClassedModel(
			entryElement, ExportImportPathUtil.getModelPath(entry), entry);
	}

	@Override
	protected void doImportMissingReference(
			PortletDataContext portletDataContext, String uuid, long groupId,
			long entryId)
		throws Exception {

		BlogsEntry existingEntry = fetchMissingReference(uuid, groupId);

		if (existingEntry == null) {
			return;
		}

		Map<Long, Long> entryIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				BlogsEntry.class);

		entryIds.put(entryId, existingEntry.getEntryId());
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, BlogsEntry entry)
		throws Exception {

		long userId = portletDataContext.getUserId(entry.getUserUuid());

		Element entryElement =
			portletDataContext.getImportDataStagedModelElement(entry);

		String content =
			_blogsEntryExportImportContentProcessor.
				replaceImportContentReferences(
					portletDataContext, entry, entry.getContent());

		entry.setContent(content);

		Calendar displayDateCal = CalendarFactoryUtil.getCalendar();

		displayDateCal.setTime(entry.getDisplayDate());

		int displayDateMonth = displayDateCal.get(Calendar.MONTH);
		int displayDateDay = displayDateCal.get(Calendar.DATE);
		int displayDateYear = displayDateCal.get(Calendar.YEAR);
		int displayDateHour = displayDateCal.get(Calendar.HOUR);
		int displayDateMinute = displayDateCal.get(Calendar.MINUTE);

		if (displayDateCal.get(Calendar.AM_PM) == Calendar.PM) {
			displayDateHour += 12;
		}

		boolean allowPingbacks = entry.isAllowPingbacks();
		boolean allowTrackbacks = entry.isAllowTrackbacks();
		String[] trackbacks = StringUtil.split(entry.getTrackbacks());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			entry);

		Map<Long, Long> fileEntryIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				FileEntry.class);

		ImageSelector coverImageImageSelector = null;

		long coverImageFileEntryId = MapUtil.getLong(
			fileEntryIds, entry.getCoverImageFileEntryId());

		String coverImageURL = null;

		if (Validator.isNotNull(entry.getCoverImageURL())) {
			coverImageURL =
				_blogsEntryExportImportContentProcessor.
					replaceImportContentReferences(
						portletDataContext, entry, entry.getCoverImageURL());
		}

		if ((coverImageFileEntryId != 0) ||
			Validator.isNotNull(coverImageURL)) {

			coverImageImageSelector = new ImageSelector(
				coverImageFileEntryId, coverImageURL, null);
		}

		ImageSelector smallImageImageSelector = null;

		long smallImageFileEntryId = 0;
		String smallImageURL = null;

		if (entry.isSmallImage()) {
			if (Validator.isNotNull(entry.getSmallImageURL())) {
				smallImageURL =
					_blogsEntryExportImportContentProcessor.
						replaceImportContentReferences(
							portletDataContext, entry,
							entry.getSmallImageURL());
			}

			if (fileEntryIds.containsKey(entry.getSmallImageId())) {
				smallImageFileEntryId = MapUtil.getLong(
					fileEntryIds, entry.getSmallImageId());

				smallImageImageSelector = new ImageSelector(
					smallImageFileEntryId, smallImageURL, null);
			}
			else {
				String smallImagePath = entryElement.attributeValue(
					"small-image-path");

				if (Validator.isNotNull(smallImagePath)) {
					String smallImageFileName =
						entry.getSmallImageId() + StringPool.PERIOD +
							entry.getSmallImageType();

					byte[] bytes = FileUtil.getBytes(
						portletDataContext.getZipEntryAsInputStream(
							smallImagePath));

					smallImageImageSelector = new ImageSelector(
						bytes, smallImageFileName, entry.getSmallImageType(),
						smallImageURL, null);
				}
			}
		}

		BlogsEntry importedEntry = null;

		if (portletDataContext.isDataStrategyMirror()) {
			serviceContext.setAttribute("urlTitle", entry.getUrlTitle());

			BlogsEntry existingEntry = fetchStagedModelByUuidAndGroupId(
				entry.getUuid(), portletDataContext.getScopeGroupId());

			if (existingEntry == null) {
				serviceContext.setUuid(entry.getUuid());

				importedEntry = _blogsEntryLocalService.addEntry(
					userId, entry.getTitle(), entry.getSubtitle(),
					entry.getDescription(), entry.getContent(),
					displayDateMonth, displayDateDay, displayDateYear,
					displayDateHour, displayDateMinute, allowPingbacks,
					allowTrackbacks, trackbacks, entry.getCoverImageCaption(),
					coverImageImageSelector, smallImageImageSelector,
					serviceContext);
			}
			else {
				importedEntry = _blogsEntryLocalService.updateEntry(
					userId, existingEntry.getEntryId(), entry.getTitle(),
					entry.getSubtitle(), entry.getDescription(),
					entry.getContent(), displayDateMonth, displayDateDay,
					displayDateYear, displayDateHour, displayDateMinute,
					allowPingbacks, allowTrackbacks, trackbacks,
					entry.getCoverImageCaption(), coverImageImageSelector,
					smallImageImageSelector, serviceContext);
			}
		}
		else {
			importedEntry = _blogsEntryLocalService.addEntry(
				userId, entry.getTitle(), entry.getSubtitle(),
				entry.getDescription(), entry.getContent(), displayDateMonth,
				displayDateDay, displayDateYear, displayDateHour,
				displayDateMinute, allowPingbacks, allowTrackbacks, trackbacks,
				entry.getCoverImageCaption(), coverImageImageSelector,
				smallImageImageSelector, serviceContext);
		}

		portletDataContext.importClassedModel(entry, importedEntry);
	}

	@Override
	protected void doRestoreStagedModel(
			PortletDataContext portletDataContext, BlogsEntry entry)
		throws Exception {

		long userId = portletDataContext.getUserId(entry.getUserUuid());

		BlogsEntry existingEntry = fetchStagedModelByUuidAndGroupId(
			entry.getUuid(), portletDataContext.getScopeGroupId());

		if ((existingEntry == null) || !existingEntry.isInTrash()) {
			return;
		}

		TrashHandler trashHandler = existingEntry.getTrashHandler();

		if (trashHandler.isRestorable(existingEntry.getEntryId())) {
			trashHandler.restoreTrashEntry(userId, existingEntry.getEntryId());
		}
	}

	protected InputStream getSmallImageInputStream(
		PortletDataContext portletDataContext, Element attachmentElement) {

		InputStream inputStream = null;

		String path = attachmentElement.attributeValue("path");

		FileEntry fileEntry = (FileEntry)portletDataContext.getZipEntryAsObject(
			path);

		String binPath = attachmentElement.attributeValue("bin-path");

		if (Validator.isNull(binPath) &&
			portletDataContext.isPerformDirectBinaryImport()) {

			try {
				inputStream = FileEntryUtil.getContentStream(fileEntry);
			}
			catch (Exception e) {
			}
		}
		else {
			inputStream = portletDataContext.getZipEntryAsInputStream(binPath);
		}

		if (inputStream == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to import small image file entry " +
						fileEntry.getFileEntryId());
			}
		}

		return inputStream;
	}

	@Reference(unbind = "-")
	protected void setBlogsEntryExportImportContentProcessor(
		BlogsEntryExportImportContentProcessor
			blogsEntryExportImportContentProcessor) {

		_blogsEntryExportImportContentProcessor =
			blogsEntryExportImportContentProcessor;
	}

	@Reference(unbind = "-")
	protected void setBlogsEntryLocalService(
		BlogsEntryLocalService blogsEntryLocalService) {

		_blogsEntryLocalService = blogsEntryLocalService;
	}

	@Reference(unbind = "-")
	protected void setImageLocalService(ImageLocalService imageLocalService) {
		_imageLocalService = imageLocalService;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BlogsEntryStagedModelDataHandler.class);

	private volatile BlogsEntryExportImportContentProcessor
		_blogsEntryExportImportContentProcessor;
	private volatile BlogsEntryLocalService _blogsEntryLocalService;
	private volatile ImageLocalService _imageLocalService;

}