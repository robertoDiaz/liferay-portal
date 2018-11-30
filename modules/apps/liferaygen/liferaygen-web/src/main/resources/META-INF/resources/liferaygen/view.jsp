<%@ page import="com.liferay.portal.kernel.model.ClassedModel" %><%--
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

<%@ include file="/liferaygen/init.jsp" %>

<%
LiferayGenAdminDisplayContext liferayGenAdminDisplayContext = new LiferayGenAdminDisplayContext(request);

LiferayGenAdminHelper liferayGenAdminHelper = liferayGenAdminDisplayContext.getLiferayGenAdminHelper();
%>

<lifera:actionURL
	name="/liferaygen/edit_liferaygen_action"
	var="editLiferayGenActionURL"
/>

<div class="container-fluid-1280 entry-body">
	<aui:form action="<%= editLiferayGenActionURL %>" method="post" name="fm">

		<%
		/*Class<?> clazz = liferayGenAdminDisplayContext.getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		InputStream inputStream = classLoader.getResourceAsStream("configuration.yml");

		String configuration = StringUtil.read(inputStream);*/

		String configuration = "this is a test";

		String errorMessage = (String)request.getAttribute("errorMessage");
		%>

		<aui:input cssClass="lfr-textarea-container" name="configuration" resizable="<%= true %>" style="width: 600px; height: 300px;" type="textarea" value="<%= configuration %>" />

		<aui:button name="generate" type="submit" value="Generate" />

		<br />

		<c:if test="<%= Validator.isNotNull(errorMessage) %>">
			<br />

			<aui:input cssClass="lfr-textarea-container" name="error" resizable="<%= true %>" style="width: 600px; height: 300px;" type="textarea" value="<%= errorMessage %>" />
		</c:if>

	<h3>Configuration syntax example</h3>
	<pre>
	globalParameter1: value1
	globalParameter2: value2
	actions:
	&nbsp;&nbsp;- action: name.of.my.Action1
	&nbsp;&nbsp;&nbsp;&nbsp;numExecutions: 10
	&nbsp;&nbsp;&nbsp;&nbsp;parameters: {localParameter1: value1, localParameter2: value2}
	&nbsp;&nbsp;- action: name.of.my.Action2
	&nbsp;&nbsp;&nbsp;&nbsp;numExecutions: 80%
	&nbsp;&nbsp;- action: name.of.my.Action3
	&nbsp;&nbsp;&nbsp;&nbsp;numExecutions: 10
	&nbsp;&nbsp;&nbsp;&nbsp;repeatTarget: false
	&nbsp;&nbsp;&nbsp;&nbsp;parameters: {localParameter3: value3, localParameter4: value4}
	</pre>

	<h3>Available actions</h3>

		<%
		List<LiferayGenAction> liferayGenAdminHelperAvailableActions = liferayGenAdminHelper.getAvailableActions();

		for (LiferayGenAction liferayGenAction : liferayGenAdminHelperAvailableActions) {
		%>

			<h4><%= liferayGenAction.getName() %></h4>

			<%= liferayGenAction.getDescription() %><br />

			<%
			Map<String, String> liferayGenActionParametersDescription = liferayGenAction.getParametersDescription();

			Map<String, String> parametersDescription = new TreeMap<String, String>(liferayGenActionParametersDescription);

			String targetDescription = parametersDescription.remove(LiferayGenActionConfig.TARGET);
			%>

			<c:choose>
				<c:when test="<%= !parametersDescription.isEmpty() %>">
					<br />

					<u>Parameters</u>:

					<ul>

					<%
					for (String parameter : parametersDescription.keySet()) {
						String parameterDescription = liferayGenActionParametersDescription.get(parameter);

						String defaultValue = "<i>&lt;empty&gt;</i>";

						Map<String, Object> parametersDefaultValues = liferayGenAction.getParametersDefaultValues();

						Object defaultValueObj = parametersDefaultValues.get(parameter);

						if (defaultValueObj != null) {
							defaultValue = defaultValueObj.toString();
						}
					%>

						<li><i><%= parameter %></i>: (default: <%= defaultValue %>) <%= parameterDescription %></li>

					<%
					}
					%>

					</ul>
				</c:when>
				<c:otherwise>
					<br />

					<c:if test=" <%= targetDescription != null %>">

						<%
						String modelProperty = StringPool.BLANK;

						if (liferayGenAction.getEntityProperties() != null) {
							modelProperty = StringPool.COLON + liferayGenAction.getEntityProperties();
						}

						Class<? extends ClassedModel> entityModel = liferayGenAction.getEntityModel();
						%>

						<u>Target param</u>: <%= targetDescription %> (<%= entityModel.getName() %><%= modelProperty %>)<br /><br />
					</c:if>
				</c:otherwise>
			</c:choose>

		<%
		}
		%>

	</aui:form>
</div>