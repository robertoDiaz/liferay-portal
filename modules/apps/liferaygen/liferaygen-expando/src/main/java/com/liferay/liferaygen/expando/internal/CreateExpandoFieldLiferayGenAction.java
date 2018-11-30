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

package com.liferay.liferaygen.expando.internal;

import com.liferay.expando.kernel.model.CustomAttributesDisplay;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.UnicodeProperties;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.expando.internal.CreateExpandoFieldLiferayGenAction",
	service = LiferayGenAction.class
)
public class CreateExpandoFieldLiferayGenAction extends BaseLiferayGenAction {

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
	public String getName() {
		return "CreateExpandoFieldLiferayGenAction";
	}

	@Override
	protected void doRun() {
		Map<String, Object> parameters = getParameters();

		long companyId = (Long)parameters.get(
			LiferayGenConfigConstants.COMPANY_ID);

		List<CustomAttributesDisplay> customAttributesDisplays =
			_portletLocalService.getCustomAttributesDisplays();

		LiferayGenValueGenerator liferayGenValueGenerator =
			new LiferayGenValueGenerator(
				_companyLocalService, _liferayGenQueryHandler, _portal,
				_portletLocalService);

		CustomAttributesDisplay customAttributesDisplay =
			liferayGenValueGenerator.getRandomObjectFromList(
				customAttributesDisplays);

		try {
			ExpandoTable expandoTable = null;

			try {
				expandoTable = _expandoTableLocalService.addDefaultTable(
					companyId, customAttributesDisplay.getClassName());
			}
			catch (Exception e) {
				expandoTable = _expandoTableLocalService.getDefaultTable(
					companyId, customAttributesDisplay.getClassName());
			}

			String name = liferayGenValueGenerator.getLowerCaseWord(10);

			int type = liferayGenValueGenerator.getRandomObjectFromArray(
				_availableTypes);

			Serializable defaultData = getDefaultDataByType(
				liferayGenValueGenerator, type);

			ExpandoColumn expandoColumn = _expandoColumnLocalService.addColumn(
				expandoTable.getTableId(), name, type, defaultData);

			UnicodeProperties typeSettings = new UnicodeProperties();

			typeSettings.put(
				ExpandoColumnConstants.PROPERTY_HIDDEN,
				GetterUtil.getString(liferayGenValueGenerator.getBoolean()));
			typeSettings.put(
				ExpandoColumnConstants.PROPERTY_VISIBLE_WITH_UPDATE_PERMISSION,
				GetterUtil.getString(liferayGenValueGenerator.getBoolean()));

			if (expandoColumn.getType() == ExpandoColumnConstants.SHORT) {
				typeSettings.put(
					ExpandoColumnConstants.INDEX_TYPE,
					GetterUtil.getString(
						liferayGenValueGenerator.getRandomObjectFromList(
							ListUtil.fromArray(
								new Integer[] {
									ExpandoColumnConstants.INDEX_TYPE_NONE,
									ExpandoColumnConstants.INDEX_TYPE_KEYWORD
								}))));
			}
			else if (expandoColumn.getType() ==
						ExpandoColumnConstants.SHORT_ARRAY) {

				typeSettings.put(
					ExpandoColumnConstants.INDEX_TYPE,
					GetterUtil.getString(
						liferayGenValueGenerator.getRandomObjectFromList(
							ListUtil.fromArray(
								new Integer[] {
									ExpandoColumnConstants.INDEX_TYPE_NONE,
									ExpandoColumnConstants.INDEX_TYPE_KEYWORD
								}))));

				typeSettings.put(
					ExpandoColumnConstants.PROPERTY_DISPLAY_TYPE,
					GetterUtil.getString(
						liferayGenValueGenerator.getRandomObjectFromList(
							ListUtil.fromArray(
								new String[] {
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_CHECKBOX,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_RADIO,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_SELECTION_LIST,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_TEXT_BOX
								}))));
			}
			else if (expandoColumn.getType() == ExpandoColumnConstants.STRING) {
				typeSettings.put(
					ExpandoColumnConstants.PROPERTY_HEIGHT,
					GetterUtil.getString(
						liferayGenValueGenerator.getRandomIntegerFromRange(
							10, 1000)));
				typeSettings.put(
					ExpandoColumnConstants.PROPERTY_WIDTH,
					GetterUtil.getString(
						liferayGenValueGenerator.getRandomIntegerFromRange(
							10, 1000)));
				typeSettings.put(
					ExpandoColumnConstants.PROPERTY_SECRET,
					GetterUtil.getString(
						liferayGenValueGenerator.getBoolean()));
				typeSettings.put(
					ExpandoColumnConstants.INDEX_TYPE,
					GetterUtil.getString(
						liferayGenValueGenerator.getRandomObjectFromList(
							ListUtil.fromArray(
								new Integer[] {
									ExpandoColumnConstants.INDEX_TYPE_NONE,
									ExpandoColumnConstants.INDEX_TYPE_TEXT,
									ExpandoColumnConstants.INDEX_TYPE_KEYWORD
								}))));
			}
			else if (expandoColumn.getType() ==
						ExpandoColumnConstants.STRING_ARRAY) {

				typeSettings.put(
					ExpandoColumnConstants.INDEX_TYPE,
					GetterUtil.getString(
						liferayGenValueGenerator.getRandomObjectFromList(
							ListUtil.fromArray(
								new Integer[] {
									ExpandoColumnConstants.INDEX_TYPE_NONE,
									ExpandoColumnConstants.INDEX_TYPE_TEXT,
									ExpandoColumnConstants.INDEX_TYPE_KEYWORD
								}))));
				typeSettings.put(
					ExpandoColumnConstants.PROPERTY_DISPLAY_TYPE,
					GetterUtil.getString(
						liferayGenValueGenerator.getRandomObjectFromList(
							ListUtil.fromArray(
								new String[] {
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_CHECKBOX,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_RADIO,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_SELECTION_LIST,
									ExpandoColumnConstants.
										PROPERTY_DISPLAY_TYPE_TEXT_BOX
								}))));
			}

			_expandoColumnLocalService.updateTypeSettings(
				expandoColumn.getColumnId(), typeSettings.toString());
		}
		catch (PortalException pe) {
			_log.error(pe, pe);
		}
		catch (SystemException se) {
			_log.error(se, se);
		}
	}

