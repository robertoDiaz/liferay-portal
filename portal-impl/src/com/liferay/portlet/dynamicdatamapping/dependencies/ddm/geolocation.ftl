<#include "../init.ftl">

<#assign latitude = "">
<#assign longitude = "">

<#assign coordinatesContainerCssClass = "hide">

<#assign fieldRawValue = paramUtil.getString(request, "${namespacedFieldName}", fieldRawValue)>

<#if (fieldRawValue != "")>
	<#assign geolocationJSONObject = jsonFactoryUtil.createJSONObject(fieldRawValue)>

	<#assign latitude = geolocationJSONObject.getDouble("latitude")>
	<#assign longitude = geolocationJSONObject.getDouble("longitude")>

	<#assign coordinatesContainerCssClass = "">
</#if>

<@aui["field-wrapper"] cssClass="geolocation-field" data=data label=label required=required>
	<@aui.input name=namespacedFieldName type="hidden" value=fieldRawValue />

	<@aui["button-row"]>
		<@aui.button onClick="window['${portletNamespace}${namespacedFieldName}SetGeolocation']();" value="geolocate" />
	</@>

	<div class="${coordinatesContainerCssClass}" id="${portletNamespace}${namespacedFieldName}CoordinatesContainer" style="padding: 15px;">

		<p>
			<span id="${portletNamespace}${namespacedFieldName}Location"></span>
		</p>

		<p>
			<span id="${portletNamespace}${namespacedFieldName}Latitude" style="display: inline-block; margin-left: 50px"></span>
			<span id="${portletNamespace}${namespacedFieldName}Longitude"></span>
		</p>

		<div id="${portletNamespace}${namespacedFieldName}map_canvas" style="border: 1px solid #ccc; width:100%; height:400px;"></div>
	</div>

	<script src="${themeDisplay.getProtocol()}://maps.googleapis.com/maps/api/js?v=3.exp" type="text/javascript"></script>

	${fieldStructure.children}
</@>

<@aui.script>
	Liferay.provide(
		window,
		'${portletNamespace}${namespacedFieldName}SetGeolocation',
		function(position) {
			var A = AUI();

			var coordinatesContainerNode = A.one('#${portletNamespace}${namespacedFieldName}CoordinatesContainer');
			var latitudeNode = A.one('#${portletNamespace}${namespacedFieldName}Latitude');
			var longitudeNode = A.one('#${portletNamespace}${namespacedFieldName}Longitude');
			var locationNode = A.one('#${portletNamespace}${namespacedFieldName}Location');
			var mapCanvasNode = A.one('#${portletNamespace}${namespacedFieldName}map_canvas');

			coordinatesContainerNode.show();

			mapCanvasNode.html('<@liferay_ui.message key="loading" />');

			Liferay.Util.getGeolocation(
				function(latitude, longitude) {
					debugger;
					var latLng = new google.maps.LatLng(latitude, longitude);
					var geocoder = new google.maps.Geocoder();

					var mapOptions = {
						center: latLng,
						zoom: 11,
						mapTypeId: google.maps.MapTypeId.ROADMAP
					};

					var map = new google.maps.Map(document.getElementById('${portletNamespace}${namespacedFieldName}map_canvas'), mapOptions);

					geocoder.geocode(
						{'latLng': latLng},
						function(results, status) {
							if (status == google.maps.GeocoderStatus.OK) {
								if (results[1]) {
									var location = results[1].formatted_address

									new google.maps.Marker(
										{
											position: latLng,
											map: map,
											title: location
										}
									);

									locationNode.html('<strong><@liferay_ui.message key="location" />: </strong>' + location);

									latitudeNode.html('<strong><@liferay_ui.message key="latitude" />: </strong>' + latitude);
									longitudeNode.html('<strong><@liferay_ui.message key="longitude" />: </strong>' + longitude);
								}
								else {
									alert('No results found');
								}
							}
							else {
								alert('Geocoder failed due to: ' + status);
							}
						}
					);
				}
			);
		},
		['aui-base', 'json']
	);
</@>