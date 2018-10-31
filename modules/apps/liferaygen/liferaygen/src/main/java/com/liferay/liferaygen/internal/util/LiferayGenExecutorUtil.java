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
import com.liferay.liferaygen.internal.BaseLiferayGenAction;
import com.liferay.liferaygen.internal.LiferayGenExecutor;
import com.liferay.liferaygen.internal.LiferayGenTarget;
import com.liferay.liferaygen.internal.LiferayGenTargetImpl;
import com.liferay.liferaygen.internal.config.LiferayGenActionConfig;
import com.liferay.liferaygen.internal.config.constants.LiferayGenConfigConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public class LiferayGenExecutorUtil {

	public static LiferayGenAction createAction(Object actionObj) {
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
				return new LiferayGenExecutorWrapper(
					(LiferayGenExecutor)object);
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

	public static LiferayGenActionConfig createActionConfig(
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

		Map<String, Object> effectiveParameters =
			ParameterUtil.calculateEffectiveParameters(
				liferayGenAction, configuration, parameters);

		liferayGenAction.configure(effectiveParameters);

		liferayGenAction.init();

		if (_log.isInfoEnabled()) {
			_log.info("Action: " + liferayGenAction.getClass());
		}

		List<Long> groupIds = ParameterUtil.getGroupIds(effectiveParameters);

		if (!liferayGenAction.hasScopeByGroupId()) {
			effectiveParameters.put(
				LiferayGenConfigConstants.GROUP_ID,
				ValueGenerator.getRandomObjectFromList(groupIds));

			groupIds = Collections.singletonList(0L);
		}

		String sql = (String)actionConfigMap.get(LiferayGenActionConfig.SQL);

		Map<Long, List<LiferayGenTarget>> liferayGentargetMap;

		try {
			liferayGentargetMap = getTargetMap(
				groupIds, liferayGenAction, sql,
				effectiveParameters.get(liferayGenAction.getEntityModelPk()));
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

			for (List<LiferayGenTarget> values : liferayGentargetMap.values()) {
				targetList.addAll(values);
			}

			liferayGentargetMap = new HashMap<>();

			liferayGentargetMap.put(0L, targetList);

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
			groupIds, liferayGenAction, liferayGentargetMap, numExecutions,
			numThreads, effectiveParameters, repeatLiferayGenTarget);
	}

	public static List<LiferayGenAction> getAvailableActions() {
		ClassLoader classLoader = LiferayGenExecutorUtil.class.getClassLoader();

		Set<Class<? extends LiferayGenAction>> actionClasses = null;

		//TODO get the action classes from registry

		Map<String, LiferayGenAction> actionMap = new TreeMap<>();

		for (Class<? extends LiferayGenAction> actionClass : actionClasses) {
			if (Modifier.isAbstract(actionClass.getModifiers())) {
				continue;
			}

			if (!classLoader.equals(actionClass.getClassLoader())) {
				continue;
			}

			try {
				actionMap.put(actionClass.getName(), actionClass.newInstance());
			}
			catch (Exception e) {
				_log.error(
					StringBundler.concat(
						"Action: ", actionClass, " Error: ", e),
					e);
			}
		}

		return new ArrayList<>(actionMap.values());
	}

	public static class LiferayGenExecutorWrapper extends BaseLiferayGenAction {

		public LiferayGenExecutorWrapper() {
		}

		public LiferayGenExecutorWrapper(
			LiferayGenExecutor liferayGenExecutor) {

			_liferayGenExecutor = liferayGenExecutor;
		}

		public void configure(Map<String, Object> parameters) {
			super.configure(parameters);

			if (_liferayGenExecutor != null) {
				_liferayGenExecutor.configure(parameters);
			}
		}

		@Override
		public String doGetDescription() {
			return "Allows executing the Executor as an Action";
		}

		@Override
		@SuppressWarnings("serial")
		public Map<String, Object> doGetParametersDefaultValues() {
			return new TreeMap<String, Object>() {
				{
					put(LiferayGenConfigConstants.ACTIONS, null);
				}
			};
		}

		@Override
		@SuppressWarnings("serial")
		public Map<String, String> doGetParametersDescription() {
			return new TreeMap<String, String>() {
				{
					put(
						LiferayGenConfigConstants.ACTIONS,
						"actions to execute");
				}
			};
		}

		@Override
		public void doRun() {
			if (_liferayGenExecutor != null) {
				_liferayGenExecutor.run();
			}
		}

		private LiferayGenExecutor _liferayGenExecutor;

	}

	protected static Class<?> getActionClass(Object actionObj) {
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

	protected static List<LiferayGenTarget> getTargetList(
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
					liferayGenAction.getEntityModelPk(), entityPKValue));
		}

		if (groupId != 0L) {
			conjunction.add(RestrictionsFactoryUtil.eq("groupId", groupId));
		}

		List<Object> objectList =
			(List<Object>)QueryUtil.executeEntityModelQuery(
				entityModel.getName(), liferayGenAction.getEntityProperties(),
				conjunction);

		return getTargetListFromObjectList(groupId, objectList);
	}

	protected static List<LiferayGenTarget> getTargetListFromObjectList(
		long groupId, List<Object> objectList) {

		List<LiferayGenTarget> targetList = new ArrayList<>();

		for (Object object : objectList) {
			LiferayGenTarget target = new LiferayGenTargetImpl(groupId, object);

			targetList.add(target);
		}

		return targetList;
	}

	protected static Map<Long, List<LiferayGenTarget>> getTargetMap(
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

				List<Object> objectList = QueryUtil.executeSql(sql);

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
		LiferayGenExecutorUtil.class);

}