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

<%@ include file="/html/portlet/sites_directory/init.jsp" %>

<%
String tabs2 = ParamUtil.getString(request, "tabs2", "display-settings");

String redirect = ParamUtil.getString(request, "redirect");

String emailFromName = ParamUtil.getString(request, "preferences--emailFromName--", SitesUtil.getEmailFromName(preferences, company.getCompanyId()));
String emailFromAddress = ParamUtil.getString(request, "preferences--emailFromAddress--", SitesUtil.getEmailFromAddress(preferences, company.getCompanyId()));

boolean emailMembershipReplyEnabled = ParamUtil.getBoolean(request, "preferences--emailMembershipReplyEnabled--", SitesUtil.getEmailMembershipReplyEnabled(preferences));
boolean emailMembershipRequestEnabled = ParamUtil.getBoolean(request, "preferences--emailMembershipRequestEnabled--", SitesUtil.getEmailMembershipRequestEnabled(preferences));

String emailParam = StringPool.BLANK;
String defaultEmailSubject = StringPool.BLANK;
String defaultEmailBody = StringPool.BLANK;

if (tabs2.equals("email-membership-reply")) {
	emailParam = "emailMembershipReply";
	defaultEmailSubject = ContentUtil.get(PropsValues.SITES_EMAIL_MEMBERSHIP_REPLY_SUBJECT);
	defaultEmailBody = ContentUtil.get(PropsValues.SITES_EMAIL_MEMBERSHIP_REPLY_BODY);
}
else if (tabs2.equals("email-membership-request")) {
	emailParam = "emailMembershipRequest";
	defaultEmailSubject = ContentUtil.get(PropsValues.SITES_EMAIL_MEMBERSHIP_REQUEST_SUBJECT);
	defaultEmailBody = ContentUtil.get(PropsValues.SITES_EMAIL_MEMBERSHIP_REQUEST_BODY);
}

String emailSubjectParam = emailParam + "Subject";
String emailBodyParam = emailParam + "Body";

String emailSubject = PrefsParamUtil.getString(preferences, request, emailSubjectParam, defaultEmailSubject);
String emailBody = PrefsParamUtil.getString(preferences, request, emailBodyParam, defaultEmailBody);
%>

<liferay-portlet:renderURL portletConfiguration="true" var="portletURL">
	<portlet:param name="tabs2" value="<%= tabs2 %>" />
	<portlet:param name="redirect" value="<%= redirect %>" />
</liferay-portlet:renderURL>

