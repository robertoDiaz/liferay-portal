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

package com.liferay.document.library.repository.dropbox.constants;

/**
 * @author Adolfo Pérez
 */
public enum DropboxEntryType {

	FILE(0), FOLDER(1), ANY(2);

	public static DropboxEntryType fromType(int type) {
		if (type == 0) {
			return FILE;
		}
		else if (type == 1) {
			return FOLDER;
		}
		else if (type == 2) {
			return ANY;
		}
		else {
			throw new IllegalArgumentException("Invalid type " + type);
		}
	}

	public int getType() {
		return _type;
	}

	private DropboxEntryType(int type) {
		_type = type;
	}

	private final int _type;

}