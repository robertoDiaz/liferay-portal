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

package com.liferay.portlet.documentlibrary.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.kernel.model.DLPreview;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;

import com.liferay.portal.kernel.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The cache model class for representing DLPreview in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see DLPreview
 * @generated
 */
@ProviderType
public class DLPreviewCacheModel implements CacheModel<DLPreview>,
	Externalizable {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DLPreviewCacheModel)) {
			return false;
		}

		DLPreviewCacheModel dlPreviewCacheModel = (DLPreviewCacheModel)obj;

		if (filePreviewId == dlPreviewCacheModel.filePreviewId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, filePreviewId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(11);

		sb.append("{filePreviewId=");
		sb.append(filePreviewId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", fileEntryId=");
		sb.append(fileEntryId);
		sb.append(", fileVersionId=");
		sb.append(fileVersionId);
		sb.append(", status=");
		sb.append(status);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public DLPreview toEntityModel() {
		DLPreviewImpl dlPreviewImpl = new DLPreviewImpl();

		dlPreviewImpl.setFilePreviewId(filePreviewId);
		dlPreviewImpl.setGroupId(groupId);
		dlPreviewImpl.setFileEntryId(fileEntryId);
		dlPreviewImpl.setFileVersionId(fileVersionId);

		if (status == null) {
			dlPreviewImpl.setStatus("");
		}
		else {
			dlPreviewImpl.setStatus(status);
		}

		dlPreviewImpl.resetOriginalValues();

		return dlPreviewImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		filePreviewId = objectInput.readLong();

		groupId = objectInput.readLong();

		fileEntryId = objectInput.readLong();

		fileVersionId = objectInput.readLong();
		status = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		objectOutput.writeLong(filePreviewId);

		objectOutput.writeLong(groupId);

		objectOutput.writeLong(fileEntryId);

		objectOutput.writeLong(fileVersionId);

		if (status == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(status);
		}
	}

	public long filePreviewId;
	public long groupId;
	public long fileEntryId;
	public long fileVersionId;
	public String status;
}