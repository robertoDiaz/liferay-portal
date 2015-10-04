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

package com.liferay.document.library.repository.dropbox.internal;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;

import com.liferay.document.library.repository.dropbox.constants.DropboxEntryType;
import com.liferay.document.library.repository.dropbox.internal.model.DropboxFileEntry;
import com.liferay.document.library.repository.dropbox.internal.model.DropboxFileVersion;
import com.liferay.document.library.repository.dropbox.internal.model.DropboxFolder;
import com.liferay.document.library.repository.dropbox.model.DropboxEntry;
import com.liferay.document.library.repository.dropbox.model.DropboxRevision;
import com.liferay.document.library.repository.dropbox.service.DropboxEntryLocalService;
import com.liferay.document.library.repository.dropbox.service.DropboxRevisionLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.RepositoryEntryLocalService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portlet.asset.service.AssetEntryLocalService;
import com.liferay.portlet.documentlibrary.service.DLAppHelperLocalService;
import com.liferay.portlet.documentlibrary.service.DLFolderLocalService;
import com.liferay.repository.external.CredentialsProvider;
import com.liferay.repository.external.ExtRepository;
import com.liferay.repository.external.ExtRepositoryAdapter;
import com.liferay.repository.external.ExtRepositoryFileEntry;
import com.liferay.repository.external.ExtRepositoryFileVersion;
import com.liferay.repository.external.ExtRepositoryFileVersionDescriptor;
import com.liferay.repository.external.ExtRepositoryFolder;
import com.liferay.repository.external.ExtRepositoryModel;
import com.liferay.repository.external.ExtRepositoryObject;
import com.liferay.repository.external.ExtRepositoryObjectType;
import com.liferay.repository.external.ExtRepositorySearchResult;
import com.liferay.repository.external.search.ExtRepositoryQueryMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import jodd.io.StreamUtil;

/**
 * @author Adolfo Pérez
 */
