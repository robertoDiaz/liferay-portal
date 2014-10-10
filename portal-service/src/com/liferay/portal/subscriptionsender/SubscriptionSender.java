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

package com.liferay.portal.subscriptionsender;

import com.liferay.portal.kernel.mail.SMTPAccount;
import com.liferay.portal.kernel.util.EscapableObject;
import com.liferay.portal.service.ServiceContext;

import java.io.File;
import java.io.Serializable;

import java.util.Locale;
import java.util.Map;

/**
 * @author Roberto Díaz
 */

public interface SubscriptionSender extends Serializable {

	public void addFileAttachment(File file);

	public void addFileAttachment(File file, String fileName);

	public void addPersistedSubscribers(String className, long classPK);

	public void addRuntimeSubscribers(String toAddress, String toName);

	public void flushNotifications() throws Exception;

	public void flushNotificationsAsync();

	public String getClassName();

	public Object getContextAttribute(String key);

	public String getMailId();

	public void initialize() throws Exception;

	public void setBody(String body);

	public void setBulk(boolean bulk);

	public void setClassName(String className);

	public void setClassPK(long classPK);

	public void setCompanyId(long companyId);

	public void setContextAttribute(String key, EscapableObject<String> value);

	public void setContextAttribute(String key, Object value);

	public void setContextAttribute(String key, Object value, boolean escape);

	public void setContextAttributes(Object... values);

	public void setContextUserPrefix(String contextUserPrefix);

	public void setEntryTitle(String entryTitle);

	public void setEntryURL(String entryURL);

	public void setFrom(String fromAddress, String fromName);

	public void setGroupId(long groupId);

	public void setHtmlFormat(boolean htmlFormat);

	public void setInReplyTo(String inReplyTo);

	public void setLocalizedBodyMap(Map<Locale, String> localizedBodyMap);

	public void setLocalizedSubjectMap(Map<Locale, String> localizedSubjectMap);

	public void setMailId(String popPortletPrefix, Object... ids);

	public void setNotificationClassNameId(long notificationClassNameId);

	public void setNotificationType(int notificationType);

	public void setPortletId(String portletId);

	public void setReplyToAddress(String replyToAddress);

	public void setScopeGroupId(long scopeGroupId);

	public void setServiceContext(ServiceContext serviceContext);

	public void setSMTPAccount(SMTPAccount smtpAccount);

	public void setSubject(String subject);

	public void setUniqueMailId(boolean uniqueMailId);

	public void setUserId(long userId);

}