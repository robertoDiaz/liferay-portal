<%--
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
--%>

<%@ include file="/html/taglib/init.jsp" %>

<%
boolean geolocation = GetterUtil.getBoolean(request.getAttribute("liferay-ui:map:geolocation"));
String name = GetterUtil.getString((String)request.getAttribute("liferay-ui:map:name"));
boolean skipMapLoading = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:map:skipMapLoading"));

name = namespace + name;
%>

<c:if test="<%= !skipMapLoading %>">
	<liferay-util:html-top outputKey="js_map_gmaps_skip_map_loading">
		<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places" type="text/javascript"></script>
	</liferay-util:html-top>
</c:if>

<div id="<%= name %>Map"></div>

<aui:script use="liferay-os-maps">
	var createMap = function(latitude, longitude) {
		Liferay.component(
			'<%= name %>',
			new Liferay.OSMaps(
				{
					boundingBox: '#<%= name %>Map',
					homeButton: true,
					latitude: latitude,
					longitude: longitude,
					searchBox: true
				}
			).render()
		);
	};

	createMap(40.416775, -3.70379);
</aui:script>