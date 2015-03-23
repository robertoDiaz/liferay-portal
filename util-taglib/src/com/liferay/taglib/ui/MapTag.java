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

package com.liferay.taglib.ui;

import com.liferay.portal.geolocation.Point;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Chema Balsas
 * @author Roberto Díaz
 */
public class MapTag extends IncludeTag {

	public void setApiKey(String apiKey) {
		_apiKey = apiKey;
	}

	public void setControls(String controls) {
		_controls = controls;
	}

	public void setGeolocation(boolean geolocation) {
		_geolocation = geolocation;
	}

	public void setLatitude(double latitude) {
		_latitude = latitude;
	}

	public void setLongitude(double longitude) {
		_longitude = longitude;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setPoints(Point[] points) {
		_points = points;
	}

	public void setProvider(String provider) {
		_provider = provider;
	}

	public void setZoom(int zoom) {
		_zoom = zoom;
	}

	@Override
	protected void cleanUp() {
		_apiKey = null;
		_controls = null;
		_geolocation = false;
		_latitude = 0;
		_longitude = 0;
		_name = null;
		_points = null;
		_provider = null;
		_zoom = 0;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute("liferay-ui:map:apiKey", _apiKey);
		request.setAttribute("liferay-ui:map:controls", _controls);
		request.setAttribute("liferay-ui:map:geolocation", _geolocation);
		request.setAttribute("liferay-ui:map:latitude", _latitude);
		request.setAttribute("liferay-ui:map:longitude", _longitude);
		request.setAttribute("liferay-ui:map:name", _name);

		if (ArrayUtil.isNotEmpty(_points)) {
			request.setAttribute("liferay-ui:map:pointsData", _getPointsData());
		}

		request.setAttribute("liferay-ui:map:provider", _provider);
		request.setAttribute("liferay-ui:map:zoom", _zoom);
	}

	private String _getPointsData() {
		JSONObject featureCollectionJSONObject =
			JSONFactoryUtil.createJSONObject();

		featureCollectionJSONObject.put("type", "FeatureCollection");

		JSONArray featureJSONArray = JSONFactoryUtil.createJSONArray();

		for (Point point : _points) {
			JSONObject featureJSONObject = JSONFactoryUtil.createJSONObject();

			featureJSONObject.put("type", "Feature");

			JSONObject geometryJSONObject = JSONFactoryUtil.createJSONObject();

			geometryJSONObject.put("type", "Point");

			JSONArray coordinatesJSONArray = JSONFactoryUtil.createJSONArray();

			coordinatesJSONArray.put(point.getLongitude());
			coordinatesJSONArray.put(point.getLatitude());

			geometryJSONObject.put("coordinates", coordinatesJSONArray);

			featureJSONObject.put("geometry", geometryJSONObject);

			Point.Tooltip tooltip = point.getTooltip();

			if (tooltip != null) {
				JSONObject propertiesJSONObject =
					JSONFactoryUtil.createJSONObject();

				String title = tooltip.getTitle();

				if (Validator.isNotNull(title)) {
					propertiesJSONObject.put("title", title);
				}

				String content = tooltip.getContent();

				if (Validator.isNotNull(content)) {
					propertiesJSONObject.put("abstract", content);
				}

				featureJSONObject.put("properties", propertiesJSONObject);
			}

			featureJSONArray.put(featureJSONObject);
		}

		featureCollectionJSONObject.put("features", featureJSONArray);

		return featureCollectionJSONObject.toString();
	}

	private static final String _PAGE = "/html/taglib/ui/map/page.jsp";

	private String _apiKey;
	private String _controls;
	private boolean _geolocation;
	private double _latitude;
	private double _longitude;
	private String _name;
	private Point[] _points;
	private String _provider;
	private int _zoom;

}