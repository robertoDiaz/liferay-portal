package com.liferay.liferaygen.util;

import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ProjectionList;
import com.liferay.portal.kernel.service.PersistedModelLocalService;

import java.util.List;

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

	public List<?> executeEntityModelQuery(
		String className, String properties);

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
