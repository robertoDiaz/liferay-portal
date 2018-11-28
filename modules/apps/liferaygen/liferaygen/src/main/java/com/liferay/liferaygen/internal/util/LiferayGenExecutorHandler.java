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

package com.liferay.liferaygen.internal.util;

import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.internal.LiferayGenActionAdapter;
import com.liferay.liferaygen.internal.LiferayGenExecutor;
import com.liferay.liferaygen.internal.LiferayGenTarget;
import com.liferay.liferaygen.internal.LiferayGenTargetImpl;
import com.liferay.liferaygen.internal.config.LiferayGenActionConfig;
import com.liferay.liferaygen.internal.config.constants.LiferayGenConfigConstants;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(immediate = true, service = LiferayGenExecutorHandler.class)
public class LiferayGenExecutorHandler {


	@Activate
	public void activate(BundleContext bundleContext) {
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, LiferayGenAction.class);
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	public LiferayGenAction createAction(Object actionObj) {
		Class<?> clazz = getActionClass(actionObj);

		if (clazz == null) {
			return null;
		}

		String actionClassName = clazz.getName();

		try {
			Object object = clazz.newInstance();

			if (LiferayGenAction.class.isInstance(object)) {
				return (LiferayGenAction)object;
			}

			if (LiferayGenExecutor.class.isInstance(object)) {
				return new LiferayGenActionAdapter((LiferayGenExecutor)object);
			}

			_log.error(
				actionClassName +
					"does not implement LiferayGenAction interface");
		}
		catch (Exception e) {
			_log.error(
				actionClassName + " - Error creating LiferayGenAction: " +
					e.getMessage(),
				e);
		}

		return null;
	}

	public List<LiferayGenAction> getAvailableActions() {
		List<LiferayGenAction> actions = new ArrayList<>();

		_serviceTrackerList.forEach(actions::add);

		return actions;
	}

