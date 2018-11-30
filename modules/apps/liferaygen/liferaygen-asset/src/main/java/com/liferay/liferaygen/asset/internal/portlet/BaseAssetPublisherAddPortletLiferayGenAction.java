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

package com.liferay.liferaygen.asset.internal.portlet;

import com.liferay.liferaygen.portlet.api.BaseAddPortletLiferayGenAction;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public abstract class BaseAssetPublisherAddPortletLiferayGenAction
	extends BaseAddPortletLiferayGenAction {

	@Override
	public void configure(Map<String, Object> parameters) {
		parameters.put(
			"portletId", "com_liferay_asset_web_portlet_AssetPortlet");

		super.configure(parameters);
	}

	@Override
	protected void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception {

		portletPreferences.setValue(
			"abstractLength",
			liferayGenValueGenerator.getRandomObjectFromArray(
				new String[] {"100", "200", "300", "400", "500"}));

		portletPreferences.setValue(
			"assetLinkBehavior",
			liferayGenValueGenerator.getRandomObjectFromArray(
				new String[] {"showFullContent", "viewInPortlet"}));

		portletPreferences.setValue(
			"pageDelta",
			liferayGenValueGenerator.getRandomObjectFromArray(
				PrefsPropsUtil.getStringArray(
					PropsKeys.SEARCH_CONTAINER_PAGE_DELTA_VALUES,
					StringPool.COMMA)));

		portletPreferences.setValue(
			"paginationType",
			liferayGenValueGenerator.getRandomObjectFromArray(
				new String[] {"none", "simple", "regular"}));

		portletPreferences.setValue(
			"socialBookmarksDisplayPosition",
			liferayGenValueGenerator.getRandomObjectFromArray(
				new String[] {"top", "bottom"}));

		portletPreferences.setValue(
			"socialBookmarksDisplayStyle",
			liferayGenValueGenerator.getRandomObjectFromArray(
				new String[] {"simple", "vertical", "horizontal"}));

		List<String> preferences = Arrays.asList(
			"enableComments", "enableCommentRatings", "enableFlags",
			"enablePermissions", "enablePrint", "enableRatings",
			"enableRelatedAssets", "enableSocialBookmarks",
			"enableTagBasedNavigation", "enableViewCountIncrement",
			"excludeZeroViewCount", "showAddContentButton", "showAssetTitle",
			"showAvailableLocales", "showContextLink",
			"showMetadataDescriptions");

		setRandomBooleanPreferences(portletPreferences, preferences);

		if (liferayGenValueGenerator.getBoolean(80)) {
			String[] metadataFields = {
				"create-date", "modified-date", "publish-date",
				"expiration-date", "priority", "author", "view-count",
				"categories", "tags"
			};

			int numberOfMetadataFields =
				liferayGenValueGenerator.getRandomIntegerFromRange(
					0, metadataFields.length);

			List<String> selectedMetadataFields =
				liferayGenValueGenerator.getRandomObjectsFromArray(
					metadataFields, numberOfMetadataFields);

			portletPreferences.setValue(
				"metadataFields", StringUtil.merge(selectedMetadataFields));
		}

		portletPreferences.store();
	}

}