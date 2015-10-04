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

package com.liferay.document.library.repository.dropbox.internal.model;

import com.liferay.document.library.repository.dropbox.model.DropboxEntry;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.repository.external.ExtRepositoryFileEntry;

import java.util.Date;

/**
 * @author Adolfo Pérez
 */
public class DropboxFileEntry implements ExtRepositoryFileEntry {

	public DropboxFileEntry(DropboxEntry dropboxEntry) {
		_dropboxEntry = dropboxEntry;
	}

	@Override
	public boolean containsPermission(
		ExtRepositoryPermission extRepositoryPermission) {

		return true;
	}

	@Override
	public String getCheckedOutBy() {
		return null;
	}

	@Override
	public Date getCreateDate() {
		return getModifiedDate();
	}

	@Override
	public String getDescription() {
		return _dropboxEntry.getDescription();
	}

	@Override
	public String getExtension() {
		return FileUtil.getExtension(_dropboxEntry.getName());
	}

	@Override
	public String getExtRepositoryModelKey() {
		return _dropboxEntry.getPath();
	}

	@Override
	public String getMimeType() {
		return ContentTypes.APPLICATION_OCTET_STREAM;
	}

	@Override
	public Date getModifiedDate() {
		return _dropboxEntry.getModifiedDate();
	}

	@Override
	public String getOwner() {
		return null;
	}

	@Override
	public long getSize() {
		return _dropboxEntry.getSize();
	}

	@Override
	public String getTitle() {
		return _dropboxEntry.getName();
	}

	private final DropboxEntry _dropboxEntry;

}