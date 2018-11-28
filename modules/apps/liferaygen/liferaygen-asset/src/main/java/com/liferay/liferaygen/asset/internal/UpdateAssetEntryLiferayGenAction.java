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

package com.liferay.liferaygen.asset.internal;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.util.LiferayGenParameterHandler;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.ratings.kernel.service.RatingsEntryLocalService;

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
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.asset.internal.UpdateAssetEntryLiferayGenAction",
	service = LiferayGenAction.class
)
public class UpdateAssetEntryLiferayGenAction extends BaseLiferayGenAction {

	@Override
	public String doGetDescription() {
		return "Updates view count and ratings of any Asset";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("setRandomViewCounterRatio", 80L);
				put("setRandomRatingsRatio", 40L);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					"setRandomViewCounterRatio",
					"Probability percentage of creating a asset with a " +
						"random view count value");
				put(
					"setRandomRatingsRatio",
					"Probability percentage of creating a asset with random " +
						"rating values");
				put(LiferayGenActionConfig.TARGET, "Asset to be updated");
			}
		};
	}

	@Override
	public Criterion getEntityFilter() {
		return RestrictionsFactoryUtil.eq("visible", true);
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return AssetEntry.class;
	}

	@Override
	public String getEntityProperties() {
		return "classNameId,classPK";
	}

	@Override
	protected void doRun() {
		Map<String, Object> parameters = getParameters();

		Object[] target = (Object[])parameters.get(
			LiferayGenActionConfig.TARGET);

		if (target == null) {
			return;
		}

		Long classNameId = (Long)target[0];
		Long classPK = (Long)target[1];

		if (Validator.isNull(classNameId) || Validator.isNull(classPK)) {
			return;
		}

		String className = _portal.getClassName(classNameId);

		int setViewCounterRatio =
			_liferayGenParameterHandler.getParamAsIntegerPercentage(
				parameters, "setRandomViewCounterRatio");

		LiferayGenValueGenerator liferayGenValueGenerator =
			new LiferayGenValueGenerator(
				_companyLocalService, _liferayGenQueryHandler, _portal,
				_portletLocalService);

		if (liferayGenValueGenerator.getBoolean(setViewCounterRatio)) {
			_setRandomViewCounter(liferayGenValueGenerator, className, classPK);
		}

		int setRandomRatingsRatio =
			_liferayGenParameterHandler.getParamAsIntegerPercentage(
				parameters, "setRandomRatingsRatio");

		if (liferayGenValueGenerator.getBoolean(setRandomRatingsRatio)) {
			_setRandomRatings(liferayGenValueGenerator, className, classPK);
		}
	}

	private void _setRandomRatings(
		LiferayGenValueGenerator liferayGenValueGenerator, String className,
		long classPK) {

		int numberOfRatings =
			liferayGenValueGenerator.getRandomIntegerFromRange(0, 20);

		ServiceContext serviceContext = new ServiceContext();

		for (int i = 0; i < numberOfRatings; i++) {
			try {
				long userId =
					liferayGenValueGenerator.getRandomUserIdFromCache();

				double score =
					liferayGenValueGenerator.getRandomDoubleFromRange(0, 1);

				_ratingsEntryLocalService.updateEntry(
					userId, className, classPK, score, serviceContext);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(e, e);
				}
			}
		}
	}

	private void _setRandomViewCounter(
		LiferayGenValueGenerator liferayGenValueGenerator, String className,
		long classPK) {

		try {
			long userId = liferayGenValueGenerator.getRandomUserIdFromCache();

			int increment = liferayGenValueGenerator.getRandomIntegerFromRange(
				0, 500);

			_assetEntryLocalService.incrementViewCounter(
				userId, className, classPK, increment);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn(e, e);
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		UpdateAssetEntryLiferayGenAction.class);

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenParameterHandler _liferayGenParameterHandler;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	@Reference
	private RatingsEntryLocalService _ratingsEntryLocalService;

}