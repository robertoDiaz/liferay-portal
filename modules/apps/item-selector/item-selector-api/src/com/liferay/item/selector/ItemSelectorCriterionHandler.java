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

package com.liferay.item.selector;

import java.util.List;

/**
 * Returns the ItemSelectorViews that belong to the ItemSelectorCriterion
 * handled by this class.
 *
 * @author Iván Zaera
 */
public interface ItemSelectorCriterionHandler<T extends ItemSelectorCriterion> {

	/**
	 * Returns the handled ItemSelectorCriterion class. This method is a
	 * way to declare that this class belongs to a ItemSelectorCriterion. Due
	 * this every ItemSelectorCriterion should have at least one
	 * ItemSelectorCriterionHandler.
	 *
	 * @return the ItemSelectorCriterion class.
	 */
	public Class<T> getItemSelectorCriterionClass();

	/**
	 * Returns the List of ItemSelectorViews for the specified
	 * ItemSelectorCriterion.
	 *
	 * @param  itemSelectorCriterion the instance of the ItemSelectorCriterion.
	 *         Some important information for the view rendered could be
	 *         retrieved from this param.
	 * @return a List of ItemSelectorViews.
	 */
	public List<ItemSelectorView<T>> getItemSelectorViews(
		T itemSelectorCriterion);

}