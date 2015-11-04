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

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.ModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link DropboxRevision}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DropboxRevision
 * @generated
 */
@ProviderType
public class DropboxRevisionWrapper implements DropboxRevision,
	ModelWrapper<DropboxRevision> {
	public DropboxRevisionWrapper(DropboxRevision dropboxRevision) {
		_dropboxRevision = dropboxRevision;
	}

	@Override
	public Class<?> getModelClass() {
		return DropboxRevision.class;
	}

	@Override
	public String getModelClassName() {
		return DropboxRevision.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("revisionId", getRevisionId());
		attributes.put("createDate", getCreateDate());
		attributes.put("entryId", getEntryId());
		attributes.put("path", getPath());
		attributes.put("repositoryId", getRepositoryId());
		attributes.put("rev", getRev());
		attributes.put("size", getSize());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long revisionId = (Long)attributes.get("revisionId");

		if (revisionId != null) {
			setRevisionId(revisionId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Long entryId = (Long)attributes.get("entryId");

		if (entryId != null) {
			setEntryId(entryId);
		}

		String path = (String)attributes.get("path");

		if (path != null) {
			setPath(path);
		}

		Long repositoryId = (Long)attributes.get("repositoryId");

		if (repositoryId != null) {
			setRepositoryId(repositoryId);
		}

		String rev = (String)attributes.get("rev");

		if (rev != null) {
			setRev(rev);
		}

		Long size = (Long)attributes.get("size");

		if (size != null) {
			setSize(size);
		}
	}

	@Override
	public java.lang.Object clone() {
		return new DropboxRevisionWrapper((DropboxRevision)_dropboxRevision.clone());
	}

	@Override
	public int compareTo(
		com.liferay.document.library.repository.dropbox.model.DropboxRevision dropboxRevision) {
		return _dropboxRevision.compareTo(dropboxRevision);
	}

	/**
	* Returns the create date of this dropbox revision.
	*
	* @return the create date of this dropbox revision
	*/
	@Override
	public Date getCreateDate() {
		return _dropboxRevision.getCreateDate();
	}

	/**
	* Returns the entry ID of this dropbox revision.
	*
	* @return the entry ID of this dropbox revision
	*/
	@Override
	public long getEntryId() {
		return _dropboxRevision.getEntryId();
	}

	@Override
	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _dropboxRevision.getExpandoBridge();
	}

	/**
	* Returns the path of this dropbox revision.
	*
	* @return the path of this dropbox revision
	*/
	@Override
	public java.lang.String getPath() {
		return _dropboxRevision.getPath();
	}

	/**
	* Returns the primary key of this dropbox revision.
	*
	* @return the primary key of this dropbox revision
	*/
	@Override
	public long getPrimaryKey() {
		return _dropboxRevision.getPrimaryKey();
	}

	@Override
	public java.io.Serializable getPrimaryKeyObj() {
		return _dropboxRevision.getPrimaryKeyObj();
	}

	/**
	* Returns the repository ID of this dropbox revision.
	*
	* @return the repository ID of this dropbox revision
	*/
	@Override
	public long getRepositoryId() {
		return _dropboxRevision.getRepositoryId();
	}

	/**
	* Returns the rev of this dropbox revision.
	*
	* @return the rev of this dropbox revision
	*/
	@Override
	public java.lang.String getRev() {
		return _dropboxRevision.getRev();
	}

	/**
	* Returns the revision ID of this dropbox revision.
	*
	* @return the revision ID of this dropbox revision
	*/
	@Override
	public long getRevisionId() {
		return _dropboxRevision.getRevisionId();
	}

	/**
	* Returns the size of this dropbox revision.
	*
	* @return the size of this dropbox revision
	*/
	@Override
	public long getSize() {
		return _dropboxRevision.getSize();
	}

	/**
	* Returns the uuid of this dropbox revision.
	*
	* @return the uuid of this dropbox revision
	*/
	@Override
	public java.lang.String getUuid() {
		return _dropboxRevision.getUuid();
	}

	@Override
	public int hashCode() {
		return _dropboxRevision.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _dropboxRevision.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _dropboxRevision.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _dropboxRevision.isNew();
	}

	@Override
	public void persist() {
		_dropboxRevision.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_dropboxRevision.setCachedModel(cachedModel);
	}

	/**
	* Sets the create date of this dropbox revision.
	*
	* @param createDate the create date of this dropbox revision
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_dropboxRevision.setCreateDate(createDate);
	}

	/**
	* Sets the entry ID of this dropbox revision.
	*
	* @param entryId the entry ID of this dropbox revision
	*/
	@Override
	public void setEntryId(long entryId) {
		_dropboxRevision.setEntryId(entryId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.model.BaseModel<?> baseModel) {
		_dropboxRevision.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portlet.expando.model.ExpandoBridge expandoBridge) {
		_dropboxRevision.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_dropboxRevision.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public void setNew(boolean n) {
		_dropboxRevision.setNew(n);
	}

	/**
	* Sets the path of this dropbox revision.
	*
	* @param path the path of this dropbox revision
	*/
	@Override
	public void setPath(java.lang.String path) {
		_dropboxRevision.setPath(path);
	}

	/**
	* Sets the primary key of this dropbox revision.
	*
	* @param primaryKey the primary key of this dropbox revision
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_dropboxRevision.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
		_dropboxRevision.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the repository ID of this dropbox revision.
	*
	* @param repositoryId the repository ID of this dropbox revision
	*/
	@Override
	public void setRepositoryId(long repositoryId) {
		_dropboxRevision.setRepositoryId(repositoryId);
	}

	/**
	* Sets the rev of this dropbox revision.
	*
	* @param rev the rev of this dropbox revision
	*/
	@Override
	public void setRev(java.lang.String rev) {
		_dropboxRevision.setRev(rev);
	}

	/**
	* Sets the revision ID of this dropbox revision.
	*
	* @param revisionId the revision ID of this dropbox revision
	*/
	@Override
	public void setRevisionId(long revisionId) {
		_dropboxRevision.setRevisionId(revisionId);
	}

	/**
	* Sets the size of this dropbox revision.
	*
	* @param size the size of this dropbox revision
	*/
	@Override
	public void setSize(long size) {
		_dropboxRevision.setSize(size);
	}

	/**
	* Sets the uuid of this dropbox revision.
	*
	* @param uuid the uuid of this dropbox revision
	*/
	@Override
	public void setUuid(java.lang.String uuid) {
		_dropboxRevision.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.model.CacheModel<com.liferay.document.library.repository.dropbox.model.DropboxRevision> toCacheModel() {
		return _dropboxRevision.toCacheModel();
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision toEscapedModel() {
		return new DropboxRevisionWrapper(_dropboxRevision.toEscapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _dropboxRevision.toString();
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxRevision toUnescapedModel() {
		return new DropboxRevisionWrapper(_dropboxRevision.toUnescapedModel());
	}

	@Override
	public java.lang.String toXmlString() {
		return _dropboxRevision.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DropboxRevisionWrapper)) {
			return false;
		}

		DropboxRevisionWrapper dropboxRevisionWrapper = (DropboxRevisionWrapper)obj;

		if (Validator.equals(_dropboxRevision,
					dropboxRevisionWrapper._dropboxRevision)) {
			return true;
		}

		return false;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedModel}
	 */
	@Deprecated
	public DropboxRevision getWrappedDropboxRevision() {
		return _dropboxRevision;
	}

	@Override
	public DropboxRevision getWrappedModel() {
		return _dropboxRevision;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _dropboxRevision.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _dropboxRevision.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_dropboxRevision.resetOriginalValues();
	}

	private final DropboxRevision _dropboxRevision;
}