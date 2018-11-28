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

package com.liferay.liferaygen.web.internal.actions;

import com.liferay.liferaygen.config.ActionConfig;
import com.liferay.liferaygen.constants.ConfigConstants;
import com.liferay.liferaygen.impl.BaseAction;
import com.liferay.liferaygen.util.ParameterUtil;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.LayoutPrototype;
import com.liferay.portal.model.LayoutTemplate;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.PortletPreferencesIds;
import com.liferay.portal.model.Theme;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutPrototypeLocalServiceUtil;
import com.liferay.portal.service.LayoutTemplateLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.mobiledevicerules.model.MDRAction;
import com.liferay.portlet.mobiledevicerules.model.MDRRuleGroupInstance;
import com.liferay.portlet.mobiledevicerules.service.MDRActionLocalServiceUtil;
import com.liferay.portlet.mobiledevicerules.service.MDRRuleGroupInstanceLocalServiceUtil;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.portlet.PortletPreferences;

import javax.servlet.http.HttpServletRequest;
public class CreateLayout extends BaseAction {

	@Override
	public String doGetDescription() {
		return "Creates a random layout (it will be randomly private/public)";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("nonDefaultParentLayoutRatio", 0L);
				put("nonPortletLayoutTypeRatio", 0L);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					"nonDefaultParentLayoutRatio",
					"Probability percentage of creating a layout with a " +
					"random parent layout");
				put(
					"nonPortletLayoutTypeRatio",
					"Probability percentage of creating a layout of non " +
					"portlet type");
				put(ActionConfig.TARGET, "Parent layout");
			}
		};
	}

	@Override
	public Criterion getEntityFilter() {
		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		conjunction.add(RestrictionsFactoryUtil.eq("type", "portlet"));
		conjunction.add(
			RestrictionsFactoryUtil.eq("layoutPrototypeLinkEnabled", false));

		return conjunction;
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return Layout.class;
	}

	@Override
	public String getEntityProperties() {
		return "plid";
	}

	@Override
	public boolean hasScopeByGroupId() {
		return true;
	}

	protected Object[] addLayout(
			long userId, String languageId, long companyId, long groupId,
			boolean privateLayout, long parentLayoutId, boolean hidden,
			String layoutType, Locale[] locales, ServiceContext serviceContext)
		throws Exception {

		Map<Locale, String> nameMap =
			ValueGenerator.getRandomValuesLocalizationMap(
				locales, ValueGenerator.getRandomIntegerFromRange(12, 20));

		Map<Locale, String> titleMap =
			ValueGenerator.getRandomValuesLocalizationMap(
				locales, ValueGenerator.getRandomIntegerFromRange(12, 20));

		Map<Locale, String> descriptionMap =
			ValueGenerator.getRandomValuesLocalizationMap(
				locales, ValueGenerator.getRandomIntegerFromRange(20, 65));

		Map<Locale, String> keywordsMap =
			ValueGenerator.getRandomValuesLocalizationMap(locales, 0);

		Map<Locale, String> robotsMap =
			ValueGenerator.getRandomValuesLocalizationMap(locales, 0);

		String friendlyURL = null;

		long layoutPrototypeId = 0;

		Layout layout = null;
		UnicodeProperties layoutTypeSettingsProperties = null;
		String oldFriendlyURL = StringPool.BLANK;

		// Add layout

		boolean inheritFromParentLayoutId = ValueGenerator.getBoolean();

		if (inheritFromParentLayoutId && (parentLayoutId > 0)) {
			Layout parentLayout = LayoutLocalServiceUtil.getLayout(
				groupId, privateLayout, parentLayoutId);

			layout = LayoutLocalServiceUtil.addLayout(
				userId, groupId, privateLayout, parentLayoutId, nameMap,
				titleMap, parentLayout.getDescriptionMap(),
				parentLayout.getKeywordsMap(), parentLayout.getRobotsMap(),
				parentLayout.getType(), hidden, friendlyURL, serviceContext);

			LayoutLocalServiceUtil.updateLayout(
				layout.getGroupId(), layout.isPrivateLayout(),
				layout.getLayoutId(), parentLayout.getTypeSettings());

			inheritMobileRuleGroups(layout, serviceContext);

			if (parentLayout.isTypePortlet()) {
				copyPreferences(
					serviceContext.getRequest(), companyId, userId, languageId,
					layout, parentLayout);

				copyLookAndFeel(layout, parentLayout);
			}
		}
		else if (layoutPrototypeId > 0) {
			LayoutPrototype layoutPrototype =
				LayoutPrototypeLocalServiceUtil.getLayoutPrototype(
					layoutPrototypeId);

			String layoutPrototypeLinkEnabled = null;

			if (Validator.isNotNull(layoutPrototypeLinkEnabled)) {
				serviceContext.setAttribute(
					"layoutPrototypeLinkEnabled", layoutPrototypeLinkEnabled);
			}

			serviceContext.setAttribute(
				"layoutPrototypeUuid", layoutPrototype.getUuid());

			layout = LayoutLocalServiceUtil.addLayout(
				userId, groupId, privateLayout, parentLayoutId, nameMap,
				titleMap, descriptionMap, keywordsMap, robotsMap, layoutType,
				hidden, friendlyURL, serviceContext);
		}
		else {
			layout = LayoutLocalServiceUtil.addLayout(
				userId, groupId, privateLayout, parentLayoutId, nameMap,
				titleMap, descriptionMap, keywordsMap, robotsMap, layoutType,
				hidden, friendlyURL, serviceContext);
		}

		UnicodeProperties formTypeSettingsProperties = new UnicodeProperties();

		if (LayoutConstants.TYPE_LINK_TO_LAYOUT.equals(layoutType)) {
			List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
				groupId, privateLayout, LayoutConstants.TYPE_PORTLET);

			if (layouts.size() > 0) {
				Layout randomLayout = ValueGenerator.getRandomObjectFromList(
					layouts);

				formTypeSettingsProperties.put(
					"linkToLayoutId=" + randomLayout.getLayoutId());
				formTypeSettingsProperties.put(
					"groupId=" + randomLayout.getGroupId());
				formTypeSettingsProperties.put("show-alternate-links=true");
				formTypeSettingsProperties.put("layoutUpdateable=true");
				formTypeSettingsProperties.put(
					"privateLayout=" + randomLayout.isPrivateLayout());
			}

			layoutTypeSettingsProperties = layout.getTypeSettingsProperties();

			layout.setTypeSettingsProperties(formTypeSettingsProperties);

			layoutTypeSettingsProperties.putAll(
				layout.getTypeSettingsProperties());
		}
		else if (LayoutConstants.TYPE_PORTLET.equals(layoutType)) {
			LayoutTypePortlet layoutTypePortlet = null;

			Theme theme = null;

			layoutTypePortlet = (LayoutTypePortlet)layout.getLayoutType();

			theme = layout.getTheme();

			String layoutTemplateId = PropsUtil.get(
				PropsKeys.DEFAULT_LAYOUT_TEMPLATE_ID);

			List<LayoutTemplate> layoutTemplates =
				LayoutTemplateLocalServiceUtil.getLayoutTemplates(
					theme.getThemeId());

			LayoutTemplate layoutTemplate =
				ValueGenerator.getRandomObjectFromList(layoutTemplates);

			while (layoutTemplate.getLayoutTemplateId().equals("freeform")) {
				layoutTemplate = ValueGenerator.getRandomObjectFromList(
					layoutTemplates);
			}

			if (layoutTemplate != null) {
				layoutTemplateId = layoutTemplate.getLayoutTemplateId();
			}

			layoutTypePortlet.setLayoutTemplateId(userId, layoutTemplateId);
		}

		LayoutLocalServiceUtil.updateLayout(
			groupId, privateLayout, layout.getLayoutId(),
			layout.getTypeSettings());

		return new Object[] {layout, oldFriendlyURL};
	}

	protected void copyLookAndFeel(Layout targetLayout, Layout sourceLayout)
		throws Exception {

		LayoutLocalServiceUtil.updateLookAndFeel(
			targetLayout.getGroupId(), targetLayout.isPrivateLayout(),
			targetLayout.getLayoutId(), sourceLayout.getThemeId(),
			sourceLayout.getColorSchemeId(), sourceLayout.getCss(), false);

		LayoutLocalServiceUtil.updateLookAndFeel(
			targetLayout.getGroupId(), targetLayout.isPrivateLayout(),
			targetLayout.getLayoutId(), sourceLayout.getWapThemeId(),
			sourceLayout.getWapColorSchemeId(), sourceLayout.getCss(), true);
	}

	protected void copyPreferences(
			HttpServletRequest request, long companyId, long userId,
			String languageId, Layout targetLayout, Layout sourceLayout)
		throws Exception {

		LayoutTypePortlet sourceLayoutTypePortlet =
			(LayoutTypePortlet)sourceLayout.getLayoutType();

		List<String> sourcePortletIds = sourceLayoutTypePortlet.getPortletIds();

		for (String sourcePortletId : sourcePortletIds) {

			// Copy preference

			PortletPreferencesIds portletPreferencesIds =
				PortletPreferencesFactoryUtil.getPortletPreferencesIds(
					request, targetLayout, sourcePortletId);

			PortletPreferencesLocalServiceUtil.getPreferences(
				portletPreferencesIds);

			PortletPreferencesIds sourcePortletPreferencesIds =
				PortletPreferencesFactoryUtil.getPortletPreferencesIds(
					request, sourceLayout, sourcePortletId);

			PortletPreferences sourcePreferences =
				PortletPreferencesLocalServiceUtil.getPreferences(
					sourcePortletPreferencesIds);

			PortletPreferencesLocalServiceUtil.updatePreferences(
				portletPreferencesIds.getOwnerId(),
				portletPreferencesIds.getOwnerType(),
				portletPreferencesIds.getPlid(),
				portletPreferencesIds.getPortletId(), sourcePreferences);

			// Copy portlet setup

			PortletPreferences targetPreferences =
				PortletPreferencesLocalServiceUtil.getPreferences(
					companyId, PortletKeys.PREFS_OWNER_ID_DEFAULT,
					PortletKeys.PREFS_OWNER_TYPE_LAYOUT, targetLayout.getPlid(),
					sourcePortletId);

			sourcePreferences =
				PortletPreferencesLocalServiceUtil.getPreferences(
						companyId,
					PortletKeys.PREFS_OWNER_ID_DEFAULT,
					PortletKeys.PREFS_OWNER_TYPE_LAYOUT, sourceLayout.getPlid(),
					sourcePortletId);

			PortletPreferencesLocalServiceUtil.updatePreferences(
				PortletKeys.PREFS_OWNER_ID_DEFAULT,
				PortletKeys.PREFS_OWNER_TYPE_LAYOUT, targetLayout.getPlid(),
				sourcePortletId, sourcePreferences);

			updateLayoutScopes(
				userId, sourceLayout, targetLayout, sourcePreferences,
				targetPreferences, sourcePortletId, languageId);
		}
	}

	@Override
	protected void doRun() {

		long companyId = (Long) _parameters.get(ConfigConstants.COMPANY_ID);
		long groupId = (Long) _parameters.get(ConfigConstants.GROUP_ID);

		Locale[] locales = (Locale[]) _parameters.get(ConfigConstants.LOCALES);

		int nonDefaultParentLayoutRatio =
			ParameterUtil.getParamAsIntegerPercentage(
				_parameters, "nonDefaultParentLayoutRatio");

		long parentPlid = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;
		long parentLayoutId = LayoutConstants.DEFAULT_PARENT_LAYOUT_ID;

		if ((nonDefaultParentLayoutRatio > 0) &&
			ValueGenerator.getBoolean(nonDefaultParentLayoutRatio)) {

			parentPlid = MapUtil.getLong(
				_parameters, ActionConfig.TARGET,
				LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);
		}

		int nonPortletLayoutTypeRatio =
			ParameterUtil.getParamAsIntegerPercentage(
				_parameters, "nonPortletLayoutTypeRatio");

		String layoutType = LayoutConstants.TYPE_PORTLET;

		if ((nonPortletLayoutTypeRatio > 0) &&
			ValueGenerator.getBoolean(nonPortletLayoutTypeRatio)) {

			layoutType = LayoutConstants.TYPE_LINK_TO_LAYOUT;
		}

		boolean privateLayout = ValueGenerator.getBoolean();
		boolean hidden = ValueGenerator.getBoolean(5);
		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		try {
			if (parentPlid != LayoutConstants.DEFAULT_PARENT_LAYOUT_ID) {
				Layout layout = LayoutLocalServiceUtil.getLayout(parentPlid);

				privateLayout = layout.isPrivateLayout();
				parentLayoutId = layout.getLayoutId();
			}

			long userId = ValueGenerator.getRandomUserIdFromCache();

			User user = UserLocalServiceUtil.fetchUser(userId);
			String languageId = user.getLanguageId();

			addLayout(
				userId, languageId, companyId, groupId, privateLayout,
				parentLayoutId, hidden, layoutType, locales, serviceContext);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected void inheritMobileRuleGroups(
			Layout layout, ServiceContext serviceContext)
		throws PortalException, SystemException {

		List<MDRRuleGroupInstance> parentMDRRuleGroupInstances =
			MDRRuleGroupInstanceLocalServiceUtil.getRuleGroupInstances(
				Layout.class.getName(), layout.getParentPlid());

		for (MDRRuleGroupInstance parentMDRRuleGroupInstance
			: parentMDRRuleGroupInstances) {

			MDRRuleGroupInstance mdrRuleGroupInstance =
				MDRRuleGroupInstanceLocalServiceUtil.addRuleGroupInstance(
					layout.getGroupId(), Layout.class.getName(),
					layout.getPlid(),
					parentMDRRuleGroupInstance.getRuleGroupId(),
					parentMDRRuleGroupInstance.getPriority(), serviceContext);

			List<MDRAction> parentMDRActions =
				MDRActionLocalServiceUtil.getActions(
					parentMDRRuleGroupInstance.getRuleGroupInstanceId());

			for (MDRAction mdrAction : parentMDRActions) {
				MDRActionLocalServiceUtil.addAction(
					mdrRuleGroupInstance.getRuleGroupInstanceId(),
					mdrAction.getNameMap(), mdrAction.getDescriptionMap(),
					mdrAction.getType(), mdrAction.getTypeSettings(),
					serviceContext);
			}
		}
	}

	protected void updateLayoutScopes(
			long userId, Layout sourceLayout, Layout targetLayout,
			PortletPreferences sourcePreferences,
			PortletPreferences targetPreferences, String sourcePortletId,
			String languageId)
		throws Exception {

		String scopeType = GetterUtil.getString(
			sourcePreferences.getValue("lfrScopeType", null));

		if (Validator.isNull(scopeType) || !scopeType.equals("layout")) {
			return;
		}

		Layout targetScopeLayout =
			LayoutLocalServiceUtil.getLayoutByUuidAndGroupId(
				targetLayout.getUuid(), targetLayout.getGroupId(),
				targetLayout.isPrivateLayout());

		if (!targetScopeLayout.hasScopeGroup()) {
			GroupLocalServiceUtil.addGroup(
				userId, Layout.class.getName(), targetLayout.getPlid(),
				targetLayout.getName(languageId), null, 0, null, false, true,
				null);
		}

		String portletTitle = PortalUtil.getPortletTitle(
			sourcePortletId, languageId);

		String newPortletTitle = PortalUtil.getNewPortletTitle(
			portletTitle, String.valueOf(sourceLayout.getLayoutId()),
			targetLayout.getName(languageId));

		targetPreferences.setValue(
			"groupId", String.valueOf(targetLayout.getGroupId()));
		targetPreferences.setValue("lfrScopeType", "layout");
		targetPreferences.setValue(
			"lfrScopeLayoutUuid", targetLayout.getUuid());
		targetPreferences.setValue(
			"portletSetupTitle_" + languageId, newPortletTitle);
		targetPreferences.setValue(
			"portletSetupUseCustomTitle", Boolean.TRUE.toString());

		targetPreferences.store();
	}

	private void addParameterLongCriterion(
		Conjunction conjunction, String parameterName) {

		Object parameterValue = _parameters.get(parameterName);

		if (parameterValue != null) {
			conjunction.add(
				RestrictionsFactoryUtil.eq(
					parameterName, GetterUtil.getLong(parameterValue)));
		}
	}

	private static Log _log = LogFactoryUtil.getLog(CreateLayout.class);

}