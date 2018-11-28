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

package com.liferay.liferaygen.web.internal.actions.expando;

import com.liferay.liferaygen.config.ActionConfig;
import com.liferay.liferaygen.constants.ConfigConstants;
import com.liferay.liferaygen.impl.BaseAction;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.ClassName;
import com.liferay.portal.model.ClassedModel;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;
import com.liferay.portlet.documentlibrary.service.DLFileVersionLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoValueLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
public class AddExpandoValue extends BaseAction {

	@Override
	public String doGetDescription() {
		return "Add random expando values to an object which accepts custom " +
			"fields";
	}

	@Override
	public Map<String, Object> doGetParametersDefaultValues() {
		return null;
	}

	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(ActionConfig.TARGET, "Expando table to be used");
			}
		};
	}

	@Override
	public Criterion getEntityFilter() {
		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		conjunction.add(
			RestrictionsFactoryUtil.eq(
				"name", ExpandoTableConstants.DEFAULT_TABLE_NAME));

		return conjunction;
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return ExpandoTable.class;
	}

	@Override
	public String getEntityProperties() {
		return "classNameId";
	}

	@Override
	protected void doRun() {
		long companyId = (Long) _parameters.get(ConfigConstants.COMPANY_ID);

		long groupId = (Long) _parameters.get(ConfigConstants.GROUP_ID);

		Long classNameId = (Long) _parameters.get(ActionConfig.TARGET);

		try {
			ExpandoTable expandoTable =
				ExpandoTableLocalServiceUtil.getDefaultTable(
					companyId, classNameId);

			List<ExpandoColumn> expandoColumns =
				ExpandoColumnLocalServiceUtil.getColumns(
					expandoTable.getTableId());

			ClassName className = ClassNameLocalServiceUtil.fetchClassName(
				classNameId);

			String cnName = className.getClassName();

			long classPK = ValueGenerator.getRandomClassPK(
				cnName, companyId, groupId);

			if (classPK == 0) {
				return;
			}

			classPK = getLatestClassPK(cnName, classPK);

			Map<String, Serializable> attributes =
				new HashMap<String, Serializable>();

			for (ExpandoColumn expandoColumn : expandoColumns) {
				attributes.put(
					expandoColumn.getName(),
					getExpandoValueForExpandoColumn(expandoColumn));
			}

			ExpandoValueLocalServiceUtil.addValues(
				companyId, className.getClassName(), expandoTable.getName(),
				classPK, attributes);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected Serializable getExpandoValueForExpandoColumn(
		ExpandoColumn expandoColumn) {

		Serializable possibleValues = expandoColumn.getDefaultValue();

		switch (expandoColumn.getType()) {
			case ExpandoColumnConstants.BOOLEAN:
				return ValueGenerator.getBoolean();
			case ExpandoColumnConstants.BOOLEAN_ARRAY:
				return new boolean[] {ValueGenerator.getBoolean()};
			case ExpandoColumnConstants.DATE:
				return ValueGenerator.getRandomDate();
			case ExpandoColumnConstants.DATE_ARRAY:
				Date[] possibleDates = (Date[])possibleValues;
				Date possibleDate = ValueGenerator.getRandomObjectFromList(
					ListUtil.fromArray(possibleDates));

				if (possibleDate != null) {
					return new Date[] {possibleDate};
				}

				break;
			case ExpandoColumnConstants.DOUBLE:
				return ValueGenerator.getRandomDoubleFromRange(
					Double.MIN_VALUE, Double.MAX_VALUE);
			case ExpandoColumnConstants.DOUBLE_ARRAY:
				double[] possibleDoubles = (double[])possibleValues;
				Double possibleDouble =
					ValueGenerator.getRandomObjectFromArray(
						ArrayUtil.toArray(possibleDoubles));

				if (possibleDouble != null) {
					return new double[] {possibleDouble};
				}

				break;
			case ExpandoColumnConstants.FLOAT:
				return ValueGenerator.getRandomFloatFromRange(
					Float.MIN_VALUE, Float.MAX_VALUE);
			case ExpandoColumnConstants.FLOAT_ARRAY:
				float[] possibleFloats = (float[])possibleValues;
				Float possibleFloat = ValueGenerator.getRandomObjectFromArray(
					ArrayUtil.toArray(possibleFloats));

				if (possibleFloat != null) {
					return new float[] {possibleFloat};
				}

				break;
			case ExpandoColumnConstants.INTEGER:
				return ValueGenerator.getRandomIntegerFromRange(
					Integer.MIN_VALUE, Integer.MAX_VALUE);
			case ExpandoColumnConstants.INTEGER_ARRAY:
				int[] possibleIntegers = (int[])possibleValues;
				Integer possibleInteger =
					ValueGenerator.getRandomObjectFromArray(
						ArrayUtil.toArray(possibleIntegers));

				if (possibleInteger != null) {
					return new int[] {possibleInteger};
				}

				break;
			case ExpandoColumnConstants.LONG:
				return ValueGenerator.getRandomLongFromRange(
					Long.MIN_VALUE, Long.MAX_VALUE);
			case ExpandoColumnConstants.LONG_ARRAY:
				long[] possibleLongs = (long[])possibleValues;
				Long possibleLong = ValueGenerator.getRandomObjectFromArray(
					ArrayUtil.toArray(possibleLongs));

				if (possibleLong != null) {
					return new long[] {possibleLong};
				}

				break;
			case ExpandoColumnConstants.NUMBER:
				return ValueGenerator.getRandomDoubleFromRange(
					Double.MIN_VALUE, Double.MAX_VALUE);
			case ExpandoColumnConstants.NUMBER_ARRAY:
				Number[] possibleNumbers = (Number[])possibleValues;
				Number possibleNumber = ValueGenerator.getRandomObjectFromList(
					ListUtil.fromArray(possibleNumbers));

				if (possibleNumber != null) {
					return new Number[] {possibleNumber};
				}

				break;
			case ExpandoColumnConstants.SHORT:
				return ValueGenerator.getRandomShortFromRange(
					Short.MIN_VALUE, Short.MAX_VALUE);
			case ExpandoColumnConstants.SHORT_ARRAY:
				short[] possibleShorts = (short[])possibleValues;
				Short possibleShort = ValueGenerator.getRandomObjectFromArray(
					ArrayUtil.toArray(possibleShorts));

				if (possibleShort != null) {
					return new short[] {possibleShort};
				}

				break;
			case ExpandoColumnConstants.STRING:
				return ValueGenerator.getLowerCaseText(25);
			case ExpandoColumnConstants.STRING_ARRAY:
				String[] possibleStrings = (String[])possibleValues;
				String possibleString = ValueGenerator.getRandomObjectFromList(
					ListUtil.fromArray(possibleStrings));

				if (possibleString != null) {
					return new String[] {possibleString};
				}

				break;
			case ExpandoColumnConstants.STRING_LOCALIZED:
				HashMap<Locale, String> stringLocalizedMap =
					new HashMap<Locale, String>();
				stringLocalizedMap.put(
					LocaleUtil.getDefault(),
					ValueGenerator.getLowerCaseText(25));
				return stringLocalizedMap;
		}

		return possibleValues;
	}

	private long getLatestClassPK(String cnName, long classPK)
		throws SystemException {

		if (JournalArticle.class.getName().equals(cnName)) {
			JournalArticle journalArticle =
				JournalArticleLocalServiceUtil.fetchLatestArticle(
					classPK, WorkflowConstants.STATUS_ANY, true);

			if (journalArticle == null) {
				return classPK;
			}

			classPK = journalArticle.getPrimaryKey();
		}

		if (DLFileEntry.class.getName().equals(cnName)) {
			DLFileVersion dlFileVersion = null;
			try {
				dlFileVersion =
					DLFileVersionLocalServiceUtil.getLatestFileVersion(
						classPK, true);
			}
			catch (Exception e) {
				return classPK;
			}

			classPK = dlFileVersion.getPrimaryKey();
		}

		return classPK;
	}

	private static Log _log = LogFactoryUtil.getLog(AddExpandoValue.class);

}