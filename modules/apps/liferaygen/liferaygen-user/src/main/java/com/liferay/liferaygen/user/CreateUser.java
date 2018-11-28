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

package com.liferay.liferaygen.web.internal.actions.user;

import com.liferay.liferaygen.constants.ConfigConstants;
import com.liferay.liferaygen.impl.BaseAction;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.DuplicateUserScreenNameException;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
public class CreateUser extends BaseAction {

	@Override
	public String doGetDescription() {
		return "Creates a random user";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("emailDomain", DOMAIN);
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
	public boolean hasScopeByGroupId() {
		return false;
	}

	protected void doRun() {
		long companyId = (Long)_parameters.get(ConfigConstants.COMPANY_ID);

		String screenName = ValueGenerator.getRandomString(8);

		screenName = StringUtil.toLowerCase(screenName);

		String firstName = ValueGenerator.getLatinName(6);

		String lastName = ValueGenerator.getLatinName(8);

		String domain = MapUtil.getString(_parameters, "emailDomain", DOMAIN);

		String emailAddress = screenName + "@" + domain;

		Date birthdate = ValueGenerator.getRandomDate(new Date(0), new Date());

		Calendar birthCalendar = Calendar.getInstance();
		birthCalendar.setTime(birthdate);

		int birthdayDay = birthCalendar.get(Calendar.DAY_OF_MONTH);

		int birthdayMonth = birthCalendar.get(Calendar.MONTH);

		int birthdayYear = birthCalendar.get(Calendar.YEAR);

		boolean male = ValueGenerator.getBoolean();

		long[] organizationIds = new long[0];
		long[] groupIds = null;
		long[] roleIds = null;
		long[] userGroupIds = null;

		try {
			ServiceContext serviceContext = new ServiceContext();

			User user = UserLocalServiceUtil.addUser(
				getRandomUserId(), companyId, //companyId
				false, //autopassword
				"password", //password1
				"password", //password2
				false, //autoscreenname
				screenName, //screenname
				emailAddress, //emailAddress
				0, //facebookId
				StringPool.BLANK, //openId
				ValueGenerator.getRandomObjectFromList(
					ListUtil.fromArray(
						LanguageUtil.getAvailableLocales())), //locale
				firstName, //firstName
				StringPool.BLANK, //middleName
				lastName, //lastName
				0, //prefixId
				0, //suffixId
				male, //male
				birthdayMonth, //birthdayMonth
				birthdayDay, //birthdayDay
				birthdayYear, //birthdayYear
				StringPool.BLANK, //jobTitle
				groupIds, //groupIds
				organizationIds, //organizationIds
				roleIds, //roleIds
				userGroupIds, //usergroupIds
				false, //sendEmail
				serviceContext); //serviceContext

			long userId = user.getUserId();

			UserLocalServiceUtil.updateEmailAddressVerified(userId, true);

			UserLocalServiceUtil.updateAgreedToTermsOfUse(userId, true);

			UserLocalServiceUtil.updatePassword(
				userId, screenName, screenName, false, true);

			UserLocalServiceUtil.updateLastLogin(userId, "127.0.0.1");

			UserLocalServiceUtil.updateReminderQuery(
				userId, "Was this user created with LiferayGen?", "Yes");

			UserLocalServiceUtil.updatePortrait(
				userId,
				ValueGenerator.getImageText(
					StringUtil.upperCase(
						firstName.substring(0, 1) + lastName.substring(0, 1))));
		}
		catch (DuplicateUserScreenNameException e) {
			_log.error(e, e);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected long getRandomUserId() throws Exception {
		long companyId = CompanyThreadLocal.getCompanyId();

		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();
		conjunction.add(RestrictionsFactoryUtil.eq("defaultUser", false));
		conjunction.add(RestrictionsFactoryUtil.eq("companyId", companyId));
		conjunction.add(RestrictionsFactoryUtil.eq("status", 0));

		Object object = ValueGenerator.getRandomObjectProperties(
			User.class.getName(), "userId", conjunction);

		return GetterUtil.getLong(object, 0L);
	}

	private static final String DOMAIN = "example.com";

	private static Log _log = LogFactoryUtil.getLog(CreateUser.class);

}