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
 * Provides an interface that determines the type of entity that shall be
 * selected and information to return. The item selector uses the criterion to
 * display only the {@link ItemSelectorView} that can select that particular
 * entity type and return the specified {@link ItemSelectorReturnType}.
 *
 * <p>
 * Implementations of this interface can provide additional information to have
 * a fine grained detail about which entities can be selected. This should be
 * done ideally using primitive types in the constructor (or very simple types
 * that can be JSON serialized) and it is mandatory to have an empty
 * constructor.
 * </p>
 *
 * <p>
 * For simplicity, it is recommended that implementations extend {@link
 * BaseItemSelectorCriterion}.
 * </p>
 *
 * @author Iván Zaera
 */
public interface ItemSelectorCriterion {

	/**
	 * Returns the desired list of return types that the caller expects and can
	 * handle, ordered by preference.
	 *
	 * <p>
	 * The order of return types is important because the first return type that
	 * can be used will be used.
	 * </p>
	 *
	 * @return the return types ordered by preference
	 */
	public List<ItemSelectorReturnType> getDesiredItemSelectorReturnTypes();

	/**
	 * Sets a list of desired return types that the caller expects and can
	 * handle, ordered by preference.
	 *
	 * <p>
	 * The order of return types is important because the first return type that
	 * can be used will be used.
	 * </p>
	 *
	 * @param desiredItemSelectorReturnTypes a preference ordered list of the
	 *        return types the caller can handle
	 */
	public void setDesiredItemSelectorReturnTypes(
		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes);

}