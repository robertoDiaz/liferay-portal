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

import com.liferay.liferaygen.config.ActionConfig;
import com.liferay.liferaygen.util.ParameterUtil;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletPreferences;
public class AddPortletDocumentLibrary extends AddPortlet {

	@Override
	public void configure(Map<String, Object> parameters) {

		parameters.put("portletId", "20");

		super.configure(parameters);
	}

	@Override
	public String doGetDescription() {
		return "Adds a random document library portlet in a random layout in " +
				"a random position. Portlet configuration will be random. " +
				"Layout can be specified in parameters";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("nonDefaultParentFolderRatio", 20);
				put("layoutId", null);
				put("plid", null);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					"nonDefaultParentFolderRatio",
					"Probability percentage of creating a document library " +
					"portlet with a selected random child folder");
				put(
					"layoutId",
					"Force adding portlet to the layout with this layoutId, " +
					"instead of using target parameter");
				put("plid", "Same as layoutId");
				put(ActionConfig.TARGET, "Layout to use during add action");
			}
		};
	}

	@Override
	protected void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception {

		List<String> preferences =
			Arrays.asList(
				"enableCommentRatings","enableRelatedAssets",
				"showFoldersSearch");

		setRandomBooleanPreferences(portletPreferences, preferences);

		portletPreferences.setValue("displayViews", getSelectedDisplayViews());

		portletPreferences.setValue("entryColumns", getSelectedEntryColumns());

		portletPreferences.setValue(
			"entriesPerPage", ValueGenerator.getRandomObjectFromArray(
				PrefsPropsUtil.getStringArray(
					PropsKeys.SEARCH_CONTAINER_PAGE_DELTA_VALUES,
					StringPool.COMMA)));

		int nonDefaultParentFolderRatio =
			ParameterUtil.getParamAsIntegerPercentage(
				_parameters, "nonDefaultParentFolderRatio");

		Long folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		if ((nonDefaultParentFolderRatio > 0) &&
			ValueGenerator.getBoolean(nonDefaultParentFolderRatio)) {

			folderId = (Long)ValueGenerator.getRandomObjectProperties(
				DLFolder.class.getName(), "folderId",
				this.getFilterByGroupId());
		}

		portletPreferences.setValue("rootFolderId", String.valueOf(folderId));

		portletPreferences.store();
	}

	private String getSelectedDisplayViews() throws SystemException {
		String[] displayViews = PrefsPropsUtil.getStringArray(
				PropsKeys.DL_DISPLAY_VIEWS, StringPool.COMMA);

		long numberOfViewsToSelect = ValueGenerator.getRandomLongFromRange(
				1, displayViews.length);

		List<String> selectedDisplayViews =
			ValueGenerator.getRandomObjectsFromArray(
				displayViews, numberOfViewsToSelect);

		String displayView = StringUtil.merge(selectedDisplayViews);
		return displayView;
	}

	private String getSelectedEntryColumns() {
		List<String> availableEntryColumns = Arrays.asList(
			"size,downloads,action,modified-date,create-date".split(","));

		long numberOfColumnsToSelect = ValueGenerator.getRandomLongFromRange(
			1, availableEntryColumns.size());

		List<String> selectedEntryColumnsList =
			ValueGenerator.getRandomObjectsFromList(
				availableEntryColumns, numberOfColumnsToSelect);

		if (selectedEntryColumnsList.isEmpty()) {
			return "name";
		}

		return "name," + StringUtil.merge(selectedEntryColumnsList);
	}

}