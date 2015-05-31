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

package com.liferay.document.library.item.selector.web;

import com.liferay.item.selector.taglib.BrowserTagItemSelectorReturnType;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.util.DLUtil;

/**
 * @author Roberto Díaz
 */
public enum DLItemSelectorReturnType
	implements BrowserTagItemSelectorReturnType {

	BASE_64, FILE_ENTRY, URL;

	@Override
	public String getName() {
		return name();
	}

	@Override
	public ObjectValuePair<String, String> getReturnTypeAndValue(
			FileEntry fileEntry, ThemeDisplay themeDisplay)
		throws Exception {

		if (this == FILE_ENTRY) {
			return new ObjectValuePair<>(
				getName(), String.valueOf(fileEntry.getFileEntryId()));
		}
		else if (this == URL) {
			return new ObjectValuePair<>(
				getName(),
				DLUtil.getImagePreviewURL(fileEntry, themeDisplay));
		}
		else {
			return new ObjectValuePair<>(getName(), StringPool.BLANK);
		}
	}

	@Override
	public boolean showDropZone() {
		return this == BASE_64;
	}

}