	protected Serializable getDefaultDataByType(
		LiferayGenValueGenerator liferayGenValueGenerator, int type) {

		if (type == ExpandoColumnConstants.BOOLEAN) {
			return liferayGenValueGenerator.getBoolean();
		}
		else if (type == ExpandoColumnConstants.BOOLEAN_ARRAY) {
			return new boolean[] {liferayGenValueGenerator.getBoolean()};
		}
		else if (type == ExpandoColumnConstants.DATE) {
			return liferayGenValueGenerator.getRandomDate();
		}
		else if (type == ExpandoColumnConstants.DATE_ARRAY) {
			return new Date[] {liferayGenValueGenerator.getRandomDate()};
		}
		else if (type == ExpandoColumnConstants.DOUBLE) {
			return liferayGenValueGenerator.getRandomDoubleFromRange(
				Double.MIN_VALUE, Double.MAX_VALUE);
		}
		else if (type == ExpandoColumnConstants.DOUBLE_ARRAY) {
			return new double[] {
				liferayGenValueGenerator.getRandomDoubleFromRange(
					Double.MIN_VALUE, Double.MAX_VALUE)
			};
		}
		else if (type == ExpandoColumnConstants.FLOAT) {
			return liferayGenValueGenerator.getRandomFloatFromRange(
				Float.MIN_VALUE, Float.MAX_VALUE);
		}
		else if (type == ExpandoColumnConstants.FLOAT_ARRAY) {
			return new float[] {
				liferayGenValueGenerator.getRandomFloatFromRange(
					Float.MIN_VALUE, Float.MAX_VALUE)
			};
		}
		else if (type == ExpandoColumnConstants.INTEGER) {
			return liferayGenValueGenerator.getRandomIntegerFromRange(
				Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		else if (type == ExpandoColumnConstants.INTEGER_ARRAY) {
			return new int[] {
				liferayGenValueGenerator.getRandomIntegerFromRange(
					Integer.MIN_VALUE, Integer.MAX_VALUE)
			};
		}
		else if (type == ExpandoColumnConstants.LONG) {
			return liferayGenValueGenerator.getRandomLongFromRange(
				Long.MIN_VALUE, Long.MAX_VALUE);
		}
		else if (type == ExpandoColumnConstants.LONG_ARRAY) {
			return new long[] {
				liferayGenValueGenerator.getRandomLongFromRange(
					Long.MIN_VALUE, Long.MAX_VALUE)
			};
		}
		else if (type == ExpandoColumnConstants.NUMBER) {
			return liferayGenValueGenerator.getRandomDoubleFromRange(
				Double.MIN_VALUE, Double.MAX_VALUE);
		}
		else if (type == ExpandoColumnConstants.NUMBER_ARRAY) {
			return new Number[] {
				liferayGenValueGenerator.getRandomDoubleFromRange(
					Double.MIN_VALUE, Double.MAX_VALUE)
			};
		}
		else if (type == ExpandoColumnConstants.SHORT) {
			return liferayGenValueGenerator.getRandomShortFromRange(
				Short.MIN_VALUE, Short.MAX_VALUE);
		}
		else if (type == ExpandoColumnConstants.SHORT_ARRAY) {
			return new short[] {
				liferayGenValueGenerator.getRandomShortFromRange(
					Short.MIN_VALUE, Short.MAX_VALUE)
			};
		}
		else if (type == ExpandoColumnConstants.STRING) {
			return liferayGenValueGenerator.getLowerCaseText(20);
		}
		else if (type == ExpandoColumnConstants.STRING_ARRAY) {
			return new String[] {liferayGenValueGenerator.getLowerCaseText(20)};
		}
		else if (type == ExpandoColumnConstants.STRING_LOCALIZED) {
			HashMap<Locale, String> stringLocalizedMap = new HashMap<>();

			stringLocalizedMap.put(
				LocaleUtil.getDefault(),
				liferayGenValueGenerator.getLowerCaseText(20));

			return stringLocalizedMap;
		}
		else if (type == ExpandoColumnConstants.STRING_ARRAY_LOCALIZED) {
			HashMap<Locale, String[]> stringArrayLocalizedMap = new HashMap<>();

			stringArrayLocalizedMap.put(
				LocaleUtil.getDefault(),
				new String[] {liferayGenValueGenerator.getLowerCaseText(20)});

			return stringArrayLocalizedMap;
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CreateExpandoFieldLiferayGenAction.class);

	private static final Integer[] _availableTypes = {
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

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private ExpandoValueLocalService _expandoValueLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}