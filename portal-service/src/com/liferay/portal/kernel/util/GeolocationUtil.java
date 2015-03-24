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

package com.liferay.portal.kernel.util;

import com.liferay.portal.geolocation.Point;
import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

/**
 * @author Roberto Díaz
 */
public class GeolocationUtil {

	public static Geolocation getGeolocation() {
		PortalRuntimePermission.checkGetBeanProperty(HtmlUtil.class);

		return _geolocation;
	}

	public Point createPoint(double latitude, double longitude) {
		return getGeolocation().createPoint(latitude, longitude);
	}

	public Point createPoint(
		double latitude, double longitude, Point.Tooltip tooltip) {

		return getGeolocation().createPoint(latitude, longitude, tooltip);
	}

	public Point[] createPointsArray(
		double latitude, double longitude, Point.Tooltip tooltip) {

		return new Point[0];
	}

	public Point.Tooltip createTooltip(String title, String content) {
		return getGeolocation().createTooltip(title, content);
	}

	public void setGeolocation(Geolocation geolocation) {
		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_geolocation = geolocation;
	}

	private static Geolocation _geolocation;

}