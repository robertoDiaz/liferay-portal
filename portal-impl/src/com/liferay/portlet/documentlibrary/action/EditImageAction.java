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

package com.liferay.portlet.documentlibrary.action;

import com.liferay.portal.NoSuchImageException;
import com.liferay.portal.NoSuchRepositoryEntryException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Base64;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.TempFileUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.assetpublisher.util.AssetPublisherUtil;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.NoSuchFileVersionException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Roberto Díaz
 */
public class EditImageAction extends EditFileEntryAction {

	@Override
	public void processAction(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.EDIT)) {
				updateImage(actionRequest);
			}

			WindowState windowState = actionRequest.getWindowState();

			if (!windowState.equals(LiferayWindowState.POP_UP)) {
				sendRedirect(actionRequest, actionResponse);
			}
			else {
				String redirect = PortalUtil.escapeRedirect(
					ParamUtil.getString(actionRequest, "redirect"));

				if (Validator.isNotNull(redirect)) {
					actionResponse.sendRedirect(redirect);
				}
			}
		}
		catch (Exception e) {
			handleUploadException(
				portletConfig, actionRequest, actionResponse, cmd, e);
		}
	}

	@Override
	public ActionForward render(
			ActionMapping actionMapping, ActionForm actionForm,
			PortletConfig portletConfig, RenderRequest renderRequest,
			RenderResponse renderResponse)
		throws Exception {

		try {
			ActionUtil.getFileEntry(renderRequest);
		}
		catch (Exception e) {
			if (e instanceof NoSuchFileEntryException ||
				e instanceof NoSuchFileVersionException ||
				e instanceof NoSuchRepositoryEntryException ||
				e instanceof PrincipalException) {

				SessionErrors.add(renderRequest, e.getClass());

				return actionMapping.findForward(
					"portlet.document_library.error");
			}
			else {
				throw e;
			}
		}

		String forward = "portlet.document_library.edit_image";

		return actionMapping.findForward(getForward(renderRequest, forward));
	}

	protected void updateImage(ActionRequest actionRequest) throws Exception {
		long fileEntryId = ParamUtil.getLong(actionRequest, "fileEntryId");

		if (fileEntryId == 0) {
			throw new NoSuchFileEntryException();
		}

		FileEntry fileEntry = DLAppServiceUtil.getFileEntry(fileEntryId);

		String blob = ParamUtil.getString(actionRequest, "blob");

		if (Validator.isNull(blob)) {
			throw new NoSuchImageException();
		}

		String formatName = MimeTypesUtil.getFormatName(
			fileEntry.getMimeType());

		File imageFile = _getImageFromBlob(blob, formatName);

		FileEntry tempFileEntry = TempFileUtil.addTempFile(
			fileEntry.getGroupId(), fileEntry.getUserId(),
			fileEntry.getTitle() + fileEntry.getVersion(),
			_TEMP_FOLDER_NAME, imageFile, fileEntry.getMimeType());

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				actionRequest);

			fileEntry = DLAppServiceUtil.updateFileEntry(
				fileEntryId, fileEntry.getTitle(), fileEntry.getMimeType(),
				fileEntry.getTitle(), fileEntry.getDescription(),
				_getChangeLog(actionRequest), false, imageFile, serviceContext);

			AssetPublisherUtil.addAndStoreSelection(
				actionRequest, DLFileEntry.class.getName(),
				fileEntry.getFileEntryId(), -1);

			AssetPublisherUtil.addRecentFolderId(
				actionRequest, DLFileEntry.class.getName(),
				fileEntry.getFolderId());

			return;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (tempFileEntry != null) {
				TempFileUtil.deleteTempFile(tempFileEntry.getFileEntryId());
			}
		}
	}

	private String _getChangeLog(ActionRequest actionRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return LanguageUtil.get(
			themeDisplay.getLocale(),
			"this-image-has-been-modified-using-web-image-editor");
	}

	private File _getImageFromBlob(String blob, String formatName)
		throws Exception {

		blob = blob.substring(blob.indexOf(StringPool.COMMA) + 1);

		byte[] decodedBytes = Base64.decode(blob);

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
			decodedBytes);

		ImageInputStream imageInputStream = ImageIO.createImageInputStream(
			byteArrayInputStream);

		Iterator<?> readerIterator = ImageIO.getImageReadersByFormatName(formatName);

		ImageReader reader = (ImageReader) readerIterator.next();

		reader.setInput(imageInputStream, true);

		ImageReadParam defaultReadParam = reader.getDefaultReadParam();

		BufferedImage bufferedImage = reader.read(0, defaultReadParam);

		File imageFile = FileUtil.createTempFile();

		ImageIO.write(bufferedImage, formatName, imageFile);

		return imageFile;
	}

	private static final String _TEMP_FOLDER_NAME =
		EditFileEntryAction.class.getName();

}