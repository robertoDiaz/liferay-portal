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

package com.liferay.liferaygen.web.internal.actions.portlet;

import com.liferay.liferaygen.impl.BaseAction;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.List;
import java.util.Locale;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.portlet.MockActionRequest;
public abstract class BasePortlet extends BaseAction {

	static {
		initUpdateLayoutActionMethod();
	}

	@Override
	public Criterion getEntityFilter() {
		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		addParameterLongCriterion(conjunction, "layoutId");
		addParameterLongCriterion(conjunction, "plid");

		conjunction.add(RestrictionsFactoryUtil.eq("type", "portlet"));
		conjunction.add(
			RestrictionsFactoryUtil.eq("layoutPrototypeLinkEnabled", false));

		return conjunction;
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return Layout.class;
	}

	@Override
	public boolean hasScopeByGroupId() {
		return true;
	}

	protected static void updateLayoutAction(
			Layout layout, User user, Locale locale, String cmd,
			String portletId, String columnId, int columnPos)
		throws Throwable {

		MockHttpServletRequest request =
			ValueGenerator.getMockHttpServletRequest(layout, user, locale);

		request.setParameter(Constants.CMD, cmd);
		request.setParameter("dataType", "json");
		request.setParameter("p_p_id", portletId);
		request.setParameter("p_p_col_id", columnId);
		request.setParameter("p_p_col_pos", String.valueOf(columnPos));

		if (_log.isDebugEnabled()) {
			_log.debug("cmd: " + cmd);
			_log.debug("portletId: " + portletId);
			_log.debug(
				"layout: groupId = " + layout.getGroupId() + " layoutId=" +
				layout.getLayoutId());
			_log.debug("user: "+user);
		}

		MockHttpServletResponse response = new MockHttpServletResponse();

		PermissionChecker previousPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();
		String previousPrincipalThreadLocalName =
			PrincipalThreadLocal.getName();

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		PermissionThreadLocal.setPermissionChecker(permissionChecker);
		PrincipalThreadLocal.setName(user.getUserId());

		try {
			_getJSONMethod.invoke(
				_updateLayoutAction, null, null, request, response);
		}
		catch (InvocationTargetException ite) {
			_log.error(ite);

			Throwable targetException = ite.getTargetException();

			if (targetException != null) {
				throw targetException;
			}
		}
		finally {
			PermissionThreadLocal.setPermissionChecker(
				previousPermissionChecker);
			PrincipalThreadLocal.setName(previousPrincipalThreadLocalName);
		}

		String content = response.getContentAsString();
		String errorMessage = response.getErrorMessage();
		long status = response.getStatus();

		if (status != HttpServletResponse.SC_OK) {
			_log.error(
				"Error executing updateLayoutAction with parameters groupId=" +
				layout.getGroupId() + " layoutId=" + layout.getLayoutId() +
				" portletId=" + portletId + " columnId=" + columnId +
				" columnPos=" + columnPos);
			_log.error("Response error: " + status + " - " + errorMessage);
			_log.error("Response content: " + content);
		}
		else if (_log.isDebugEnabled()) {
			_log.debug("Response content: " + content);
		}
	}

	protected Portlet getColumnPortlet(
			Layout layout, String columnId, int columnPos)
		throws PortalException, SystemException {

		List<Portlet> portlets = getColumnPortlets(layout, columnId);

		if (portlets.size()>columnPos) {
			return portlets.get(columnPos);
		}

		return null;
	}

	protected List<Portlet> getColumnPortlets(Layout layout, String columnId)
		throws PortalException, SystemException {

		LayoutTypePortlet ltp = (LayoutTypePortlet)layout.getLayoutType();

		return ltp.getAllPortlets(columnId);
	}

	protected int getColumnSize(Layout layout, String columnId)
		throws PortalException, SystemException {

		List<Portlet> portlets = getColumnPortlets(layout, columnId);

		return portlets.size();
	}

	protected Locale getLayoutDefaultLocale(Layout layout) {
		String languageId = LocalizationUtil.getDefaultLocale(
			layout.getTitle());

		Locale locale = LocaleUtil.fromLanguageId(languageId);
		return locale;
	}

