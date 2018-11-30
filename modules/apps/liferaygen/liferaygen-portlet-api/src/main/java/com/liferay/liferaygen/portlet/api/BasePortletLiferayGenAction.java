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

package com.liferay.liferaygen.portlet.api;

import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTemplate;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.LayoutPermissionUtil;
import com.liferay.portal.kernel.service.permission.PortletPermissionUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.portlet.MockActionRequest;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public abstract class BasePortletLiferayGenAction extends BaseLiferayGenAction {

	@Override
	public Criterion getEntityFilter() {
		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		_addParameterLongCriterion(conjunction, "layoutId");
		_addParameterLongCriterion(conjunction, "plid");

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

	protected Portlet getColumnPortlet(
		Layout layout, String columnId, int columnPos) {

		List<Portlet> portlets = getColumnPortlets(layout, columnId);

		if (portlets.size() > columnPos) {
			return portlets.get(columnPos);
		}

		return null;
	}

	protected List<Portlet> getColumnPortlets(Layout layout, String columnId) {
		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		return layoutTypePortlet.getAllPortlets(columnId);
	}

	protected int getColumnSize(Layout layout, String columnId) {
		List<Portlet> portlets = getColumnPortlets(layout, columnId);

		return portlets.size();
	}

	protected Locale getLayoutDefaultLocale(Layout layout) {
		String languageId = LocalizationUtil.getDefaultLanguageId(
			layout.getTitle());

		return LocaleUtil.fromLanguageId(languageId);
	}

	protected PortletPreferences getPortletPreferences(
		Layout layout, Portlet portlet) {

		return PortletPreferencesFactoryUtil.getLayoutPortletSetup(
			layout, portlet.getPortletId());
	}

	protected User getRandomUserWithPermissions(Layout layout, String portletId)
		throws PortalException {

		long companyId = layout.getCompanyId();

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			companyId, portletId);

		List<Long> userIds = liferayGenValueGenerator.getAllUserIdsFromCache();

		while (!userIds.isEmpty()) {
			Long userId = liferayGenValueGenerator.removeRandomObjectFromList(
				userIds);

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
				StringBundler.concat(
					"No user with permissions was found for portletId = ",
					portletId, " and layout plid = ", layout.getLayoutId(),
					" groupId = ", layout.getGroupId()));
		}

		return null;
	}

	protected void initUpdateLayoutActionMethod() {
		if (_updateLayoutAction != null) {
			return;
		}

		try {
			ClassLoader portalClassLoader =
				PortalClassLoaderUtil.getClassLoader();

			Class<?> updateLayoutActionClass = portalClassLoader.loadClass(
				"com.liferay.portal.action.UpdateLayoutAction");

			for (Method method : updateLayoutActionClass.getMethods()) {
				if ("getJSON".equals(method.getName())) {
					_getJSONMethod = method;

					break;
				}
			}

			_updateLayoutAction = updateLayoutActionClass.newInstance();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void setPreferencesParameter(
		MockActionRequest actionRequest, String key, String value) {

		key = StringBundler.concat(
			"preferences--", key, StringPool.DOUBLE_DASH);

		actionRequest.setParameter(key, value);
	}

	protected void setRandomBooleanPreferences(
			PortletPreferences portletPreferences, List<String> preferences)
		throws ReadOnlyException {

		for (String preference : preferences) {
			portletPreferences.setValue(
				preference,
				Boolean.toString(liferayGenValueGenerator.getBoolean()));
		}
	}

	protected void updateLayoutAction(
			Layout layout, User user, Locale locale, String cmd,
			String portletId, String columnId, int columnPos)
		throws Throwable {

		MockHttpServletRequest request =
			liferayGenValueGenerator.getMockHttpServletRequest(
				layout, user, locale);

		request.setParameter(Constants.CMD, cmd);
		request.setParameter("dataType", "json");
		request.setParameter("p_p_id", portletId);
		request.setParameter("p_p_col_id", columnId);
		request.setParameter("p_p_col_pos", String.valueOf(columnPos));

		if (_log.isDebugEnabled()) {
			_log.debug("cmd: " + cmd);
			_log.debug("portletId: " + portletId);
			_log.debug(
				StringBundler.concat(
					"layout: groupId = ", layout.getGroupId(), " layoutId = ",
					layout.getLayoutId()));
			_log.debug("user: " + user);
		}

		PermissionChecker previousPermissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		String previousPrincipalThreadLocalName =
			PrincipalThreadLocal.getName();

		MockHttpServletResponse response = new MockHttpServletResponse();

		try {
			PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(user);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);

			PrincipalThreadLocal.setName(user.getUserId());

			_getJSONMethod.invoke(
				_updateLayoutAction, null, null, request, response);
		}
		catch (InvocationTargetException ite) {
			_log.error(ite, ite);

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
				StringBundler.concat(
					"Error executing updateLayoutAction with parameters",
					"groupId = ", layout.getGroupId(), " layoutId = ",
					layout.getLayoutId(), " portletId = ", portletId,
					" columnId = ", columnId, " columnPos=", columnPos));
			_log.error(
				StringBundler.concat(
					"Response error: ", status, " - ", errorMessage));
			_log.error("Response content: " + content);
		}
		else if (_log.isDebugEnabled()) {
			_log.debug("Response content: " + content);
		}
	}

	protected void validateLayout(long groupId, Layout layout) {
		if (layout == null) {
			throw new IllegalArgumentException("Layout is null");
		}

		if (layout.getGroupId() != groupId) {
			throw new IllegalArgumentException(
				StringBundler.concat(
					"Layout with plid = ", layout.getPlid(),
					" does not belong to ", groupId));
		}

		if (!layout.isTypePortlet() ||
			!(layout.getLayoutType() instanceof LayoutTypePortlet)) {

			throw new IllegalArgumentException(
				StringBundler.concat(
					"Layout with plid = ", layout.getPlid(),
					" is not portlet type"));
		}

		if (layout.isLayoutPrototypeLinkActive()) {
			throw new IllegalArgumentException(
				StringBundler.concat(
					"Layout with plid = ", layout.getPlid(),
					" is linked to a page template"));
		}

		if (_log.isWarnEnabled()) {
			LayoutTypePortlet layoutTypePortlet =
				(LayoutTypePortlet)layout.getLayoutType();

			LayoutTemplate layoutTemplate =
				layoutTypePortlet.getLayoutTemplate();

			if (Validator.isNull(layoutTemplate.getName())) {
				_log.warn(
					StringBundler.concat(
						"Layout with plid = ", layout.getPlid(),
						" does not have a configured layoutTemplate"));
			}
		}
	}

	protected LiferayGenValueGenerator liferayGenValueGenerator;
	protected Map<String, Object> parameters;

	private void _addParameterLongCriterion(
		Conjunction conjunction, String parameterName) {

		Object parameterValue = parameters.get(parameterName);

		if (parameterValue != null) {
			conjunction.add(
				RestrictionsFactoryUtil.eq(
					parameterName, GetterUtil.getLong(parameterValue)));
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		BasePortletLiferayGenAction.class);

	private Method _getJSONMethod;
	private Object _updateLayoutAction;

}