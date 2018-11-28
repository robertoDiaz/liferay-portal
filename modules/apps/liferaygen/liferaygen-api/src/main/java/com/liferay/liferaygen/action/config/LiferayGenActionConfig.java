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

package com.liferay.liferaygen.action.config;

import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.liferaygen.LiferayGenTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public class LiferayGenActionConfig {

	public static final String ACTION = "action";

	public static final String NUMEXECUTIONS = "numExecutions";

	public static final String NUMTHREADS = "numThreads";

	public static final String PARAMETERS = "parameters";

	public static final String RELATED_TARGETS = "relatedTargets";

	public static final String REPEAT_TARGET = "repeatTarget";

	public static final String SQL = "sql";

	public static final String TARGET = "target";

	public LiferayGenActionConfig(
		List<Long> groupIds, LiferayGenAction liferayGenAction,
		Map<Long, List<LiferayGenTarget>> liferayGenTargetMap,
		long numExecutions, long numThreads, Map<String, Object> parameters,
		boolean repeatLiferayGenTarget,
		LiferayGenValueGenerator liferayGenValueGenerator) {

		_groupIds = groupIds;
		_liferayGenAction = liferayGenAction;
		_liferayGenTargetMap = liferayGenTargetMap;
		_numExecutions = numExecutions;
		_numThreads = numThreads;
		_parameters = parameters;
		_repeatLiferayGenTarget = repeatLiferayGenTarget;
		_liferayGenValueGenerator = liferayGenValueGenerator;
	}

	public long getGroupId() {
		Number number =
			(Number)_liferayGenValueGenerator.getRandomObjectFromList(
				_groupIds);

		return number.longValue();
	}

	public LiferayGenAction getLiferayGenAction() {
		return _liferayGenAction;
	}

	public List<LiferayGenTarget> getLiferayGenTargetList(long groupId) {
		List<LiferayGenTarget> targetList = _liferayGenTargetMap.get(groupId);

		if (targetList == null) {
			return null;
		}

		return new ArrayList<>(targetList);
	}

	public Map<Long, List<LiferayGenTarget>> getLiferayGenTargetMapCopy() {
		Map<Long, List<LiferayGenTarget>> map = new HashMap<>();

		for (Map.Entry<Long, List<LiferayGenTarget>> entry : map.entrySet()) {
			List<LiferayGenTarget> list = new ArrayList<>(entry.getValue());

			map.put(entry.getKey(), list);
		}

		return map;
	}

	public long getNumExecutions() {
		return _numExecutions;
	}

	public long getNumThreads() {
		return _numThreads;
	}

	public Map<String, Object> getParameters() {
		return _parameters;
	}

	public boolean hasLiferayGenTargetsGroupedByGroupId() {
		Set<Long> keys = _liferayGenTargetMap.keySet();

		if (keys.size() != 1) {
			return true;
		}

		if (keys.toArray(new Long[1])[0] != 0L) {
			return true;
		}

		return false;
	}

	public boolean isRepeatLiferayGenTarget() {
		return _repeatLiferayGenTarget;
	}

	private final List<Long> _groupIds;
	private final LiferayGenAction _liferayGenAction;
	private final Map<Long, List<LiferayGenTarget>> _liferayGenTargetMap;
	private final LiferayGenValueGenerator _liferayGenValueGenerator;
	private final long _numExecutions;
	private final long _numThreads;
	private final Map<String, Object> _parameters;
	private final boolean _repeatLiferayGenTarget;

}