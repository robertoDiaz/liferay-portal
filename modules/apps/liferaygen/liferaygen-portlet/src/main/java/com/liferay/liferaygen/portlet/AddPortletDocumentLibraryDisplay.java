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
public class AddPortletDocumentLibraryDisplay extends AddPortlet {

	@Override
	public void configure(Map<String, Object> parameters) {

		parameters.put("portletId", "110");

		super.configure(parameters);
	}

	@Override
	public String doGetDescription() {
		return "Adds a random document library display portlet in a random " +
			"layout in a random position. Portlet configuration will be " +
				"random. Layout can be specified in parameters";
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
				"showActions","showFolderMenu","showTabs","showFoldersSearch",
				"showSubfolders","enableCommentRatings");

		setRandomBooleanPreferences(portletPreferences, preferences);

		portletPreferences.setValue(
			"folderColumns",
			getSelectedEntryColumns("num-of-folders,num-of-documents,action"));

		portletPreferences.setValue(
			"fileEntryColumns",
			getSelectedEntryColumns("size,downloads,locked,action"));

		portletPreferences.setValue(
			"foldersPerPage",
			String.valueOf(ValueGenerator.getRandomIntegerFromRange(10, 200)));

		portletPreferences.setValue(
			"fileEntriesPerPage",
			String.valueOf(ValueGenerator.getRandomIntegerFromRange(10, 200)));

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

	private String getSelectedEntryColumns(String columns) {
		List<String> availableEntryColumns = Arrays.asList(columns.split(","));

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