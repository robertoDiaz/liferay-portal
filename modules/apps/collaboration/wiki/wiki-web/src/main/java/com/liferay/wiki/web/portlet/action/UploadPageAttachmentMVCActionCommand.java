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

package com.liferay.wiki.web.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.upload.UploadHandler;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.wiki.constants.WikiPortletKeys;
import com.liferay.wiki.web.upload.PageAttachmentWikiUploadHandler;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + WikiPortletKeys.WIKI,
		"mvc.command.name=/wiki/upload_page_attachment"
	},
	service = MVCActionCommand.class
)
public class UploadPageAttachmentMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long resourcePrimKey = ParamUtil.getLong(
			actionRequest, "resourcePrimKey");
		long maxFileSize = GetterUtil.getLong(
			actionRequest.getParameter("maxFileSize"),
			PropsValues.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE);
		String[] validExtensions = GetterUtil.getStringValues(
			actionRequest.getParameterValues("validExtensions"),
			PropsValues.DL_FILE_EXTENSIONS);

		UploadHandler uploadHandler = new PageAttachmentWikiUploadHandler(
			resourcePrimKey, maxFileSize, validExtensions);

		uploadHandler.upload(actionRequest, actionResponse);
	}

}