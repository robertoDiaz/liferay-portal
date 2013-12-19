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
%>

<div class="lfr-preview-file lfr-preview-image" id="<portlet:namespace />previewFile">
	<div class="lfr-preview-file-content lfr-preview-image-content" id="<portlet:namespace />previewFileContent">
		<div class="lfr-preview-file-image-current-column">
			<div class="lfr-preview-file-image-container image-processor">
				<img class="lfr-preview-file-image-current" src="<%= DLUtil.getPreviewURL(fileEntry, fileEntry.getLatestFileVersion(), themeDisplay, "&imagePreview=1") %>" />
			</div>
		</div>
	</div>
	<div class="toolbar"></div>
</div>

<%
PortletURL editFileEntryURL = renderResponse.createActionURL();

editFileEntryURL.setParameter("struts_action", "/document_library/edit_file_entry");
%>

<aui:form action="<%= editFileEntryURL %>" cssClass="lfr-dynamic-form" enctype="multipart/form-data" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveFileEntry();" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.INLINE_EDIT %>" />
	<aui:input name="fileEntryId" type="hidden" value="<%= fileEntry.getFileEntryId() %>" />
	<aui:input name="blob" type="hidden" />

	<aui:button name="saveButton" value="save" />
</aui:form>

<aui:script use="aui-image-editor">
	var imageEditor = new A.ImageEditor({
        srcNode: '#<portlet:namespace />previewFile'
    }).render();

    A.one('#<portlet:namespace />saveButton').on('click', function(event) {
        A.one('#<portlet:namespace />blob').val(imageEditor.getImageData());
        submitForm(document.<portlet:namespace />fm);
    });
</aui:script>