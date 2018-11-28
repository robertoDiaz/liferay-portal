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
import com.liferay.liferaygen.web.internal.util.ThreadLocalData;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.cache.thread.local.Lifecycle;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCacheManager;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CentralizedThreadLocal;

import java.util.concurrent.Callable;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public class CallableLiferayGenAction implements Callable<Void> {

	public CallableLiferayGenAction(
		LiferayGenAction liferayGenAction, String name,
		ThreadLocalData threadLocalData) {

		_liferayGenAction = liferayGenAction;
		_name = name;
		_threadLocalData = threadLocalData;
	}

	@Override
	public Void call() {
		long startTime = System.currentTimeMillis();

		if (_log.isDebugEnabled()) {
			_log.debug(_name + ": starting execution");
		}

		try {
			if (_threadLocalData != null) {
				_threadLocalData.setThreadLocalData();
			}

			_liferayGenAction.run();
		}
		finally {
			if (_threadLocalData != null) {
				ThreadLocalCacheManager.clearAll(Lifecycle.REQUEST);

				CentralizedThreadLocal.clearShortLivedThreadLocals();
			}
		}

		long endTime = System.currentTimeMillis();

		if (_log.isDebugEnabled()) {
			_log.debug(
				StringBundler.concat(
					_name, ": executed in ", endTime - startTime, " ms"));
		}

		return null;
	}

	private static Log _log = LogFactoryUtil.getLog(
		CallableLiferayGenAction.class);

	private final LiferayGenAction _liferayGenAction;
	private final String _name;
	private final ThreadLocalData _threadLocalData;

}