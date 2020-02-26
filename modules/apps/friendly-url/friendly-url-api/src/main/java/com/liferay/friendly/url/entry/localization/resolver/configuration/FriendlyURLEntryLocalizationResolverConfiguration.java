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

package com.liferay.friendly.url.entry.localization.resolver.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Roberto Díaz
 */
@ExtendedObjectClassDefinition(category = "localization")
@Meta.OCD(
	id = "com.liferay.friendly.url.entry.localization.resolver.configuration.FriendlyURLEntryLocalizationResolverConfiguration",
	localization = "content/Language",
	name = "friendly-url-localization-resolver-configuration-name"
)
public interface FriendlyURLEntryLocalizationResolverConfiguration {

	@Meta.AD(
		deflt = "default",
		name = "friendly-url-localization-resolver-policy-name",
		required = false
	)
	public String friendlyURLEntryLocalizationResolverPolicyName();

}