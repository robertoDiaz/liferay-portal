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

package com.liferay.item.selector.criteria.image.criterion;

import com.liferay.item.selector.BaseItemSelectorCriterion;
import com.liferay.item.selector.criteria.DocumentItemSelectorCriterion;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.util.PropsValues;

import java.net.URL;

import java.util.Set;

/**
 * @author Roberto Díaz
 */
public class ImageItemSelectorCriterion extends BaseItemSelectorCriterion
	implements DocumentItemSelectorCriterion {

	public ImageItemSelectorCriterion() {
		super(_AVAILABLE_RETURN_TYPES);
	}

	public ImageItemSelectorCriterion(long repositoryId) {
		super(_AVAILABLE_RETURN_TYPES);

		_repositoryId = repositoryId;
	}

	public String[] getMimeTypes() {
		return PropsValues.DL_FILE_ENTRY_PREVIEW_IMAGE_MIME_TYPES;
	}

	public long getRepositoryId() {
		return _repositoryId;
	}

	@Override
	public boolean isInternalProperty(String key) {
		if (key.equals("mimeTypes")) {
			return true;
		}

		return super.isInternalProperty(key);
	}

	public void setRepositoryId(long repositoryId) {
		_repositoryId = repositoryId;
	}

	private static final Set<Class<?>> _AVAILABLE_RETURN_TYPES =
		getInmutableSet(FileEntry.class, URL.class);

	private long _repositoryId;

}