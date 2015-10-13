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

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author Sergio González
 */
public class ItemSelectorUtil {
	public static ItemSelector getItemSelector() {
		PortalRuntimePermission.checkGetBeanProperty(ItemSelectorUtil.class);

		return _serviceTracker.getService();
	}

	private static final ServiceTracker
		<ItemSelector, ItemSelector> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(ItemSelectorUtil.class);

		_serviceTracker = new ServiceTracker<>(
			bundle.getBundleContext(), ItemSelector.class, null);

		_serviceTracker.open();
	}

}