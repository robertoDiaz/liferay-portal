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

<aui:script>
	AUI({
		modules: {
			external: {
				fullpath: '/html/js/alloy/aui-image-editor/aui-image-editor.js',
				requires: ['aui-base']
			}
		}
	}).use('image-editor', function(A) {
		debugger;
	});
</aui:script>

<aui:script use="aui-image-editor">
	var imageEditor = new A.ImageEditor({
		srcNode: '#<portlet:namespace />previewFile'
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