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

package com.liferay.portal.geolocation;

/**
 * @author Roberto Díaz
 */
public class Point {

	public Point(double latitude, double longitude) {
		this(latitude, longitude, null, null);
	}

	public Point(double latitude, double longitude, Tooltip tooltip) {
		this(latitude, longitude, tooltip, null);
	}

	public Point(
		double latitude, double longitude, Tooltip tooltip, String icon) {

		_latitude = latitude;
		_longitude = longitude;
		_tooltip = tooltip;
		_icon = icon;
	}

	public double getLatitude() {
		return _latitude;
	}

	public double getLongitude() {
		return _longitude;
	}

	public Tooltip getTooltip() {
		return _tooltip;
	}

	public String getIcon() {
		return _icon;
	}

	public static class Tooltip {

		public Tooltip(String title, String content) {
			_title = title;
			_content = content;
		}

		public String getContent() {
			return _content;
		}

		public String getTitle() {
			return _title;
		}

		private final String _content;
		private final String _title;

	}

	private final String _icon;
	private final double _latitude;
	private final double _longitude;
	private final Tooltip _tooltip;

}