public class DropboxRepository
	extends ExtRepositoryAdapter implements ExtRepository {

	public DropboxRepository(
		long companyId, long groupId, long repositoryId,
		UnicodeProperties typeSettingsProperties,
		DbxClientFactory dbxClientFactory,
		AssetEntryLocalService assetEntryLocalService,
		CompanyLocalService companyLocalService,
		DLAppHelperLocalService dlAppHelperLocalService,
		DLFolderLocalService dlFolderLocalService,
		DropboxEntryLocalService dropboxEntryLocalService,
		DropboxRevisionLocalService dropboxRevisionLocalService,
		RepositoryEntryLocalService repositoryEntryLocalService,
		UserLocalService userLocalService) {

		super(null);

		setAssetEntryLocalService(assetEntryLocalService);
		setCompanyId(companyId);
		setCompanyLocalService(companyLocalService);
		setDLAppHelperLocalService(dlAppHelperLocalService);
		setDLFolderLocalService(dlFolderLocalService);
		setGroupId(groupId);
		setRepositoryId(repositoryId);
		setRepositoryEntryLocalService(repositoryEntryLocalService);
		setTypeSettingsProperties(typeSettingsProperties);
		setUserLocalService(userLocalService);

		_dbxClientFactory = dbxClientFactory;
		_dropboxEntryLocalService = dropboxEntryLocalService;
		_dropboxRevisionLocalService = dropboxRevisionLocalService;
	}

	@Override
	public ExtRepositoryFileEntry addExtRepositoryFileEntry(
			String extRepositoryParentFolderKey, String mimeType, String title,
			String description, String changeLog, InputStream inputStream)
		throws PortalException {

		File tempFile = null;
		InputStream is = null;

		try {
			DbxClient dbxClient = getDbxClient();

			tempFile = FileUtil.createTempFile(inputStream);

			is = new FileInputStream(tempFile);

			String dropboxPath = getDropboxPath(
				extRepositoryParentFolderKey, title);

			DbxEntry.File dbxFile = dbxClient.uploadFile(
				dropboxPath, DbxWriteMode.add(), tempFile.length(), is);

			DropboxEntry dropboxEntry =
				_dropboxEntryLocalService.addDropboxEntry(
					getCompanyId(), getGroupId(), getRepositoryId(),
					getUserId(), dropboxPath, getRev(dbxFile), title,
					description, changeLog, dbxFile.numBytes,
					DropboxEntryType.FILE);

			return new DropboxFileEntry(dropboxEntry);
		}
		catch (DbxException | IOException e) {
			throw new PortalException(e);
		}
		finally {
			StreamUtil.close(is);
			FileUtil.delete(tempFile);
		}
	}

	@Override
	public ExtRepositoryFolder addExtRepositoryFolder(
			String extRepositoryParentFolderKey, String name,
			String description)
		throws PortalException {

		try {
			DbxClient dbxClient = getDbxClient();

			String dropboxPath = getDropboxPath(
				extRepositoryParentFolderKey, name);

			DbxEntry.Folder dbxFolder = dbxClient.createFolder(dropboxPath);

			DropboxEntry dropboxEntry =
				_dropboxEntryLocalService.addDropboxEntry(
					getCompanyId(), getGroupId(), getRepositoryId(),
					getUserId(), dropboxPath, getRev(dbxFolder), name,
					description, StringPool.BLANK, 0, DropboxEntryType.FOLDER);

			return new DropboxFolder(dropboxEntry);
		}
		catch (DbxException de) {
			throw new PortalException(de);
		}
	}

	@Override
	public ExtRepositoryFileVersion cancelCheckOut(
			String extRepositoryFileEntryKey)
		throws PortalException {

		throw new UnsupportedOperationException(
			"Cancel check-out is not supported for Dropbox repositories");
	}

	@Override
	public void checkInExtRepositoryFileEntry(
			String extRepositoryFileEntryKey, boolean createMajorVersion,
			String changeLog)
		throws PortalException {

		throw new UnsupportedOperationException(
			"Check-in is not supported for Dropbox repositories");
	}

	@Override
	public ExtRepositoryFileEntry checkOutExtRepositoryFileEntry(
			String extRepositoryFileEntryKey)
		throws PortalException {

		throw new UnsupportedOperationException(
			"Check-out is not supported for Dropbox repositories");
	}

	@Override
	public <T extends ExtRepositoryObject> T copyExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFileEntryKey, String newExtRepositoryFolderKey,
			String newTitle)
		throws PortalException {

		try {
			DbxClient dbxClient = getDbxClient();

			String dropboxPath = getDropboxPath(
				newExtRepositoryFolderKey, newTitle);

			DbxEntry dbxEntry = dbxClient.copy(
				extRepositoryFileEntryKey, dropboxPath);

			DropboxEntry dropboxEntry =
				_dropboxEntryLocalService.copyDropboxEntry(
					getCompanyId(), getGroupId(), getRepositoryId(),
					getUserId(), extRepositoryFileEntryKey, dropboxPath,
					getRev(dbxEntry));

			return (T)createExtRepositoryObject(dropboxEntry);
		}
		catch (DbxException de) {
			throw new PortalException(de);
		}
	}

	@Override
	public void deleteExtRepositoryObject(
			ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			String extRepositoryObjectKey)
		throws PortalException {

		try {
			DbxClient dbxClient = getDbxClient();

			dbxClient.delete(extRepositoryObjectKey);

			_dropboxEntryLocalService.deleteDropboxEntry(
				getRepositoryId(), extRepositoryObjectKey);
		}
		catch (DbxException de) {
			throw new PortalException(de);
		}
	}

	@Override
	public InputStream getContentStream(
			ExtRepositoryFileEntry extRepositoryFileEntry)
		throws PortalException {

		DropboxFileEntry dropboxFileEntry =
			(DropboxFileEntry)extRepositoryFileEntry;

		return getContentStream(getDbxFile(extRepositoryFileEntry));
	}

	@Override
	public InputStream getContentStream(
			ExtRepositoryFileVersion extRepositoryFileVersion)
		throws PortalException {

		return getContentStream(getDbxFile(extRepositoryFileVersion));
	}

	@Override
	public ExtRepositoryFileVersion getExtRepositoryFileVersion(
			ExtRepositoryFileEntry extRepositoryFileEntry, String version)
		throws PortalException {

		DropboxEntry dropboxEntry = _dropboxEntryLocalService.getDropboxEntry(
			getRepositoryId(),
			extRepositoryFileEntry.getExtRepositoryModelKey());

		DropboxRevision dropboxRevision =
			_dropboxRevisionLocalService.getDropboxRevision(
				getRepositoryId(), dropboxEntry.getEntryId(), version);

		return new DropboxFileVersion(dropboxRevision);
	}

	@Override
	public ExtRepositoryFileVersionDescriptor
		getExtRepositoryFileVersionDescriptor(
			String extRepositoryFileVersionKey) {

		int i = extRepositoryFileVersionKey.lastIndexOf(CharPool.AT);

		if (i == -1) {
			throw new IllegalArgumentException(
				"Dropbox repository version keys must be of the form " +
					"path@rev: " + extRepositoryFileVersionKey);
		}

		return new ExtRepositoryFileVersionDescriptor(
			extRepositoryFileVersionKey.substring(0, i),
			extRepositoryFileVersionKey.substring(i + 1));
	}

	@Override
	public List<ExtRepositoryFileVersion> getExtRepositoryFileVersions(
			ExtRepositoryFileEntry extRepositoryFileEntry)
		throws PortalException {

		DropboxEntry dropboxEntry = _dropboxEntryLocalService.getDropboxEntry(
			getRepositoryId(),
			extRepositoryFileEntry.getExtRepositoryModelKey());

		List<DropboxRevision> dropboxRevisions =
			_dropboxRevisionLocalService.getDropboxRevisions(
				getRepositoryId(), dropboxEntry.getEntryId());

		List<ExtRepositoryFileVersion> extRepositoryFileVersions =
			new ArrayList<>(dropboxRevisions.size());

		for (DropboxRevision dropboxRevision : dropboxRevisions) {
			extRepositoryFileVersions.add(
				new DropboxFileVersion(dropboxRevision));
		}

		return extRepositoryFileVersions;
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryObjectKey)
		throws PortalException {

		DropboxEntry dropboxEntry = _dropboxEntryLocalService.getDropboxEntry(
			getRepositoryId(), extRepositoryObjectKey);

		return (T)createExtRepositoryObject(dropboxEntry);
	}

	@Override
	public <T extends ExtRepositoryObject> T getExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFolderKey, String title)
		throws PortalException {

		return getExtRepositoryObject(
			extRepositoryObjectType,
			getDropboxPath(extRepositoryFolderKey, title));
	}

	@Override
	public <T extends ExtRepositoryObject> List<T> getExtRepositoryObjects(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryFolderKey)
		throws PortalException {

		List<DropboxEntry> dropboxEntries =
			_dropboxEntryLocalService.getDropboxEntries(
				getRepositoryId(), extRepositoryFolderKey,
				getDropboxEntryType(extRepositoryObjectType));

		List<T> extRepositoryObjects = new ArrayList<>();

		for (DropboxEntry dropboxEntry : dropboxEntries) {
			extRepositoryObjects.add(
				(T)createExtRepositoryObject(dropboxEntry));
		}

		return extRepositoryObjects;
	}

	@Override
	public int getExtRepositoryObjectsCount(
			ExtRepositoryObjectType<? extends ExtRepositoryObject>
				extRepositoryObjectType,
			String extRepositoryFolderKey)
		throws PortalException {

		return _dropboxEntryLocalService.getDropboxEntriesCount(
			getRepositoryId(), extRepositoryFolderKey,
			getDropboxEntryType(extRepositoryObjectType));
	}

	@Override
	public ExtRepositoryFolder getExtRepositoryParentFolder(
			ExtRepositoryObject extRepositoryObject)
		throws PortalException {

		String extRepositoryModelKey =
			extRepositoryObject.getExtRepositoryModelKey();

		if (extRepositoryModelKey.equals(StringPool.SLASH)) {
			return null;
		}

		if (extRepositoryModelKey.lastIndexOf(CharPool.SLASH) == 0) {
			return DropboxFolder.ROOT;
		}

		DropboxEntry dropboxEntry = _dropboxEntryLocalService.getDropboxEntry(
			getRepositoryId(), extRepositoryModelKey);

		return new DropboxFolder(
			_dropboxEntryLocalService.getDropboxEntry(
				getRepositoryId(), dropboxEntry.getParentPath()));
	}

	@Override
	public String getRootFolderKey() throws PortalException {
		return StringPool.SLASH;
	}

	@Override
	public List<String> getSubfolderKeys(
			String extRepositoryFolderKey, boolean recurse)
		throws PortalException {

		try {
			DbxClient dbxClient = getDbxClient();

			List<String> subfolderKeys = new ArrayList<>();

			collectSubfolderKeys(
				extRepositoryFolderKey, dbxClient, subfolderKeys, recurse);

			return subfolderKeys;
		}
		catch (DbxException de) {
			throw new PortalException(de);
		}
	}

	@Override
	public void initRepository(
			UnicodeProperties typeSettingsProperties,
			CredentialsProvider credentialsProvider)
		throws PortalException {
	}

	@Override
	public <T extends ExtRepositoryObject> T moveExtRepositoryObject(
			ExtRepositoryObjectType<T> extRepositoryObjectType,
			String extRepositoryObjectKey, String newExtRepositoryFolderKey,
			String newTitle)
		throws PortalException {

		try {
			DbxClient dbxClient = getDbxClient();

			String dropboxPath = getDropboxPath(
				newExtRepositoryFolderKey, newTitle);

			DbxEntry entry = dbxClient.move(
				extRepositoryObjectKey, dropboxPath);

			DropboxEntry dropboxEntry =
				_dropboxEntryLocalService.moveDropboxEntry(
					getCompanyId(), getGroupId(), getRepositoryId(),
					getUserId(), extRepositoryObjectKey, dropboxPath,
					getRev(entry));

			return (T)createExtRepositoryObject(dropboxEntry);
		}
		catch (DbxException de) {
			throw new PortalException(de);
		}
	}

	@Override
	public List<ExtRepositorySearchResult<?>> search(
			SearchContext searchContext, Query query,
			ExtRepositoryQueryMapper extRepositoryQueryMapper)
		throws PortalException {

		throw new UnsupportedOperationException(
			"Search is not supported in Dropbox repositories");
	}

	@Override
	public ExtRepositoryFileEntry updateExtRepositoryFileEntry(
			String extRepositoryFileEntryKey, String mimeType,
			InputStream inputStream)
		throws PortalException {

		File tempFile = null;
		InputStream is = null;

		try {
			DbxClient dbxClient = getDbxClient();

			DbxEntry metadata = dbxClient.getMetadata(
				extRepositoryFileEntryKey);

			DbxEntry.File fileMetadata = metadata.asFile();

			tempFile = FileUtil.createTempFile(inputStream);

			is = new FileInputStream(tempFile);

			DbxEntry.File file = dbxClient.uploadFile(
				extRepositoryFileEntryKey,
				DbxWriteMode.update(fileMetadata.rev), tempFile.length(), is);

			DropboxEntry dropboxEntry =
				_dropboxEntryLocalService.updateDropboxEntry(
					getCompanyId(), getGroupId(), getRepositoryId(),
					getUserId(), extRepositoryFileEntryKey, file.rev,
					tempFile.length());

			return new DropboxFileEntry(dropboxEntry);
		}
		catch (DbxException | IOException e) {
			throw new PortalException(e);
		}
		finally {
			StreamUtil.close(is);
			FileUtil.delete(tempFile);
		}
	}

	protected void collectSubfolderKeys(
			String path, DbxClient dbxClient, List<String> subfolderKeys,
			boolean recurse)
		throws DbxException {

		DbxEntry.WithChildren metadataWithChildren =
			dbxClient.getMetadataWithChildren(path);

		for (DbxEntry dbxEntry : metadataWithChildren.children) {
			if (dbxEntry.isFolder()) {
				subfolderKeys.add(dbxEntry.path);

				if (recurse) {
					collectSubfolderKeys(
						dbxEntry.path, dbxClient, subfolderKeys, recurse);
				}
			}
		}
	}

	protected ExtRepositoryObject createExtRepositoryObject(
		DropboxEntry dropboxEntry) {

		DropboxEntryType dropboxEntryType = DropboxEntryType.fromType(
			dropboxEntry.getType());

		if (dropboxEntryType == DropboxEntryType.FILE) {
			return new DropboxFileEntry(dropboxEntry);
		}

		if (dropboxEntryType == DropboxEntryType.FOLDER) {
			return new DropboxFolder(dropboxEntry);
		}

		throw new IllegalArgumentException(
			"Expected file or folder, got " + dropboxEntryType);
	}

	protected String escapePathComponent(String pathComponent) {
		return pathComponent; // For the moment, we won't escape anything.
	}

	protected InputStream getContentStream(DbxEntry.File dbxFile)
		throws PortalException {

		return getContentStream(dbxFile, dbxFile.rev);
	}

	protected InputStream getContentStream(
			DbxEntry.File dbxFile, String revision)
		throws PortalException {

		DbxClient.Downloader downloader = null;

		try {
			DbxClient dbxClient = getDbxClient();

			downloader = dbxClient.startGetFile(dbxFile.path, revision);

			final File tempFile = FileUtil.createTempFile(downloader.body);

			return new FileInputStream(tempFile) {

				@Override
				public void close() throws IOException {
					super.close();

					FileUtil.delete(tempFile);
				}

			};
		}
		catch (IOException | DbxException e) {
			throw new PortalException(e);
		}
		finally {
			downloader.close();
		}
	}

	protected DbxClient getDbxClient() {
		return _dbxClientFactory.getDbxClient(
			getRepositoryId(), getTypeSettingsProperties());
	}

	protected DbxEntry.File getDbxFile(
			ExtRepositoryFileEntry extRepositoryFileEntry)
		throws PortalException {

		try {
			DbxClient dbxClient = getDbxClient();

			DbxEntry dbxEntry = dbxClient.getMetadata(
				extRepositoryFileEntry.getExtRepositoryModelKey());

			if (!dbxEntry.isFile()) {
				throw new IllegalArgumentException(
					"Expected a dropbox file, got: " +
						extRepositoryFileEntry.getExtRepositoryModelKey());
			}

			return dbxEntry.asFile();
		}
		catch (DbxException de) {
			throw new PortalException(de);
		}
	}

	protected DbxEntry.File getDbxFile(
			ExtRepositoryFileVersion extRepositoryFileVersion)
		throws PortalException {

		try {
			DropboxFileVersion dropboxFileVersion =
				(DropboxFileVersion)extRepositoryFileVersion;

			DropboxRevision dropboxRevision =
				dropboxFileVersion.getDropboxRevision();

			DbxClient dbxClient = getDbxClient();

			DbxEntry dbxEntry = dbxClient.getMetadata(
				dropboxRevision.getPath());

			if (!dbxEntry.isFile()) {
				throw new IllegalArgumentException(
					"Expected dropbox file, got: " + dbxEntry);
			}

			return dbxEntry.asFile();
		}
		catch (DbxException de) {
			throw new PortalException(de);
		}
	}

	protected <T extends ExtRepositoryModel> DropboxEntryType
		getDropboxEntryType(
			ExtRepositoryObjectType<T> extRepositoryObjectType) {

		if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
			return DropboxEntryType.FILE;
		}

		if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
			return DropboxEntryType.FOLDER;
		}

		return DropboxEntryType.ANY;
	}

	protected String getDropboxParentPath(DbxEntry dbxEntry) {
		if (dbxEntry.path.equals(StringPool.SLASH)) {
			return null;
		}

		int i = dbxEntry.path.lastIndexOf(CharPool.SLASH);

		if (i == 0) {
			return StringPool.SLASH;
		}

		return dbxEntry.path.substring(0, i);
	}

	protected String getDropboxPath(
		String extRepositoryParentFolderKey, String extRepositoryObjectName) {

		String[] parentPathComponents = StringUtil.split(
			extRepositoryParentFolderKey, StringPool.SLASH);

		String[] extRepositoryObjectPathComponents =
			new String[parentPathComponents.length + 1];

		int i = 0;

		for (i = 0; i < parentPathComponents.length; i++) {
			extRepositoryObjectPathComponents[i] = escapePathComponent(
				parentPathComponents[i]);
		}

		extRepositoryObjectPathComponents[i] = escapePathComponent(
			extRepositoryObjectName);

		String path = StringUtil.merge(
			extRepositoryObjectPathComponents, StringPool.SLASH);

		if (!path.startsWith(StringPool.SLASH)) {
			path = StringPool.SLASH + path;
		}

		return path;
	}

	protected String getRev(DbxEntry dbxEntry) {
		if (dbxEntry.isFolder()) {
			return null;
		}

		DbxEntry.File dbxFile = dbxEntry.asFile();

		return dbxFile.rev;
	}

	protected long getUserId() {
		return PrincipalThreadLocal.getUserId();
	}

	protected boolean isOfType(
		DbxEntry dbxEntry, ExtRepositoryObjectType extRepositoryObjectType) {

		if (extRepositoryObjectType == ExtRepositoryObjectType.FILE) {
			return dbxEntry.isFile();
		}

		if (extRepositoryObjectType == ExtRepositoryObjectType.FOLDER) {
			return dbxEntry.isFolder();
		}

		return true;
	}

	private final DbxClientFactory _dbxClientFactory;
	private final DropboxEntryLocalService _dropboxEntryLocalService;
	private final DropboxRevisionLocalService _dropboxRevisionLocalService;

}