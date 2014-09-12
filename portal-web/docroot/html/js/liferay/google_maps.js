AUI.add(
	'liferay-google-maps',
	function(A) {
		var Lang = A.Lang;

		var GoogleMaps = A.Component.create(
			{
				ATTRS: {

					draggableMarker: {
						validator: Lang.isBoolean,
						value: true
					},

					latitude: {
						validator: Lang.isNumber,
						value: 999
					},

					longitude: {
						validator: Lang.isNumber,
						value: 999
					},

					namespace: {
						validator: Lang.isString
					},

					mapContainerId: {
						validator: Lang.isString,
						value: 'map_canvas'
					},

					protocol: {
						validator: Lang.isString,
						value: 'http'
					},

					zoom: {
						validator: Lang.isNumber,
						value: 11
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Widget,

				NAME: 'googlemaps',

				prototype: {
					initializer: function() {
						var instance = this;

						instance.renderUI();
					},

					renderUI: function() {
						var instance = this;

						instance._mapContainer = instance.one('#' + instance.get('mapContainerId'));

						instance._mapContainer.html('<p>' + Liferay.Language.get<('loading') + '</p>');

						instance._initMap();
					},

					getFormattedLocation: function(index) {
						var instance = this;

						return instance._location[index].formatted_address;
					},

					getLatitude: function() {
						var instance = this;

						return instance._latitude;
					},

					getLongitude: function() {
						var instance = this;

						return instance._longitude;
					},

					_addErrorMessage : function(message) {
						alert(message);
					},

					_addGoHomeButton: function() {
						var instance = this;

						var homeControlDiv = A.Node.create('<div></div>');

						homeControlDiv.html('<div class="glyphicon glyphicon-screenshot" id="' + instance.get('namespace') + 'home-button" title="' + Liferay.Language.get('set-current-location') + '"></div>');

						homeControlDiv.setStyles(
							{
								backgroundColor: 'white',
								borderRadius: '2px',
								boxShadow: '0 1px 4px rgba(0,0,0,0.3)',
								color: '#6a6a6a',
								cursor: 'pointer',
								fontSize: '19px',
								margin: '5px'
							}
						);

						var homeControlDivDOMNode = homeControlDiv.getDOMNode();

						homeControlDivDOMNode.index = 1;

						instance._map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(homeControlDivDOMNode);

						google.maps.event.addDomListener(
							homeControlDivDOMNode,
							'click',
							function() {
								instance._setCurrentLocation();
							}
						);
					},

					_initMarker: function() {
						var instance = this;

						instance._marker = new google.maps.Marker(
							{
								animation: google.maps.Animation.DROP,
								draggable: instance.get('draggableMarker'),
								position: instance._latLng,
								map: instance._map,
								title: instance._location[1].formatted_address
							}
						);

						google.maps.event.addListener(
							instance._marker,
							'dragend',
							function() {
								instance._latLng = instance._marker.getPosition()

								instance._geocode();
							}
						);
					},

					_geocode: function() {
						var instance = this;

						var geocoder = new google.maps.Geocoder();

						geocoder.geocode(
							{'latLng': instance._latLng},
							function(results, status) {
								if (status == google.maps.GeocoderStatus.OK) {
									if (results[1]) {
										instance._location = results;

										if (instance._marker) {
											instance._moveMarker();
										}
										else {
											instance._initMarker();
											instance._addGoHomeButton();
										}

										instance._map.setCenter(instance._latLng);

										instance.fire('locationReady');
									}
									else{
										instance._addErrorMessage('No results found');
									}
								}
								else {
									instance._addErrorMessage('Geocoder failed due to: ' + status);
								}
							}
						);
					},

					_initMap: function() {
						var instance = this;

						instance._latitude = instance.get('latitude');
						instance._longitude = instance.get('longitude');

						instance._latLng = new google.maps.LatLng(instance._latitude, instance._longitude);

						var mapOptions = {
							center: instance._latLng,
							zoom: instance.get('zoom'),
							mapTypeId: google.maps.MapTypeId.ROADMAP
						};

						instance._mapContainer.setStyles(
							{
								border: '1px solid #ccc',
								height: '400px',
								width: '100%'
							}
						);

						instance._map = new google.maps.Map(instance._mapContainer.getDOMNode(), mapOptions);

						instance._geocode();
					},

					_moveMarker: function() {
						var instance = this;

						instance._latitude = instance._latLng.lat();
						instance._longitude = instance._latLng.lng();

						instance._marker.setPosition(instance._latLng)
						instance._marker.setTitle(instance.getFormattedLocation(1));
					},

					_setCurrentLocation: function() {
						var instance = this;

						Liferay.Util.getGeolocation(
							function(latitude, longitude) {
								instance._latLng = new google.maps.LatLng(latitude, longitude);

								instance._geocode();
							}
						);
					}
				},

				register: function(id, config) {
					var instance = this;

					GoogleMaps._id = id;
					GoogleMaps._registered[id] = config;

					A.Get.js(themeDisplay.getProtocol() + '://maps.googleapis.com/maps/api/js?v=3.exp&callback=Liferay.GoogleMaps.initialize');
				},

				initialize: function() {
					var instance = this;

					var config = GoogleMaps._registered[GoogleMaps._id];
					var id = GoogleMaps._id;

					Liferay.component(
						id,
						function() {
							var instances = instance._instances;

							var googleMapsInstance = instances[id];

							if (!googleMapsInstance) {
								googleMapsInstance = new GoogleMaps(config);

								instances[id] = googleMapsInstance;

								Liferay.fire(config.namespace + 'googleMapsInitialized');
							}

							return googleMapsInstance;
						}
					);

					Liferay.component(id).render();
				},

				_id : {},
				_instances: {},
				_registered: {}
			}
		);

		Liferay.GoogleMaps = GoogleMaps;
	},
	'',
	{
		requires: ['aui-base','liferay-portlet-base']
	}
);