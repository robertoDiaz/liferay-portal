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
import com.liferay.portal.security.permission.PermissionChecker;
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
	public Boolean contains(
		PermissionChecker permissionChecker, String className, long classPK,
		String actionId) throws PortalException {

		return true;
	}

	@Override
	public String getServiceName() {
		return DLConstants.SERVICE_NAME;
	}

	@Override
	public void notify(BaseModel baseModel, String entryURL)
		throws PortalException {

		DLFileVersion dlFileVersion = (DLFileVersion)baseModel;

		if (!dlFileVersion.isApproved() || Validator.isNull(entryURL)) {
			return;
		}

		DLFileEntry dlFileEntry = dlFileVersion.getFileEntry();

		Folder folder = null;

		long folderId = dlFileEntry.getFolderId();

		DLSettings dlSettings = DLSettings.getInstance(
			dlFileVersion.getGroupId());

		command = (String)subscriptionSenderContext.get(
			SubscriptionSenderConstants.SUBSCRIPTION_SENDER_COMMAND);

		if (!isSubmisionCommandEnabled(
				dlSettings.isEmailFileEntryAddedEnabled(),
				dlSettings.isEmailFileEntryUpdatedEnabled())) {

			return;
		}

		String entryTitle = dlFileEntry.getTitle();

		String fromName = dlSettings.getEmailFromName();
		String fromAddress = dlSettings.getEmailFromAddress();

		Map<Locale, String> localizedSubjectMap = null;
		Map<Locale, String> localizedBodyMap = null;

		if (isCommandUpdate()) {
			localizedSubjectMap = dlSettings.getEmailFileEntryUpdatedSubject();
			localizedBodyMap = dlSettings.getEmailFileEntryUpdatedBody();
		}
		else {
			localizedSubjectMap = dlSettings.getEmailFileEntryAddedSubject();
			localizedBodyMap = dlSettings.getEmailFileEntryAddedBody();
		}

		if (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
			folder = DLAppLocalServiceUtil.getFolder(folderId);
		}

		Locale locale = (Locale)subscriptionSenderContext.get(
			SubscriptionSenderConstants.SUBSCRIPTION_SENDER_LOCALE);

		String folderName = LanguageUtil.get(locale, "home");

		if (folder != null) {
			folderName = folder.getName();
		}

		DLFileEntryType dlFileEntryType =
			DLFileEntryTypeLocalServiceUtil.getDLFileEntryType(
				dlFileEntry.getFileEntryTypeId());

		setClassPK(dlFileVersion.getFileEntryId());
		setClassName(DLFileEntryConstants.getClassName());
		setCompanyId(dlFileVersion.getCompanyId());
		setContextAttributes(
			"[$DOCUMENT_STATUS_BY_USER_NAME$]",
			dlFileVersion.getStatusByUserName(), "[$DOCUMENT_TITLE$]",
			entryTitle, "[$DOCUMENT_TYPE$]", dlFileEntryType.getName(locale),
			"[$DOCUMENT_URL$]", entryURL, "[$FOLDER_NAME$]", folderName);
		setContextUserPrefix("DOCUMENT");
		setEntryTitle(dlFileEntry.getTitle());
		setEntryURL(entryURL);
		setFrom(fromAddress, fromName);
		setHtmlFormat(true);
		setLocalizedBodyMap(localizedBodyMap);
		setLocalizedSubjectMap(localizedSubjectMap);
		setMailId("file_entry", dlFileVersion.getFileEntryId());

		int notificationType =
			UserNotificationDefinition.NOTIFICATION_TYPE_ADD_ENTRY;

		if (isCommandUpdate()) {
			notificationType =
				UserNotificationDefinition.NOTIFICATION_TYPE_UPDATE_ENTRY;
		}

		setNotificationType(notificationType);

		setPortletId(PortletKeys.DOCUMENT_LIBRARY);
		setReplyToAddress(fromAddress);
		setScopeGroupId(dlFileVersion.getGroupId());
		setUserId(dlFileVersion.getUserId());

		addPersistedSubscribers(
			Folder.class.getName(), dlFileVersion.getGroupId());

		if (folder != null) {
			addPersistedSubscribers(
				Folder.class.getName(), folder.getFolderId());

			for (Long ancestorFolderId : folder.getAncestorFolderIds()) {
				addPersistedSubscribers(
					Folder.class.getName(), ancestorFolderId);
			}
		}

		if (dlFileEntryType.getFileEntryTypeId() ==
				DLFileEntryTypeConstants.FILE_ENTRY_TYPE_ID_BASIC_DOCUMENT) {

			addPersistedSubscribers(
				DLFileEntryType.class.getName(), dlFileVersion.getGroupId());
		}
		else {
			addPersistedSubscribers(
				DLFileEntryType.class.getName(),
				dlFileEntryType.getFileEntryTypeId());
		}

		addPersistedSubscribers(
			DLFileEntry.class.getName(), dlFileEntry.getFileEntryId());

		flushNotificationsAsync();
	}

}