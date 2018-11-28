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

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionList;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(immediate = true, service = LiferayGenQueryHandler.class)
public class LiferayGenQueryHandler {

	public DynamicQuery createDynamicQuery(
		PersistedModelLocalService persistedModelLocalService) {

		return (DynamicQuery)
			_liferayGenServiceExecutorHelper.executeServiceMethod(
				persistedModelLocalService, "dynamicQuery", (Class<?>)null);
	}

	@SuppressWarnings("unchecked")
	public List<Object> executeDynamicQuery(
		PersistedModelLocalService persistedModelLocalService,
		DynamicQuery dynamicQuery) {

		return (List<Object>)
			_liferayGenServiceExecutorHelper.executeServiceMethod(
				persistedModelLocalService, "dynamicQuery", DynamicQuery.class,
				dynamicQuery);
	}

	@SuppressWarnings("unchecked")
	//TODO typed method is possible??
	public List<Object> executeDynamicQuery(
		PersistedModelLocalService persistedModelLocalService,
		DynamicQuery dynamicQuery, int start, int end) {

		return (List<Object>)
			_liferayGenServiceExecutorHelper.executeServiceMethod(
				persistedModelLocalService, "dynamicQuery",
				new Class<?>[] {DynamicQuery.class, int.class, int.class},
				dynamicQuery, start, end);
	}

	public long executeDynamicQueryCount(
		PersistedModelLocalService persistedModelLocalService,
		DynamicQuery dynamicQuery) {

		return (Long)_liferayGenServiceExecutorHelper.executeServiceMethod(
			persistedModelLocalService, "dynamicQueryCount", DynamicQuery.class,
			dynamicQuery);
	}

	public List<?> executeEntityModelQuery(
		String className, String properties) {

		return executeEntityModelQuery(className, properties, null);
	}

	public List<?> executeEntityModelQuery(
		String className, String properties, Criterion criterion) {

		return executeEntityModelQuery(
			className, properties, criterion, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS);
	}

	//TODO typed method is possible??
	public List<Object> executeEntityModelQuery(
		String className, String properties, Criterion criterion, int start,
		int end) {

		PersistedModelLocalService persistedModelLocalService =
			_persistedModelLocalServiceRegistry.getPersistedModelLocalService(
				className);

		DynamicQuery dynamicQuery = createDynamicQuery(
			persistedModelLocalService);

		if (properties != null) {
			ProjectionList projectionList = getPropertyProjection(
				properties.split(","));

			dynamicQuery.setProjection(projectionList);
		}

		if (criterion != null) {
			dynamicQuery.add(criterion);
		}

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS)) {
			return executeDynamicQuery(
				persistedModelLocalService, dynamicQuery);
		}

		return executeDynamicQuery(
			persistedModelLocalService, dynamicQuery, start, end);
	}

	//TODO typed method is possible??
	public List<Object> executeEntityModelQuery(
		String className, String properties, int start, int end) {

		return executeEntityModelQuery(className, properties, null, start, end);
	}

	public long executeEntityModelQueryCount(String className) {
		return executeEntityModelQueryCount(className, null);
	}

	public long executeEntityModelQueryCount(
		String className, Criterion criterion) {

		PersistedModelLocalService persistedModelLocalService =
			_persistedModelLocalServiceRegistry.getPersistedModelLocalService(
				className);

		DynamicQuery dynamicQuery = createDynamicQuery(
			persistedModelLocalService);

		if (criterion != null) {
			dynamicQuery.add(criterion);
		}

		return executeDynamicQueryCount(
			persistedModelLocalService, dynamicQuery);
	}

	//TODO typed method is possible??
	public List<Object> executeSql(String sql) throws Exception {
		if (sql == null) {
			return null;
		}

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getConnection();

			sql = _portal.transformSQL(sql);

			if (_log.isInfoEnabled()) {
				_log.info("SQL: " + sql);
			}

			ps = con.prepareStatement(sql);

			rs = ps.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();

			int numberOfColumns = rsmd.getColumnCount();

			List<Object> results = new ArrayList<>();

			while (rs.next()) {
				if (numberOfColumns == 1) {
					results.add(rs.getObject(1));

					continue;
				}

				Object[] row = new Object[numberOfColumns];

				for (int i = 1; i <= numberOfColumns; i++) {
					row[i - 1] = rs.getObject(i);
				}

				results.add(row);
			}

			return results;
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

	public ProjectionList getPropertyProjection(String[] attributes) {
		String[] op = new String[attributes.length];
		String[] attributesAux = new String[attributes.length];

		boolean grouping = false;

		for (int i = 0; i < attributes.length; i++) {
			String attribute = attributes[i];

			if (attribute.indexOf("(") > 0) {
				op[i] = attribute.substring(0, attribute.indexOf("("));
				attributesAux[i] = attribute.substring(
					attribute.indexOf("(") + 1, attribute.indexOf(")"));
				grouping = true;
			}
			else {
				op[i] = null;
				attributesAux[i] = attribute;
			}
		}

		if (grouping) {
			for (int i = 0; i < op.length; i++) {
				if (op[i] == null) {
					op[i] = "groupProperty";
				}
			}
		}

		ProjectionList projectionList = ProjectionFactoryUtil.projectionList();

		for (int i = 0; i < attributesAux.length; i++) {
			Projection projection = getPropertyProjection(
				attributesAux[i], op[i]);

			if (projection != null) {
				projectionList.add(projection);
			}
		}

		return projectionList;
	}

	protected Projection getPropertyProjection(String attribute, String op) {
		if ("rowCount".equals(op)) {
			return ProjectionFactoryUtil.rowCount();
		}

		Projection property = null;

		if (Validator.isNull(op)) {
			property = ProjectionFactoryUtil.property(attribute);
		}
		else if ("count".equals(op)) {
			property = ProjectionFactoryUtil.count(attribute);
		}
		else if ("countDistinct".equals(op)) {
			property = ProjectionFactoryUtil.countDistinct(attribute);
		}
		else if ("distinct".equals(op)) {
			property = ProjectionFactoryUtil.distinct(
				ProjectionFactoryUtil.property(attribute));
		}
		else if ("groupProperty".equals(op)) {
			property = ProjectionFactoryUtil.groupProperty(attribute);
		}
		else if ("max".equals(op)) {
			property = ProjectionFactoryUtil.max(attribute);
		}
		else if ("min".equals(op)) {
			property = ProjectionFactoryUtil.min(attribute);
		}
		else if ("sum".equals(op)) {
			property = ProjectionFactoryUtil.sum(attribute);
		}

		return property;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayGenQueryHandler.class);

	@Reference
	private LiferayGenServiceExecutorHelper _liferayGenServiceExecutorHelper;

	@Reference
	private PersistedModelLocalServiceRegistry
		_persistedModelLocalServiceRegistry;

	@Reference
	private Portal _portal;

}