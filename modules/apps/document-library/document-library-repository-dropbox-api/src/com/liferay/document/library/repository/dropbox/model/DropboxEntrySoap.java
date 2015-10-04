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

package com.liferay.document.library.repository.dropbox.model;

import aQute.bnd.annotation.ProviderType;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@ProviderType
public class DropboxEntrySoap implements Serializable {
	public static DropboxEntrySoap toSoapModel(DropboxEntry model) {
		DropboxEntrySoap soapModel = new DropboxEntrySoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setEntryId(model.getEntryId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setRepositoryId(model.getRepositoryId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setChangeLog(model.getChangeLog());
		soapModel.setDescription(model.getDescription());
		soapModel.setName(model.getName());
		soapModel.setPath(model.getPath());
		soapModel.setParentPath(model.getParentPath());
		soapModel.setRev(model.getRev());
		soapModel.setSize(model.getSize());
		soapModel.setType(model.getType());

		return soapModel;
	}

	public static DropboxEntrySoap[] toSoapModels(DropboxEntry[] models) {
		DropboxEntrySoap[] soapModels = new DropboxEntrySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static DropboxEntrySoap[][] toSoapModels(DropboxEntry[][] models) {
		DropboxEntrySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new DropboxEntrySoap[models.length][models[0].length];
		}
		else {
			soapModels = new DropboxEntrySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static DropboxEntrySoap[] toSoapModels(List<DropboxEntry> models) {
		List<DropboxEntrySoap> soapModels = new ArrayList<DropboxEntrySoap>(models.size());

		for (DropboxEntry model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new DropboxEntrySoap[soapModels.size()]);
	}

	public DropboxEntrySoap() {
	}

	public long getPrimaryKey() {
		return _entryId;
	}

	public void setPrimaryKey(long pk) {
		setEntryId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getEntryId() {
		return _entryId;
	}

	public void setEntryId(long entryId) {
		_entryId = entryId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getRepositoryId() {
		return _repositoryId;
	}

	public void setRepositoryId(long repositoryId) {
		_repositoryId = repositoryId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getChangeLog() {
		return _changeLog;
	}

	public void setChangeLog(String changeLog) {
		_changeLog = changeLog;
	}

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getPath() {
		return _path;
	}

	public void setPath(String path) {
		_path = path;
	}

	public String getParentPath() {
		return _parentPath;
	}

	public void setParentPath(String parentPath) {
		_parentPath = parentPath;
	}

	public String getRev() {
		return _rev;
	}

	public void setRev(String rev) {
		_rev = rev;
	}

	public long getSize() {
		return _size;
	}

	public void setSize(long size) {
		_size = size;
	}

	public int getType() {
		return _type;
	}

	public void setType(int type) {
		_type = type;
	}

	private String _uuid;
	private long _entryId;
	private long _groupId;
	private long _companyId;
	private long _repositoryId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private String _changeLog;
	private String _description;
	private String _name;
	private String _path;
	private String _parentPath;
	private String _rev;
	private long _size;
	private int _type;
}