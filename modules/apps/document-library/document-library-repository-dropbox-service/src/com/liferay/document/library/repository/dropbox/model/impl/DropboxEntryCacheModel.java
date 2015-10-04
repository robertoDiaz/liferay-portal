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

import com.liferay.document.library.repository.dropbox.model.DropboxEntry;

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
 * The cache model class for representing DropboxEntry in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @see DropboxEntry
 * @generated
 */
@ProviderType
public class DropboxEntryCacheModel implements CacheModel<DropboxEntry>,
	Externalizable {
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DropboxEntryCacheModel)) {
			return false;
		}

		DropboxEntryCacheModel dropboxEntryCacheModel = (DropboxEntryCacheModel)obj;

		if (entryId == dropboxEntryCacheModel.entryId) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, entryId);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(35);

		sb.append("{uuid=");
		sb.append(uuid);
		sb.append(", entryId=");
		sb.append(entryId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", companyId=");
		sb.append(companyId);
		sb.append(", repositoryId=");
		sb.append(repositoryId);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", userName=");
		sb.append(userName);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", modifiedDate=");
		sb.append(modifiedDate);
		sb.append(", changeLog=");
		sb.append(changeLog);
		sb.append(", description=");
		sb.append(description);
		sb.append(", name=");
		sb.append(name);
		sb.append(", path=");
		sb.append(path);
		sb.append(", parentPath=");
		sb.append(parentPath);
		sb.append(", rev=");
		sb.append(rev);
		sb.append(", size=");
		sb.append(size);
		sb.append(", type=");
		sb.append(type);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public DropboxEntry toEntityModel() {
		DropboxEntryImpl dropboxEntryImpl = new DropboxEntryImpl();

		if (uuid == null) {
			dropboxEntryImpl.setUuid(StringPool.BLANK);
		}
		else {
			dropboxEntryImpl.setUuid(uuid);
		}

		dropboxEntryImpl.setEntryId(entryId);
		dropboxEntryImpl.setGroupId(groupId);
		dropboxEntryImpl.setCompanyId(companyId);
		dropboxEntryImpl.setRepositoryId(repositoryId);
		dropboxEntryImpl.setUserId(userId);

		if (userName == null) {
			dropboxEntryImpl.setUserName(StringPool.BLANK);
		}
		else {
			dropboxEntryImpl.setUserName(userName);
		}

		if (createDate == Long.MIN_VALUE) {
			dropboxEntryImpl.setCreateDate(null);
		}
		else {
			dropboxEntryImpl.setCreateDate(new Date(createDate));
		}

		if (modifiedDate == Long.MIN_VALUE) {
			dropboxEntryImpl.setModifiedDate(null);
		}
		else {
			dropboxEntryImpl.setModifiedDate(new Date(modifiedDate));
		}

		if (changeLog == null) {
			dropboxEntryImpl.setChangeLog(StringPool.BLANK);
		}
		else {
			dropboxEntryImpl.setChangeLog(changeLog);
		}

		if (description == null) {
			dropboxEntryImpl.setDescription(StringPool.BLANK);
		}
		else {
			dropboxEntryImpl.setDescription(description);
		}

		if (name == null) {
			dropboxEntryImpl.setName(StringPool.BLANK);
		}
		else {
			dropboxEntryImpl.setName(name);
		}

		if (path == null) {
			dropboxEntryImpl.setPath(StringPool.BLANK);
		}
		else {
			dropboxEntryImpl.setPath(path);
		}

		if (parentPath == null) {
			dropboxEntryImpl.setParentPath(StringPool.BLANK);
		}
		else {
			dropboxEntryImpl.setParentPath(parentPath);
		}

		if (rev == null) {
			dropboxEntryImpl.setRev(StringPool.BLANK);
		}
		else {
			dropboxEntryImpl.setRev(rev);
		}

		dropboxEntryImpl.setSize(size);
		dropboxEntryImpl.setType(type);

		dropboxEntryImpl.resetOriginalValues();

		return dropboxEntryImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		uuid = objectInput.readUTF();
		entryId = objectInput.readLong();
		groupId = objectInput.readLong();
		companyId = objectInput.readLong();
		repositoryId = objectInput.readLong();
		userId = objectInput.readLong();
		userName = objectInput.readUTF();
		createDate = objectInput.readLong();
		modifiedDate = objectInput.readLong();
		changeLog = objectInput.readUTF();
		description = objectInput.readUTF();
		name = objectInput.readUTF();
		path = objectInput.readUTF();
		parentPath = objectInput.readUTF();
		rev = objectInput.readUTF();
		size = objectInput.readLong();
		type = objectInput.readInt();
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

		objectOutput.writeLong(entryId);
		objectOutput.writeLong(groupId);
		objectOutput.writeLong(companyId);
		objectOutput.writeLong(repositoryId);
		objectOutput.writeLong(userId);

		if (userName == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(userName);
		}

		objectOutput.writeLong(createDate);
		objectOutput.writeLong(modifiedDate);

		if (changeLog == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(changeLog);
		}

		if (description == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(description);
		}

		if (name == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(name);
		}

		if (path == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(path);
		}

		if (parentPath == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(parentPath);
		}

		if (rev == null) {
			objectOutput.writeUTF(StringPool.BLANK);
		}
		else {
			objectOutput.writeUTF(rev);
		}

		objectOutput.writeLong(size);
		objectOutput.writeInt(type);
	}

	public String uuid;
	public long entryId;
	public long groupId;
	public long companyId;
	public long repositoryId;
	public long userId;
	public String userName;
	public long createDate;
	public long modifiedDate;
	public String changeLog;
	public String description;
	public String name;
	public String path;
	public String parentPath;
	public String rev;
	public long size;
	public int type;
}