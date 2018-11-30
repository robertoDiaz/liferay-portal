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

package com.liferay.liferaygen.journal.internal.portlet;

import aQute.bnd.annotation.component.Activate;

import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.service.DDMTemplateLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleResource;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.portlet.api.BaseAddPortletLiferayGenAction;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;

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
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.journal.internal.portlet.AddJournalPortletLiferayGenAction",
	service = LiferayGenAction.class
)
public class AddJournalPortletLiferayGenAction
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
		parameters.put("portletId", "56");

		super.configure(parameters);
	}

	public String doGetDescription() {
		return StringBundler.concat(
			"Adds a random journal portlet in a random layout in a random",
			"position. Portlet configuration will be random. Layout can be ",
			"specified in parameters");
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
				put(
					LiferayGenActionConfig.TARGET,
					"Layout to use during add action");
			}
		};
	}

	@Override
	public String getName() {
		return "AddJournalPortletLiferayGenAction";
	}

	@Override
	protected void doUpdatePortletPreferences(
			User user, Layout layout, Portlet portlet,
			PortletPreferences portletPreferences)
		throws Exception {

		Long resourcePrimKey =
			(Long)liferayGenValueGenerator.getRandomObjectProperties(
				JournalArticleResource.class.getName(), "resourcePrimKey",
				getFilterByGroupId());

		if (resourcePrimKey == null) {
			return;
		}

		JournalArticle journalArticle =
			_journalArticleLocalService.getLatestArticle(resourcePrimKey);

		long groupId = journalArticle.getGroupId();

		portletPreferences.setValue("groupId", String.valueOf(groupId));

		String articleId = journalArticle.getArticleId();

		portletPreferences.setValue("articleId", articleId);

		DDMStructure journalStructure = _ddmStructureLocalService.getStructure(
			groupId, _portal.getClassNameId(JournalArticle.class),
			journalArticle.getDDMStructureKey(), true);

		if (journalArticle.isTemplateDriven()) {
			DDMTemplate journalTemplate =
				liferayGenValueGenerator.getRandomObjectFromList(
					_ddmTemplateLocalService.getTemplatesByClassPK(
						groupId, journalStructure.getStructureId()));

			if (journalTemplate != null) {
				portletPreferences.setValue(
					"templateId",
					GetterUtil.getString(journalTemplate.getTemplateId()));
			}
		}

		List<String> preferences = Arrays.asList(
			"enableComments", "enableCommentRatings", "enablePrint",
			"enableRatings", "enableRelatedAssets", "enableViewCountIncrement",
			"showAvailableLocales");

		setRandomBooleanPreferences(portletPreferences, preferences);

		portletPreferences.store();
	}

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DDMTemplateLocalService _ddmTemplateLocalService;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}