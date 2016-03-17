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

package com.liferay.blogs.web.portlet.action;

import com.liferay.blogs.web.constants.BlogsPortletKeys;
import com.liferay.blogs.web.upload.TempImageBlogsUploadHandler;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.upload.UploadHandler;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.util.PropsValues;
import org.osgi.service.component.annotations.Component;

/**
 * @author Sergio González
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS,
		"javax.portlet.name=" + BlogsPortletKeys.BLOGS_ADMIN,
		"mvc.command.name=/blogs/upload_temp_image"
	},
	service = MVCActionCommand.class
)
public class UploadTempImageMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long maxFileSize = GetterUtil.getLong(
			actionRequest.getParameter("maxFileSize"),
			PropsValues.BLOGS_IMAGE_MAX_SIZE);
		String[] validExtensions = GetterUtil.getStringValues(
			actionRequest.getParameterValues("validExtensions"),
			PropsValues.BLOGS_IMAGE_EXTENSIONS);

		UploadHandler uploadHandler = new TempImageBlogsUploadHandler(
			maxFileSize, validExtensions);

		uploadHandler.upload(actionRequest, actionResponse);
	}

}