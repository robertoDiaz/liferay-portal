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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Iván Zaera
 */
public abstract class BaseItemSelectorCriterion
	implements ItemSelectorCriterion {

	@Override
	public Set<ItemSelectorReturnType> getAvailableReturnTypes() {
		return _availableReturnTypes;
	}

	@Override
	public Set<ItemSelectorReturnType> getDesiredReturnTypes() {
		return _desiredReturnTypes;
	}

	@Override
	public void setDesiredReturnTypes(
		Set<ItemSelectorReturnType> desiredReturnTypes) {

		if (!_availableReturnTypes.containsAll(desiredReturnTypes)) {
			throw new IllegalArgumentException(
				"Desired return types must be a subset of available return " +
					"types");
		}

		_desiredReturnTypes = desiredReturnTypes;
	}

	protected static Set<ItemSelectorReturnType> getInmutableSet(
		ItemSelectorReturnType... itemSelectorReturnTypes) {

		Set<ItemSelectorReturnType> set = new HashSet<>();

		Collections.addAll(set, itemSelectorReturnTypes);

		return Collections.unmodifiableSet(set);
	}

	protected BaseItemSelectorCriterion(
		Set<ItemSelectorReturnType> availableReturnTypes) {

		_availableReturnTypes = availableReturnTypes;
		_desiredReturnTypes = _availableReturnTypes;
	}

	private final Set<ItemSelectorReturnType> _availableReturnTypes;
	private Set<ItemSelectorReturnType> _desiredReturnTypes;

}