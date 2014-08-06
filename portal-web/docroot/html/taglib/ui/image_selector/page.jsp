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

<%@ include file="/html/taglib/ui/image_selector/init.jsp" %>

<%
String randomNamespace = PortalUtil.generateRandomKey(request, "taglib_ui_image_selector_page") + StringPool.UNDERLINE;

String callback = (String)request.getAttribute("liferay-ui:image-selector:callback");

String currentImageURL = ParamUtil.getString(request, "currentLogoURL");

String action = HttpUtil.addParameter(themeDisplay.getPathMain() + "/portal/image_selector", "p_auth", AuthTokenUtil.getToken(request));

long maxFileSize = PrefsPropsUtil.getLong(PropsKeys.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE);
%>

<aui:form action="<%= action %>" cssClass="taglib-image-selector" enctype="multipart/form-data" method="post" name='<%= randomNamespace + "imageSelectorFM" %>' onSubmit="event.preventDefault();" useNamespace="<%= false %>">
	<aui:input id="<%= randomNamespace + Constants.CMD %>" name="<%= Constants.CMD %>" type="hidden" />
	<aui:input id='<%= randomNamespace + "fileEntryId" %>' name="fileEntryId" type="hidden" />
	<aui:input id='<%= randomNamespace + "p_auth" %>' name="p_auth" type="hidden" value="<%= AuthTokenUtil.getToken(request) %>" />

	<liferay-ui:error exception="<%= FileExtensionException.class %>">
		<liferay-ui:message arguments="<%= StringUtil.merge(PropsValues.DL_FILE_EXTENSIONS, StringPool.COMMA) %>" key="please-enter-a-file-with-a-valid-extension-x" translateArguments="<%= false %>" />
	</liferay-ui:error>

	<liferay-ui:error exception="<%= FileSizeException.class %>">
		<liferay-ui:message arguments="<%= TextFormatter.formatStorageSize(maxFileSize, locale) %>" key="please-enter-a-file-with-a-valid-file-size-no-larger-than-x" translateArguments="<%= false %>" />
	</liferay-ui:error>

	<liferay-ui:error exception="<%= NoSuchFileException.class %>" message="an-unexpected-error-occurred-while-uploading-your-file" />
	<liferay-ui:error exception="<%= UploadException.class %>" message="an-unexpected-error-occurred-while-uploading-your-file" />

	<aui:fieldset cssClass="lfr-image-selector">
		<div class="image-upload-wrapper" id='<%= randomNamespace + "imageUploadWrapper" %>'>
			<aui:a href="javascript:;" id='<%= randomNamespace + "imageUploadLink" %>'><liferay-ui:message key="upload" /></aui:a>
			<aui:input id='<%= randomNamespace + "imageUploadFile" %>' label="" name="file" onChange='<%= randomNamespace + "uploadImage();" %>' size="50" type="file" wrapperCssClass="image-upload-file">
				<aui:validator name="acceptFiles">
					'<%= StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.DL_FILE_EXTENSIONS, StringPool.COMMA)) %>'
				</aui:validator>
			</aui:input>
		</div>

		<div class="hide image-preview-wrapper" id='<%= randomNamespace + "imagePreviewWrapper" %>'>
			<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="image-preview" />" id='<%= randomNamespace + "imagePreview" %>' src="<%= HtmlUtil.escape(currentImageURL) %>" />

			<div class="image-preview-action-buttons">
				<aui:button name='<%= randomNamespace + "apply" %>' type="submit" value="apply" />

				<aui:button name='<%= randomNamespace + "cancel" %>' value="cancel" />
			</div>
		</div>
	</aui:fieldset>
</aui:form>

