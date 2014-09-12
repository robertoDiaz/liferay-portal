<#include "../init.ftl">

<#assign latitude = "">
<#assign longitude = "">

<#assign fieldRawValue = paramUtil.getString(request, "${namespacedFieldName}", fieldRawValue)>

<#if (fieldRawValue != "")>
	<#assign geolocationJSONObject = jsonFactoryUtil.createJSONObject(fieldRawValue)>

	<#assign latitude = geolocationJSONObject.getDouble("latitude")>
	<#assign longitude = geolocationJSONObject.getDouble("longitude")>

	<#assign coordinatesContainerCssClass = "">
</#if>

<@aui["field-wrapper"] cssClass="geolocation-field" data=data label=label required=required>
	<@aui.input name=namespacedFieldName type="hidden" value=fieldRawValue />

	<div id="${portletNamespace}${namespacedFieldName}CoordinatesContainer" style="padding: 15px;">
		<p>
			<span id="${portletNamespace}${namespacedFieldName}Location"></span>
		</p>

		<div id="${portletNamespace}${namespacedFieldName}map_canvas" ></div>
	</div>

	${fieldStructure.children}
</@>

<@aui.script use="json, liferay-google-maps" >

	var drawMap = function(latitude, longitude) {
		var id = 'DDMGeolocalizationMap';

		Liferay.GoogleMaps.register(
			id,
			{
				latitude: latitude,
				longitude: longitude,
				namespace: '${portletNamespace}${namespacedFieldName}'
			}
		);

		Liferay.on(
			'${portletNamespace}${namespacedFieldName}googleMapsInitialized',
			function() {
				var googleMaps = Liferay.component(id);

				googleMaps.on(
					'locationReady',
					function() {
						var inputNode = A.one('#${portletNamespace}${namespacedFieldName}');
						var locationNode = A.one('#${portletNamespace}${namespacedFieldName}Location');

						inputNode.val(
							A.JSON.stringify(
								{
									latitude: googleMaps.getLatitude(),
									longitude: googleMaps.getLongitude()
								}
							)
						);

						locationNode.html('<span class="glyphicon glyphicon-map-marker" style="margin-right: 5px;"></span>' + googleMaps.getFormattedLocation(1));
					}
				);
			}
		);
	}

	<#if (fieldRawValue != "")>
		drawMap(${latitude}, ${longitude});
	<#else>
		Liferay.Util.getGeolocation(
			function(latitude, longitude) {
				drawMap(latitude, longitude);
			}
		);
	</#if>
</@>