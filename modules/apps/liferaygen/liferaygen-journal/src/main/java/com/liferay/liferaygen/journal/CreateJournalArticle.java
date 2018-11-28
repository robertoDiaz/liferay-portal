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

package com.liferay.liferaygen.web.internal.actions.journal;

import com.liferay.liferaygen.config.ActionConfig;
import com.liferay.liferaygen.constants.ConfigConstants;
import com.liferay.liferaygen.impl.BaseAction;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.dynamicdatamapping.model.DDMStructure;
import com.liferay.portlet.dynamicdatamapping.model.DDMTemplate;
import com.liferay.portlet.dynamicdatamapping.service.DDMStructureLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleConstants;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleServiceUtil;
import com.liferay.util.PwdGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
public class CreateJournalArticle extends BaseAction {

	@Override
	public String doGetDescription() {
		return "Creates a random web content using a structure";
	}

	@Override
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("numberOfVersions", DEFAULT_NUMBER_OF_VERSIONS);
				put("structureId", null);
			}
		};
	}

	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(ActionConfig.TARGET, "Structure to use during add action");
				put(
					"numberOfVersions",
					"Number of versions to add per web content");
				put("structureId", "Structure used to create web contents");
			}
		};
	}

	@Override
	public Criterion getEntityFilter() {
		long journalArticleClassNameId = PortalUtil.getClassNameId(
			JournalArticle.class);

		Criterion filterByClassNameId = RestrictionsFactoryUtil.eq(
			"classNameId", journalArticleClassNameId);

		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();
		conjunction.add(filterByClassNameId);

		return conjunction;
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return DDMStructure.class;
	}

	@Override
	public String getEntityModelPk() {
		return "structureId";
	}

	protected static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal;
	}

	protected void addDynamicContent(
			Element dynamicContent, String fieldType,
			Map<String, byte[]> images, String instanceId, String name,
			String[] listElementTypes, String index)
		throws Exception {

		if (fieldType.equals("boolean")) {
			Boolean value = ValueGenerator.getBoolean();

			dynamicContent.addCDATA(value.toString());
		}
		else if (fieldType.equals("ddm-date")) {
			Long value = ValueGenerator.getRandomLongFromRange(
				0, 100*365*24*60*60*1000);

			dynamicContent.addCDATA(String.valueOf(value*1000));
		}
		else if (fieldType.equals("ddm-decimal")) {
			Float value = ValueGenerator.getRandomFloatFromRange(
				0, Float.MAX_VALUE);

			dynamicContent.addCDATA(value.toString());
		}
		else if (fieldType.equals("document_library")) {
			DLFileEntry dlFileEntry =
				(DLFileEntry)ValueGenerator.getRandomObject(
					DLFileEntry.class.getName(), getFilterByGroupId());

			dynamicContent.addCDATA(getDLFileEntryURL(dlFileEntry));
		}
		else if (fieldType.equals("text_area")) {
			dynamicContent.addText(generateTextAreaContent());
		}
		else if (fieldType.equals("image")) {
			String formatName = ValueGenerator.getRandomObjectFromList(
				ValueGenerator.getAvailableImageIOFormats());

			byte[] image = ValueGenerator.getRandomImage(
				formatName, ValueGenerator.getRandomIntegerFromRange(100, 200),
				ValueGenerator.getRandomIntegerFromRange(60, 120));

			if (image == null) {image = new byte[0]; }

			String key = instanceId + "_" + name + "_" + index;

			String languageId = dynamicContent.attributeValue("language-id");

			if (languageId != null) {
				key = key + "_" + languageId;
			}

			images.put(key, image);

			dynamicContent.addCDATA("dummy_text");
		}
		else if (fieldType.equals("ddm-integer")) {
			Integer value = ValueGenerator.getRandomIntegerFromRange(
				0, Integer.MAX_VALUE);

			dynamicContent.addCDATA(value.toString());
		}
		else if (fieldType.equals("ddm-number")) {
			Double value = ValueGenerator.getRandomDoubleFromRange(
				0, Double.MAX_VALUE);

			dynamicContent.addCDATA(value.toString());
		}
		else if (fieldType.equals("link_to_layout")) {
			Layout layout = (Layout)ValueGenerator.getRandomObject(
				Layout.class.getName(), getFilterByGroupId());

			dynamicContent.addCDATA(getLinkToLayout(layout));
		}
		else if (fieldType.equals("radio") || fieldType.endsWith("list")) {
			if (fieldType.equals("radio")) {
				dynamicContent.addCDATA(
					"[\"" + ValueGenerator.getRandomObjectFromArray(
						listElementTypes) + "\"]");
			}
			else if (fieldType.equals("list")) {
				dynamicContent.addCDATA(
					ValueGenerator.getRandomObjectFromArray(listElementTypes));
			}
			else {
				List<String> values = ValueGenerator.getRandomObjectsFromArray(
					listElementTypes,
					ValueGenerator.getRandomIntegerFromRange(
						1, listElementTypes.length));

				for (String value : values) {
					Element option = dynamicContent.addElement("option");

					option.addCDATA(value);
				}
			}
		}
		else if (fieldType.startsWith("text")) {
			dynamicContent.addText(
				ValueGenerator.getLowerCaseText(1, MAX_TEXT_FIELD_LENGTH));
		}
	}

	protected void addLocalization(
			Element element, Map<String, byte[]> images, Document structureDoc,
			Map<String, String[]> listValues, Locale locale)
		throws Exception {

		List<Element> dynamicElements = element.elements("dynamic-element");

		for (Element dynamicElement : dynamicElements) {
			addLocalization(
				dynamicElement, images, structureDoc, listValues, locale);

			if (isLocalizable(structureDoc, dynamicElement)) {
				Element dynamicContentCurrentLocale = dynamicElement.addElement(
					"dynamic-content");

				dynamicContentCurrentLocale.addAttribute(
					"language-id", locale.toString());

				String index = dynamicElement.attributeValue("index");
				String instanceId = dynamicElement.attributeValue(
					"instance-id");
				String name = dynamicElement.attributeValue("name");
				String type = dynamicElement.attributeValue("type");

				if (type.equals("selection_break")) {
					break;
				}

				addDynamicContent(
					dynamicContentCurrentLocale, type, images, instanceId, name,
					listValues.get(name), index);
			}
		}
	}

	protected JournalArticle addLocalizationVersion(
			JournalArticle journalArticle, Document doc, Locale locale,
			ServiceContext serviceContext)
		throws Exception {

		Element root = doc.getRootElement();

		String availableLocales = root.attributeValue("available-locales");

		availableLocales = StringUtil.add(
			availableLocales, locale.toString(), StringPool.COMMA);

		if (availableLocales.endsWith(",")) {
			availableLocales = availableLocales.substring(
				0, availableLocales.length() - 1);
		}

		root.addAttribute("available-locales", availableLocales);

		Map<String, byte[]> images = new HashMap<String, byte[]>();

		long journalArticleClassnameId =
			ClassNameLocalServiceUtil.getClassNameId(JournalArticle.class);

		DDMStructure structure =
			DDMStructureLocalServiceUtil.getStructure(
				journalArticle.getGroupId(), journalArticleClassnameId,
				journalArticle.getStructureId());

		Document structureDoc = SAXReaderUtil.read(structure.getCompleteXsd());

		Map<String, String[]> listsValues = new TreeMap<String, String[]>();

		addLocalization(
			root, images, structureDoc,
			getListsValuesFromStructure(
				structureDoc.getRootElement(), listsValues),
			locale);

		return JournalArticleServiceUtil.updateArticleTranslation(
			journalArticle.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), locale,
			ValueGenerator.getLowerCaseText(TITLE_LENGTH),
			ValueGenerator.getLowerCaseText(DESCRIPTION_LENGTH),
			doc.formattedString(), images, serviceContext);
	}

	protected void addValue(
			Element element, Map<String, byte[]> images, Locale locale)
		throws Exception {

		boolean emptyField = ValueGenerator.getBoolean(PERCENTAGE_EMPTY_FIELDS);

		String index = element.attributeValue("index");
		String instanceId = element.attributeValue("instance-id");
		String name = element.attributeValue("name");
		String type = element.attributeValue("type");

		if (type.equals("selection_break")) {
			return;
		}

		String[] listElementTypes = null;

		if (type.equals("radio") || type.endsWith("list")) {
			listElementTypes = getListValues(element);

			element.clearContent();
		}

		Element dynamicContent = element.addElement("dynamic-content");

		dynamicContent.addAttribute("language-id", locale.toString());

		if (emptyField) {
			if (type.equals("radio")) {
				dynamicContent.addCDATA("[]");
			}
			else {
				dynamicContent.addCDATA(StringPool.BLANK);
			}

			return;
		}

		addDynamicContent(
			dynamicContent, type, images, instanceId, name, listElementTypes,
			index);
	}

	protected void addValues(
			Element element, Map<String, byte[]> images, Locale locale,
			Map<String, Integer> repetitionsMap)
		throws Exception {

		List<Element> dynamicElements = element.elements("dynamic-element");

		for (Element dynamicElement : dynamicElements) {
			String nameValue = dynamicElement.attributeValue("name");

			dynamicElement.addAttribute(
				"index", getIndexValue(repetitionsMap, nameValue));

			addValues(dynamicElement, images, locale, repetitionsMap);

			Element metadataElement = dynamicElement.element("meta-data");

			if (metadataElement != null) {
				dynamicElement.remove(metadataElement);
			}

			Attribute typeAttribute = dynamicElement.attribute("type");
			String typeValue = null;

			if (typeAttribute != null) {
				typeValue = typeAttribute.getValue();
			}

			Attribute dataTypeAttribute = dynamicElement.attribute("dataType");
			String dataTypeValue = null;

			if (dataTypeAttribute != null) {
				dataTypeValue = dataTypeAttribute.getValue();
			}

			if (Validator.isNull(dataTypeValue)) {
				dynamicElement.remove(typeAttribute);
				dynamicElement.addAttribute("type", "selection_break");
			}
			else {
				if (dataTypeValue.equals("boolean")) {
					dynamicElement.remove(typeAttribute);
					dynamicElement.addAttribute("type", "boolean");
				}
				else if (typeValue.equals("ddm-documentlibrary")) {
					dynamicElement.remove(typeAttribute);
					dynamicElement.addAttribute("type", "document_library");
				}
				else if (typeValue.equals("ddm-text-html")) {
					dynamicElement.remove(typeAttribute);
					dynamicElement.addAttribute("type", "text_area");
				}
				else if (typeValue.equals("textarea")) {
					dynamicElement.remove(typeAttribute);
					dynamicElement.addAttribute("type", "text_box");
				}
				else if (typeValue.equals("select")) {
					dynamicElement.remove(typeAttribute);

					if (GetterUtil.getBoolean(
							dynamicElement.attributeValue("multiple"), false)) {

						dynamicElement.addAttribute("type", "multi-list");
					}
					else {
						dynamicElement.addAttribute("type", "list");
					}
				}
				else if (typeValue.equals("ddm-link-to-page")) {
					dynamicElement.remove(typeAttribute);
					dynamicElement.addAttribute("type", "link_to_layout");
				}
				else if (typeValue.equals("wcm-image")) {
					dynamicElement.remove(typeAttribute);
					dynamicElement.addAttribute("type", "image");
				}

				dynamicElement.remove(dataTypeAttribute);
			}

			Attribute localizableAttribute = dynamicElement.attribute(
				"localizable");

			if (localizableAttribute != null) {
				dynamicElement.remove(localizableAttribute);
			}

			Attribute readOnlyAttribute = dynamicElement.attribute("readOnly");

			if (readOnlyAttribute != null) {
				dynamicElement.remove(readOnlyAttribute);
			}

			Attribute requiredAttribute = dynamicElement.attribute("required");

			if (requiredAttribute != null) {
				dynamicElement.remove(requiredAttribute);
			}

			Attribute showLabelAttribute = dynamicElement.attribute(
				"showLabel");

			if (showLabelAttribute != null) {
				dynamicElement.remove(showLabelAttribute);
			}

			Attribute widthAttribute = dynamicElement.attribute("width");

			if (widthAttribute != null) {
				dynamicElement.remove(widthAttribute);
			}

			Attribute fieldNamespaceAttribute = dynamicElement.attribute(
				"fieldNamespace");

			if (fieldNamespaceAttribute != null) {
				dynamicElement.remove(fieldNamespaceAttribute);
			}

			Attribute multipleAttribute = dynamicElement.attribute("multiple");

			if (multipleAttribute != null) {
				dynamicElement.remove(multipleAttribute);
			}

			Attribute indexTypeAttribute = dynamicElement.attribute(
				"indexType");

			if (indexTypeAttribute != null) {
				String indexTypeValue = indexTypeAttribute.getValue();
				dynamicElement.remove(indexTypeAttribute);
				dynamicElement.addAttribute("index-type", indexTypeValue);
			}

			String instanceId = PwdGenerator.getPassword(4);

			dynamicElement.addAttribute("instance-id", instanceId);

			addValue(dynamicElement, images, locale);
		}
	}

	protected void addVersions(JournalArticle journalArticle) throws Exception {
		int numberOfVersions = Integer.parseInt(
				MapUtil.getString(_parameters, "numberOfVersions"));

		if (numberOfVersions == 0) {
			return;
		}

		Locale[] locales = (Locale[]) _parameters.get(ConfigConstants.LOCALES);

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAttribute("defaultLanguageId", defaultLanguageId);
		serviceContext.setScopeGroupId(journalArticle.getGroupId());

		Document doc = SAXReaderUtil.read(journalArticle.getContent());

		for (int i = 0; i < numberOfVersions; i++) {
			if ((i < locales.length) && (locales[i] != defaultLocale)) {
				journalArticle = addLocalizationVersion(
					journalArticle, doc, locales[i], serviceContext);
			}

			Calendar cal = toCalendar(journalArticle.getDisplayDate());

			Map<String, byte[]> images = new HashMap<String, byte[]>();

			long userId = ValueGenerator.getRandomUserIdFromCache();
			long groupId = journalArticle.getGroupId();
			long folderId = 0; /* TODO upgrade to 6.2: select a random folder */

			journalArticle = JournalArticleLocalServiceUtil.updateArticle(
				userId, groupId, folderId, journalArticle.getArticleId(),
				journalArticle.getVersion(), journalArticle.getTitleMap(),
				journalArticle.getDescriptionMap(), journalArticle.getContent(),
				journalArticle.getType(), journalArticle.getStructureId(),
				journalArticle.getTemplateId(), journalArticle.getLayoutUuid(),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.YEAR), cal.get(Calendar.HOUR),
				cal.get(Calendar.MINUTE), 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0,
				true, true, false, StringPool.BLANK, null, images,
				StringPool.BLANK, serviceContext);
		}
	}

	@Override
	protected void doRun() {
		try {
			Locale defaultLocale = LocaleUtil.getSiteDefault();

			DDMStructure ddmStructure = (DDMStructure) _parameters.get(
				ActionConfig.TARGET);

			Document doc = SAXReaderUtil.read(ddmStructure.getCompleteXsd());

			Element root = doc.getRootElement();

			root.addAttribute("available-locales", defaultLocale.toString());

			root.addAttribute("default-locale", defaultLocale.toString());

			generateRepeatableFields(root);

			Map<String, byte[]> images = new HashMap<String, byte[]>();

			Map<String, Integer> repetitions = new HashMap<String, Integer>();

			addValues(root, images, defaultLocale, repetitions);

			long groupId = GetterUtil.getLong(
				_parameters.get(ConfigConstants.GROUP_ID));

			long userId = ValueGenerator.getRandomUserIdFromCache();

			long folderId = 0; /* TODO upgrade to 6.2: select a random folder */

			Map<Locale, String> titleMap = new HashMap<Locale, String>();

			titleMap.put(
				defaultLocale, ValueGenerator.getLowerCaseText(TITLE_LENGTH));

			Map<Locale, String> descriptionMap = new HashMap<Locale, String>();

			descriptionMap.put(
				defaultLocale,
				ValueGenerator.getLowerCaseText(DESCRIPTION_LENGTH));

			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

			ServiceContext serviceContext = new ServiceContext();

			serviceContext.setAttribute("defaultLanguageId", defaultLanguageId);

			serviceContext.setScopeGroupId(groupId);

			if (_log.isDebugEnabled()) {
				_log.debug("Generated xml: " + doc.formattedString());
			}

			JournalArticle journalArticle =
				JournalArticleLocalServiceUtil.addArticle(
					userId, groupId, folderId, 0, 0, StringPool.BLANK, true,
					JournalArticleConstants.VERSION_DEFAULT, titleMap,
					descriptionMap, doc.formattedString(), "general",
					ddmStructure.getStructureKey(),
					getJournalTemplateKey(ddmStructure.getStructureId()), null,
					cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
					cal.get(Calendar.YEAR), cal.get(Calendar.HOUR),
					cal.get(Calendar.MINUTE), 0, 0, 0, 0, 0, true, 0, 0, 0, 0,
					0, true, true, false, StringPool.BLANK, null, images,
					StringPool.BLANK, serviceContext);

			addVersions(journalArticle);
		}
		catch (Exception e) {
			_log.error("Error creating journal article", e);
		}
	}

	protected void generateRepeatableFields(Element element) {
		List<Element> dynamicElements = element.elements("dynamic-element");

		List<Element> dynamicElementsCopy = new ArrayList<Element>(
			dynamicElements);

		for (Element dynamicElement : dynamicElementsCopy) {
			Attribute repeatableAttr = dynamicElement.attribute("repeatable");

			boolean repeatable = false;

			if (Validator.isNotNull(repeatableAttr)) {
				dynamicElement.remove(repeatableAttr);

				String repeatableValue = repeatableAttr.getValue();

				repeatable = "true".equals(repeatableValue);
			}

			if (repeatable) {
				int repetitions = ValueGenerator.getRandomIntegerFromRange(
					0, MAX_REPETITIONS);

				int pos = dynamicElements.indexOf(dynamicElement);

				for (int i = 1; i <= repetitions; i++) {
					Element dynamicElementCopy = dynamicElement.createCopy();

					generateRepeatableFields(dynamicElementCopy);

					dynamicElements.add(pos + i, dynamicElementCopy);
				}
			}

			generateRepeatableFields(dynamicElement);
		}
	}

	protected String generateTextAreaContent() throws Exception {
		String html = "<p>";

		html += ValueGenerator.getLowerCaseText(
			1, MAX_TEXT_AREA_PARAGRAPH_LENGTH);

		html += CharPool.SPACE + getImageHtmlTag() + CharPool.SPACE +
			ValueGenerator.getLowerCaseText(1, MAX_TEXT_FIELD_LENGTH);

		html += CharPool.SPACE + getLinkToDocumentHtmlTag() + CharPool.SPACE +
			ValueGenerator.getLowerCaseText(1, MAX_TEXT_FIELD_LENGTH);

		html += CharPool.SPACE + getLinkToPageHtmlTag() + CharPool.SPACE +
			ValueGenerator.getLowerCaseText(1, MAX_TEXT_FIELD_LENGTH);

		html += "</p>";

		return html;
	}

	protected String getDLFileEntryURL(DLFileEntry dlFileEntry) {
		if (dlFileEntry == null) {
			return StringPool.BLANK;
		}

		return "/documents/" + dlFileEntry.getGroupId() +
			CharPool.FORWARD_SLASH + dlFileEntry.getFolderId() +
			CharPool.FORWARD_SLASH + dlFileEntry.getTitle() +
			CharPool.FORWARD_SLASH + dlFileEntry.getUuid();
	}

	protected String getImageHtmlTag() throws Exception {
		DLFileEntry image = ValueGenerator.getRandomImageFromDL(
			getFilterByGroupId());

		if (image == null) {
			throw new Exception(
				"There is no images in DL. Please generate at least one " +
				"before executiing this");
		}

		return "<img alt=\"\" src=\"" + getDLFileEntryURL(image) + "\" /> ";
	}

	protected String getJournalTemplateKey(long structureId) throws Exception {
		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();
		conjunction.add(
			RestrictionsFactoryUtil.eq(
				"classNameId",
				ClassNameLocalServiceUtil.getClassNameId(DDMStructure.class)));
		conjunction.add(RestrictionsFactoryUtil.eq("classPK", structureId));

		DDMTemplate ddmTemplate =
			(DDMTemplate)ValueGenerator.getRandomObject(
				DDMTemplate.class.getName(), conjunction);

		return ddmTemplate.getTemplateKey();
	}

	protected String getLinkToDocumentHtmlTag() throws Exception {
		DLFileEntry dlFileEntry = (DLFileEntry)ValueGenerator.getRandomObject(
			DLFileEntry.class.getName(), getFilterByGroupId());

		StringBundler html = new StringBundler(5);

		html.append("<a href=\"");
		html.append(getDLFileEntryURL(dlFileEntry));
		html.append("\"> ");
		html.append(dlFileEntry.getTitle());
		html.append("</a>");

		return html.toString();
	}

	protected String getLinkToLayout(Layout layout) throws Exception {
		if (layout == null) {
			return StringPool.BLANK;
		}

		String suffix = "public";

		if (layout.isPrivateLayout()) {
			if (layout.getGroup().isUser()) {
				suffix = "private-user";
			}
			else {
				suffix = "private-group";
			}
		}

		StringBundler layoutLinkSB = new StringBundler(5);

		layoutLinkSB.append(layout.getLayoutId());
		layoutLinkSB.append(CharPool.AT);
		layoutLinkSB.append(suffix);
		layoutLinkSB.append(CharPool.AT);
		layoutLinkSB.append(layout.getGroupId());

		return layoutLinkSB.toString();
	}

	protected String getLinkToPageHtmlTag() throws Exception {
		Layout layout = (Layout)ValueGenerator.getRandomObject(
			Layout.class.getName(), getFilterByGroupId());

		if (layout == null) {
			return StringPool.BLANK;
		}

		Group group = GroupLocalServiceUtil.getGroup(layout.getGroupId());

		String layoutUrlPrefix = PropsUtil.get(
			PropsKeys.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING);

		if (layout.isPrivateLayout()) {
			if (group.isSite()) {
				layoutUrlPrefix =
					PropsUtil.get(
						PropsKeys.
							LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING);
			}
			else if (group.isUser()) {
				layoutUrlPrefix =
					PropsUtil.get(
						PropsKeys.
							LAYOUT_FRIENDLY_URL_PRIVATE_USER_SERVLET_MAPPING);
			}
		}

		StringBundler html = new StringBundler(7);

		html.append("<a href=\"");
		html.append(layoutUrlPrefix);
		html.append(group.getFriendlyURL());
		html.append(layout.getFriendlyURL());
		html.append("\">");
		html.append(layout.getTitle(LocaleUtil.getSiteDefault()));
		html.append("</a>");

		return html.toString();
	}

	protected Map<String, String[]> getListsValuesFromStructure(
			Element element, Map<String, String[]> listsValues)
		throws Exception {

		List<Element> dynamicElements = element.elements("dynamic-element");

		for (Element dynamicElement : dynamicElements) {
			getListsValuesFromStructure(dynamicElement, listsValues);

			String type = dynamicElement.attributeValue("type");

			if (type.equals("radio") || type.equals("select")) {
				String name = dynamicElement.attributeValue("name");

				String[] values = getListValues(dynamicElement);

				listsValues.put(name, values);
			}
		}

		return listsValues;
	}

	protected String[] getListValues(Element elementList) {
		List<Element> valueElements = elementList.elements("dynamic-element");

		String[] values = new String[valueElements.size()];

		int i = 0;

		for (Element valueElement : valueElements) {
			values[i] = valueElement.attributeValue("value");

			i++;
		}

		return values;
	}

	protected boolean isLocalizable(Document document, Element dynamicElement) {
		String name = dynamicElement.attributeValue("name");

		name = HtmlUtil.escapeXPathAttribute(name);

		XPath xPathSelector = SAXReaderUtil.createXPath(
			"//dynamic-element[@name=".concat(name).concat("]"));

		for (Node node : xPathSelector.selectNodes(document)) {
			Element nodeElement = (Element)node;

			if (GetterUtil.getBoolean(
					nodeElement.attributeValue("localizable"))) {

				return true;
			}
		}

		return false;
	}

	private String getIndexValue(
		Map<String, Integer> repetitionsMap, String nameValue) {

		Integer index = MapUtil.getInteger(repetitionsMap, nameValue, 0);

		repetitionsMap.put(nameValue, index + 1);

		return String.valueOf(index);
	}

	private static final int DEFAULT_NUMBER_OF_VERSIONS = 30;

	private static final int DESCRIPTION_LENGTH = 20;

	private static final int MAX_REPETITIONS = 3;

	private static final int MAX_TEXT_AREA_PARAGRAPH_LENGTH = 100;

	private static final int MAX_TEXT_BOX_FIELD_LENGTH = 200;

	private static final int MAX_TEXT_FIELD_LENGTH = 50;

	private static final int PERCENTAGE_EMPTY_FIELDS = 30;

	private static final int TITLE_LENGTH = 10;

	private static Log _log = LogFactoryUtil.getLog(CreateJournalArticle.class);

}