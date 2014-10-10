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

package com.liferay.portal.subscriptionsender;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.security.permission.PermissionChecker;

import java.io.Serializable;

import java.util.Map;

/**
 * @author Roberto Díaz
 */
public interface SubscriptionSender extends Serializable {

	public Boolean contains(
			PermissionChecker permissionChecker, String className, long classPK,
			String actionId)
		throws PortalException;

	public String getServiceName();

	public void notify(BaseModel baseModel, String entryURL)
		throws PortalException;

	public void setSubscriptionSenderContext(
		Map<String, Object> subscriptionSenderContext);

}