	protected PortletPreferences getPortletPreferences(
			Layout layout, Portlet portlet)
		throws SystemException {

		return PortletPreferencesFactoryUtil.getLayoutPortletSetup(
			layout, portlet.getPortletId());
	}

	protected User getRandomUserWithPermissions(Layout layout, String portletId)
		throws PortalException, SystemException {

		long companyId = layout.getCompanyId();

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
				companyId, portletId);

		List<Long> userIds = ValueGenerator.getAllUserIdsFromCache();

		while (!userIds.isEmpty()) {
			Long userId = ValueGenerator.removeRandomObjectFromList(userIds);

			try {
				User user = UserLocalServiceUtil.fetchUser(userId);

				PermissionChecker permissionChecker =
						PermissionCheckerFactoryUtil.create(user);

				if (LayoutPermissionUtil.contains(
						permissionChecker, layout, ActionKeys.UPDATE) &&
					PortletPermissionUtil.contains(
						permissionChecker, layout, portlet,
						ActionKeys.ADD_TO_PAGE)) {

					return user;
				}
			}
			catch (Exception e) {
				if (_log.isDebugEnabled()) {
					_log.debug(e, e);
				}
			}
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"No user with permissions was found for portlet " +
					portletId + " and layout plid=" + layout.getLayoutId() +
						" groupId=" + layout.getGroupId());
		}

		return null;
	}

	protected void setPreferencesParameter(
			MockActionRequest actionRequest, String key, String value) {

		String prefix = DefaultConfigurationAction.PREFERENCES_PREFIX;
		key = prefix.concat(key).concat(StringPool.DOUBLE_DASH);
		actionRequest.setParameter(key, value);
	}

	protected void setRandomBooleanPreferences(
			PortletPreferences portletPreferences, List<String> preferences)
		throws ReadOnlyException {

		for (String preference : preferences) {
			portletPreferences.setValue(
				preference, Boolean.toString(ValueGenerator.getBoolean()));
		}
	}

	protected void validateLayout(long groupId, Layout layout) {

		if (layout == null) {
			throw new IllegalArgumentException("Layout is null");
		}

		if (layout.getGroupId() != groupId) {
			throw new IllegalArgumentException(
				"Layout with plid=" + layout.getPlid() + " don't belong to " +
					groupId);
		}

		if (!layout.isTypePortlet() ||
			!(layout.getLayoutType() instanceof LayoutTypePortlet)) {
				throw new IllegalArgumentException(
					"Layout with plid=" + layout.getPlid() + " is not of " +
					"type portlet");
		}

		if (layout.isLayoutPrototypeLinkActive()) {
			throw new IllegalArgumentException(
				"Layout with plid=" + layout.getPlid() + " is linked to a " +
				" page template");
		}

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		if (Validator.isNull(layoutTypePortlet.getLayoutTemplate().getName()) &&
			_log.isWarnEnabled()) {

			_log.warn(
				"Layout with plid=" + layout.getPlid() + " has no " +
				"layoutTemplate configured");
		}
	}

	private static void initUpdateLayoutActionMethod() {

		if (_updateLayoutAction != null) {
			return;
		}

		try {
			ClassLoader portalClassLoader =
				PortalClassLoaderUtil.getClassLoader();

			Class<?> _updateLayoutActionClass = portalClassLoader.loadClass(
				"com.liferay.portal.action.UpdateLayoutAction");

			for (Method method : _updateLayoutActionClass.getMethods()) {
				if ("getJSON".equals(method.getName())) {
					_getJSONMethod = method;

					break;
				}
			}

			_updateLayoutAction = _updateLayoutActionClass.newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void addParameterLongCriterion(
		Conjunction conjunction, String parameterName) {

		Object parameterValue = _parameters.get(parameterName);

		if (parameterValue != null) {
			conjunction.add(
				RestrictionsFactoryUtil.eq(
					parameterName, GetterUtil.getLong(parameterValue)));
		}
	}

	private static Log _log = LogFactoryUtil.getLog(BasePortlet.class);

	private static Method _getJSONMethod;
	private static Object _updateLayoutAction;

}