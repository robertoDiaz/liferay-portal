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
public class DropboxRevisionSoap implements Serializable {
	public static DropboxRevisionSoap toSoapModel(DropboxRevision model) {
		DropboxRevisionSoap soapModel = new DropboxRevisionSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setRevisionId(model.getRevisionId());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setEntryId(model.getEntryId());
		soapModel.setPath(model.getPath());
		soapModel.setRepositoryId(model.getRepositoryId());
		soapModel.setRev(model.getRev());
		soapModel.setSize(model.getSize());

		return soapModel;
	}

	public static DropboxRevisionSoap[] toSoapModels(DropboxRevision[] models) {
		DropboxRevisionSoap[] soapModels = new DropboxRevisionSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static DropboxRevisionSoap[][] toSoapModels(
		DropboxRevision[][] models) {
		DropboxRevisionSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new DropboxRevisionSoap[models.length][models[0].length];
		}
		else {
			soapModels = new DropboxRevisionSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static DropboxRevisionSoap[] toSoapModels(
		List<DropboxRevision> models) {
		List<DropboxRevisionSoap> soapModels = new ArrayList<DropboxRevisionSoap>(models.size());

		for (DropboxRevision model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new DropboxRevisionSoap[soapModels.size()]);
	}

	public DropboxRevisionSoap() {
	}

	public long getPrimaryKey() {
		return _revisionId;
	}

	public void setPrimaryKey(long pk) {
		setRevisionId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getRevisionId() {
		return _revisionId;
	}

	public void setRevisionId(long revisionId) {
		_revisionId = revisionId;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public long getEntryId() {
		return _entryId;
	}

	public void setEntryId(long entryId) {
		_entryId = entryId;
	}

	public String getPath() {
		return _path;
	}

	public void setPath(String path) {
		_path = path;
	}

	public long getRepositoryId() {
		return _repositoryId;
	}

	public void setRepositoryId(long repositoryId) {
		_repositoryId = repositoryId;
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

	private String _uuid;
	private long _revisionId;
	private Date _createDate;
	private long _entryId;
	private String _path;
	private long _repositoryId;
	private String _rev;
	private long _size;
}