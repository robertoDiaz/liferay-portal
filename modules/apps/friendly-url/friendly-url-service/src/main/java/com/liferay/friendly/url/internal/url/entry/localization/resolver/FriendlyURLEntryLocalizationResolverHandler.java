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

package com.liferay.friendly.url.internal.url.entry.localization.resolver;

import com.liferay.friendly.url.entry.localization.resolver.FriendlyURLEntryLocalizationResolver;
import com.liferay.friendly.url.entry.localization.resolver.configuration.FriendlyURLEntryLocalizationResolverConfiguration;
import com.liferay.friendly.url.entry.localization.resolver.FriendlyURLEntryLocalizationResolverPolicy;
import com.liferay.friendly.url.model.FriendlyURLEntryLocalization;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMapFactory;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.NoSuchGroupException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.util.PortalInstances;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Roberto Díaz
 */
@Component(
	configurationPid = "com.liferay.friendly.url.entry.localization.resolver.configuration.FriendlyURLEntryLocalizationResolverConfiguration",
	immediate = true,
	service = FriendlyURLEntryLocalizationResolverHandler.class
)
public class FriendlyURLEntryLocalizationResolverHandler {

	public void resolvePath(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, String path)
		throws IOException, PortalException {

		long groupId = _getGroupId(httpServletRequest, path);

		FriendlyURLEntryLocalization friendlyURLEntryLocalization = null;

		for (FriendlyURLEntryLocalizationResolver
				modelLocalizedFriendlyURLResolver : _serviceTrackerList) {

			friendlyURLEntryLocalization =
				modelLocalizedFriendlyURLResolver.
					fetchFriendlyURLEntryLocalization(groupId, path);

			if (friendlyURLEntryLocalization != null) {
				break;
			}
		}

		if (friendlyURLEntryLocalization != null) {
			_friendlyURLEntryLocalizationResolverPolicy.applyPolicy(
				httpServletRequest, httpServletResponse,
				friendlyURLEntryLocalization, path);
		}
	}

	@Activate
	@Modified
	protected void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		_serviceTrackerMap = ServiceTrackerMapFactory.openSingleValueMap(
			bundleContext, FriendlyURLEntryLocalizationResolverPolicy.class,
			"friendly.url.localization.resolver.policy.name");

		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, FriendlyURLEntryLocalizationResolver.class);

		_friendlyURLEntryLocalizationResolverConfiguration =
			ConfigurableUtil.createConfigurable(
				FriendlyURLEntryLocalizationResolverConfiguration.class,
				properties);

		_friendlyURLEntryLocalizationResolverPolicy =
			_serviceTrackerMap.getService(
				_friendlyURLEntryLocalizationResolverConfiguration.
					friendlyURLEntryLocalizationResolverPolicyName());
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerMap.close();
		_serviceTrackerList.close();
	}

	private long _getGroupId(HttpServletRequest httpServletRequest, String path)
		throws PortalException {

		String friendlyURL = path;

		int pos = path.indexOf(CharPool.SLASH, 1);

		if (pos != -1) {
			friendlyURL = path.substring(0, pos);
		}

		long companyId = PortalInstances.getCompanyId(httpServletRequest);

		Group group = _groupLocalService.fetchFriendlyURLGroup(
			companyId, friendlyURL);

		if (group == null) {
			String screenName = friendlyURL.substring(1);

			User user = _userLocalService.fetchUserByScreenName(
				companyId, screenName);

			if (user != null) {
				group = user.getGroup();
			}
			else if (_log.isWarnEnabled()) {
				_log.warn("No user exists with friendly URL " + screenName);
			}
		}

		if (group == null) {
			StringBundler sb = new StringBundler(5);

			sb.append("{companyId=");
			sb.append(companyId);
			sb.append(", friendlyURL=");
			sb.append(friendlyURL);
			sb.append("}");

			throw new NoSuchGroupException(sb.toString());
		}

		return group.getGroupId();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FriendlyURLEntryLocalizationResolverHandler.class);

	private FriendlyURLEntryLocalizationResolverConfiguration
		_friendlyURLEntryLocalizationResolverConfiguration;
	private FriendlyURLEntryLocalizationResolverPolicy
		_friendlyURLEntryLocalizationResolverPolicy;

	@Reference
	private GroupLocalService _groupLocalService;

	private ServiceTrackerList
		<FriendlyURLEntryLocalizationResolver,
		 FriendlyURLEntryLocalizationResolver> _serviceTrackerList;
	private ServiceTrackerMap
		<String, FriendlyURLEntryLocalizationResolverPolicy> _serviceTrackerMap;

	@Reference
	private UserLocalService _userLocalService;

}