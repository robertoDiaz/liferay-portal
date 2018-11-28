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

import com.liferay.liferaygen.constants.ConfigConstants;
import com.liferay.liferaygen.impl.BaseAction;
import com.liferay.liferaygen.util.ValueGenerator;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portlet.expando.model.CustomAttributesDisplay;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.service.ExpandoColumnLocalServiceUtil;
import com.liferay.portlet.expando.service.ExpandoTableLocalServiceUtil;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class CreateExpandoField extends BaseAction {

	@Override
	public String doGetDescription() {
		return "Creates an expando field";
	}

	@Override
	public Map<String, Object> doGetParametersDefaultValues() {
		return null;
	}

	@Override
	public Map<String, String> doGetParametersDescription() {
		return null;
	}

	@Override
	protected void doRun() {

		long companyId = (Long) _parameters.get(ConfigConstants.COMPANY_ID);

		List<CustomAttributesDisplay> customAttributesDisplays =
				PortletLocalServiceUtil.getCustomAttributesDisplays();

		CustomAttributesDisplay cad = ValueGenerator.getRandomObjectFromList(
			customAttributesDisplays);

		try {
			ExpandoTable expandoTable = null;
			try {
				expandoTable = ExpandoTableLocalServiceUtil.addDefaultTable(
						companyId, cad.getClassName());
			}
			catch (Exception e) {
				expandoTable = ExpandoTableLocalServiceUtil.getDefaultTable(
						companyId, cad.getClassName());
			}

			String name = ValueGenerator.getLowerCaseWord(10);
			int type = ValueGenerator.getRandomObjectFromArray(availableTypes);

			Serializable defaultData = getDefaultDataByType(type);
			ExpandoColumn expandoColumn =
					ExpandoColumnLocalServiceUtil.addColumn(
							expandoTable.getTableId(), name, type, defaultData);

			UnicodeProperties typeSettings = new UnicodeProperties();
			typeSettings.put(
				ExpandoColumnConstants.PROPERTY_HIDDEN,
				GetterUtil.getString(ValueGenerator.getBoolean()));
			typeSettings.put(
				ExpandoColumnConstants.PROPERTY_VISIBLE_WITH_UPDATE_PERMISSION,
				GetterUtil.getString(ValueGenerator.getBoolean()));

			switch (expandoColumn.getType()) {
				case ExpandoColumnConstants.BOOLEAN:
				case ExpandoColumnConstants.DATE:
				case ExpandoColumnConstants.DOUBLE:
				case ExpandoColumnConstants.FLOAT:
				case ExpandoColumnConstants.INTEGER:
				case ExpandoColumnConstants.LONG:
				case ExpandoColumnConstants.NUMBER:
				case ExpandoColumnConstants.SHORT:
					typeSettings.put(
						ExpandoColumnConstants.INDEX_TYPE,
						GetterUtil.getString(
							ValueGenerator.getRandomObjectFromList(
								ListUtil.fromArray(new Integer[] {
									ExpandoColumnConstants.INDEX_TYPE_NONE,
									ExpandoColumnConstants.INDEX_TYPE_KEYWORD
								}))));
					break;
				case ExpandoColumnConstants.BOOLEAN_ARRAY:
				case ExpandoColumnConstants.DATE_ARRAY:
				case ExpandoColumnConstants.DOUBLE_ARRAY:
				case ExpandoColumnConstants.FLOAT_ARRAY:
				case ExpandoColumnConstants.INTEGER_ARRAY:
				case ExpandoColumnConstants.LONG_ARRAY:
				case ExpandoColumnConstants.NUMBER_ARRAY:
				case ExpandoColumnConstants.SHORT_ARRAY:
					typeSettings.put(
						ExpandoColumnConstants.INDEX_TYPE,
						GetterUtil.getString(
							ValueGenerator.getRandomObjectFromList(
								ListUtil.fromArray(new Integer[] {
									ExpandoColumnConstants.INDEX_TYPE_NONE,
									ExpandoColumnConstants.INDEX_TYPE_KEYWORD
								}))));
					typeSettings.put(
						ExpandoColumnConstants.PROPERTY_DISPLAY_TYPE,
						GetterUtil.getString(
							ValueGenerator.getRandomObjectFromList(
								ListUtil.fromArray(new String[] {
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_CHECKBOX,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_RADIO,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_SELECTION_LIST,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_TEXT_BOX}))));
					break;
				case ExpandoColumnConstants.STRING:
					typeSettings.put(
						ExpandoColumnConstants.PROPERTY_HEIGHT,
						GetterUtil.getString(
							ValueGenerator.getRandomIntegerFromRange(
								10, 1000)));
					typeSettings.put(
						ExpandoColumnConstants.PROPERTY_WIDTH,
						GetterUtil.getString(
							ValueGenerator.getRandomIntegerFromRange(
								10, 1000)));
					typeSettings.put(
						ExpandoColumnConstants.PROPERTY_SECRET,
						GetterUtil.getString(ValueGenerator.getBoolean()));
					typeSettings.put(
						ExpandoColumnConstants.INDEX_TYPE,
						GetterUtil.getString(
							ValueGenerator.getRandomObjectFromList(
								ListUtil.fromArray(new Integer[] {
									ExpandoColumnConstants.INDEX_TYPE_NONE,
									ExpandoColumnConstants.INDEX_TYPE_TEXT,
									ExpandoColumnConstants.INDEX_TYPE_KEYWORD
								}))));
					break;
				case ExpandoColumnConstants.STRING_ARRAY:
					typeSettings.put(
						ExpandoColumnConstants.INDEX_TYPE,
						GetterUtil.getString(
							ValueGenerator.getRandomObjectFromList(
								ListUtil.fromArray(new Integer[] {
									ExpandoColumnConstants.INDEX_TYPE_NONE,
									ExpandoColumnConstants.INDEX_TYPE_TEXT,
									ExpandoColumnConstants.INDEX_TYPE_KEYWORD
								}))));
					typeSettings.put(
						ExpandoColumnConstants.PROPERTY_DISPLAY_TYPE,
						GetterUtil.getString(
							ValueGenerator.getRandomObjectFromList(
								ListUtil.fromArray(new String[] {
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_CHECKBOX,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_RADIO,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_SELECTION_LIST,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_TEXT_BOX}))));
					break;
				default:
					break;
			}

			ExpandoColumnLocalServiceUtil.updateTypeSettings(
				expandoColumn.getColumnId(), typeSettings.toString());
		}
		catch (PortalException e) {
			_log.error(e, e);
		}
		catch (SystemException e) {
			_log.error(e, e);
		}
	}

	protected Serializable getDefaultDataByType(int type) {
		switch (type) {
			case ExpandoColumnConstants.BOOLEAN:
				return ValueGenerator.getBoolean();
			case ExpandoColumnConstants.BOOLEAN_ARRAY:
				return new boolean[] {ValueGenerator.getBoolean()};
			case ExpandoColumnConstants.DATE:
				return ValueGenerator.getRandomDate();
			case ExpandoColumnConstants.DATE_ARRAY:
				return new Date[] {ValueGenerator.getRandomDate()};
			case ExpandoColumnConstants.DOUBLE:
				return ValueGenerator.getRandomDoubleFromRange(
					Double.MIN_VALUE, Double.MAX_VALUE);
			case ExpandoColumnConstants.DOUBLE_ARRAY:
				return new double[] {ValueGenerator.getRandomDoubleFromRange(
					Double.MIN_VALUE, Double.MAX_VALUE)};
			case ExpandoColumnConstants.FLOAT:
				return ValueGenerator.getRandomFloatFromRange(
					Float.MIN_VALUE, Float.MAX_VALUE);
			case ExpandoColumnConstants.FLOAT_ARRAY:
				return new float[] {ValueGenerator.getRandomFloatFromRange(
					Float.MIN_VALUE, Float.MAX_VALUE)};
			case ExpandoColumnConstants.INTEGER:
				return ValueGenerator.getRandomIntegerFromRange(
					Integer.MIN_VALUE, Integer.MAX_VALUE);
			case ExpandoColumnConstants.INTEGER_ARRAY:
				return new int[] {ValueGenerator.getRandomIntegerFromRange(
					Integer.MIN_VALUE, Integer.MAX_VALUE)};
			case ExpandoColumnConstants.LONG:
				return ValueGenerator.getRandomLongFromRange(
					Long.MIN_VALUE, Long.MAX_VALUE);
			case ExpandoColumnConstants.LONG_ARRAY:
				return new long[] {ValueGenerator.getRandomLongFromRange(
					Long.MIN_VALUE, Long.MAX_VALUE)};
			case ExpandoColumnConstants.NUMBER:
				return ValueGenerator.getRandomDoubleFromRange(
					Double.MIN_VALUE, Double.MAX_VALUE);
			case ExpandoColumnConstants.NUMBER_ARRAY:
				return new Number[] {ValueGenerator.getRandomDoubleFromRange(
					Double.MIN_VALUE, Double.MAX_VALUE)};
			case ExpandoColumnConstants.SHORT:
				return ValueGenerator.getRandomShortFromRange(
					Short.MIN_VALUE, Short.MAX_VALUE);
			case ExpandoColumnConstants.SHORT_ARRAY:
				return new short[] {ValueGenerator.getRandomShortFromRange(
					Short.MIN_VALUE, Short.MAX_VALUE)};
			case ExpandoColumnConstants.STRING:
				return ValueGenerator.getLowerCaseText(20);
			case ExpandoColumnConstants.STRING_ARRAY:
				return new String[] {ValueGenerator.getLowerCaseText(20)};
			case ExpandoColumnConstants.STRING_LOCALIZED:
				HashMap<Locale, String> stringLocalizedMap =
					new HashMap<Locale, String>();
				stringLocalizedMap.put(
					LocaleUtil.getDefault(),
					ValueGenerator.getLowerCaseText(20));
				return stringLocalizedMap;
			case ExpandoColumnConstants.STRING_ARRAY_LOCALIZED:
				HashMap<Locale, String[]> stringArrayLocalizedMap =
					new HashMap<Locale, String[]>();
				stringArrayLocalizedMap.put(
					LocaleUtil.getDefault(),
					new String[] {ValueGenerator.getLowerCaseText(20)});
				return stringArrayLocalizedMap;
		}

		return StringPool.BLANK;
	}

	private static final Integer[] availableTypes = {
		ExpandoColumnConstants.BOOLEAN, ExpandoColumnConstants.DATE,
		ExpandoColumnConstants.DOUBLE, ExpandoColumnConstants.DOUBLE_ARRAY,
		ExpandoColumnConstants.FLOAT, ExpandoColumnConstants.FLOAT_ARRAY,
		ExpandoColumnConstants.INTEGER, ExpandoColumnConstants.INTEGER_ARRAY,
		ExpandoColumnConstants.LONG, ExpandoColumnConstants.LONG_ARRAY,
		ExpandoColumnConstants.NUMBER, ExpandoColumnConstants.NUMBER_ARRAY,
		ExpandoColumnConstants.SHORT, ExpandoColumnConstants.SHORT_ARRAY,
		ExpandoColumnConstants.STRING, ExpandoColumnConstants.STRING_ARRAY,
		ExpandoColumnConstants.STRING_LOCALIZED
	};

	private static Log _log = LogFactoryUtil.getLog(CreateExpandoField.class);

}