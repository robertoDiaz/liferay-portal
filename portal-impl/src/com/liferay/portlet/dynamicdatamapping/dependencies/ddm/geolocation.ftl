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
		<div class="glyphicon glyphicon-map-marker" id="${portletNamespace}${namespacedFieldName}Location"></div>
		<div id="${portletNamespace}${namespacedFieldName}Map"></div>
	</div>

	${fieldStructure.children}
</@>

<@aui.script use="json, liferay-google-maps" >
	var drawMap = function(latitude, longitude) {
		Liferay.GoogleMaps.register(
			'${portletNamespace}${namespacedFieldName}',
			{
				boundingBox: '#${portletNamespace}${namespacedFieldName}Map',
				homeButton: true,
				latitude: latitude,
				longitude: longitude,
				on: {
					locationError: function(event) {
						console.log(event.status);
					},
					locationUpdated: function(event) {
						var inputNode = A.one('#${portletNamespace}${namespacedFieldName}');

						var location = event.location.geometry.location;

						inputNode.val(
							A.JSON.stringify(
								{
									latitude: location.lat(),
									longitude: location.lng()
								}
							)
						);

						var locationNode = A.one('#${portletNamespace}${namespacedFieldName}Location');

						locationNode.html(event.location.formatted_address);
					}
				},
				searchBox: true
			}
		);
	};

	<#if (fieldRawValue != "")>
		drawMap(${latitude}, ${longitude});
	<#else>
		Liferay.Util.getGeolocation(drawMap);
	</#if>
</@>