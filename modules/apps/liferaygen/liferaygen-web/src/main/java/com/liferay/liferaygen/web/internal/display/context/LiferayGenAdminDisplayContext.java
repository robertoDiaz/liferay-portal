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

package com.liferay.liferaygen.web.internal.display.context;

import com.liferay.liferaygen.web.internal.constants.LiferayGenWebKeys;
import com.liferay.liferaygen.web.internal.helper.LiferayGenAdminHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Roberto Díaz
 */
public class LiferayGenAdminDisplayContext {

	public LiferayGenAdminDisplayContext(HttpServletRequest request) {
		_request = request;
	}

	public LiferayGenAdminHelper getLiferayGenAdminHelper() {
		return (LiferayGenAdminHelper)_request.getAttribute(
			LiferayGenWebKeys.LIFERAY_GEN_ADMIN_HELPER);
	}

	private final HttpServletRequest _request;

}