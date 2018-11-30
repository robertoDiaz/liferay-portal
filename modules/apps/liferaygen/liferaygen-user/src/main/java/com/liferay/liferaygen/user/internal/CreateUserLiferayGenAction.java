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

package com.liferay.liferaygen.user.internal;

import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.user.internal.CreateUserLiferayGenAction",
	service = LiferayGenAction.class
)
public class CreateUserLiferayGenAction extends BaseLiferayGenAction {

	@Override
	public String doGetDescription() {
		return "Creates a random user";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("emailDomain", _DOMAIN);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					"emailDomain",
					"Domain to be used for generating emails the generated " +
						"email will be [screenName]@[emailDomain]");
			}
		};
	}

	@Override
	public String getName() {
		return "CreateUserLiferayGenAction";
	}

	@Override
	public boolean hasScopeByGroupId() {
		return false;
	}

	protected void doRun() {
		Map<String, Object> parameters = getParameters();

		long companyId = (Long)parameters.get(
			LiferayGenConfigConstants.COMPANY_ID);

		LiferayGenValueGenerator liferayGenValueGenerator =
			new LiferayGenValueGenerator(
				_companyLocalService, _liferayGenQueryHandler, _portal,
				_portletLocalService);

		String screenName = liferayGenValueGenerator.getRandomString(8);

		screenName = StringUtil.toLowerCase(screenName);

		String firstName = liferayGenValueGenerator.getLatinName(6);

		String lastName = liferayGenValueGenerator.getLatinName(8);

		String domain = MapUtil.getString(parameters, "emailDomain", _DOMAIN);

		String emailAddress = screenName + "@" + domain;

		Date birthdate = liferayGenValueGenerator.getRandomDate(
			new Date(0), new Date());

		Calendar birthCalendar = Calendar.getInstance();

		birthCalendar.setTime(birthdate);

		int birthdayDay = birthCalendar.get(Calendar.DAY_OF_MONTH);

		int birthdayMonth = birthCalendar.get(Calendar.MONTH);

		int birthdayYear = birthCalendar.get(Calendar.YEAR);

		boolean male = liferayGenValueGenerator.getBoolean();

		long[] organizationIds = new long[0];
		long[] groupIds = null;
		long[] roleIds = null;
		long[] userGroupIds = null;

		try {
			ServiceContext serviceContext = new ServiceContext();

			User user = _userLocalService.addUser(
				getRandomUserId(liferayGenValueGenerator), companyId, false,
				"password", "password", false, screenName, emailAddress, 0,
				StringPool.BLANK,
				liferayGenValueGenerator.getRandomObjectFromList(
					ListUtil.fromCollection(
						LanguageUtil.getAvailableLocales())),
				firstName, StringPool.BLANK, lastName, 0, 0, male,
				birthdayMonth, birthdayDay, birthdayYear, StringPool.BLANK,
				groupIds, organizationIds, roleIds, userGroupIds, false,
				serviceContext);

			long userId = user.getUserId();

			_userLocalService.updateEmailAddressVerified(userId, true);

			_userLocalService.updateAgreedToTermsOfUse(userId, true);

			_userLocalService.updatePassword(
				userId, screenName, screenName, false, true);

			_userLocalService.updateLastLogin(userId, "127.0.0.1");

			_userLocalService.updateReminderQuery(
				userId, "Was this user created with LiferayGen?", "Yes");

			_userLocalService.updatePortrait(
				userId,
				liferayGenValueGenerator.getImageText(
					StringUtil.upperCase(
						firstName.substring(0, 1) + lastName.substring(0, 1))));
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected long getRandomUserId(
			LiferayGenValueGenerator liferayGenValueGenerator)
		throws Exception {

		long companyId = CompanyThreadLocal.getCompanyId();

		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		conjunction.add(RestrictionsFactoryUtil.eq("defaultUser", false));
		conjunction.add(RestrictionsFactoryUtil.eq("companyId", companyId));
		conjunction.add(RestrictionsFactoryUtil.eq("status", 0));

		Object object = liferayGenValueGenerator.getRandomObjectProperties(
			User.class.getName(), "userId", conjunction);

		return GetterUtil.getLong(object);
	}

	private static final String _DOMAIN = "example.com";

	private static final Log _log = LogFactoryUtil.getLog(
		CreateUserLiferayGenAction.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private UserLocalService _userLocalService;

}