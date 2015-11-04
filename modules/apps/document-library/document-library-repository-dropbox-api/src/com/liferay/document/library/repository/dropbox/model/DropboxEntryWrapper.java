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
 * This class is a wrapper for {@link DropboxEntry}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DropboxEntry
 * @generated
 */
@ProviderType
public class DropboxEntryWrapper implements DropboxEntry,
	ModelWrapper<DropboxEntry> {
	public DropboxEntryWrapper(DropboxEntry dropboxEntry) {
		_dropboxEntry = dropboxEntry;
	}

	@Override
	public Class<?> getModelClass() {
		return DropboxEntry.class;
	}

	@Override
	public String getModelClassName() {
		return DropboxEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("entryId", getEntryId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("repositoryId", getRepositoryId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("changeLog", getChangeLog());
		attributes.put("description", getDescription());
		attributes.put("name", getName());
		attributes.put("path", getPath());
		attributes.put("parentPath", getParentPath());
		attributes.put("rev", getRev());
		attributes.put("size", getSize());
		attributes.put("type", getType());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long entryId = (Long)attributes.get("entryId");

		if (entryId != null) {
			setEntryId(entryId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long repositoryId = (Long)attributes.get("repositoryId");

		if (repositoryId != null) {
			setRepositoryId(repositoryId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String changeLog = (String)attributes.get("changeLog");

		if (changeLog != null) {
			setChangeLog(changeLog);
		}

		String description = (String)attributes.get("description");

		if (description != null) {
			setDescription(description);
		}

		String name = (String)attributes.get("name");

		if (name != null) {
			setName(name);
		}

		String path = (String)attributes.get("path");

		if (path != null) {
			setPath(path);
		}

		String parentPath = (String)attributes.get("parentPath");

		if (parentPath != null) {
			setParentPath(parentPath);
		}

		String rev = (String)attributes.get("rev");

		if (rev != null) {
			setRev(rev);
		}

		Long size = (Long)attributes.get("size");

		if (size != null) {
			setSize(size);
		}

		Integer type = (Integer)attributes.get("type");

		if (type != null) {
			setType(type);
		}
	}

	@Override
	public java.lang.Object clone() {
		return new DropboxEntryWrapper((DropboxEntry)_dropboxEntry.clone());
	}

	@Override
	public int compareTo(
		com.liferay.document.library.repository.dropbox.model.DropboxEntry dropboxEntry) {
		return _dropboxEntry.compareTo(dropboxEntry);
	}

	/**
	* Returns the change log of this dropbox entry.
	*
	* @return the change log of this dropbox entry
	*/
	@Override
	public java.lang.String getChangeLog() {
		return _dropboxEntry.getChangeLog();
	}

	/**
	* Returns the company ID of this dropbox entry.
	*
	* @return the company ID of this dropbox entry
	*/
	@Override
	public long getCompanyId() {
		return _dropboxEntry.getCompanyId();
	}

	/**
	* Returns the create date of this dropbox entry.
	*
	* @return the create date of this dropbox entry
	*/
	@Override
	public Date getCreateDate() {
		return _dropboxEntry.getCreateDate();
	}

	/**
	* Returns the description of this dropbox entry.
	*
	* @return the description of this dropbox entry
	*/
	@Override
	public java.lang.String getDescription() {
		return _dropboxEntry.getDescription();
	}

	/**
	* Returns the entry ID of this dropbox entry.
	*
	* @return the entry ID of this dropbox entry
	*/
	@Override
	public long getEntryId() {
		return _dropboxEntry.getEntryId();
	}

	@Override
	public com.liferay.portlet.expando.model.ExpandoBridge getExpandoBridge() {
		return _dropboxEntry.getExpandoBridge();
	}

	/**
	* Returns the group ID of this dropbox entry.
	*
	* @return the group ID of this dropbox entry
	*/
	@Override
	public long getGroupId() {
		return _dropboxEntry.getGroupId();
	}

	/**
	* Returns the modified date of this dropbox entry.
	*
	* @return the modified date of this dropbox entry
	*/
	@Override
	public Date getModifiedDate() {
		return _dropboxEntry.getModifiedDate();
	}

	/**
	* Returns the name of this dropbox entry.
	*
	* @return the name of this dropbox entry
	*/
	@Override
	public java.lang.String getName() {
		return _dropboxEntry.getName();
	}

	/**
	* Returns the parent path of this dropbox entry.
	*
	* @return the parent path of this dropbox entry
	*/
	@Override
	public java.lang.String getParentPath() {
		return _dropboxEntry.getParentPath();
	}

	/**
	* Returns the path of this dropbox entry.
	*
	* @return the path of this dropbox entry
	*/
	@Override
	public java.lang.String getPath() {
		return _dropboxEntry.getPath();
	}

	/**
	* Returns the primary key of this dropbox entry.
	*
	* @return the primary key of this dropbox entry
	*/
	@Override
	public long getPrimaryKey() {
		return _dropboxEntry.getPrimaryKey();
	}

	@Override
	public java.io.Serializable getPrimaryKeyObj() {
		return _dropboxEntry.getPrimaryKeyObj();
	}

	/**
	* Returns the repository ID of this dropbox entry.
	*
	* @return the repository ID of this dropbox entry
	*/
	@Override
	public long getRepositoryId() {
		return _dropboxEntry.getRepositoryId();
	}

	/**
	* Returns the rev of this dropbox entry.
	*
	* @return the rev of this dropbox entry
	*/
	@Override
	public java.lang.String getRev() {
		return _dropboxEntry.getRev();
	}

	/**
	* Returns the size of this dropbox entry.
	*
	* @return the size of this dropbox entry
	*/
	@Override
	public long getSize() {
		return _dropboxEntry.getSize();
	}

	/**
	* Returns the type of this dropbox entry.
	*
	* @return the type of this dropbox entry
	*/
	@Override
	public int getType() {
		return _dropboxEntry.getType();
	}

	/**
	* Returns the user ID of this dropbox entry.
	*
	* @return the user ID of this dropbox entry
	*/
	@Override
	public long getUserId() {
		return _dropboxEntry.getUserId();
	}

	/**
	* Returns the user name of this dropbox entry.
	*
	* @return the user name of this dropbox entry
	*/
	@Override
	public java.lang.String getUserName() {
		return _dropboxEntry.getUserName();
	}

	/**
	* Returns the user uuid of this dropbox entry.
	*
	* @return the user uuid of this dropbox entry
	*/
	@Override
	public java.lang.String getUserUuid() {
		return _dropboxEntry.getUserUuid();
	}

	/**
	* Returns the uuid of this dropbox entry.
	*
	* @return the uuid of this dropbox entry
	*/
	@Override
	public java.lang.String getUuid() {
		return _dropboxEntry.getUuid();
	}

	@Override
	public int hashCode() {
		return _dropboxEntry.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _dropboxEntry.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _dropboxEntry.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _dropboxEntry.isNew();
	}

	@Override
	public void persist() {
		_dropboxEntry.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_dropboxEntry.setCachedModel(cachedModel);
	}

	/**
	* Sets the change log of this dropbox entry.
	*
	* @param changeLog the change log of this dropbox entry
	*/
	@Override
	public void setChangeLog(java.lang.String changeLog) {
		_dropboxEntry.setChangeLog(changeLog);
	}

	/**
	* Sets the company ID of this dropbox entry.
	*
	* @param companyId the company ID of this dropbox entry
	*/
	@Override
	public void setCompanyId(long companyId) {
		_dropboxEntry.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this dropbox entry.
	*
	* @param createDate the create date of this dropbox entry
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_dropboxEntry.setCreateDate(createDate);
	}

	/**
	* Sets the description of this dropbox entry.
	*
	* @param description the description of this dropbox entry
	*/
	@Override
	public void setDescription(java.lang.String description) {
		_dropboxEntry.setDescription(description);
	}

	/**
	* Sets the entry ID of this dropbox entry.
	*
	* @param entryId the entry ID of this dropbox entry
	*/
	@Override
	public void setEntryId(long entryId) {
		_dropboxEntry.setEntryId(entryId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.model.BaseModel<?> baseModel) {
		_dropboxEntry.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portlet.expando.model.ExpandoBridge expandoBridge) {
		_dropboxEntry.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.service.ServiceContext serviceContext) {
		_dropboxEntry.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the group ID of this dropbox entry.
	*
	* @param groupId the group ID of this dropbox entry
	*/
	@Override
	public void setGroupId(long groupId) {
		_dropboxEntry.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this dropbox entry.
	*
	* @param modifiedDate the modified date of this dropbox entry
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_dropboxEntry.setModifiedDate(modifiedDate);
	}

	/**
	* Sets the name of this dropbox entry.
	*
	* @param name the name of this dropbox entry
	*/
	@Override
	public void setName(java.lang.String name) {
		_dropboxEntry.setName(name);
	}

	@Override
	public void setNew(boolean n) {
		_dropboxEntry.setNew(n);
	}

	/**
	* Sets the parent path of this dropbox entry.
	*
	* @param parentPath the parent path of this dropbox entry
	*/
	@Override
	public void setParentPath(java.lang.String parentPath) {
		_dropboxEntry.setParentPath(parentPath);
	}

	/**
	* Sets the path of this dropbox entry.
	*
	* @param path the path of this dropbox entry
	*/
	@Override
	public void setPath(java.lang.String path) {
		_dropboxEntry.setPath(path);
	}

	/**
	* Sets the primary key of this dropbox entry.
	*
	* @param primaryKey the primary key of this dropbox entry
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_dropboxEntry.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(java.io.Serializable primaryKeyObj) {
		_dropboxEntry.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the repository ID of this dropbox entry.
	*
	* @param repositoryId the repository ID of this dropbox entry
	*/
	@Override
	public void setRepositoryId(long repositoryId) {
		_dropboxEntry.setRepositoryId(repositoryId);
	}

	/**
	* Sets the rev of this dropbox entry.
	*
	* @param rev the rev of this dropbox entry
	*/
	@Override
	public void setRev(java.lang.String rev) {
		_dropboxEntry.setRev(rev);
	}

	/**
	* Sets the size of this dropbox entry.
	*
	* @param size the size of this dropbox entry
	*/
	@Override
	public void setSize(long size) {
		_dropboxEntry.setSize(size);
	}

	/**
	* Sets the type of this dropbox entry.
	*
	* @param type the type of this dropbox entry
	*/
	@Override
	public void setType(int type) {
		_dropboxEntry.setType(type);
	}

	/**
	* Sets the user ID of this dropbox entry.
	*
	* @param userId the user ID of this dropbox entry
	*/
	@Override
	public void setUserId(long userId) {
		_dropboxEntry.setUserId(userId);
	}

	/**
	* Sets the user name of this dropbox entry.
	*
	* @param userName the user name of this dropbox entry
	*/
	@Override
	public void setUserName(java.lang.String userName) {
		_dropboxEntry.setUserName(userName);
	}

	/**
	* Sets the user uuid of this dropbox entry.
	*
	* @param userUuid the user uuid of this dropbox entry
	*/
	@Override
	public void setUserUuid(java.lang.String userUuid) {
		_dropboxEntry.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this dropbox entry.
	*
	* @param uuid the uuid of this dropbox entry
	*/
	@Override
	public void setUuid(java.lang.String uuid) {
		_dropboxEntry.setUuid(uuid);
	}

	@Override
	public com.liferay.portal.model.CacheModel<com.liferay.document.library.repository.dropbox.model.DropboxEntry> toCacheModel() {
		return _dropboxEntry.toCacheModel();
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry toEscapedModel() {
		return new DropboxEntryWrapper(_dropboxEntry.toEscapedModel());
	}

	@Override
	public java.lang.String toString() {
		return _dropboxEntry.toString();
	}

	@Override
	public com.liferay.document.library.repository.dropbox.model.DropboxEntry toUnescapedModel() {
		return new DropboxEntryWrapper(_dropboxEntry.toUnescapedModel());
	}

	@Override
	public java.lang.String toXmlString() {
		return _dropboxEntry.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DropboxEntryWrapper)) {
			return false;
		}

		DropboxEntryWrapper dropboxEntryWrapper = (DropboxEntryWrapper)obj;

		if (Validator.equals(_dropboxEntry, dropboxEntryWrapper._dropboxEntry)) {
			return true;
		}

		return false;
	}

	/**
	 * @deprecated As of 6.1.0, replaced by {@link #getWrappedModel}
	 */
	@Deprecated
	public DropboxEntry getWrappedDropboxEntry() {
		return _dropboxEntry;
	}

	@Override
	public DropboxEntry getWrappedModel() {
		return _dropboxEntry;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _dropboxEntry.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _dropboxEntry.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_dropboxEntry.resetOriginalValues();
	}

	private final DropboxEntry _dropboxEntry;
}