<aui:row>
	<aui:col width="<%= 50 %>">
		<liferay-portlet:actionURL portletConfiguration="true" var="configurationURL" />

		<aui:form action="<%= configurationURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveConfiguration();" %>'>
			<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

			<%
			String tabs2Names = "display-settings,email-from,email-membership-reply,email-membership-request";
			%>

			<liferay-ui:tabs
				names="<%= tabs2Names %>"
				param="tabs2"
				url="<%= portletURL %>"
			/>
			<c:choose>
				<c:when test='<%= tabs2.equals("email-from") %>'>
					<aui:fieldset>
						<aui:input cssClass="lfr-input-text-container" label="name" name="preferences--emailFromName--" value="<%= emailFromName %>" />

						<aui:input cssClass="lfr-input-text-container" label="address" name="preferences--emailFromAddress--" value="<%= emailFromAddress %>" />
					</aui:fieldset>

					<div class="definition-of-terms">
						<h4><liferay-ui:message key="definition-of-terms" /></h4>

						<dl>
							<dt>
								[$BLOGS_ENTRY_STATUS_BY_USER_NAME$]
							</dt>
							<dd>
								<liferay-ui:message key="the-user-who-updated-the-blog-entry" />
							</dd>
							<dt>
								[$BLOGS_ENTRY_USER_ADDRESS$]
							</dt>
							<dd>
								<liferay-ui:message key="the-email-address-of-the-user-who-added-the-blog-entry" />
							</dd>
							<dt>
								[$BLOGS_ENTRY_USER_NAME$]
							</dt>
							<dd>
								<liferay-ui:message key="the-user-who-added-the-blog-entry" />
							</dd>
							<dt>
								[$COMPANY_ID$]
							</dt>
							<dd>
								<liferay-ui:message key="the-company-id-associated-with-the-blog" />
							</dd>
							<dt>
								[$COMPANY_MX$]
							</dt>
							<dd>
								<liferay-ui:message key="the-company-mx-associated-with-the-blog" />
							</dd>
							<dt>
								[$COMPANY_NAME$]
							</dt>
							<dd>
								<liferay-ui:message key="the-company-name-associated-with-the-blog" />
							</dd>
							<dt>
								[$PORTLET_NAME$]
							</dt>
							<dd>
								<%= PortalUtil.getPortletTitle(renderResponse) %>
							</dd>
							<dt>
								[$SITE_NAME$]
							</dt>
							<dd>
								<liferay-ui:message key="the-site-name-associated-with-the-blog" />
							</dd>
						</dl>
					</div>
				</c:when>
				<c:when test='<%= tabs2.startsWith("email-membership-") %>'>
					<aui:fieldset>
						<c:choose>
							<c:when test='<%= tabs2.equals("email-membership-reply") %>'>
								<aui:input label="enabled" name="preferences--emailMembershipReplyEnabled--" type="checkbox" value="<%= emailMembershipReplyEnabled %>" />
							</c:when>
							<c:when test='<%= tabs2.equals("email-membership-request") %>'>
								<aui:input label="enabled" name="preferences--emailMembershipRequestEnabled--" type="checkbox" value="<%= emailMembershipRequestEnabled %>" />
							</c:when>
						</c:choose>

						<aui:input cssClass="lfr-input-text-container" label="subject" name='<%= "preferences--" + emailSubjectParam + "--" %>' value="<%= emailSubject %>" />

						<aui:field-wrapper label="body">
							<liferay-ui:input-editor editorImpl="<%= EDITOR_WYSIWYG_IMPL_KEY %>" />

							<aui:input name='<%= "preferences--" + emailBodyParam + "--" %>' type="hidden" />
						</aui:field-wrapper>
					</aui:fieldset>

					<div class="definition-of-terms">
						<h4><liferay-ui:message key="definition-of-terms" /></h4>

						<dl>
							<dt>
								[$BLOGS_ENTRY_USER_ADDRESS$]
							</dt>
							<dd>
								<liferay-ui:message key="the-email-address-of-the-user-who-added-the-blog-entry" />
							</dd>
							<dt>
								[$BLOGS_ENTRY_USER_NAME$]
							</dt>
							<dd>
								<liferay-ui:message key="the-user-who-added-the-blog-entry" />
							</dd>
							<dt>
								[$BLOGS_ENTRY_URL$]
							</dt>
							<dd>
								<liferay-ui:message key="the-blog-entry-url" />
							</dd>
							<dt>
								[$COMPANY_ID$]
							</dt>
							<dd>
								<liferay-ui:message key="the-company-id-associated-with-the-blog" />
							</dd>
							<dt>
								[$COMPANY_MX$]
							</dt>
							<dd>
								<liferay-ui:message key="the-company-mx-associated-with-the-blog" />
							</dd>
							<dt>
								[$COMPANY_NAME$]
							</dt>
							<dd>
								<liferay-ui:message key="the-company-name-associated-with-the-blog" />
							</dd>
							<dt>
								[$FROM_ADDRESS$]
							</dt>
							<dd>
								<%= HtmlUtil.escape(emailFromAddress) %>
							</dd>
							<dt>
								[$FROM_NAME$]
							</dt>
							<dd>
								<%= HtmlUtil.escape(emailFromName) %>
							</dd>
							<dt>
								[$PORTAL_URL$]
							</dt>
							<dd>
								<%= company.getVirtualHostname() %>
							</dd>
							<dt>
								[$PORTLET_NAME$]
							</dt>
							<dd>
								<%= PortalUtil.getPortletTitle(renderResponse) %>
							</dd>
							<dt>
								[$SITE_NAME$]
							</dt>
							<dd>
								<liferay-ui:message key="the-site-name-associated-with-the-blog" />
							</dd>
							<dt>
								[$TO_ADDRESS$]
							</dt>
							<dd>
								<liferay-ui:message key="the-address-of-the-email-recipient" />
							</dd>
							<dt>
								[$TO_NAME$]
							</dt>
							<dd>
								<liferay-ui:message key="the-name-of-the-email-recipient" />
							</dd>
						</dl>
					</div>

				</c:when>
				<c:when test='<%= tabs2.equals("display-settings") %>'>
					<%@ include file="/html/portlet/sites_directory/display_settings.jspf" %>
				</c:when>
			</c:choose>
			<aui:button-row>
				<aui:button type="submit" />
			</aui:button-row>
		</aui:form>
	</aui:col>
	<aui:col width="<%= 50 %>">
		<liferay-portlet:preview
			portletName="<%= portletResource %>"
			queryString="struts_action=/navigation/view"
			showBorders="<%= true %>"
		/>
	</aui:col>
</aui:row>

<aui:script>
	function <portlet:namespace />initEditor() {
		return "<%= UnicodeFormatter.toString(emailBody) %>";
	}

	function <portlet:namespace />updateLanguage() {
		document.<portlet:namespace />fm.<portlet:namespace /><%= Constants.CMD %>.value = '';
		submitForm(document.<portlet:namespace />fm);
	}

	Liferay.provide(
		window,
		'<portlet:namespace />saveConfiguration',
		function() {
			<c:if test='<%= tabs2.startsWith("email-membership-") %>'>
				document.<portlet:namespace />fm.<portlet:namespace /><%= emailBodyParam %>.value = window.<portlet:namespace />editor.getHTML();
			</c:if>

			submitForm(document.<portlet:namespace />fm);
		},
		['liferay-util-list-fields']
	);
</aui:script>

<%!
public static final String EDITOR_WYSIWYG_IMPL_KEY = "editor.wysiwyg.portal-web.docroot.html.portlet.site_directory.configuration.jsp";
%>