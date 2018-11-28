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

package com.liferay.liferaygen.internal.config;

import com.liferay.liferaygen.internal.config.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.internal.util.LiferayGenValueGenerator;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.yaml.snakeyaml.Yaml;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(immediate = true, service = LiferayGenConfigHandler.class)
public class LiferayGenConfigHandler {

	public Map<String, Object> getConfiguration(String configurationText)
		throws Exception {

		Map<String, Object> configuration = new HashMap<>();

		long companyId = _portal.getDefaultCompanyId();

		Group group = _groupLocalService.getGroup(
			companyId, GroupConstants.GUEST);

		configuration.put(LiferayGenConfigConstants.COMPANY_ID, companyId);

		configuration.put(
			LiferayGenConfigConstants.GROUP_ID, group.getGroupId());

		//TODO delete
		System.out.println(configurationText);

		Yaml yaml = new Yaml();

		@SuppressWarnings("unchecked")
		Map<String, Object> configurationMap = (Map<String, Object>)yaml.load(
			configurationText);

		configuration.putAll(configurationMap);

		String localesString = (String)configuration.get(
			LiferayGenConfigConstants.LOCALES);

		Locale[] locales = _getLocales(localesString);

		configuration.put(LiferayGenConfigConstants.LOCALES, locales);

		return configuration;
	}

	private Locale[] _getLocales(String localesString) {
		Set<Locale> availableLocales = LanguageUtil.getAvailableLocales();

		Locale[] availableLocalesArray = new Locale[availableLocales.size()];

		for (Locale availableLocale : availableLocales) {
			availableLocalesArray = ArrayUtil.append(
				availableLocalesArray, availableLocale);
		}

		Locale[] locales = null;

		if (Validator.isNull(localesString) || "all".equals(localesString)) {
			List<Locale> randomLocales =
				_liferayGenValueGenerator.getRandomObjectsFromArray(
					availableLocalesArray, availableLocalesArray.length, false);

			locales = randomLocales.toArray(new Locale[randomLocales.size()]);
		}
		else if ("random".equals(localesString)) {
			int randomLength = 0;

			if ((availableLocales != null) &&
				(availableLocalesArray.length > 0)) {

				randomLength =
					_liferayGenValueGenerator.getRandomIntegerFromRange(
						1, availableLocalesArray.length);
			}

			List<Locale> randomLocales =
				_liferayGenValueGenerator.getRandomObjectsFromArray(
					availableLocalesArray, randomLength, false);

			locales = randomLocales.toArray(new Locale[randomLocales.size()]);
		}
		else {
			String[] localesArray = StringUtil.split(localesString);

			List<Locale> localesList = new ArrayList<>();

			for (String localesArrayString : localesArray) {
				String[] locale = StringUtil.split(localesArrayString, "_");

				if (ArrayUtil.contains(
						availableLocalesArray,
						new Locale(locale[0], locale[1]))) {

					localesList.add(new Locale(locale[0], locale[1]));
				}
			}

			locales = localesList.toArray(new Locale[localesList.size()]);
		}

		if (!ArrayUtil.contains(locales, LocaleUtil.getDefault())) {
			locales = ArrayUtil.append(locales, LocaleUtil.getDefault());
		}

		return locales;
	}

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private LiferayGenValueGenerator _liferayGenValueGenerator;

	@Reference
	private Portal _portal;

}