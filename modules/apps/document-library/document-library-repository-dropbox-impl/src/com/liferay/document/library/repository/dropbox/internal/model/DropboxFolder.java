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
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.repository.external.ExtRepositoryFolder;

import java.util.Date;

/**
 * @author Adolfo Pérez
 */
public class DropboxFolder implements ExtRepositoryFolder {

	public static final ExtRepositoryFolder ROOT = new ExtRepositoryFolder() {

		@Override
		public String getName() {
			return StringPool.BLANK;
		}

		@Override
		public boolean isRoot() {
			return true;
		}

		@Override
		public boolean containsPermission(
			ExtRepositoryPermission extRepositoryPermission) {

			return true;
		}

		@Override
		public String getDescription() {
			return StringPool.BLANK;
		}

		@Override
		public String getExtension() {
			return StringPool.BLANK;
		}

		@Override
		public Date getModifiedDate() {
			return null;
		}

		@Override
		public Date getCreateDate() {
			return null;
		}

		@Override
		public String getExtRepositoryModelKey() {
			return StringPool.BLANK;
		}

		@Override
		public String getOwner() {
			return null;
		}

		@Override
		public long getSize() {
			return 0;
		}
	};

	public DropboxFolder(DropboxEntry dropboxEntry) {
		_dropboxEntry = dropboxEntry;
	}

	@Override
	public boolean containsPermission(
		ExtRepositoryPermission extRepositoryPermission) {

		return true;
	}

	@Override
	public Date getCreateDate() {
		return _dropboxEntry.getCreateDate();
	}

	@Override
	public String getDescription() {
		return _dropboxEntry.getDescription();
	}

	@Override
	public String getExtension() {
		return StringPool.BLANK;
	}

	@Override
	public String getExtRepositoryModelKey() {
		return _dropboxEntry.getPath();
	}

	@Override
	public Date getModifiedDate() {
		return _dropboxEntry.getModifiedDate();
	}

	@Override
	public String getName() {
		return _dropboxEntry.getName();
	}

	@Override
	public String getOwner() {
		return null;
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public boolean isRoot() {
		String path = _dropboxEntry.getPath();

		return path.equals(StringPool.SLASH);
	}

	private final DropboxEntry _dropboxEntry;

}