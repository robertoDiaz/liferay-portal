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
				<img class="lfr-preview-file-image-current" id="<portlet:namespace />previewFileImage" src="<%= DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, "&imagePreview=1") %>" />
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

	<aui:button name="saveButton" onClick='<%= renderResponse.getNamespace() + "saveImage();" %>' value="save" />
</aui:form>

<aui:script>
	function <portlet:namespace />saveImage() {
		submitForm(document.<portlet:namespace />fm);
	}
</aui:script>
