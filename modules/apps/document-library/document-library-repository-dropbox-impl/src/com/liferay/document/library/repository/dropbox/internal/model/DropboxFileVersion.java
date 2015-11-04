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

import com.liferay.document.library.repository.dropbox.model.DropboxRevision;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.repository.external.ExtRepositoryFileVersion;

import java.util.Date;

/**
 * @author Adolfo Pérez
 */
public class DropboxFileVersion implements ExtRepositoryFileVersion {

	public DropboxFileVersion(DropboxRevision dropboxRevision) {
		_dropboxRevision = dropboxRevision;
	}

	@Override
	public String getChangeLog() {
		return StringPool.BLANK;
	}

	@Override
	public Date getCreateDate() {
		return _dropboxRevision.getCreateDate();
	}

	public DropboxRevision getDropboxRevision() {
		return _dropboxRevision;
	}

	@Override
	public String getExtRepositoryModelKey() {
		return _dropboxRevision.getPath() + StringPool.AT +
			_dropboxRevision.getRev();
	}

	@Override
	public String getMimeType() {
		return ContentTypes.APPLICATION_OCTET_STREAM;
	}

	@Override
	public String getOwner() {
		return null;
	}

	@Override
	public long getSize() {
		return _dropboxRevision.getSize();
	}

	@Override
	public String getVersion() {
		return _dropboxRevision.getRev();
	}

	private final DropboxRevision _dropboxRevision;

}