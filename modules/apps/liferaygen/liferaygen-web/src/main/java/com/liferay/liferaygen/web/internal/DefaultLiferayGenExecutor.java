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

package com.liferay.liferaygen.web.internal;

import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.web.internal.config.LiferayGenActionConfig;
import com.liferay.liferaygen.web.internal.config.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.web.internal.util.LiferayGenExecutorHandler;
import com.liferay.liferaygen.web.internal.util.ThreadLocalData;
import com.liferay.liferaygen.web.internal.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public class DefaultLiferayGenExecutor implements LiferayGenExecutor {


	public DefaultLiferayGenExecutor(
		Map<String, Object> configuration,
		LiferayGenExecutorHandler liferayGenExecutorHandler,
		LiferayGenValueGenerator liferayGenValueGenerator) {

		_configuration = configuration;
		_liferayGenExecutorHandler = liferayGenExecutorHandler;
		_liferayGenValueGenerator = liferayGenValueGenerator;
	}

	@Override
	public void configure(Map<String, Object> configuration) {
		_configuration = configuration;
	}

	public void run() {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> actions =
			(List<Map<String, Object>>)_configuration.get(
				LiferayGenConfigConstants.ACTIONS);

		long i = 0;

		for (Map<String, Object> action : actions) {
			if (_log.isInfoEnabled()) {
				_log.info("Executing action number " + (i++));
			}

			long startTime = System.currentTimeMillis();

			_liferayGenValueGenerator.resetCaches();

			LiferayGenActionConfig liferayGenActionConfig =
				_liferayGenExecutorHandler.getLiferayGenActionConfig(
					_configuration, action);

			if (liferayGenActionConfig != null) {
				execute(liferayGenActionConfig);
			}

			long endTime = System.currentTimeMillis();

			if (_log.isInfoEnabled()) {
				_log.info(
					"Executed in " + ((endTime - startTime) / 1000.0) + " s");
			}
		}
	}

	protected void execute(LiferayGenActionConfig liferayGenActionConfig) {
		LiferayGenAction liferayGenAction =
			liferayGenActionConfig.getLiferayGenAction();

		Class<? extends LiferayGenAction> c = liferayGenAction.getClass();

		if (_log.isInfoEnabled()) {
			_log.info("Executing liferayGenAction: " + c);
			_log.info(
				"numExecutions: " + liferayGenActionConfig.getNumExecutions());
			_log.info("numThreads: " + liferayGenActionConfig.getNumThreads());
			_log.info("parameters: " + liferayGenActionConfig.getParameters());
		}

		ExecutorService executor = null;

		ThreadLocalData threadLocalData = null;

		List<Future<Void>> futures = new ArrayList<>();

		if (liferayGenActionConfig.getNumThreads() > 0) {
			executor = Executors.newFixedThreadPool(
				(int)liferayGenActionConfig.getNumThreads());

			threadLocalData = ThreadLocalData.getThreadLocalData();
		}

		Map<Long, List<LiferayGenTarget>> targetMapCopy =
			liferayGenActionConfig.getLiferayGenTargetMapCopy();

		long numExecutions = liferayGenActionConfig.getNumExecutions();

		for (int i = 0; i < numExecutions; i++) {
			String name = StringBundler.concat(
				c.getSimpleName(), StringPool.POUND, i, StringPool.SLASH,
				liferayGenActionConfig.getNumExecutions());

			if (_log.isDebugEnabled()) {
				_log.debug(name + ": prepare execution");
			}

			Map<String, Object> parameters = new HashMap<>(
				liferayGenActionConfig.getParameters());

			long groupId = liferayGenActionConfig.getGroupId();

			LiferayGenTarget target = getRandomTarget(
				liferayGenActionConfig, targetMapCopy);

			if (target != null) {
				groupId = target.getGroupId();

				parameters.put(
					LiferayGenActionConfig.TARGET, target.getValue());
			}

			if (liferayGenAction.hasScopeByGroupId()) {
				parameters.put(LiferayGenConfigConstants.GROUP_ID, groupId);
			}

			LiferayGenAction actionCopy =
				_liferayGenExecutorHandler.createAction(liferayGenAction);

			actionCopy.configure(parameters);

			CallableLiferayGenAction callableLiferayGenAction =
				new CallableLiferayGenAction(actionCopy, name, threadLocalData);

			if (executor == null) {
				callableLiferayGenAction.call();
			}
			else {
				Future<Void> future = executor.submit(callableLiferayGenAction);

				futures.add(future);
			}
		}

		long startTime = System.currentTimeMillis();
		long i = 0;

		for (Future<Void> future : futures) {
			try {
				future.get();
				i++;
			}
			catch (Exception e) {
				_log.error(e, e);
			}

			if (_log.isInfoEnabled() && (i % 200) == 0) {
				String simpleName = c.getSimpleName() + " - ";

				long executionTime = System.currentTimeMillis() - startTime;

				long averageTime = executionTime / i;

				long remain = liferayGenActionConfig.getNumExecutions() - i;

				double eta = (averageTime * remain) / 1000.0;

				_log.info(
					StringBundler.concat(
						simpleName, "Executed ", i, " actions"));
				_log.info(
					StringBundler.concat(
						simpleName, "Remain ", remain, " actions"));
				_log.info(
					StringBundler.concat(
						simpleName, "Average time: ", averageTime, " ms"));
				_log.info(StringBundler.concat(simpleName, "ETA: ", eta, " s"));
			}
		}

		if (executor != null) {
			executor.shutdownNow();
		}

		liferayGenAction.destroy();
	}

	protected LiferayGenTarget getRandomTarget(
		LiferayGenActionConfig liferayGenActionConfig,
		Map<Long, List<LiferayGenTarget>> targetMapCopy) {

		long targetGroupId = 0L;

		if (liferayGenActionConfig.hasLiferayGenTargetsGroupedByGroupId()) {
			targetGroupId = liferayGenActionConfig.getGroupId();
		}

		List<LiferayGenTarget> targetList = targetMapCopy.get(targetGroupId);

		if (targetList == null) {
			return null;
		}

		if (targetList.isEmpty()) {
			targetList = liferayGenActionConfig.getLiferayGenTargetList(
				targetGroupId);
		}

		if (liferayGenActionConfig.isRepeatLiferayGenTarget()) {
			return _liferayGenValueGenerator.getRandomObjectFromList(
				targetList);
		}

		return _liferayGenValueGenerator.removeRandomObjectFromList(targetList);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultLiferayGenExecutor.class);

	private Map<String, Object> _configuration;

	private LiferayGenExecutorHandler _liferayGenExecutorHandler;

	private LiferayGenValueGenerator _liferayGenValueGenerator;

}