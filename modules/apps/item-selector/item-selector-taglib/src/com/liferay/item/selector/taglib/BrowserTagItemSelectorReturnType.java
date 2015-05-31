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

package com.liferay.item.selector.taglib;

import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.theme.ThemeDisplay;

/**
 * @author Roberto Díaz
 */
public interface BrowserTagItemSelectorReturnType
	extends ItemSelectorReturnType {

	public ObjectValuePair<String, String> getReturnTypeAndValue(
			FileEntry fileEntry, ThemeDisplay themeDisplay)
		throws Exception;

	public boolean showDropZone();

}