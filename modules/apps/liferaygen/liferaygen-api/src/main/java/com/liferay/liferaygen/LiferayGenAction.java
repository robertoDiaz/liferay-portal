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

package com.liferay.liferaygen;

import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.model.ClassedModel;

import java.util.Map;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
public interface LiferayGenAction {

	/**
	 * Configures the action object with executor configuration+parameters
	 *
	 * @param parameters
	 */
	public void configure(Map<String, Object> parameters);

	/**
	 * Method to be called after all action executions are finished
	 */
	public void destroy();

	/**
	 * Returns action description
	 *
	 * @return description
	 */
	public String getDescription();

	/**
	 * Returns the filter that will use in the query that fills parameter
	 * "target". More information see getEntityModel and getEntityProperties
	 * If implementation returns null, no filter is applied
	 *
	 * @return
	 */
	public Criterion getEntityFilter();

	/**
	 * Returns the entity model that will be queried in order to fill parameter
	 * "target". If implementation returns null, no query will be executed
	 *
	 * @return
	 */
	public Class<? extends ClassedModel> getEntityModel();

	/**
	 * Returns the entity model primary key name that can be used as a filter to
	 * get the parameter "target". If implementation returns null, no filter
	 * will be used
	 *
	 * @return
	 */
	public String getEntityModelPK();

	/**
	 * Returns the entity properties that will be queried to fill "target"
	 * parameter. Properties must be separater by commas (i.e: userId,contactId)
	 * If implementation returns null, the whole object will be filled.
	 *
	 * @return
	 */
	public String getEntityProperties();

	/**
	 * Returns action description
	 *
	 * @return description
	 */
	public String getName();

	/**
	 * Returns the default values for action parameters
	 *
	 * @return default parameters
	 */
	public Map<String, Object> getParametersDefaultValues();

	/**
	 * Returns the description of each action parameters
	 *
	 * @return default parameters
	 */
	public Map<String, String> getParametersDescription();

	/**
	 * Returns if action has scope by groupId or not
	 *
	 * @return scope by groupId
	 */
	public boolean hasScopeByGroupId();

	/**
	 * Method to be called before any action execution is launched
	 */
	public void init();

	/**
	 * Executes the action
	 */
	public void run();

}