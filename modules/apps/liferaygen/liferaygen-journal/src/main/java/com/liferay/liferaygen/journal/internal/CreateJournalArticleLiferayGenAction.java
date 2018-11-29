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

package com.liferay.liferaygen.journal.internal;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMTemplate;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.model.JournalArticleConstants;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.PwdGenerator;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Attribute;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

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
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.journal.internal.CreateJournalArticleLiferayGenAction",
	service = LiferayGenAction.class
)
public class CreateJournalArticleLiferayGenAction extends BaseLiferayGenAction {

	@Override
	public String doGetDescription() {
		return "Creates a random web content using a structure";
	}

	@Override
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("numberOfVersions", _DEFAULT_NUMBER_OF_VERSIONS);
				put("structureId", null);
			}
		};
	}

	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					LiferayGenActionConfig.TARGET,
					"Structure to use during add action");
				put(
					"numberOfVersions",
					"Number of versions to add per web content");
				put("structureId", "Structure used to create web contents");
			}
		};
	}

	@Override
	public Criterion getEntityFilter() {
		long journalArticleClassNameId = _portal.getClassNameId(
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
	public String getEntityModelPK() {
		return "structureId";
	}

	protected static Calendar toCalendar(Date date) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		return cal;
	}

	protected void addDynamicContent(
			LiferayGenValueGenerator liferayGenValueGenerator,
			Element dynamicContent, String fieldType,
			Map<String, byte[]> images, String instanceId, String name,
			String[] listElementTypes, String index)
		throws Exception {

		if (fieldType.equals("boolean")) {
			Boolean value = liferayGenValueGenerator.getBoolean();

			dynamicContent.addCDATA(value.toString());
		}
		else if (fieldType.equals("ddm-date")) {
			Long value = liferayGenValueGenerator.getRandomLongFromRange(
				0, 100 * 365 * 24 * 60 * 60 * 1000);

			dynamicContent.addCDATA(String.valueOf(value * 1000));
		}
		else if (fieldType.equals("ddm-decimal")) {
			Float value = liferayGenValueGenerator.getRandomFloatFromRange(
				0, Float.MAX_VALUE);

			dynamicContent.addCDATA(value.toString());
		}
		else if (fieldType.equals("document_library")) {
			DLFileEntry dlFileEntry =
				(DLFileEntry)liferayGenValueGenerator.getRandomObject(
					DLFileEntry.class.getName(), getFilterByGroupId());

			dynamicContent.addCDATA(getDLFileEntryURL(dlFileEntry));
		}
		else if (fieldType.equals("text_area")) {
			dynamicContent.addText(
				generateTextAreaContent(liferayGenValueGenerator));
		}
		else if (fieldType.equals("image")) {
			String formatName =
				liferayGenValueGenerator.getRandomObjectFromList(
					liferayGenValueGenerator.getAvailableImageIOFormats());

			byte[] image = liferayGenValueGenerator.getRandomImage(
				formatName,
				liferayGenValueGenerator.getRandomIntegerFromRange(100, 200),
				liferayGenValueGenerator.getRandomIntegerFromRange(60, 120));

			if (image == null) {
				image = new byte[0];
			}

			String key = StringBundler.concat(
				instanceId, "_", name, "_", index);

			String languageId = dynamicContent.attributeValue("language-id");

			if (languageId != null) {
				key = key + "_" + languageId;
			}

			images.put(key, image);

			dynamicContent.addCDATA("dummy_text");
		}
		else if (fieldType.equals("ddm-integer")) {
			Integer value = liferayGenValueGenerator.getRandomIntegerFromRange(
				0, Integer.MAX_VALUE);

			dynamicContent.addCDATA(value.toString());
		}
		else if (fieldType.equals("ddm-number")) {
			Double value = liferayGenValueGenerator.getRandomDoubleFromRange(
				0, Double.MAX_VALUE);

			dynamicContent.addCDATA(value.toString());
		}
		else if (fieldType.equals("link_to_layout")) {
			Layout layout = (Layout)liferayGenValueGenerator.getRandomObject(
				Layout.class.getName(), getFilterByGroupId());

			dynamicContent.addCDATA(getLinkToLayout(layout));
		}
		else if (fieldType.equals("radio") || fieldType.endsWith("list")) {
			if (fieldType.equals("radio")) {
				dynamicContent.addCDATA(
					StringBundler.concat(
						"[\"",
						liferayGenValueGenerator.getRandomObjectFromArray(
							listElementTypes),
						"\"]"));
			}
			else if (fieldType.equals("list")) {
				dynamicContent.addCDATA(
					liferayGenValueGenerator.getRandomObjectFromArray(
						listElementTypes));
			}
			else {
				List<String> values =
					liferayGenValueGenerator.getRandomObjectsFromArray(
						listElementTypes,
						liferayGenValueGenerator.getRandomIntegerFromRange(
							1, listElementTypes.length));

				for (String value : values) {
					Element option = dynamicContent.addElement("option");

					option.addCDATA(value);
				}
			}
		}
		else if (fieldType.startsWith("text")) {
			dynamicContent.addText(
				liferayGenValueGenerator.getLowerCaseText(
					1, _MAX_TEXT_FIELD_LENGTH));
		}
	}

	protected void addLocalization(
			LiferayGenValueGenerator liferayGenValueGenerator, Element element,
			Map<String, byte[]> images, Document structureDoc,
			Map<String, String[]> listValues, Locale locale)
		throws Exception {

		List<Element> dynamicElements = element.elements("dynamic-element");

		for (Element dynamicElement : dynamicElements) {
			addLocalization(
				liferayGenValueGenerator, dynamicElement, images, structureDoc,
				listValues, locale);

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
					liferayGenValueGenerator, dynamicContentCurrentLocale, type,
					images, instanceId, name, listValues.get(name), index);
			}
		}
	}

	protected JournalArticle addLocalizationVersion(
			LiferayGenValueGenerator liferayGenValueGenerator,
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

		Map<String, byte[]> images = new HashMap<>();

		long journalArticleClassnameId = _classNameLocalService.getClassNameId(
			JournalArticle.class);

		//TODO obtener esto: (null para compilar)
		/*DDMStructure ddmStructure =
			_ddmStructureLocalService.getStructure(
				journalArticle.getGroupId(), journalArticleClassnameId,
				journalArticle.getStructureId());

		Document structureDoc = SAXReaderUtil.read(structure.getCompleteXsd());
		*/

		DDMStructure ddmStructure = _ddmStructureLocalService.getStructure(
			journalArticle.getGroupId(), journalArticleClassnameId,
			journalArticle.getDDMStructureKey());

		Document structureDoc = null;

		Map<String, String[]> listsValues = new TreeMap<>();

		addLocalization(
			liferayGenValueGenerator, root, images, structureDoc,
			getListsValuesFromStructure(
				structureDoc.getRootElement(), listsValues),
			locale);

		return _journalArticleLocalService.updateArticleTranslation(
			journalArticle.getGroupId(), journalArticle.getArticleId(),
			journalArticle.getVersion(), locale,
			liferayGenValueGenerator.getLowerCaseText(_TITLE_LENGTH),
			liferayGenValueGenerator.getLowerCaseText(_DESCRIPTION_LENGTH),
			doc.formattedString(), images, serviceContext);
	}

	protected void addValue(
			LiferayGenValueGenerator liferayGenValueGenerator, Element element,
			Map<String, byte[]> images, Locale locale)
		throws Exception {

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

		if (liferayGenValueGenerator.getBoolean(_PERCENTAGE_EMPTY_FIELDS)) {
			if (type.equals("radio")) {
				dynamicContent.addCDATA("[]");
			}
			else {
				dynamicContent.addCDATA(StringPool.BLANK);
			}

			return;
		}

		addDynamicContent(
			liferayGenValueGenerator, dynamicContent, type, images, instanceId,
			name, listElementTypes, index);
	}

	protected void addValues(
			LiferayGenValueGenerator liferayGenValueGenerator, Element element,
			Map<String, byte[]> images, Locale locale,
			Map<String, Integer> repetitionsMap)
		throws Exception {

		List<Element> dynamicElements = element.elements("dynamic-element");

		for (Element dynamicElement : dynamicElements) {
			String nameValue = dynamicElement.attributeValue("name");

			dynamicElement.addAttribute(
				"index", _getIndexValue(repetitionsMap, nameValue));

			addValues(
				liferayGenValueGenerator, dynamicElement, images, locale,
				repetitionsMap);

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
							dynamicElement.attributeValue("multiple"))) {

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

			addValue(liferayGenValueGenerator, dynamicElement, images, locale);
		}
	}

	protected void addVersions(
			LiferayGenValueGenerator liferayGenValueGenerator,
			Map<String, Object> parameters, JournalArticle journalArticle)
		throws Exception {

		int numberOfVersions = GetterUtil.getInteger(
			MapUtil.getString(parameters, "numberOfVersions"));

		if (numberOfVersions == 0) {
			return;
		}

		Locale[] locales = (Locale[])parameters.get(
			LiferayGenConfigConstants.LOCALES);

		Locale defaultLocale = LocaleUtil.getSiteDefault();

		String defaultLanguageId = LocaleUtil.toLanguageId(defaultLocale);

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setAttribute("defaultLanguageId", defaultLanguageId);
		serviceContext.setScopeGroupId(journalArticle.getGroupId());

		Document doc = SAXReaderUtil.read(journalArticle.getContent());

		for (int i = 0; i < numberOfVersions; i++) {
			if ((i < locales.length) && (locales[i] != defaultLocale)) {
				journalArticle = addLocalizationVersion(
					liferayGenValueGenerator, journalArticle, doc, locales[i],
					serviceContext);
			}

			Calendar cal = toCalendar(journalArticle.getDisplayDate());

			Map<String, byte[]> images = new HashMap<>();

			long userId = liferayGenValueGenerator.getRandomUserIdFromCache();
			long groupId = journalArticle.getGroupId();
			long folderId = 0; /* TODO upgrade to 6.2: select a random folder */

			//TODO obtener esto: (null para compilar)
			/*journalArticle = _journalArticleLocalService.updateArticle(
				userId, groupId, folderId, journalArticle.getArticleId(),
				journalArticle.getVersion(), journalArticle.getTitleMap(),
				journalArticle.getDescriptionMap(), journalArticle.getContent(),
				journalArticle.getType(), journalArticle.getStructureId(),
				journalArticle.getTemplateId(), journalArticle.getLayoutUuid(),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
				cal.get(Calendar.YEAR), cal.get(Calendar.HOUR),
				cal.get(Calendar.MINUTE), 0, 0, 0, 0, 0, true, 0, 0, 0, 0, 0,
				true, true, false, StringPool.BLANK, null, images,
				StringPool.BLANK, serviceContext);*/

			journalArticle = _journalArticleLocalService.updateArticle(
				userId, groupId, folderId, journalArticle.getArticleId(),
				journalArticle.getVersion(), journalArticle.getTitleMap(),
				journalArticle.getDescriptionMap(), null, null,
				journalArticle.getStructureId(), journalArticle.getTemplateId(),
				journalArticle.getLayoutUuid(), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
				cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), 0, 0, 0, 0, 0,
				true, 0, 0, 0, 0, 0, true, true, false, StringPool.BLANK, null,
				images, StringPool.BLANK, serviceContext);
		}
	}

	@Override
	protected void doRun() {
		try {
			Locale defaultLocale = LocaleUtil.getSiteDefault();

			Map<String, Object> parameters = getParameters();

			DDMStructure ddmStructure = (DDMStructure)parameters.get(
				LiferayGenActionConfig.TARGET);

			//TODO null para compilar
			//Document doc = SAXReaderUtil.read(ddmStructure.getCompleteXsd());

			Document doc = null;

			Element root = doc.getRootElement();

			root.addAttribute("available-locales", defaultLocale.toString());

			root.addAttribute("default-locale", defaultLocale.toString());

			LiferayGenValueGenerator liferayGenValueGenerator =
				new LiferayGenValueGenerator(
					_companyLocalService, _liferayGenQueryHandler, _portal,
					_portletLocalService);

			generateRepeatableFields(liferayGenValueGenerator, root);

			Map<String, byte[]> images = new HashMap<>();

			Map<String, Integer> repetitions = new HashMap<>();

			addValues(
				liferayGenValueGenerator, root, images, defaultLocale,
				repetitions);

			long groupId = GetterUtil.getLong(
				parameters.get(LiferayGenConfigConstants.GROUP_ID));

			long userId = liferayGenValueGenerator.getRandomUserIdFromCache();

			long folderId = 0; /* TODO upgrade to 6.2: select a random folder */

			Map<Locale, String> titleMap = new HashMap<>();

			titleMap.put(
				defaultLocale,
				liferayGenValueGenerator.getLowerCaseText(_TITLE_LENGTH));

			Map<Locale, String> descriptionMap = new HashMap<>();

			descriptionMap.put(
				defaultLocale,
				liferayGenValueGenerator.getLowerCaseText(_DESCRIPTION_LENGTH));

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

			//TODO cambiar el null por lo que corresponda
			/*JournalArticle journalArticle =
				_journalArticleLocalService.addArticle(
					userId, groupId, folderId, 0, 0, StringPool.BLANK, true,
					JournalArticleConstants.VERSION_DEFAULT, titleMap,
					descriptionMap, doc.formattedString(), "general",
					ddmStructure.getStructureKey(),
					getJournalTemplateKey(ddmStructure.getStructureId()), null,
					cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
					cal.get(Calendar.YEAR), cal.get(Calendar.HOUR),
					cal.get(Calendar.MINUTE), 0, 0, 0, 0, 0, true, 0, 0, 0, 0,
					0, true, true, false, StringPool.BLANK, null, images,
					StringPool.BLANK, serviceContext);*/

			JournalArticle journalArticle =
				_journalArticleLocalService.addArticle(
					userId, groupId, folderId, 0, 0, StringPool.BLANK, true,
					JournalArticleConstants.VERSION_DEFAULT, titleMap,
					descriptionMap, null, "general",
					ddmStructure.getStructureKey(),
					getJournalTemplateKey(
						liferayGenValueGenerator,
						ddmStructure.getStructureId()),
					null, cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR),
					cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), 0, 0, 0,
					0, 0, true, 0, 0, 0, 0, 0, true, true, false,
					StringPool.BLANK, null, images, StringPool.BLANK,
					serviceContext);

			addVersions(liferayGenValueGenerator, parameters, journalArticle);
		}
		catch (Exception e) {
			_log.error("Error creating journal article", e);
		}
	}

	protected void generateRepeatableFields(
		LiferayGenValueGenerator liferayGenValueGenerator, Element element) {

		List<Element> dynamicElements = element.elements("dynamic-element");

		List<Element> dynamicElementsCopy = new ArrayList<>(dynamicElements);

		for (Element dynamicElement : dynamicElementsCopy) {
			Attribute repeatableAttr = dynamicElement.attribute("repeatable");

			boolean repeatable = false;

			if (repeatableAttr != null) {
				dynamicElement.remove(repeatableAttr);

				String repeatableValue = repeatableAttr.getValue();

				repeatable = repeatableValue.equals(StringPool.TRUE);
			}

			if (repeatable) {
				int repetitions =
					liferayGenValueGenerator.getRandomIntegerFromRange(
						0, _MAX_REPETITIONS);

				int pos = dynamicElements.indexOf(dynamicElement);

				for (int i = 1; i <= repetitions; i++) {
					Element dynamicElementCopy = dynamicElement.createCopy();

					generateRepeatableFields(
						liferayGenValueGenerator, dynamicElementCopy);

					dynamicElements.add(pos + i, dynamicElementCopy);
				}
			}

			generateRepeatableFields(liferayGenValueGenerator, dynamicElement);
		}
	}

	protected String generateTextAreaContent(
			LiferayGenValueGenerator liferayGenValueGenerator)
		throws Exception {

		return StringBundler.concat(
			"<p>",
			liferayGenValueGenerator.getLowerCaseText(
				1, _MAX_TEXT_AREA_PARAGRAPH_LENGTH),
			CharPool.SPACE, getImageHtmlTag(liferayGenValueGenerator),
			CharPool.SPACE,
			liferayGenValueGenerator.getLowerCaseText(
				1, _MAX_TEXT_FIELD_LENGTH),
			CharPool.SPACE, getLinkToDocumentHtmlTag(liferayGenValueGenerator),
			CharPool.SPACE,
			liferayGenValueGenerator.getLowerCaseText(
				1, _MAX_TEXT_FIELD_LENGTH),
			CharPool.SPACE, getLinkToPageHtmlTag(liferayGenValueGenerator),
			CharPool.SPACE,
			liferayGenValueGenerator.getLowerCaseText(
				1, _MAX_TEXT_FIELD_LENGTH),
			"</p>");
	}

	protected String getDLFileEntryURL(DLFileEntry dlFileEntry) {
		if (dlFileEntry == null) {
			return StringPool.BLANK;
		}

		return StringBundler.concat(
			"/documents/", dlFileEntry.getGroupId(), CharPool.FORWARD_SLASH,
			dlFileEntry.getFolderId(), CharPool.FORWARD_SLASH,
			dlFileEntry.getTitle(), CharPool.FORWARD_SLASH,
			dlFileEntry.getUuid());
	}

	protected String getImageHtmlTag(
			LiferayGenValueGenerator liferayGenValueGenerator)
		throws Exception {

		DLFileEntry image = liferayGenValueGenerator.getRandomImageFromDL(
			getFilterByGroupId());

		if (image == null) {
			throw new Exception(
				"No images could be found in DL Repository. Please generate " +
					"at least one before executing this.");
		}

		return "<img alt=\"\" src=\"" + getDLFileEntryURL(image) + "\" /> ";
	}

	protected String getJournalTemplateKey(
			LiferayGenValueGenerator liferayGenValueGenerator, long structureId)
		throws Exception {

		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		conjunction.add(
			RestrictionsFactoryUtil.eq(
				"classNameId",
				_classNameLocalService.getClassNameId(DDMStructure.class)));
		conjunction.add(RestrictionsFactoryUtil.eq("classPK", structureId));

		DDMTemplate ddmTemplate =
			(DDMTemplate)liferayGenValueGenerator.getRandomObject(
				DDMTemplate.class.getName(), conjunction);

		return ddmTemplate.getTemplateKey();
	}

	protected String getLinkToDocumentHtmlTag(
			LiferayGenValueGenerator liferayGenValueGenerator)
		throws Exception {

		DLFileEntry dlFileEntry =
			(DLFileEntry)liferayGenValueGenerator.getRandomObject(
				DLFileEntry.class.getName(), getFilterByGroupId());

		return StringBundler.concat(
			"<a href=\"", getDLFileEntryURL(dlFileEntry), "\"> ",
			dlFileEntry.getTitle(), "</a>");
	}

	protected String getLinkToLayout(Layout layout) throws Exception {
		if (layout == null) {
			return StringPool.BLANK;
		}

		String suffix = "public";

		if (layout.isPrivateLayout()) {
			Group group = layout.getGroup();

			if (group.isUser()) {
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

	protected String getLinkToPageHtmlTag(
			LiferayGenValueGenerator liferayGenValueGenerator)
		throws Exception {

		Layout layout = (Layout)liferayGenValueGenerator.getRandomObject(
			Layout.class.getName(), getFilterByGroupId());

		if (layout == null) {
			return StringPool.BLANK;
		}

		Group group = _groupLocalService.getGroup(layout.getGroupId());

		String layoutUrlPrefix = PropsUtil.get(
			PropsKeys.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING);

		if (layout.isPrivateLayout()) {
			if (group.isSite()) {
				layoutUrlPrefix = PropsUtil.get(
					PropsKeys.
						LAYOUT_FRIENDLY_URL_PRIVATE_GROUP_SERVLET_MAPPING);
			}
			else if (group.isUser()) {
				layoutUrlPrefix = PropsUtil.get(
					PropsKeys.LAYOUT_FRIENDLY_URL_PRIVATE_USER_SERVLET_MAPPING);
			}
		}

		return StringBundler.concat(
			"<a href=\"", layoutUrlPrefix, group.getFriendlyURL(),
			layout.getFriendlyURL(), "\">",
			layout.getTitle(LocaleUtil.getSiteDefault()), "</a>");
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

	private String _getIndexValue(
		Map<String, Integer> repetitionsMap, String nameValue) {

		Integer index = MapUtil.getInteger(repetitionsMap, nameValue, 0);

		repetitionsMap.put(nameValue, index + 1);

		return String.valueOf(index);
	}

	private static final int _DEFAULT_NUMBER_OF_VERSIONS = 30;

	private static final int _DESCRIPTION_LENGTH = 20;

	private static final int _MAX_REPETITIONS = 3;

	private static final int _MAX_TEXT_AREA_PARAGRAPH_LENGTH = 100;

	private static final int _MAX_TEXT_FIELD_LENGTH = 50;

	private static final int _PERCENTAGE_EMPTY_FIELDS = 30;

	private static final int _TITLE_LENGTH = 10;

	private static final Log _log = LogFactoryUtil.getLog(
		CreateJournalArticleLiferayGenAction.class);

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private ExpandoValueLocalService _expandoValueLocalService;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}