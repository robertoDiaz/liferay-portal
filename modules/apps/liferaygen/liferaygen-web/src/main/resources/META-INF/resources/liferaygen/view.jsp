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

<%@ include file="/liferaygen/init.jsp" %>

<portlet:actionURL name="generate" var="generateURL" />

<aui:form action="<%= generateURL %>" method="post" name="fm">

<%
ClassLoader classLoader = ConfigUtil.class.getClassLoader();

InputStream inputStream = classLoader.getResourceAsStream("configuration.yml");

String configuration = StringUtil.read(inputStream);

String errorMessage = (String)request.getAttribute("errorMessage");
%>

	<aui:input cssClass="lfr-textarea-container" name="configuration" resizable="<%= true %>" style="width: 600px; height: 300px;" type="textarea" value="<%= configuration %>" />

	<aui:button name="generate" type="submit" value="Generate" />

	<br />

<%
	if (Validator.isNotNull(errorMessage)) {
%>

	<br />

	<aui:input cssClass="lfr-textarea-container" name="error" resizable="<%= true %>" style="width: 600px; height: 300px;" type="textarea" value="<%= errorMessage %>" />

<%
	}
%>

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
	List<Action> actionList = ExecutorUtil.getAvailableActions();

	for (Action action : actionList) {
%>

		<h4><%= action.getClass().getName() %></h4>
		<%= action.getDescription() %><br />

<%
	Map<String, String> parametersDescription = new TreeMap<String, String>(action.getParametersDescription());
	String targetDescription = parametersDescription.remove(ActionConfig.TARGET);
	if (!parametersDescription.isEmpty())
	{
%>

		<br /><u>Parameters</u>:
		<ul>

<%
		for (String parameter : parametersDescription.keySet()) {
			String parameterDescription = action.getParametersDescription().get(parameter);
			String defaultValue = "<i>&lt;empty&gt;</i>";
			Object defaultValueObj = action.getParametersDefaultValues().get(parameter);
			if (defaultValueObj != null) {
				defaultValue = defaultValueObj.toString();
			}
%>

			<li><i><%= parameter %></i>: (default: <%= defaultValue %>) <%= parameterDescription %></li>

<%
		}
%>

		</ul>

<%
	}
	if (parametersDescription.isEmpty()) {
%>

	<br />

<%
	}
	if (targetDescription != null) {
		String modelProperty = "";
		if (action.getEntityProperties() != null) {
			modelProperty = ":" + action.getEntityProperties();
		}
%>

		<u>Target param</u>: <%= targetDescription %> (<%= action.getEntityModel().getName() %><%= modelProperty %>)<br /><br />

<%
	}
}
%>

</aui:form>