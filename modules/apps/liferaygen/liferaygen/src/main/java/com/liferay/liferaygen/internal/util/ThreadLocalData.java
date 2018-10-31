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

package com.liferay.liferaygen.internal.util;

import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.NotificationThreadLocal;
import com.liferay.portal.kernel.util.TimeZoneThreadLocal;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public class ThreadLocalData {

	public static ThreadLocalData getThreadLocalData() {
		return new ThreadLocalData();
	}

	public void setThreadLocalData() {
		CompanyThreadLocal.setCompanyId(_companyId);

		PermissionThreadLocal.setPermissionChecker(_permissionChecker);

		PermissionThreadLocal.setAddResource(_permissionAddResource);

		PrincipalThreadLocal.setName(_principalName);

		PrincipalThreadLocal.setPassword(_principalPassword);

		LocaleThreadLocal.setDefaultLocale(_defaultLocale);

		LocaleThreadLocal.setThemeDisplayLocale(_themeDisplayLocale);

		NotificationThreadLocal.setEnabled(_notification);

		TimeZoneThreadLocal.setThemeDisplayTimeZone(_themeDisplayTimeZone);

		ServiceContextThreadLocal.pushServiceContext(_serviceContext);
	}

	private ThreadLocalData() {
		_companyId = CompanyThreadLocal.getCompanyId();

		_permissionChecker = PermissionThreadLocal.getPermissionChecker();

		_permissionAddResource = PermissionThreadLocal.isAddResource();

		_principalName = PrincipalThreadLocal.getName();

		_principalPassword = PrincipalThreadLocal.getPassword();

		_defaultLocale = LocaleThreadLocal.getDefaultLocale();

		_themeDisplayLocale = LocaleThreadLocal.getThemeDisplayLocale();

		_notification = NotificationThreadLocal.isEnabled();

		_themeDisplayTimeZone = TimeZoneThreadLocal.getThemeDisplayTimeZone();

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		if (serviceContext == null) {
			_serviceContext = new ServiceContext();
		}
		else {
			_serviceContext = (ServiceContext)serviceContext.clone();
		}
	}

	private final Long _companyId;
	private final Locale _defaultLocale;
	private final boolean _notification;
	private final boolean _permissionAddResource;
	private final PermissionChecker _permissionChecker;
	private final String _principalName;
	private final String _principalPassword;
	private final ServiceContext _serviceContext;
	private final Locale _themeDisplayLocale;
	private final TimeZone _themeDisplayTimeZone;

}