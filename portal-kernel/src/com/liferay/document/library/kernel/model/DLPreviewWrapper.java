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

package com.liferay.document.library.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link DLPreview}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see DLPreview
 * @generated
 */
@ProviderType
public class DLPreviewWrapper implements DLPreview, ModelWrapper<DLPreview> {
	public DLPreviewWrapper(DLPreview dlPreview) {
		_dlPreview = dlPreview;
	}

	@Override
	public Class<?> getModelClass() {
		return DLPreview.class;
	}

	@Override
	public String getModelClassName() {
		return DLPreview.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("filePreviewId", getFilePreviewId());
		attributes.put("groupId", getGroupId());
		attributes.put("fileEntryId", getFileEntryId());
		attributes.put("fileVersionId", getFileVersionId());
		attributes.put("status", getStatus());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long filePreviewId = (Long)attributes.get("filePreviewId");

		if (filePreviewId != null) {
			setFilePreviewId(filePreviewId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long fileEntryId = (Long)attributes.get("fileEntryId");

		if (fileEntryId != null) {
			setFileEntryId(fileEntryId);
		}

		Long fileVersionId = (Long)attributes.get("fileVersionId");

		if (fileVersionId != null) {
			setFileVersionId(fileVersionId);
		}

		String status = (String)attributes.get("status");

		if (status != null) {
			setStatus(status);
		}
	}

	@Override
	public Object clone() {
		return new DLPreviewWrapper((DLPreview)_dlPreview.clone());
	}

	@Override
	public int compareTo(DLPreview dlPreview) {
		return _dlPreview.compareTo(dlPreview);
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _dlPreview.getExpandoBridge();
	}

	/**
	* Returns the file entry ID of this document library preview.
	*
	* @return the file entry ID of this document library preview
	*/
	@Override
	public long getFileEntryId() {
		return _dlPreview.getFileEntryId();
	}

	/**
	* Returns the file preview ID of this document library preview.
	*
	* @return the file preview ID of this document library preview
	*/
	@Override
	public long getFilePreviewId() {
		return _dlPreview.getFilePreviewId();
	}

	/**
	* Returns the file version ID of this document library preview.
	*
	* @return the file version ID of this document library preview
	*/
	@Override
	public long getFileVersionId() {
		return _dlPreview.getFileVersionId();
	}

	/**
	* Returns the group ID of this document library preview.
	*
	* @return the group ID of this document library preview
	*/
	@Override
	public long getGroupId() {
		return _dlPreview.getGroupId();
	}

	/**
	* Returns the primary key of this document library preview.
	*
	* @return the primary key of this document library preview
	*/
	@Override
	public long getPrimaryKey() {
		return _dlPreview.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _dlPreview.getPrimaryKeyObj();
	}

	/**
	* Returns the status of this document library preview.
	*
	* @return the status of this document library preview
	*/
	@Override
	public String getStatus() {
		return _dlPreview.getStatus();
	}

	@Override
	public int hashCode() {
		return _dlPreview.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _dlPreview.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _dlPreview.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _dlPreview.isNew();
	}

	@Override
	public void persist() {
		_dlPreview.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_dlPreview.setCachedModel(cachedModel);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_dlPreview.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_dlPreview.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_dlPreview.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the file entry ID of this document library preview.
	*
	* @param fileEntryId the file entry ID of this document library preview
	*/
	@Override
	public void setFileEntryId(long fileEntryId) {
		_dlPreview.setFileEntryId(fileEntryId);
	}

	/**
	* Sets the file preview ID of this document library preview.
	*
	* @param filePreviewId the file preview ID of this document library preview
	*/
	@Override
	public void setFilePreviewId(long filePreviewId) {
		_dlPreview.setFilePreviewId(filePreviewId);
	}

	/**
	* Sets the file version ID of this document library preview.
	*
	* @param fileVersionId the file version ID of this document library preview
	*/
	@Override
	public void setFileVersionId(long fileVersionId) {
		_dlPreview.setFileVersionId(fileVersionId);
	}

	/**
	* Sets the group ID of this document library preview.
	*
	* @param groupId the group ID of this document library preview
	*/
	@Override
	public void setGroupId(long groupId) {
		_dlPreview.setGroupId(groupId);
	}

	@Override
	public void setNew(boolean n) {
		_dlPreview.setNew(n);
	}

	/**
	* Sets the primary key of this document library preview.
	*
	* @param primaryKey the primary key of this document library preview
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_dlPreview.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_dlPreview.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the status of this document library preview.
	*
	* @param status the status of this document library preview
	*/
	@Override
	public void setStatus(String status) {
		_dlPreview.setStatus(status);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<DLPreview> toCacheModel() {
		return _dlPreview.toCacheModel();
	}

	@Override
	public DLPreview toEscapedModel() {
		return new DLPreviewWrapper(_dlPreview.toEscapedModel());
	}

	@Override
	public String toString() {
		return _dlPreview.toString();
	}

	@Override
	public DLPreview toUnescapedModel() {
		return new DLPreviewWrapper(_dlPreview.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _dlPreview.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DLPreviewWrapper)) {
			return false;
		}

		DLPreviewWrapper dlPreviewWrapper = (DLPreviewWrapper)obj;

		if (Objects.equals(_dlPreview, dlPreviewWrapper._dlPreview)) {
			return true;
		}

		return false;
	}

	@Override
	public DLPreview getWrappedModel() {
		return _dlPreview;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _dlPreview.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _dlPreview.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_dlPreview.resetOriginalValues();
	}

	private final DLPreview _dlPreview;
}