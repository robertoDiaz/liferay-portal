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

package com.liferay.portal.action;

import com.liferay.portal.ImageTypeException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadServletRequest;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.TempFileUtil;
import com.liferay.portal.kernel.util.TextFormatter;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.portletfilerepository.PortletFileRepositoryUtil;
import com.liferay.portal.struts.JSONAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.documentlibrary.FileExtensionException;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.documentlibrary.NoSuchFileException;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;

import java.awt.image.RenderedImage;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Sergio González
 */
public class ImageSelectorAction extends JSONAction {

	@Override
	public String getJSON(
			ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		UploadServletRequest uploadServletRequest = null;

		String cmd = ParamUtil.getString(request, Constants.CMD);

		if (Validator.isNull(cmd)) {
			uploadServletRequest = PortalUtil.getUploadServletRequest(request);

			cmd = ParamUtil.getString(uploadServletRequest, Constants.CMD);
		}

		try {
			UploadException uploadException =
				(UploadException)request.getAttribute(WebKeys.UPLOAD_EXCEPTION);

			if (uploadException != null) {
				if (uploadException.isExceededSizeLimit()) {
					throw new FileSizeException(uploadException.getCause());
				}

				throw new PortalException(uploadException.getCause());
			}
			else if (cmd.equals("saveImage")) {
				ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
					WebKeys.THEME_DISPLAY);

				InputStream tempFileEntryStream = null;

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				try {
					FileEntry fileEntry = saveImage(request);

					jsonObject.put("fileEntryId", fileEntry.getFileEntryId());
					jsonObject.put(
						"fileEntryURL",
						PortletFileRepositoryUtil.getPortletFileEntryURL(
							themeDisplay, fileEntry, StringPool.BLANK));
					jsonObject.put("success", Boolean.TRUE);
				}
				catch (Exception e) {
					jsonObject.put("success", Boolean.FALSE);
				}
				finally {
					StreamUtil.cleanUp(tempFileEntryStream);
				}

				return jsonObject.toString();
			}
			else if (cmd.equals("uploadImage")) {
				ThemeDisplay themeDisplay =
					(ThemeDisplay)uploadServletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				try {
					FileEntry fileEntry = uploadImage(uploadServletRequest);

					jsonObject.put("fileEntryId", fileEntry.getFileEntryId());
					jsonObject.put(
						"fileEntryURL",
						PortletFileRepositoryUtil.getPortletFileEntryURL(
							themeDisplay, fileEntry, StringPool.BLANK));
					jsonObject.put("success", Boolean.TRUE);
				}
				catch (Exception e) {
					jsonObject.put("success", Boolean.FALSE);
				}

				return jsonObject.toString();
			}
		}
		catch (Exception e) {
			JSONObject jsonObject = handleUploadException(request, cmd, e);

			if (jsonObject != null) {
				return jsonObject.toString();
			}
		}

		return StringPool.BLANK;
	}

	protected String getTempImageFolderName() {
		Class<?> clazz = getClass();

		return clazz.getName();
	}

	protected String getUniqueTempFileName(
		long groupId, long userId, String folderName) {

		String uniqueTempFileName = StringUtil.randomString();

		try {
			TempFileUtil.getTempFile(
				groupId, userId, uniqueTempFileName, folderName);

			return getUniqueTempFileName(groupId, userId, folderName);
		}
		catch (PortalException pe) {
			return uniqueTempFileName;
		}
	}

	protected JSONObject handleUploadException(
			HttpServletRequest request, String cmd, Exception e)
		throws Exception {

		if (e instanceof FileExtensionException ||
			e instanceof FileSizeException ||
			e instanceof ImageTypeException ||
			e instanceof NoSuchFileException ||
			e instanceof UploadException) {

			if (cmd.equals(Constants.ADD_TEMP)) {
				ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
					WebKeys.THEME_DISPLAY);

				String errorMessage = StringPool.BLANK;

				if (e instanceof FileExtensionException) {
					errorMessage = themeDisplay.translate(
						"please-enter-a-file-with-a-valid-extension-x",
						StringUtil.merge(
							PropsValues.DL_FILE_EXTENSIONS, StringPool.COMMA));
				}
				else if (e instanceof FileSizeException) {
					long maxFileSize = PrefsPropsUtil.getLong(
						PropsKeys.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE);

					errorMessage = themeDisplay.translate(
						"please-enter-a-file-with-a-valid-file-size-no" +
							"-larger-than-x",
						TextFormatter.formatStorageSize(
							maxFileSize, themeDisplay.getLocale()));
				}
				else if (e instanceof ImageTypeException) {
					errorMessage = themeDisplay.translate(
						"please-enter-a-file-with-a-valid-file-type");
				}
				else if (e instanceof NoSuchFileException ||
						 e instanceof UploadException) {

					errorMessage = themeDisplay.translate(
						"an-unexpected-error-occurred-while-uploading" +
							"-your-file");
				}

				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				jsonObject.put("errorMessage", errorMessage);

				return jsonObject;
			}
			else {
				SessionErrors.add(request, e.getClass(), e);
			}
		}
		else {
			throw e;
		}

		return null;
	}

	protected FileEntry saveImage(HttpServletRequest request) throws Exception {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		int xPos = ParamUtil.getInteger(request, "xPos");
		int yPos = ParamUtil.getInteger(request, "yPos");
		int width = ParamUtil.getInteger(request, "width");
		int height = ParamUtil.getInteger(request, "height");
		long fileEntryId = ParamUtil.getLong(request, "fileEntryId");

		FileEntry fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(
			fileEntryId);

		ImageBag imageBag = ImageToolUtil.read(fileEntry.getContentStream());

		RenderedImage renderedImage = imageBag.getRenderedImage();

		renderedImage = ImageToolUtil.crop(
			renderedImage, height, width, xPos, yPos);

		byte[] bytes = ImageToolUtil.getBytes(
			renderedImage, imageBag.getType());

		File file = FileUtil.createTempFile(bytes);

		FileEntry resizedFileEntry =
			PortletFileRepositoryUtil.addPortletFileEntry(
				themeDisplay.getScopeGroupId(), themeDisplay.getUserId(), "", 0,
				PortletKeys.IMAGE_SELECTOR,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, file,
				fileEntry.getTitle() + "_resized",
				MimeTypesUtil.getContentType(fileEntry.getTitle()), true);

		TempFileUtil.deleteTempFile(fileEntryId);

		return resizedFileEntry;
	}

	protected FileEntry uploadImage(UploadServletRequest uploadServletRequest)
		throws Exception {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)uploadServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		String fileName = uploadServletRequest.getFileName("file");
		InputStream inputStream = uploadServletRequest.getFileAsStream("file");

		String uniqueTempFileName = getUniqueTempFileName(
			themeDisplay.getScopeGroupId(), themeDisplay.getUserId(),
			getTempImageFolderName());

		return TempFileUtil.addTempFile(
			themeDisplay.getScopeGroupId(), themeDisplay.getUserId(),
			uniqueTempFileName, getTempImageFolderName(), inputStream,
			MimeTypesUtil.getContentType(fileName));
	}

}