<aui:script use="aui-io,io-upload-iframe,json-parse,dd-drag">
	var imageUploadLink = A.one('#<%= randomNamespace %>imageUploadLink')
	var imageUploadFile = A.one('#<%= randomNamespace %>imageUploadFile')

	imageUploadLink.on(
		'click',
		function(event) {
			imageUploadFile.simulate('click');
		}
	);

	var form = A.one('#<%= randomNamespace %>imageSelectorFM');

	var applyButton = A.one('#<%= randomNamespace %>apply');

	applyButton.on(
		'click',
		function(event) {
			var imagePreviewWrapper = document.querySelector('#<%= randomNamespace %>imagePreviewWrapper');

			var imagePreviewWrapperWidth = A.DOM.region(imagePreviewWrapper).width;
			var imagePreviewWrapperHeight = A.DOM.region(imagePreviewWrapper).height;
			var imagePreviewWrapperX = A.DOM.region(imagePreviewWrapper).left;
			var imagePreviewWrapperY = A.DOM.region(imagePreviewWrapper).top;

			var imagePreview = document.querySelector('#<%= randomNamespace %>imagePreview');

			var imagePreviewWidth = A.DOM.region(imagePreview).width;
			var imagePreviewHeight = A.DOM.region(imagePreview).height;
			var imagePreviewX = A.DOM.region(imagePreview).left;
			var imagePreviewY = A.DOM.region(imagePreview).top;

			var fileEntryIdNode = A.one('#<%= randomNamespace %>fileEntryId')

debugger;

			A.io.request(
				'<%= action %>',
				{
					data: {
						'<%= Constants.CMD %>': 'saveImage',
						'xPos': imagePreviewWrapperX - imagePreviewX,
						'yPos': imagePreviewWrapperY - imagePreviewY,
						'width': imagePreviewWrapperWidth,
						'height': imagePreviewWrapperHeight,
						'fileEntryId': fileEntryIdNode.val()
					},
					dataType: 'JSON',
					on: {
						complete: function(event, id, obj) {
							var responseText = obj.responseText;

							var responseData = A.JSON.parse(responseText);

							if (responseData.success) {
								var fileEntryId = responseData.fileEntryId;

								<%= callback %>(fileEntryId);
							}
						}
					}
				}
			);
		}
	);

	Liferay.provide(
		window,
		'<%= randomNamespace %>uploadImage',
		function() {
			document.<%= randomNamespace %>imageSelectorFM.<%= randomNamespace + Constants.CMD %>.value = 'uploadImage';

			A.io.request(
				form.get('action'),
				{
					dataType: 'JSON',
					form: {
						id: form,
						upload: true
					},
					on: {
						complete: function(event, id, obj) {

							var responseText = obj.responseText;

							var responseData = A.JSON.parse(responseText);

							if (responseData.success) {
								var imagePreviewNode = A.one('#<%= randomNamespace %>imagePreview');

								var fileEntryIdNode = A.one('#<%= randomNamespace %>fileEntryId')

								fileEntryIdNode.val(responseData.fileEntryId);

								imagePreviewNode.attr('src', responseData.fileEntryURL);

								var imagePreviewWrapper = A.one('#<%= randomNamespace %>imagePreviewWrapper');

								imagePreviewWrapper.show();

								var imageUploadWrapper = A.one('#<%= randomNamespace %>imageUploadWrapper');

								imageUploadWrapper.hide();
		                    }
						}
					}
				}
			);
		}
	);

	var cancelButton = A.one('#<%= randomNamespace %>cancel');

	cancelButton.on(
		'click',
		function(event) {
			showImageSelector();
		}
	);

	var showImageSelector = function() {
		var imageUploadWrapper = A.one('#<%= randomNamespace %>imageUploadWrapper');

		imageUploadWrapper.show();

		var imagePreviewWrapper = A.one('#<%= randomNamespace %>imagePreviewWrapper');

		imagePreviewWrapper.hide();
	};

	var dd = new A.DD.Drag({
		node: '#<%= randomNamespace %>imagePreview'
	});
</aui:script>

<aui:script use="aui-base">
	var imagePreviewWrapper = A.one('#<%= randomNamespace %>imagePreviewWrapper');
	var imagePreview = A.one('#<%= randomNamespace %>imagePreview');

	var wrapperRegion = A.DOM.region(imagePreviewWrapper);
	var imageRegion = A.DOM.region(imagePreview);
</aui:script>