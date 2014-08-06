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

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergio González
 * @author Roberto Díaz
 */
public class ImageSelectorTag extends IncludeTag {

	public String getCallback() {
		return _callback;
	}

	public void setCallback(String callback) {
		_callback = callback;
	}

	public void setImageId(long imageId) {
		_imageId = imageId;
	} @Override
	protected void cleanUp() {
		_maxFileSize = 0;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute("liferay-ui:image-selector:callback", _callback);
		request.setAttribute(
			"liferay-ui:image-selector:imageId", String.valueOf(_imageId));

		if (_maxFileSize == 0) {
			try {
				_maxFileSize =
					PrefsPropsUtil.getLong(
						PropsKeys.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE);
			}
			catch (SystemException se) {
			}
		}

		request.setAttribute(
			"liferay-ui:image-selector:maxFileSize",
			String.valueOf(_maxFileSize));
	}

	private static final String _PAGE =
		"/html/taglib/ui/image_selector/page.jsp";

	private String _callback;
	private long _imageId;
	private long _maxFileSize;

	public void setMaxFileSize(long maxFileSize) {
		_maxFileSize = maxFileSize;
	}

}