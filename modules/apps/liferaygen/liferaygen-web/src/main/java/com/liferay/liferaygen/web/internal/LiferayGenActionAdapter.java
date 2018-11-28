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

import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public class LiferayGenActionAdapter extends BaseLiferayGenAction {

	public LiferayGenActionAdapter() {
	}

	public LiferayGenActionAdapter(LiferayGenExecutor liferayGenExecutor) {
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
				put(LiferayGenConfigConstants.ACTIONS, "actions to execute");
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