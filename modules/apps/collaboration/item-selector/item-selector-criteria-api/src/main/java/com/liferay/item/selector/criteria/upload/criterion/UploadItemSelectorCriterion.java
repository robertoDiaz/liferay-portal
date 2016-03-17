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

package com.liferay.item.selector.criteria.upload.criterion;

import com.liferay.item.selector.BaseItemSelectorCriterion;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.PropsValues;

import javax.portlet.PortletURL;

/**
 * @author Ambrín Chaudhary
 * @author Roberto Díaz
 */
public class UploadItemSelectorCriterion extends BaseItemSelectorCriterion {

	public UploadItemSelectorCriterion() {
	}

	public UploadItemSelectorCriterion(
		PortletURL portletURL, String repositoryName) {

		this(
			StringPool.BLANK, portletURL, repositoryName,
			PropsValues.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE,
			new String[] {StringPool.STAR});
	}

	public UploadItemSelectorCriterion(
		PortletURL portletURL, String repositoryName, long maxFileSize,
		String[] validExtensions) {

		this(
			StringPool.BLANK, portletURL, repositoryName, maxFileSize,
			validExtensions);
	}

	public UploadItemSelectorCriterion(String url, String repositoryName) {
		this(
			url, repositoryName,
			PropsValues.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE,
			new String[] {StringPool.STAR});
	}

	public UploadItemSelectorCriterion(
		String url, String repositoryName, long maxFileSize,
		String[] validExtensions) {

		this(url, null, repositoryName, maxFileSize, validExtensions);
	}

	public long getMaxFileSize() {
		return _maxFileSize;
	}

	public String getRepositoryName() {
		return _repositoryName;
	}

	public String getURL() {
		return _url;
	}

	public String[] getValidExtensions() {
		return _validExtensions;
	}

	public void setMaxFileSize(long maxFileSize) {
		_maxFileSize = maxFileSize;
	}

	public void setRepositoryName(String repositoryName) {
		_repositoryName = repositoryName;
	}

	public void setURL(String url) {
		_url = url;
	}

	public void setValidExtensions(String[] validExtensions) {
		_validExtensions = validExtensions;
	}

	private UploadItemSelectorCriterion(
		String url, PortletURL portletURL, String repositoryName,
		long maxFileSize, String[] validExtensions) {

		if (Validator.isNotNull(url)) {
			_url = url;
		}
		else {
			_url = populatePortletURL(portletURL, maxFileSize, validExtensions);
		}

		_repositoryName = repositoryName;
		_maxFileSize = maxFileSize;
		_validExtensions = validExtensions;
	}

	private String populatePortletURL(
		PortletURL portletURL, long maxFileSize, String[] validExtensions) {

		if (maxFileSize != 0) {
			portletURL.setParameter("maxFileSize", String.valueOf(maxFileSize));
		}

		if (ArrayUtil.isNotEmpty(validExtensions)) {
			portletURL.setParameter("validExtensions", validExtensions);
		}

		return portletURL.toString();
	}

	private long _maxFileSize;
	private String _repositoryName;
	private String _url;
	private String[] _validExtensions;

}