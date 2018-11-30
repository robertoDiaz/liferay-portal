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

package com.liferay.liferaygen.document.library.internal.portlet;

import aQute.bnd.annotation.component.Activate;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.portlet.api.BaseAddPortletLiferayGenAction;
import com.liferay.liferaygen.util.LiferayGenParameterHandler;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletPreferences;

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
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.document.library.internal.portlet.AddDLPortletLiferayGenAction",
	service = LiferayGenAction.class
)
public class AddDLPortletLiferayGenAction
	extends BaseAddPortletLiferayGenAction {

	@Activate
	public void activate() {
		liferayGenValueGenerator = new LiferayGenValueGenerator(
			_companyLocalService, _liferayGenQueryHandler, _portal,
			_portletLocalService);

		initUpdateLayoutActionMethod();
	}

	@Override
	public void configure(Map<String, Object> parameters) {
		parameters.put("portletId", "20");

		super.configure(parameters);
	}

	@Override
	public String doGetDescription() {
		return StringBundler.concat(
			"Adds a random document library portlet in a random layout in a ",
			"random position. Portlet configuration will be random. Layout ",
			"can be specified in parameters");
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
				put(
					LiferayGenActionConfig.TARGET,
					"Layout to use during add action");
			}
		};
	}

	@Override
	protected void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception {

		List<String> preferences = Arrays.asList(
			"enableCommentRatings", "enableRelatedAssets", "showFoldersSearch");

		setRandomBooleanPreferences(portletPreferences, preferences);

		portletPreferences.setValue("displayViews", _getSelectedDisplayViews());

		portletPreferences.setValue("entryColumns", _getSelectedEntryColumns());

		portletPreferences.setValue(
			"entriesPerPage",
			liferayGenValueGenerator.getRandomObjectFromArray(
				PrefsPropsUtil.getStringArray(
					PropsKeys.SEARCH_CONTAINER_PAGE_DELTA_VALUES,
					StringPool.COMMA)));

		int nonDefaultParentFolderRatio =
			_liferayGenParameterHandler.getParamAsIntegerPercentage(
				parameters, "nonDefaultParentFolderRatio");

		Long folderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		if ((nonDefaultParentFolderRatio > 0) &&
			liferayGenValueGenerator.getBoolean(nonDefaultParentFolderRatio)) {

			folderId = (Long)liferayGenValueGenerator.getRandomObjectProperties(
				DLFolder.class.getName(), "folderId", getFilterByGroupId());
		}

		portletPreferences.setValue("rootFolderId", String.valueOf(folderId));

		portletPreferences.store();
	}

	private String _getSelectedDisplayViews() {
		String[] displayViews = PrefsPropsUtil.getStringArray(
			PropsKeys.DL_DISPLAY_VIEWS, StringPool.COMMA);

		long numberOfViewsToSelect =
			liferayGenValueGenerator.getRandomLongFromRange(
				1, displayViews.length);

		List<String> selectedDisplayViews =
			liferayGenValueGenerator.getRandomObjectsFromArray(
				displayViews, numberOfViewsToSelect);

		String displayView = StringUtil.merge(selectedDisplayViews);

		return displayView;
	}

	private String _getSelectedEntryColumns() {
		List<String> availableEntryColumns = Arrays.asList(
			"size,downloads,action,modified-date,create-date".split(","));

		long numberOfColumnsToSelect =
			liferayGenValueGenerator.getRandomLongFromRange(
				1, availableEntryColumns.size());

		List<String> selectedEntryColumnsList =
			liferayGenValueGenerator.getRandomObjectsFromList(
				availableEntryColumns, numberOfColumnsToSelect);

		if (selectedEntryColumnsList.isEmpty()) {
			return "name";
		}

		return "name," + StringUtil.merge(selectedEntryColumnsList);
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenParameterHandler _liferayGenParameterHandler;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}