	public LiferayGenActionConfig getLiferayGenActionConfig(
		Map<String, Object> configuration,
		Map<String, Object> actionConfigMap) {

		Object actionClassName = actionConfigMap.get(
			LiferayGenActionConfig.ACTION);

		LiferayGenAction liferayGenAction = createAction(actionClassName);

		if (liferayGenAction == null) {
			return null;
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> parameters =
			(Map<String, Object>)actionConfigMap.get(
				LiferayGenActionConfig.PARAMETERS);

		Map<String, Object> backedParameters =
			_liferayGenParameterHandler.getBackedParameters(
				liferayGenAction, configuration, parameters);

		liferayGenAction.configure(backedParameters);

		liferayGenAction.init();

		if (_log.isInfoEnabled()) {
			_log.info("Action: " + liferayGenAction.getClass());
		}

		List<Long> groupIds = LiferayGenParameterHandler.getGroupIds(
			backedParameters);

		if (!liferayGenAction.hasScopeByGroupId()) {
			backedParameters.put(
				LiferayGenConfigConstants.GROUP_ID,
				_liferayGenValueGenerator.getRandomObjectFromList(groupIds));

			groupIds = Collections.singletonList(0L);
		}

		String sql = (String)actionConfigMap.get(LiferayGenActionConfig.SQL);

		Map<Long, List<LiferayGenTarget>> liferayGenTargetMap;

		try {
			liferayGenTargetMap = getTargetMap(
				groupIds, liferayGenAction, sql,
				backedParameters.get(liferayGenAction.getEntityModelPK()));
		}
		catch (Exception e) {
			_log.error(e, e);

			return null;
		}

		Boolean repeatLiferayGenTarget = null;

		try {
			repeatLiferayGenTarget = (Boolean)actionConfigMap.get(
				LiferayGenActionConfig.REPEAT_TARGET);
		}
		catch (Exception e) {
			if (_log.isDebugEnabled()) {
				_log.debug(e, e);
			}
		}

		long numExecutions = 0;

		double numExecutionsPercentage = 0;

		Object numExecutionsObj = actionConfigMap.get(
			LiferayGenActionConfig.NUMEXECUTIONS);

		if (numExecutionsObj == null) {
			numExecutionsPercentage = 1.0;
		}

		if (numExecutionsObj instanceof Number) {
			numExecutions = ((Number)numExecutionsObj).longValue();
		}

		if (numExecutionsObj instanceof String) {
			String numExecutionsString = (String)numExecutionsObj;

			if (numExecutionsString.endsWith("%")) {
				double percentage = GetterUtil.getDouble(
					numExecutionsString.replace("%", ""));

				numExecutionsPercentage = percentage / 100.0;
			}
		}

		if ((numExecutions == 0) && (numExecutionsPercentage != 0)) {
			List<LiferayGenTarget> targetList = new ArrayList<>();

			for (List<LiferayGenTarget> values : liferayGenTargetMap.values()) {
				targetList.addAll(values);
			}

			liferayGenTargetMap = new HashMap<>();

			liferayGenTargetMap.put(0L, targetList);

			numExecutions = (long)(numExecutionsPercentage * targetList.size());

			repeatLiferayGenTarget = false;
		}
		else if (repeatLiferayGenTarget == null) {
			repeatLiferayGenTarget = true;
		}

		long numThreads = 0;

		Object numThreadsObj = actionConfigMap.get(
			LiferayGenActionConfig.NUMTHREADS);

		if (numThreadsObj == null) {
			numThreadsObj = configuration.get(
				LiferayGenActionConfig.NUMTHREADS);
		}

		if (numThreadsObj instanceof Number) {
			numThreads = ((Number)numThreadsObj).longValue();
		}

		return new LiferayGenActionConfig(
			groupIds, liferayGenAction, liferayGenTargetMap, numExecutions,
			numThreads, backedParameters, repeatLiferayGenTarget,
			_liferayGenValueGenerator);
	}

	protected Class<?> getActionClass(Object actionObj) {
		if (actionObj instanceof Class) {
			return (Class<?>)actionObj;
		}

		if (actionObj instanceof LiferayGenAction) {
			return ((LiferayGenAction)actionObj).getClass();
		}

		if (!(actionObj instanceof String)) {
			_log.error(actionObj + " is not valid");
		}

		String actionClassName = (String)actionObj;

		try {
			ClassLoader classLoader = LiferayGenAction.class.getClassLoader();

			return (Class<?>)classLoader.loadClass(actionClassName);
		}
		catch (Exception e) {
			_log.error(
				actionClassName + " - Error creating Action: " + e.getMessage(),
				e);
		}

		return null;
	}

	protected List<LiferayGenTarget> getTargetList(
		long groupId, LiferayGenAction liferayGenAction, Object entityPKValue) {

		Class<? extends ClassedModel> entityModel =
			liferayGenAction.getEntityModel();

		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		if (liferayGenAction.getEntityFilter() != null) {
			conjunction.add(liferayGenAction.getEntityFilter());
		}

		if (entityPKValue != null) {
			conjunction.add(
				RestrictionsFactoryUtil.eq(
					liferayGenAction.getEntityModelPK(), entityPKValue));
		}

		if (groupId != 0L) {
			conjunction.add(RestrictionsFactoryUtil.eq("groupId", groupId));
		}

		List<Object> objectList =
			(List<Object>)_liferayGenQueryHandler.executeEntityModelQuery(
				entityModel.getName(), liferayGenAction.getEntityProperties(),
				conjunction);

		return getTargetListFromObjectList(groupId, objectList);
	}

	protected List<LiferayGenTarget> getTargetListFromObjectList(
		long groupId, List<Object> objectList) {

		List<LiferayGenTarget> targetList = new ArrayList<>();

		for (Object object : objectList) {
			LiferayGenTarget target = new LiferayGenTargetImpl(groupId, object);

			targetList.add(target);
		}

		return targetList;
	}

	protected Map<Long, List<LiferayGenTarget>> getTargetMap(
			List<Long> groupIds, LiferayGenAction liferayGenAction, String sql,
			Object entityPKValue)
		throws Exception {

		if (Validator.isNotNull(sql)) {
			Map<Long, List<LiferayGenTarget>> targetMap = new HashMap<>();

			if (!StringUtil.contains(StringUtil.toLowerCase(sql), " where ")) {
				sql = sql.concat(" where 1 = 1");
			}

			for (Long groupId : groupIds) {
				String sqlGroup = sql;

				if (groupId != 0L) {
					sqlGroup = sqlGroup + " and groupId = " + groupId;
				}

				List<Object> objectList = _liferayGenQueryHandler.executeSql(
					sql);

				List<LiferayGenTarget> targetList = getTargetListFromObjectList(
					groupId, objectList);

				targetMap.put(groupId, targetList);
			}

			return targetMap;
		}

		if (liferayGenAction.getEntityModel() != null) {
			Map<Long, List<LiferayGenTarget>> targetMap = new HashMap<>();

			for (Long groupId : groupIds) {
				List<LiferayGenTarget> targetList = getTargetList(
					groupId, liferayGenAction, entityPKValue);

				targetMap.put(groupId, targetList);
			}

			return targetMap;
		}

		return Collections.emptyMap();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayGenExecutorHandler.class);

	@Reference
	private LiferayGenParameterHandler _liferayGenParameterHandler;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private LiferayGenValueGenerator _liferayGenValueGenerator;

	private ServiceTrackerList<LiferayGenAction, LiferayGenAction>
		_serviceTrackerList;
}