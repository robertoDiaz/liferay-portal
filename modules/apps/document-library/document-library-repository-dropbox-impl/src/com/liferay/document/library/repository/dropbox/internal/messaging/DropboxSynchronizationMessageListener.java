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

package com.liferay.document.library.repository.dropbox.internal.messaging;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxDelta;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

import com.liferay.document.library.repository.dropbox.constants.DropboxEntryType;
import com.liferay.document.library.repository.dropbox.constants.DropboxRepositoryPortletKeys;
import com.liferay.document.library.repository.dropbox.internal.DbxClientFactory;
import com.liferay.document.library.repository.dropbox.internal.DropboxRepository;
import com.liferay.document.library.repository.dropbox.service.DropboxEntryLocalServiceUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseSchedulerEntryMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.scheduler.SchedulerEntry;
import com.liferay.portal.kernel.scheduler.TimeUnit;
import com.liferay.portal.kernel.scheduler.TriggerFactory;
import com.liferay.portal.kernel.scheduler.TriggerFactoryUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.Repository;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.RepositoryLocalServiceUtil;

import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true,
	property = {"javax.portlet.name=" + DropboxRepositoryPortletKeys.DROPBOX},
	service = SchedulerEntry.class
)
public class DropboxSynchronizationMessageListener
	extends BaseSchedulerEntryMessageListener {

	@Activate
	protected void activate() {
		schedulerEntryImpl.setTrigger(
			TriggerFactoryUtil.createTrigger(
				getEventListenerClass(), getEventListenerClass(),
				_CHECK_INTERVAL, TimeUnit.SECOND));
	}

	@Override
	protected void doReceive(Message message) throws Exception {
		synchronize();
	}

	protected String getCursor(Repository repository) {
		UnicodeProperties typeSettingsProperties =
			repository.getTypeSettingsProperties();

		return typeSettingsProperties.getProperty("DROPBOX_CURSOR");
	}

	protected DropboxEntryType getDropboxEntryType(DbxEntry dbxEntry) {
		if (dbxEntry.isFile()) {
			return DropboxEntryType.FILE;
		}

		else return DropboxEntryType.FOLDER;
	}

	protected List<Repository> getDropboxRepositories() {
		DynamicQuery dynamicQuery = RepositoryLocalServiceUtil.dynamicQuery();

		long classNameId = ClassNameLocalServiceUtil.getClassNameId(
			DropboxRepository.class);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq("classNameId", classNameId));

		return RepositoryLocalServiceUtil.dynamicQuery(dynamicQuery);
	}

	protected String getRev(DbxEntry dbxEntry) {
		if (dbxEntry.isFolder()) {
			return null;
		}

		DbxEntry.File dbxFile = dbxEntry.asFile();

		return dbxFile.rev;
	}

	protected long getSize(DbxEntry dbxEntry) {
		if (dbxEntry.isFolder()) {
			return 0;
		}

		DbxEntry.File dbxFile = dbxEntry.asFile();

		return dbxFile.numBytes;
	}

	protected void saveCursor(Repository repository, String cursor) {
		UnicodeProperties typeSettingsProperties =
			repository.getTypeSettingsProperties();

		typeSettingsProperties.put("DROPBOX_CURSOR", cursor);

		RepositoryLocalServiceUtil.updateRepository(repository);
	}

	@Reference(
		target = "(javax.portlet.name=" + DropboxRepositoryPortletKeys.DROPBOX + ")"
	)
	protected void setPortlet(Portlet portlet) {
	}

	@Reference(unbind = "-")
	protected void setTriggerFactory(TriggerFactory triggerFactory) {
	}

	protected void synchronize() {
		List<Repository> repositories = getDropboxRepositories();

		for (Repository repository : repositories) {
			try {
				synchronizeRepository(repository);
			}
			catch (DbxException | PortalException e) {
				_log.error("Failed to synchronize repository", e);
			}
		}
	}

	protected void synchronizeRepository(Repository repository)
		throws DbxException, PortalException {

		DbxClientFactory dbxClientFactory =
			DbxClientFactory.getDbxClientFactory();

		DbxClient dbxClient = dbxClientFactory.getDbxClient(
			repository.getRepositoryId(),
			repository.getTypeSettingsProperties());

		String cursor = getCursor(repository);

		DbxDelta<DbxEntry> delta = null;

		do {
			delta = dbxClient.getDelta(cursor);

			for (DbxDelta.Entry<DbxEntry> deltaEntry : delta.entries) {
				if (deltaEntry.metadata == null) {
					DropboxEntryLocalServiceUtil.deleteDropboxEntry(
						repository.getRepositoryId(), deltaEntry.lcPath);
				}
				else {
					DropboxEntryLocalServiceUtil.synchronizeDropboxEntry(
						repository.getCompanyId(), repository.getGroupId(),
						repository.getRepositoryId(), repository.getUserId(),
						deltaEntry.metadata.path, getRev(deltaEntry.metadata),
						deltaEntry.metadata.name, getSize(deltaEntry.metadata),
						getDropboxEntryType(deltaEntry.metadata));
				}
			}

			cursor = delta.cursor;
		} while (delta.hasMore);

		saveCursor(repository, cursor);
	}

	private static final int _CHECK_INTERVAL = 30;

	private static final Log _log = LogFactoryUtil.getLog(
		DropboxSynchronizationMessageListener.class);

}