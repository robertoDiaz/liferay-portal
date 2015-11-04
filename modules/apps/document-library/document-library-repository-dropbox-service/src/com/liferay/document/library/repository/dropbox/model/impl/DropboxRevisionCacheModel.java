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

package com.liferay.document.library.repository.dropbox.model.impl;

import aQute.bnd.annotation.ProviderType;

import com.liferay.document.library.repository.dropbox.model.DropboxRevision;

import com.liferay.portal.kernel.util.HashUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing DropboxRevision in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see DropboxRevision
 * @generated
 */
@ProviderType
public class DropboxRevisionCacheModel implements CacheModel<DropboxRevision>,
	Externalizable {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DropboxRevisionCacheModel)) {
			return false;
		}

		DropboxRevisionCacheModel dropboxRevisionCacheModel = (DropboxRevisionCacheModel)obj;

		if (revisionId == dropboxRevisionCacheModel.revisionId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, revisionId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(17);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", revisionId=");
		sb.append(revisionId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", entryId=");
		sb.append(entryId);
		sb.append(", path=");
		sb.append(path);
		sb.append(", repositoryId=");
		sb.append(repositoryId);
		sb.append(", rev=");
		sb.append(rev);
		sb.append(", size=");
		sb.append(size);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public DropboxRevision toEntityModel() {
		DropboxRevisionImpl dropboxRevisionImpl = new DropboxRevisionImpl();

		if (uuid == null) {
			dropboxRevisionImpl.setUuid(StringPool.BLANK);
		}
		else {
			dropboxRevisionImpl.setUuid(uuid);
		}

		dropboxRevisionImpl.setRevisionId(revisionId);

		if (createDate == Long.MIN_VALUE) {
			dropboxRevisionImpl.setCreateDate(null);
		}
		else {
			dropboxRevisionImpl.setCreateDate(new Date(createDate));
		}

		dropboxRevisionImpl.setEntryId(entryId);

		if (path == null) {
			dropboxRevisionImpl.setPath(StringPool.BLANK);
		}
		else {
			dropboxRevisionImpl.setPath(path);
		}

		dropboxRevisionImpl.setRepositoryId(repositoryId);

		if (rev == null) {
			dropboxRevisionImpl.setRev(StringPool.BLANK);
		}
		else {
			dropboxRevisionImpl.setRev(rev);
		}

		dropboxRevisionImpl.setSize(size);

		dropboxRevisionImpl.resetOriginalValues();

		return dropboxRevisionImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();
		revisionId = objectInput.readLong();
		createDate = objectInput.readLong();
		entryId = objectInput.readLong();
		path = objectInput.readUTF();
		repositoryId = objectInput.readLong();
		rev = objectInput.readUTF();
		size = objectInput.readLong();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput)
		throws IOException {
		if (uuid == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(uuid);
		}

		objectOutput.writeLong(revisionId);
		objectOutput.writeLong(createDate);
		objectOutput.writeLong(entryId);

		if (path == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(path);
		}

		objectOutput.writeLong(repositoryId);

		if (rev == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(rev);
		}

		objectOutput.writeLong(size);
	}

	public String uuid;
	public long revisionId;
	public long createDate;
	public long entryId;
	public String path;
	public long repositoryId;
	public String rev;
	public long size;
}