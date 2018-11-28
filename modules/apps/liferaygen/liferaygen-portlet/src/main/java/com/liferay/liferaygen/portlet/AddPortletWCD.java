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
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.service.DDMTemplateLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletPreferences;
public class AddPortletWCD extends AddPortlet {

	@Override
	public void configure(Map<String, Object> parameters) {

		parameters.put("portletId", "56");

		super.configure(parameters);
	}

	public String doGetDescription() {
		return "Adds a random web content display portlet in a random layout " +
				"in a random position. Portlet configuration will be random. " +
				"Layout can be specified in parameters";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
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

		Long resourcePrimKey =
			(Long)ValueGenerator.getRandomObjectProperties(
				JournalArticleResource.class.getName(), "resourcePrimKey",
				this.getFilterByGroupId());

		if (resourcePrimKey == null) {
			return;
		}

		JournalArticle journalArticle =
			JournalArticleLocalServiceUtil.getLatestArticle(resourcePrimKey);

		long groupId = journalArticle.getGroupId();
		String articleId = journalArticle.getArticleId();
		String templateId = null;

		DDMStructure journalStructure =
			DDMStructureLocalServiceUtil.getStructure(
				groupId, JOURNAL_ARTICLE_CLASS_NAME_ID,
				journalArticle.getStructureId(), true);

		if (journalArticle.isTemplateDriven()) {
			DDMTemplate journalTemplate =
				ValueGenerator.getRandomObjectFromList(
					DDMTemplateLocalServiceUtil.getTemplatesByClassPK(
						groupId, journalStructure.getStructureId()));

			if (journalTemplate != null) {
				templateId = GetterUtil.getString(
					journalTemplate.getTemplateId());
			}
		}

		portletPreferences.setValue("groupId", Long.toString(groupId));
		portletPreferences.setValue("articleId", articleId);

		if (Validator.isNotNull(templateId)) {
			portletPreferences.setValue("templateId", templateId);
		}

		List<String> preferences =
			Arrays.asList(
				"enableComments","enableCommentRatings","enablePrint",
				"enableRatings","enableRelatedAssets",
				"enableViewCountIncrement","showAvailableLocales");

		setRandomBooleanPreferences(portletPreferences, preferences);

		portletPreferences.store();
	}

	private static final long JOURNAL_ARTICLE_CLASS_NAME_ID =
		PortalUtil.getClassNameId(JournalArticle.class);

}