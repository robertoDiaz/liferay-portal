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

package com.liferay.friendly.url.internal.url.entry.localization.resolver.policy;

import com.liferay.friendly.url.entry.localization.resolver.FriendlyURLEntryLocalizationResolverPolicy;
import com.liferay.friendly.url.model.FriendlyURLEntryLocalization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	property = "friendly.url.localization.resolver.policy.name=default",
	service = FriendlyURLEntryLocalizationResolverPolicy.class
)
public class DefaultFriendlyURLEntryLocalizationResolverPolicy
	implements FriendlyURLEntryLocalizationResolverPolicy {

	@Override
	public void applyPolicy(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			FriendlyURLEntryLocalization friendlyURLEntryLocalization,
			String path) {
	}

}