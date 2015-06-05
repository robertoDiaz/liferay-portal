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

package com.liferay.item.selector.criteria.file.criterion;

import com.liferay.item.selector.BaseItemSelectorCriterion;
import com.liferay.item.selector.criteria.DocumentItemSelectorCriterion;
import com.liferay.portal.kernel.repository.model.FileEntry;

import java.net.URL;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public class FileItemSelectorCriterion extends BaseItemSelectorCriterion
	implements DocumentItemSelectorCriterion {

	public FileItemSelectorCriterion() {
		super(_AVAILABLE_RETURN_TYPES);
	}

	public FileItemSelectorCriterion(long repositoryId, String[] mimeTypes) {
		super(_AVAILABLE_RETURN_TYPES);

		_repositoryId = repositoryId;
		_mimeTypes = mimeTypes;
	}

	public String[] getMimeTypes() {
		return _mimeTypes;
	}

	public long getRepositoryId() {
		return _repositoryId;
	}

	public void setMimeTypes(String[] mimeTypes) {
		_mimeTypes = mimeTypes;
	}

	public void setRepositoryId(long repositoryId) {
		_repositoryId = repositoryId;
	}

	private static final Set<Class<?>> _AVAILABLE_RETURN_TYPES =
		getInmutableSet(FileEntry.class, URL.class);

	private String[] _mimeTypes;
	private long _repositoryId;

}