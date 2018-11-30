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

package com.liferay.liferaygen.util;

import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ProjectionList;
import com.liferay.portal.kernel.service.PersistedModelLocalService;

import java.util.List;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public interface LiferayGenQueryHandler {

	public DynamicQuery createDynamicQuery(
		PersistedModelLocalService persistedModelLocalService);

	public List<Object> executeDynamicQuery(
		PersistedModelLocalService persistedModelLocalService,
		DynamicQuery dynamicQuery);

	public List<Object> executeDynamicQuery(
		PersistedModelLocalService persistedModelLocalService,
		DynamicQuery dynamicQuery, int start, int end);

	public long executeDynamicQueryCount(
		PersistedModelLocalService persistedModelLocalService,
		DynamicQuery dynamicQuery);

	public List<?> executeEntityModelQuery(String className, String properties);

	public List<?> executeEntityModelQuery(
		String className, String properties, Criterion criterion);

	public List<Object> executeEntityModelQuery(
		String className, String properties, Criterion criterion, int start,
		int end);

	public List<Object> executeEntityModelQuery(
		String className, String properties, int start, int end);

	public long executeEntityModelQueryCount(String className);

	public long executeEntityModelQueryCount(
		String className, Criterion criterion);

	public List<Object> executeSql(String sql) throws Exception;

	public ProjectionList getPropertyProjection(String[] attributes);

}