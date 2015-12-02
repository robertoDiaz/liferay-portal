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

package com.liferay.portal.kernel.servlet.taglib.ui;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.portletfilerepository.PortletFileRepositoryUtil;

/**
 * @author Sergio González
 * @author Roberto Díaz
 */
public class ImageSelector {

	public ImageSelector(
		byte[] imageBytes, String imageTitle, String imageMimeType,
		String imageURL, String imageCropRegion) {

		_imageBytes = imageBytes;
		_imageTitle = imageTitle;
		_imageMimeType = imageMimeType;
		_imageURL = imageURL;
		_imageCropRegion = imageCropRegion;

		_imageId = 0;
	}

	public ImageSelector(long imageId, String imageURL, String imageCropRegion)
		throws PortalException {

		_imageId = imageId;
		_imageURL = imageURL;
		_imageCropRegion = imageCropRegion;

		if (imageId != 0) {
			FileEntry fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(
				imageId);

			_imageMimeType = fileEntry.getMimeType();
			_imageTitle = fileEntry.getTitle();
		}
		else {
			_imageMimeType = null;
			_imageTitle = null;
		}

		_imageBytes = null;
	}

	public byte[] getImageBytes() {
		return _imageBytes;
	}

	public String getImageCropRegion() {
		return _imageCropRegion;
	}

	public long getImageId() {
		return _imageId;
	}

	public String getImageMimeType() {
		return _imageMimeType;
	}

	public String getImageTitle() {
		return _imageTitle;
	}

	public String getImageURL() {
		return _imageURL;
	}

	private final byte[] _imageBytes;
	private final String _imageCropRegion;
	private final long _imageId;
	private final String _imageMimeType;
	private final String _imageTitle;
	private final String _imageURL;

}