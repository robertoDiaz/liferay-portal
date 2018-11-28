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

package com.liferay.liferaygen.web.internal.actions.portlet;

import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
public abstract class AddPortletAssetPublisherBase extends AddPortlet {

	@Override
	public void configure(Map<String, Object> parameters) {

		parameters.put("portletId", "101");

		super.configure(parameters);
	}

	@Override
	protected void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception {

		portletPreferences.setValue(
			"abstractLength", ValueGenerator.getRandomObjectFromArray(
				new String[] {"100","200","300","400","500"}));

		portletPreferences.setValue(
			"assetLinkBehavior", ValueGenerator.getRandomObjectFromArray(
				new String[] {"showFullContent","viewInPortlet"}));

		portletPreferences.setValue(
			"pageDelta", ValueGenerator.getRandomObjectFromArray(
				PrefsPropsUtil.getStringArray(
					PropsKeys.SEARCH_CONTAINER_PAGE_DELTA_VALUES,
					StringPool.COMMA)));

		portletPreferences.setValue(
			"paginationType", ValueGenerator.getRandomObjectFromArray(
				new String[] {"none","simple","regular"}));

		portletPreferences.setValue(
			"socialBookmarksDisplayPosition",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {"top","bottom"}));

		portletPreferences.setValue(
			"socialBookmarksDisplayStyle",
			ValueGenerator.getRandomObjectFromArray(
				new String[] {"simple","vertical","horizontal"}));

		List<String> preferences =
			Arrays.asList(
				"enableComments","enableCommentRatings","enableFlags",
				"enablePermissions","enablePrint","enableRatings",
				"enableRelatedAssets","enableSocialBookmarks",
				"enableTagBasedNavigation","enableViewCountIncrement",
				"excludeZeroViewCount","showAddContentButton","showAssetTitle",
				"showAvailableLocales","showContextLink",
				"showMetadataDescriptions");

		setRandomBooleanPreferences(portletPreferences, preferences);

		if (ValueGenerator.getBoolean(80)) {
			String[] metadataFields = new String[] {
				"create-date","modified-date","publish-date","expiration-date",
				"priority","author","view-count","categories","tags"};

			int numberOfMetadataFields =
				ValueGenerator.getRandomIntegerFromRange(
					0, metadataFields.length);

			List<String> selectedMetadataFields =
				ValueGenerator.getRandomObjectsFromArray(
					metadataFields, numberOfMetadataFields);

			portletPreferences.setValue(
				"metadataFields", StringUtil.merge(selectedMetadataFields));
		}

		portletPreferences.store();
	}

}