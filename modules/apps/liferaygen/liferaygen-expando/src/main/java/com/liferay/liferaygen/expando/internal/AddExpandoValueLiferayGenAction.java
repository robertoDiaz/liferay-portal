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

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.expando.kernel.model.ExpandoColumn;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.model.ExpandoTableConstants;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassName;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.io.Serializable;

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
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.expando.internal.AddExpandoValueLiferayGenAction",
	service = LiferayGenAction.class
)
public class AddExpandoValueLiferayGenAction extends BaseLiferayGenAction {

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
				put(LiferayGenActionConfig.TARGET, "Expando table to be used");
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
	public String getName() {
		return "AddExpandoValueLiferayGenAction";
	}

	@Override
	protected void doRun() {
		Map<String, Object> parameters = getParameters();

		long companyId = (Long)parameters.get(
			LiferayGenConfigConstants.COMPANY_ID);

		long groupId = (Long)parameters.get(LiferayGenConfigConstants.GROUP_ID);

		Long classNameId = (Long)parameters.get(LiferayGenActionConfig.TARGET);

		try {
			ClassName className = _classNameLocalService.fetchClassName(
				classNameId);

			String cnName = className.getClassName();

			LiferayGenValueGenerator liferayGenValueGenerator =
				new LiferayGenValueGenerator(
					_companyLocalService, _liferayGenQueryHandler, _portal,
					_portletLocalService);

			long classPK = liferayGenValueGenerator.getRandomClassPK(
				cnName, companyId, groupId);

			if (classPK == 0) {
				return;
			}

			classPK = _getLatestClassPK(cnName, classPK);

			Map<String, Serializable> attributes = new HashMap<>();

			ExpandoTable expandoTable =
				_expandoTableLocalService.getDefaultTable(
					companyId, classNameId);

			List<ExpandoColumn> expandoColumns =
				_expandoColumnLocalService.getColumns(
					expandoTable.getTableId());

			for (ExpandoColumn expandoColumn : expandoColumns) {
				attributes.put(
					expandoColumn.getName(),
					getExpandoValueForExpandoColumn(
						liferayGenValueGenerator, expandoColumn));
			}

			_expandoValueLocalService.addValues(
				companyId, className.getClassName(), expandoTable.getName(),
				classPK, attributes);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected Serializable getExpandoValueForExpandoColumn(
		LiferayGenValueGenerator liferayGenValueGenerator,
		ExpandoColumn expandoColumn) {

		Serializable possibleValues = expandoColumn.getDefaultValue();

		if (expandoColumn.getType() == ExpandoColumnConstants.BOOLEAN) {
			return liferayGenValueGenerator.getBoolean();
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.BOOLEAN_ARRAY) {

			return new boolean[] {liferayGenValueGenerator.getBoolean()};
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.DATE) {
			return liferayGenValueGenerator.getRandomDate();
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.DATE_ARRAY) {
			Date[] possibleDates = (Date[])possibleValues;

			Date possibleDate =
				liferayGenValueGenerator.getRandomObjectFromList(
					ListUtil.fromArray(possibleDates));

			if (possibleDate != null) {
				return new Date[] {possibleDate};
			}
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.DOUBLE) {
			return liferayGenValueGenerator.getRandomDoubleFromRange(
				Double.MIN_VALUE, Double.MAX_VALUE);
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.DOUBLE_ARRAY) {

			double[] possibleDoubles = (double[])possibleValues;

			Double possibleDouble =
				liferayGenValueGenerator.getRandomObjectFromArray(
					ArrayUtil.toArray(possibleDoubles));

			if (possibleDouble != null) {
				return new double[] {possibleDouble};
			}
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.FLOAT) {
			return liferayGenValueGenerator.getRandomFloatFromRange(
				Float.MIN_VALUE, Float.MAX_VALUE);
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.FLOAT_ARRAY) {

			float[] possibleFloats = (float[])possibleValues;

			Float possibleFloat =
				liferayGenValueGenerator.getRandomObjectFromArray(
					ArrayUtil.toArray(possibleFloats));

			if (possibleFloat != null) {
				return new float[] {possibleFloat};
			}
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.INTEGER) {
			return liferayGenValueGenerator.getRandomIntegerFromRange(
				Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.INTEGER_ARRAY) {

			int[] possibleIntegers = (int[])possibleValues;

			Integer possibleInteger =
				liferayGenValueGenerator.getRandomObjectFromArray(
					ArrayUtil.toArray(possibleIntegers));

			if (possibleInteger != null) {
				return new int[] {possibleInteger};
			}
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.LONG) {
			return liferayGenValueGenerator.getRandomLongFromRange(
				Long.MIN_VALUE, Long.MAX_VALUE);
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.LONG_ARRAY) {
			long[] possibleLongs = (long[])possibleValues;

			Long possibleLong =
				liferayGenValueGenerator.getRandomObjectFromArray(
					ArrayUtil.toArray(possibleLongs));

			if (possibleLong != null) {
				return new long[] {possibleLong};
			}
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.NUMBER) {
			return liferayGenValueGenerator.getRandomDoubleFromRange(
				Double.MIN_VALUE, Double.MAX_VALUE);
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.NUMBER_ARRAY) {

			Number[] possibleNumbers = (Number[])possibleValues;

			Number possibleNumber =
				liferayGenValueGenerator.getRandomObjectFromList(
					ListUtil.fromArray(possibleNumbers));

			if (possibleNumber != null) {
				return new Number[] {possibleNumber};
			}
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.SHORT) {
			return liferayGenValueGenerator.getRandomShortFromRange(
				Short.MIN_VALUE, Short.MAX_VALUE);
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.SHORT_ARRAY) {

			short[] possibleShorts = (short[])possibleValues;

			Short possibleShort =
				liferayGenValueGenerator.getRandomObjectFromArray(
					ArrayUtil.toArray(possibleShorts));

			if (possibleShort != null) {
				return new short[] {possibleShort};
			}
		}
		else if (expandoColumn.getType() == ExpandoColumnConstants.STRING) {
			return liferayGenValueGenerator.getLowerCaseText(25);
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.STRING_ARRAY) {

			String[] possibleStrings = (String[])possibleValues;

			String possibleString =
				liferayGenValueGenerator.getRandomObjectFromList(
					ListUtil.fromArray(possibleStrings));

			if (possibleString != null) {
				return new String[] {possibleString};
			}
		}
		else if (expandoColumn.getType() ==
					ExpandoColumnConstants.STRING_LOCALIZED) {

			HashMap<Locale, String> stringLocalizedMap = new HashMap<>();

			stringLocalizedMap.put(
				LocaleUtil.getDefault(),
				liferayGenValueGenerator.getLowerCaseText(25));

			return stringLocalizedMap;
		}

		return possibleValues;
	}

	private long _getLatestClassPK(String className, long classPK) {
		if (StringUtil.equals(JournalArticle.class.getName(), className)) {
			JournalArticle journalArticle =
				_journalArticleLocalService.fetchLatestArticle(
					classPK, WorkflowConstants.STATUS_ANY, true);

			if (journalArticle == null) {
				return classPK;
			}

			classPK = journalArticle.getPrimaryKey();
		}

		if (StringUtil.equals(DLFileEntry.class.getName(), className)) {
			FileVersion fileVersion = null;

			try {
				fileVersion = _dlAppLocalService.getFileVersion(classPK);

				return fileVersion.getPrimaryKey();
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"There is no file entry with " + classPK + " as PK", e);
				}
			}
		}

		return classPK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AddExpandoValueLiferayGenAction.class);

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private ExpandoColumnLocalService _expandoColumnLocalService;

	@Reference
	private ExpandoTableLocalService _expandoTableLocalService;

	@Reference
	private ExpandoValueLocalService _expandoValueLocalService;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}