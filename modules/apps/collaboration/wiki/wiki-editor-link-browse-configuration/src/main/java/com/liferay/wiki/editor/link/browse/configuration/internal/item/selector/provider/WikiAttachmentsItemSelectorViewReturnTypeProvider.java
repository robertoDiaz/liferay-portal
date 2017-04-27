/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.wiki.editor.link.browse.configuration.internal.item.selector.provider;

import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.ItemSelectorView;
import com.liferay.item.selector.ItemSelectorViewReturnTypeProvider;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * @author Roberto Díaz
 */
@Component(
	property = {
		"item.selector.view.key=com.liferay.wiki.web.internal.item.selector.view.WikiAttachmentItemSelectorView"
	}
)
public class WikiAttachmentsItemSelectorViewReturnTypeProvider implements
	ItemSelectorViewReturnTypeProvider {

	@Override
	public List<ItemSelectorReturnType> populateSupportedItemSelectorReturnTypes(
		List<ItemSelectorReturnType> supportedItemSelectorReturnTypes) {

		supportedItemSelectorReturnTypes.add(new URLItemSelectorReturnType());

		return supportedItemSelectorReturnTypes;
	}

	@Reference(
		target = "(item.selector.view.key=com.liferay.wiki.web.internal.item.selector.view.WikiAttachmentItemSelectorView)"
	)
	private ItemSelectorView _itemSelectorView;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.frontend.editor.alloyeditor.link.browse.web)"
	)
	private ServletContext _servletContext;

}
