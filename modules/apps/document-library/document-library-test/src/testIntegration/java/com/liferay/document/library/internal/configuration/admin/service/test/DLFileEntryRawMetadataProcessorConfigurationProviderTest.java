/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.internal.configuration.admin.service.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.configuration.DLFileEntryRawMetadataProcessorConfiguration;
import com.liferay.document.library.configuration.DLFileEntryRawMetadataProcessorConfigurationProvider;
import com.liferay.petra.function.UnsafeRunnable;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.test.util.ConfigurationTemporarySwapper;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Dictionary;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * @author Stefano Motta
 */
@RunWith(Arquillian.class)
public class DLFileEntryRawMetadataProcessorConfigurationProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetCompanyGroupAndSystemValues() throws Exception {
		try (ConfigurationTemporarySwapper configurationTemporarySwapper =
				new ConfigurationTemporarySwapper(
					DLFileEntryRawMetadataProcessorConfiguration.class.
						getName(),
					_createDictionary(
						new String[] {ContentTypes.APPLICATION_TEXT}))) {

			_withCompanyConfiguration(
				new String[] {ContentTypes.APPLICATION_ZIP},
				() -> _withGroupConfiguration(
					new String[] {ContentTypes.APPLICATION_JSON},
					() -> {
						String[] excludedMimeTypes =
							_dlFileEntryRawMetadataProcessorConfigurationProvider.
								getCompanyExcludedMimeTypes(
									TestPropsValues.getCompanyId());

						Assert.assertEquals(2, excludedMimeTypes.length);
						Assert.assertTrue(
							ArrayUtil.contains(
								excludedMimeTypes,
								ContentTypes.APPLICATION_TEXT) &&
							ArrayUtil.contains(
								excludedMimeTypes,
								ContentTypes.APPLICATION_ZIP));

						excludedMimeTypes =
							_dlFileEntryRawMetadataProcessorConfigurationProvider.
								getGroupExcludedMimeTypes(
									TestPropsValues.getGroupId());

						Assert.assertEquals(3, excludedMimeTypes.length);
						Assert.assertTrue(
							ArrayUtil.contains(
								excludedMimeTypes,
								ContentTypes.APPLICATION_JSON) &&
							ArrayUtil.contains(
								excludedMimeTypes,
								ContentTypes.APPLICATION_TEXT) &&
							ArrayUtil.contains(
								excludedMimeTypes,
								ContentTypes.APPLICATION_ZIP));

						excludedMimeTypes =
							_dlFileEntryRawMetadataProcessorConfigurationProvider.
								getSystemExcludedMimeTypes();

						Assert.assertEquals(1, excludedMimeTypes.length);
						Assert.assertTrue(
							ArrayUtil.contains(
								excludedMimeTypes,
								ContentTypes.APPLICATION_TEXT));
					}));
		}
	}

	private HashMapDictionary<String, Object> _createDictionary(
		String[] excludedMimeTypes) {

		return HashMapDictionaryBuilder.<String, Object>put(
			"excludedMimeTypes", excludedMimeTypes
		).build();
	}

	private <E extends Exception> void _withCompanyConfiguration(
			String[] excludedMimeTypes, UnsafeRunnable<E> unsafeRunnable)
		throws Exception {

		Dictionary<String, Object> properties = _createDictionary(
			excludedMimeTypes);

		properties.put("companyId", TestPropsValues.getCompanyId());

		_withConfiguration(properties, unsafeRunnable);
	}

	private <E extends Exception> void _withConfiguration(
			Dictionary<String, Object> properties,
			UnsafeRunnable<E> unsafeRunnable)
		throws Exception {

		Configuration configuration =
			_configurationAdmin.createFactoryConfiguration(
				DLFileEntryRawMetadataProcessorConfiguration.class.getName() +
					".scoped",
				StringPool.QUESTION);

		try {
			configuration.update(properties);

			unsafeRunnable.run();
		}
		finally {
			configuration.delete();
		}
	}

	private <E extends Exception> void _withGroupConfiguration(
			String[] excludedMimeTypes, UnsafeRunnable<E> unsafeRunnable)
		throws Exception {

		Dictionary<String, Object> properties = _createDictionary(
			excludedMimeTypes);

		properties.put("groupId", TestPropsValues.getGroupId());

		_withConfiguration(properties, unsafeRunnable);
	}

	@Inject
	private ConfigurationAdmin _configurationAdmin;

	@Inject
	private DLFileEntryRawMetadataProcessorConfigurationProvider
		_dlFileEntryRawMetadataProcessorConfigurationProvider;

}