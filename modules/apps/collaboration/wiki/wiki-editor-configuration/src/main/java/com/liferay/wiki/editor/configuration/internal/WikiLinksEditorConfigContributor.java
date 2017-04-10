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

package com.liferay.wiki.editor.configuration.internal;

import com.liferay.item.selector.ItemSelector;
import com.liferay.item.selector.ItemSelectorCriterion;
import com.liferay.item.selector.ItemSelectorReturnType;
import com.liferay.item.selector.criteria.URLItemSelectorReturnType;
import com.liferay.item.selector.criteria.file.criterion.FileItemSelectorCriterion;
import com.liferay.layout.item.selector.criterion.LayoutItemSelectorCriterion;
import com.liferay.portal.kernel.editor.configuration.BaseEditorConfigContributor;
import com.liferay.portal.kernel.editor.configuration.EditorConfigContributor;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.portlet.RequestBackedPortletURLFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.wiki.constants.WikiPortletKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.liferay.wiki.item.selector.criterion.WikiAttachmentItemSelectorCriterion;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.PortletURL;

/**
 * @author Chema Balsas
 */
@Component(
	property = {
		"editor.name=alloyeditor_creole",
		"javax.portlet.name=" + WikiPortletKeys.WIKI,
		"javax.portlet.name=" + WikiPortletKeys.WIKI_ADMIN,
		"javax.portlet.name=" + WikiPortletKeys.WIKI_DISPLAY
	},
	service = EditorConfigContributor.class
)
public class WikiLinksEditorConfigContributor
	extends BaseEditorConfigContributor {

	@Override
	public void populateConfigJSONObject(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		ThemeDisplay themeDisplay,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory) {

		JSONObject toolbarsJSONObject = jsonObject.getJSONObject("toolbars");

		if (toolbarsJSONObject == null) {
			return;
		}

		JSONObject stylesToolbarJSONObject = toolbarsJSONObject.getJSONObject(
			"styles");

		if (stylesToolbarJSONObject == null) {
			return;
		}

		JSONArray selectionsJSONArray = stylesToolbarJSONObject.getJSONArray(
			"selections");

		if (selectionsJSONArray == null) {
			return;
		}

		for (int i = 0; i < selectionsJSONArray.length(); i++) {
			JSONObject selectionJSONObject = selectionsJSONArray.getJSONObject(
				i);

			JSONArray buttonsJSONArray = selectionJSONObject.getJSONArray(
				"buttons");

			selectionJSONObject.put(
				"buttons", updateButtonsJSONArray(buttonsJSONArray));
		}

		String namespace = GetterUtil.getString(
			inputEditorTaglibAttributes.get(
				"liferay-ui:input-editor:namespace"));

		String name = GetterUtil.getString(
			inputEditorTaglibAttributes.get("liferay-ui:input-editor:name"));

		populateFileBrowserURL(
			jsonObject, inputEditorTaglibAttributes,
			requestBackedPortletURLFactory,
			namespace + name + "selectDocument");
	}

	protected JSONObject getWikiLinkButtonJSONObject(String buttonName) {
		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		JSONObject cfgJSONObject = JSONFactoryUtil.createJSONObject();

		cfgJSONObject.put("appendProtocol", false);

		jsonObject.put("cfg", cfgJSONObject);

		jsonObject.put("name", buttonName);

		return jsonObject;
	}

	protected JSONArray updateButtonsJSONArray(JSONArray oldButtonsJSONArray) {
		JSONArray newButtonsJSONArray = JSONFactoryUtil.createJSONArray();

		for (int j = 0; j < oldButtonsJSONArray.length(); j++) {
			JSONObject buttonJSONObject = oldButtonsJSONArray.getJSONObject(j);

			if (buttonJSONObject == null) {
				String buttonName = oldButtonsJSONArray.getString(j);

				if (buttonName.equals("link") ||
					buttonName.equals("linkEdit")) {

					buttonJSONObject = getWikiLinkButtonJSONObject(buttonName);

					newButtonsJSONArray.put(buttonJSONObject);
				}
				else {
					newButtonsJSONArray.put(buttonName);
				}
			}
			else {
				String buttonName = buttonJSONObject.getString("name");

				if (buttonName.equals("link") ||
					buttonName.equals("linkEdit")) {

					JSONObject cfgJSONObject = buttonJSONObject.getJSONObject(
						"cfg");

					if (cfgJSONObject == null) {
						cfgJSONObject = JSONFactoryUtil.createJSONObject();

						buttonJSONObject.put("cfg", cfgJSONObject);
					}

					cfgJSONObject.put("appendProtocol", false);
				}

				newButtonsJSONArray.put(buttonJSONObject);
			}
		}

		return newButtonsJSONArray;
	}

	protected void populateFileBrowserURL(
		JSONObject jsonObject, Map<String, Object> inputEditorTaglibAttributes,
		RequestBackedPortletURLFactory requestBackedPortletURLFactory,
		String eventName) {

		List<ItemSelectorReturnType> desiredItemSelectorReturnTypes =
			new ArrayList<>();

		desiredItemSelectorReturnTypes.add(new URLItemSelectorReturnType());

		ItemSelectorCriterion fileItemSelectorCriterion =
			new FileItemSelectorCriterion();

		fileItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		LayoutItemSelectorCriterion layoutItemSelectorCriterion =
			new LayoutItemSelectorCriterion();

		layoutItemSelectorCriterion.setDesiredItemSelectorReturnTypes(
			desiredItemSelectorReturnTypes);

		Map<String, String> fileBrowserParamsMap =
			(Map<String, String>)inputEditorTaglibAttributes.get(
				"liferay-ui:input-editor:fileBrowserParams");

		long wikiPageResourcePrimKey = 0;

		if (fileBrowserParamsMap != null) {
			wikiPageResourcePrimKey = GetterUtil.getLong(
				fileBrowserParamsMap.get("wikiPageResourcePrimKey"));
		}

		PortletURL itemSelectorURL = null;

		if (wikiPageResourcePrimKey != 0) {
			ItemSelectorCriterion wikiAttachmentItemSelectorCriterion =
				new WikiAttachmentItemSelectorCriterion(
					wikiPageResourcePrimKey);

			wikiAttachmentItemSelectorCriterion.
				setDesiredItemSelectorReturnTypes(
					desiredItemSelectorReturnTypes);

			itemSelectorURL = _itemSelector.getItemSelectorURL(
				requestBackedPortletURLFactory, eventName,
				fileItemSelectorCriterion, layoutItemSelectorCriterion,
				wikiAttachmentItemSelectorCriterion);
		}
		else {
			itemSelectorURL = _itemSelector.getItemSelectorURL(
				requestBackedPortletURLFactory, eventName,
				fileItemSelectorCriterion, layoutItemSelectorCriterion);
		}

		jsonObject.put("documentBrowseLinkUrl", itemSelectorURL.toString());
	}

	@Reference
	private ItemSelector _itemSelector;

}