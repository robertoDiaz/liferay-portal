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

package com.liferay.item.selector.taglib;

import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.Base64ItemSelectorReturnType;
import com.liferay.item.selector.criteria.FileEntryItemSelectorReturnType;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;
import com.liferay.item.selector.criteria.UploadableFileReturnType;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.util.ClassUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.documentlibrary.util.AudioProcessorUtil;
import com.liferay.portlet.documentlibrary.util.DLUtil;
import com.liferay.portlet.documentlibrary.util.ImageProcessorUtil;
import com.liferay.portlet.documentlibrary.util.VideoProcessorUtil;

import java.util.Iterator;
import java.util.List;

/**
 * @author Sergio González
 * @author Roberto Díaz
 */
public class ItemSelectorBrowserReturnTypeUtil
	implements ItemSelectorReturnType {

	public static ItemSelectorReturnType
		getFirstAvailableDraggableFileReturnType(
			List<ItemSelectorReturnType> desiredItemSelectorReturnTypes) {

		return getFirstAvailableItemSelectorReturnType(
			desiredItemSelectorReturnTypes, _draggableFileReturnTypeNames);
	}

	public static ItemSelectorReturnType
		getFirstAvailableExistingFileEntryReturnType(
			List<ItemSelectorReturnType> desiredItemSelectorReturnTypes) {

		return getFirstAvailableItemSelectorReturnType(
			desiredItemSelectorReturnTypes, _existingFileEntryReturnTypeNames);
	}

	public static String getValue(
			ItemSelectorReturnType itemSelectorReturnType, FileEntry fileEntry,
			ThemeDisplay themeDisplay)
		throws Exception {

		String className = ClassUtil.getClassName(itemSelectorReturnType);

		if (className.equals(FileEntryItemSelectorReturnType.class.getName())) {
			return getFileEntryValue(fileEntry, themeDisplay);
		}
		else if (className.equals(URLItemSelectorReturnType.class.getName())) {
			return getURLValue(fileEntry, themeDisplay);
		}

		return StringPool.BLANK;
	}

	protected static String getFileEntryValue(
			FileEntry fileEntry, ThemeDisplay themeDisplay)
		throws Exception {

		JSONObject fileEntryJSONObject = JSONFactoryUtil.createJSONObject();

		fileEntryJSONObject.put("fileEntryId", fileEntry.getFileEntryId());
		fileEntryJSONObject.put("groupId", fileEntry.getGroupId());
		fileEntryJSONObject.put("title", fileEntry.getTitle());

		String[] previewFileURLs = getURLs(fileEntry, themeDisplay);

		fileEntryJSONObject.put("url", previewFileURLs[0]);
		fileEntryJSONObject.put("uuid", fileEntry.getUuid());

		return fileEntryJSONObject.toString();
	}

	protected static String[] getURLs(
			FileEntry fileEntry, ThemeDisplay themeDisplay)
		throws PortalException {

		FileVersion fileVersion = fileEntry.getLatestFileVersion(true);

		boolean hasAudio = AudioProcessorUtil.hasAudio(fileVersion);
		boolean hasImages = ImageProcessorUtil.hasImages(fileVersion);
		boolean hasVideo = VideoProcessorUtil.hasVideo(fileVersion);

		String[] previewFileURLs = null;
		String videoThumbnailURL = null;

		String previewQueryString = null;

		if (hasAudio) {
			previewQueryString = "&audioPreview=1";
		}
		else if (hasImages) {
			previewQueryString = "&imagePreview=1";
		}

		else if (hasVideo) {
			previewQueryString = "&videoPreview=1";

			videoThumbnailURL = DLUtil.getPreviewURL(
				fileEntry, fileVersion, themeDisplay, "&videoThumbnail=1");
		}

		if (Validator.isNotNull(previewQueryString)) {
			if (hasAudio) {
				previewFileURLs = new String[PropsValues.DL_FILE_ENTRY_PREVIEW_AUDIO_CONTAINERS.length];

				for (int i = 0; i < PropsValues.DL_FILE_ENTRY_PREVIEW_AUDIO_CONTAINERS.length; i++) {
					previewFileURLs[i] = DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, previewQueryString + "&type=" + PropsValues.DL_FILE_ENTRY_PREVIEW_AUDIO_CONTAINERS[i]);
				}
			} else if (hasVideo) {
				if (PropsValues.DL_FILE_ENTRY_PREVIEW_VIDEO_CONTAINERS.length > 0) {
					previewFileURLs = new String[PropsValues.DL_FILE_ENTRY_PREVIEW_VIDEO_CONTAINERS.length];

					for (int i = 0; i < PropsValues.DL_FILE_ENTRY_PREVIEW_VIDEO_CONTAINERS.length; i++) {
						previewFileURLs[i] = DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, previewQueryString + "&type=" + PropsValues.DL_FILE_ENTRY_PREVIEW_VIDEO_CONTAINERS[i]);
					}
				} else {
					previewFileURLs = new String[1];

					previewFileURLs[0] = videoThumbnailURL;
				}
			} else {
				previewFileURLs = new String[1];

				previewFileURLs[0] = DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, previewQueryString);
			}
		}

		return previewFileURLs;
	}

	protected static ItemSelectorReturnType
		getFirstAvailableItemSelectorReturnType(
			List<ItemSelectorReturnType> desiredItemSelectorReturnTypes,
			List<String> itemSelectorReturnTypeTypes) {

		Iterator<ItemSelectorReturnType> iterator =
			desiredItemSelectorReturnTypes.iterator();

		while (iterator.hasNext()) {
			ItemSelectorReturnType itemSelectorReturnType = iterator.next();

			String className = ClassUtil.getClassName(itemSelectorReturnType);

			if (itemSelectorReturnTypeTypes.contains(className)) {
				return itemSelectorReturnType;
			}
		}

		return null;
	}

	protected static String getURLValue(
			FileEntry fileEntry, ThemeDisplay themeDisplay)
		throws Exception {

		String[] previewFileURLs = getURLs(fileEntry, themeDisplay);

		return previewFileURLs[0];
	}

	private static final List<String> _draggableFileReturnTypeNames =
		ListUtil.fromArray(
			new String[] {
				ClassUtil.getClassName(new Base64ItemSelectorReturnType()),
				ClassUtil.getClassName(new UploadableFileReturnType())
			});
	private static final List<String> _existingFileEntryReturnTypeNames =
		ListUtil.fromArray(
			new String[] {
				ClassUtil.getClassName(new FileEntryItemSelectorReturnType()),
				ClassUtil.getClassName(new URLItemSelectorReturnType())
			});

}