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

package com.liferay.portlet.documentlibrary.subscriptionsender;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.notifications.UserNotificationDefinition;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.subscriptionsender.BaseSubscriptionSender;
import com.liferay.portal.subscriptionsender.SubscriptionSenderConstants;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.documentlibrary.DLSettings;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFileEntryConstants;
import com.liferay.portlet.documentlibrary.model.DLFileEntryType;
import com.liferay.portlet.documentlibrary.model.DLFileEntryTypeConstants;
import com.liferay.portlet.documentlibrary.model.DLFileVersion;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLFileEntryTypeLocalServiceUtil;
import com.liferay.portlet.documentlibrary.util.DLConstants;

import java.util.Locale;
import java.util.Map;

/**
 * @author Roberto Díaz
 */
public class DLSubscriptionSender extends BaseSubscriptionSender {

	@Override
	public String getServiceName() {
		return DLConstants.SERVICE_NAME;
	}

	@Override
	public void notify(BaseModel baseModel, String entryURL)
		throws PortalException {

		if (!fileVersion.isApproved() || Validator.isNull(entryURL)) {
			return;
		}

		DLSettings dlSettings = DLSettings.getInstance(
			fileVersion.getGroupId());

		if (serviceContext.isCommandAdd() &&
			dlSettings.isEmailFileEntryAddedEnabled()) {
		}
		else if (serviceContext.isCommandUpdate() &&
			dlSettings.isEmailFileEntryUpdatedEnabled()) {
		}
		else {
			return;
		}

		String entryTitle = fileVersion.getTitle();

		String fromName = dlSettings.getEmailFromName();
		String fromAddress = dlSettings.getEmailFromAddress();

		Map<Locale, String> localizedSubjectMap = null;
		Map<Locale, String> localizedBodyMap = null;

		if (serviceContext.isCommandUpdate()) {
			localizedSubjectMap = dlSettings.getEmailFileEntryUpdatedSubject();
			localizedBodyMap = dlSettings.getEmailFileEntryUpdatedBody();
		}
		else {
			localizedSubjectMap = dlSettings.getEmailFileEntryAddedSubject();
			localizedBodyMap = dlSettings.getEmailFileEntryAddedBody();
		}

		FileEntry fileEntry = fileVersion.getFileEntry();

		Folder folder = null;

		long folderId = fileEntry.getFolderId();

		if (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			folder = dlAppLocalService.getFolder(folderId);
		}

		String folderName = LanguageUtil.get(
			serviceContext.getLocale(), "home");

		if (folder != null) {
			folderName = folder.getName();
		}

		SubscriptionSender subscriptionSender = new SubscriptionSender();

		DLFileEntry dlFileEntry = (DLFileEntry)fileEntry.getModel();

		DLFileEntryType dlFileEntryType =
			dlFileEntryTypeLocalService.getDLFileEntryType(
				dlFileEntry.getFileEntryTypeId());

		subscriptionSender.setClassPK(fileVersion.getFileEntryId());
		subscriptionSender.setClassName(DLFileEntryConstants.getClassName());
		subscriptionSender.setCompanyId(fileVersion.getCompanyId());
		subscriptionSender.setContextAttributes(
			"[$DOCUMENT_STATUS_BY_USER_NAME$]",
			fileVersion.getStatusByUserName(), "[$DOCUMENT_TITLE$]", entryTitle,
			"[$DOCUMENT_TYPE$]",
			dlFileEntryType.getName(serviceContext.getLocale()),
			"[$DOCUMENT_URL$]", entryURL, "[$FOLDER_NAME$]", folderName);
		subscriptionSender.setContextUserPrefix("DOCUMENT");
		subscriptionSender.setEntryTitle(entryTitle);
		subscriptionSender.setEntryURL(entryURL);
		subscriptionSender.setFrom(fromAddress, fromName);
		subscriptionSender.setHtmlFormat(true);
		subscriptionSender.setLocalizedBodyMap(localizedBodyMap);
		subscriptionSender.setLocalizedSubjectMap(localizedSubjectMap);
		subscriptionSender.setMailId(
			"file_entry", fileVersion.getFileEntryId());

		int notificationType =
			UserNotificationDefinition.NOTIFICATION_TYPE_ADD_ENTRY;

		if (serviceContext.isCommandUpdate()) {
			notificationType =
				UserNotificationDefinition.NOTIFICATION_TYPE_UPDATE_ENTRY;
		}

		subscriptionSender.setNotificationType(notificationType);

		subscriptionSender.setPortletId(PortletKeys.DOCUMENT_LIBRARY);
		subscriptionSender.setReplyToAddress(fromAddress);
		subscriptionSender.setScopeGroupId(fileVersion.getGroupId());
		subscriptionSender.setServiceContext(serviceContext);
		subscriptionSender.setUserId(fileVersion.getUserId());

		subscriptionSender.addPersistedSubscribers(
			Folder.class.getName(), fileVersion.getGroupId());

		if (folder != null) {
			subscriptionSender.addPersistedSubscribers(
				Folder.class.getName(), folder.getFolderId());

			for (Long ancestorFolderId : folder.getAncestorFolderIds()) {
				subscriptionSender.addPersistedSubscribers(
					Folder.class.getName(), ancestorFolderId);
			}
		}

		if (dlFileEntryType.getFileEntryTypeId() ==
			DLFileEntryTypeConstants.FILE_ENTRY_TYPE_ID_BASIC_DOCUMENT) {

			subscriptionSender.addPersistedSubscribers(
				DLFileEntryType.class.getName(), fileVersion.getGroupId());
		}
		else {
			subscriptionSender.addPersistedSubscribers(
				DLFileEntryType.class.getName(),
				dlFileEntryType.getFileEntryTypeId());
		}

		subscriptionSender.addPersistedSubscribers(
			DLFileEntry.class.getName(), fileEntry.getFileEntryId());

		subscriptionSender.flushNotificationsAsync();
	}

}