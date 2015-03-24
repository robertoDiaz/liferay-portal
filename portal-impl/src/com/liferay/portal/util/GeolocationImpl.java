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

package com.liferay.portal.util;

import com.liferay.portal.geolocation.Point;
import com.liferay.portal.kernel.util.Geolocation;

/**
 * @author Roberto Díaz
 */
public class GeolocationImpl implements Geolocation {

	@Override
	public Point createPoint(double latitude, double longitude) {
		return new Point(latitude, longitude);
	}

	@Override
	public Point createPoint(
		double latitude, double longitude, Point.Tooltip tooltip) {

		return new Point(latitude, longitude, tooltip);
	}

	@Override
	public Point.Tooltip createTooltip(String title, String content) {
		return new Point.Tooltip(title, content);
	}
}