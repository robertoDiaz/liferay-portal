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

<%@ include file="/html/portlet/document_library/view_file_entry.jsp" %>

<%@page import="com.liferay.portal.repository.liferayrepository.model.LiferayFileVersion" %>

<liferay-portlet:actionURL varImpl="editImageURL">
	<liferay-portlet:param name="struts_action" value="/document_library/edit_image" />
</liferay-portlet:actionURL>

<aui:form action="<%= editImageURL %>" cssClass="lfr-dynamic-form" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveFileEntry();" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.EDIT %>" />
	<aui:input name="fileEntryId" type="hidden" value="<%= fileEntry.getFileEntryId() %>" />
	<aui:input name="fileEntryTypeId" type="hidden" value="<%= fileEntryTypeId %>" />
	<aui:input name="blob" type="hidden" />

	<aui:button name="saveButton" value="save" />
</aui:form>

<aui:script use="aui-image-editor">
	var imageEditor = new A.ImageEditor({
		srcNode: '.lfr-preview-file-image-container'
	}).render();

	A.one('#<portlet:namespace />saveButton').on('click', function(event) {
		A.one('#<portlet:namespace />blob').val(imageEditor.getImageData());
		submitForm(document.<portlet:namespace />fm);
	});
</aui:script>

<%!
private boolean _isEditableImage(FileVersion fileVersion)
	throws SystemException {

	if (fileVersion instanceof LiferayFileVersion) {
		String[] editableMimeTypes = {"image/jpeg", "image/png"};

		return ArrayUtil.contains(
			editableMimeTypes, fileVersion.getMimeType());
	}

	return false;
}

private static Log _log = LogFactoryUtil.getLog("portal-web.docroot.html.portlet.document_library.edit_file_entry_jsp");
%>

<style type="text/css">
	.image-editor-base {
	    display: inline-block;
	    position: relative;
	}

	.image-editor-base canvas {
	    display: block;
	    position: absolute;
	    top: 0;
	}

	.image-editor-base .image-editor-processors {
	    bottom: 0;
	    position: absolute;
	    width: 100%;
	}

	.image-editor-base .image-editor-processors .image-processor-status {
	    background-color: #000000;
	    background-color: rgba(0, 0, 0, 0.4);
	    color: #FFFFFF;
	}

	.image-editor-base .image-processor-info, .image-editor-base .image-processor-widgets {
	    text-align: center;
	}

	.image-editor-base .image-processor-swf {
	    position: absolute;
	}

	.image-editor-base .image-editor-trigger-default, .image-editor-base .toolbar {
	    bottom: 10px;
	    left: 10px;
	    position: absolute;
	}
</style>