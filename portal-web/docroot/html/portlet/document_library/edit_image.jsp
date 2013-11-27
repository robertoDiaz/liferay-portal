<%--
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/document_library/init.jsp" %>


<%
FileEntry fileEntry = (FileEntry)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FILE_ENTRY);

FileVersion fileVersion = (FileVersion)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FILE_VERSION);

if (fileVersion == null) {
	fileVersion = fileEntry.getLatestFileVersion();
}

long fileEntryTypeId = ParamUtil.getLong(request, "fileEntryTypeId", -1);

if ((fileEntryTypeId == -1) && (fileVersion.getModel() instanceof DLFileVersion)) {
	DLFileVersion dlFileVersion = (DLFileVersion)fileVersion.getModel();

	fileEntryTypeId = dlFileVersion.getFileEntryTypeId();
}
%>

<div class="lfr-preview-file" id="<portlet:namespace />previewFile">
	<div class="lfr-preview-file-content" id="<portlet:namespace />previewFileContent">
		<div class="lfr-preview-file-image-current-column">
			<div class="lfr-preview-file-image-container">
				<canvas id="lfr-preview-file-image" />
			</div>
		</div>
	</div>
</div>

<liferay-portlet:actionURL varImpl="editImageURL">
	<liferay-portlet:param name="struts_action" value="/document_library/edit_image" />
</liferay-portlet:actionURL>

<aui:form action="<%= editImageURL %>" cssClass="lfr-dynamic-form" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveFileEntry();" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.EDIT %>" />
	<aui:input name="fileEntryId" type="hidden" value="<%= fileEntry.getFileEntryId() %>" />
	<aui:input name="fileEntryTypeId" type="hidden" value="<%= fileEntryTypeId %>" />
	<aui:input name="blob" type="hidden" />

	<aui:button name="hokiti pokiti" onClick='<%= renderResponse.getNamespace() + "randomImg();" %>' value="Hokiti Pokiti" />
	<aui:button name="saveButton" onClick='<%= renderResponse.getNamespace() + "saveImage();" %>' value="save" />
</aui:form>

<aui:script>
	var canvas = document.getElementById('lfr-preview-file-image'),
		width = 0,
		height = 0,
		ctx = canvas.getContext('2d'),
		img = new Image();

	img.onload = function() {
		width = img.width;
		height = img.height;
		canvas.setAttribute('width', width);
		canvas.setAttribute('height', height);
		ctx.drawImage(img, 0, 0);
	};

	img.src = '<%= DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, "&imagePreview=1") %>';

	function <portlet:namespace />saveImage() {
		var blob = canvas.toDataURL('<%= fileVersion.getMimeType() %>');

		var blobInput = document.getElementById('<portlet:namespace />blob');
		blobInput.setAttribute('value', blob);

		submitForm(document.<portlet:namespace />fm);
	}

	function <portlet:namespace />randomImg() {
		var imageData = ctx.getImageData(0, 0, width, height),
			data = imageData.data;

		var extraRed = Math.round((Math.random() -0.5) * 40),
			extraGreen = Math.round((Math.random() -0.5) * 40),
			extraBlue = Math.round((Math.random() -0.5) * 40);

		for (var i=0; i < data.length; i+=4) {
			var r = data[i] + extraRed;
			var g = data[i+1] + extraGreen;
			var b = data[i+2] + extraBlue;

			data[i] = r;
			data[i+1] = g;
			data[i+2] = b;
		}

		ctx.putImageData(imageData, 0, 0);
	}
</aui:script>