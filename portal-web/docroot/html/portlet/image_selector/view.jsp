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

<%@ include file="/html/portlet/init.jsp" %>

<%
String currentImageURL = ParamUtil.getString(request, "currentLogoURL");
long maxFileSize = ParamUtil.getLong(request, "maxFileSize");
%>

<portlet:actionURL var="uploadImageURL">
	<portlet:param name="struts_action" value="/image_selector/view" />
	<portlet:param name="maxFileSize" value="<%= String.valueOf(maxFileSize) %>" />
</portlet:actionURL>

<aui:form action="<%= uploadImageURL %>" cssClass="image-selector-form" enctype="multipart/form-data" method="post" name="imageSelectorFM" onSubmit="event.preventDefault();">
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="fileEntryId" type="hidden" />

	<liferay-ui:error exception="<%= FileExtensionException.class %>">
		<liferay-ui:message arguments="<%= StringUtil.merge(PropsValues.DL_FILE_EXTENSIONS, StringPool.COMMA) %>" key="please-enter-a-file-with-a-valid-extension-x" translateArguments="<%= false %>" />
	</liferay-ui:error>

	<liferay-ui:error exception="<%= FileSizeException.class %>">
		<liferay-ui:message arguments="<%= TextFormatter.formatStorageSize(maxFileSize, locale) %>" key="please-enter-a-file-with-a-valid-file-size-no-larger-than-x" translateArguments="<%= false %>" />
	</liferay-ui:error>

	<liferay-ui:error exception="<%= NoSuchFileException.class %>" message="an-unexpected-error-occurred-while-uploading-your-file" />
	<liferay-ui:error exception="<%= UploadException.class %>" message="an-unexpected-error-occurred-while-uploading-your-file" />

	<aui:fieldset cssClass="lfr-image-selector">
		<div class="image-upload-wrapper" id="<portlet:namespace />imageUploadWrapper">
			<aui:a href="javascript:;" id="imageUploadLink"><liferay-ui:message key="upload" /></aui:a>
			<aui:input id="imageUploadFile" label="" name="file" onChange='<%= renderResponse.getNamespace() + "uploadImage();" %>' size="50" type="file" wrapperCssClass="image-upload-file">
				<aui:validator name="acceptFiles">
					'<%= StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.DL_FILE_EXTENSIONS, StringPool.COMMA)) %>'
				</aui:validator>
			</aui:input>
		</div>

		<div class="hide image-preview-wrapper" id="<portlet:namespace />imagePreviewWrapper">
			<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="image-preview" />" id="<portlet:namespace />imagePreview" src="<%= HtmlUtil.escape(currentImageURL) %>" />

			<div class="image-preview-action-buttons">
				<aui:button name="saveImage" type="submit" value="apply" />

				<aui:button name="cancel" type="cancel" value="cancel" />
			</div>
		</div>
	</aui:fieldset>
</aui:form>

<aui:script use="aui-io,io-upload-iframe,json-parse,dd-drag">
	var imageUploadLink = A.one('#<portlet:namespace />imageUploadLink')
	var imageUploadFile = A.one('#<portlet:namespace />imageUploadFile')

	imageUploadLink.on(
		'click',
		function(event) {
			imageUploadFile.simulate('click');
		}
	);

	var form = A.one('#<portlet:namespace />imageSelectorFM');

	var saveButton = A.one('#<portlet:namespace />saveImage');

	saveButton.on(
		'click',
		function(event) {
			var imagePreviewWrapper = document.querySelector('#<portlet:namespace />imagePreviewWrapper');

			var imagePreviewWrapperWidth = A.DOM.region(imagePreviewWrapper).width;
			var imagePreviewWrapperHeight = A.DOM.region(imagePreviewWrapper).height;
			var imagePreviewWrapperX = A.DOM.region(imagePreviewWrapper).left;
			var imagePreviewWrapperY = A.DOM.region(imagePreviewWrapper).top;

			var imagePreview = document.querySelector('#<portlet:namespace />imagePreview');

			var imagePreviewWidth = A.DOM.region(imagePreview).width;
			var imagePreviewHeight = A.DOM.region(imagePreview).height;
			var imagePreviewX = A.DOM.region(imagePreview).left;
			var imagePreviewY = A.DOM.region(imagePreview).top;

			var fileEntryIdNode = A.one('#<portlet:namespace />fileEntryId')

			<portlet:actionURL var="saveImageURL">
				<portlet:param name="struts_action" value="/image_selector/view" />
				<portlet:param name="cmd" value="saveImage" />
			</portlet:actionURL>

			A.io.request(
				'<%= saveImageURL.toString() %>',
				{
					data: {
						'<portlet:namespace />xPos': imagePreviewWrapperX - imagePreviewX,
						'<portlet:namespace />yPos': imagePreviewWrapperY - imagePreviewY,
						'<portlet:namespace />width': imagePreviewWrapperWidth,
						'<portlet:namespace />height': imagePreviewWrapperHeight,
						'<portlet:namespace />fileEntryId': fileEntryIdNode.val()
					},
					dataType: 'JSON',
					on: {
						complete: function(event, id, obj) {
							var responseText = obj.responseText;

							var responseData = A.JSON.parse(responseText);

							if (responseData.success) {
								debugger;
		                    }
						}
					}
				}
			);
		}
	);

	Liferay.provide(
		window,
		'<portlet:namespace />uploadImage',
		function() {
			document.<portlet:namespace />imageSelectorFM.<portlet:namespace /><%= Constants.CMD %>.value = 'uploadImage';

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
								var imagePreviewNode = A.one('#<portlet:namespace />imagePreview');

								var fileEntryIdNode = A.one('#<portlet:namespace />fileEntryId')

								fileEntryIdNode.val(responseData.fileEntryId);

								imagePreviewNode.attr('src', responseData.fileEntryURL);

								var imagePreviewWrapper = A.one('#<portlet:namespace />imagePreviewWrapper');

								imagePreviewWrapper.show();

								var imageUploadWrapper = A.one('#<portlet:namespace />imageUploadWrapper');

								imageUploadWrapper.hide();
		                    }
						}
					}
				}
			);
		}
	);

	var cancelButton = A.one('#<portlet:namespace />cancel');

	cancelButton.on(
		'click',
		function(event) {
			showImageSelector();
		}
	);

	var showImageSelector = function() {
		var imageUploadWrapper = A.one('#<portlet:namespace />imageUploadWrapper');

		imageUploadWrapper.show();

		var imagePreviewWrapper = A.one('#<portlet:namespace />imagePreviewWrapper');

		imagePreviewWrapper.hide();
	};

	var dd = new A.DD.Drag({
		node: '#<portlet:namespace />imagePreview'
	});
</aui:script>

<aui:script use="aui-base">
	var imagePreviewWrapper = A.one('#<portlet:namespace />imagePreviewWrapper');
	var imagePreview = A.one('#<portlet:namespace />imagePreview');

	var wrapperRegion = A.DOM.region(imagePreviewWrapper);
	var imageRegion = A.DOM.region(imagePreview);
</aui:script>