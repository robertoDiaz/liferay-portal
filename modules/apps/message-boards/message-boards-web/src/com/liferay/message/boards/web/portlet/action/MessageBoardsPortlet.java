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

package com.liferay.message.boards.web.portlet.action;

import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.upload.LiferayFileItemException;
import com.liferay.portal.kernel.upload.UploadException;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.TrashedModel;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.ActionResponseImpl;
import com.liferay.portlet.documentlibrary.FileSizeException;
import com.liferay.portlet.messageboards.MBGroupServiceSettings;
import com.liferay.portlet.messageboards.model.MBBan;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBThread;
import com.liferay.portlet.messageboards.service.MBBanServiceUtil;
import com.liferay.portlet.messageboards.service.MBMessageServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBThreadServiceUtil;
import com.liferay.portlet.messageboards.service.permission.MBMessagePermission;
import com.liferay.portlet.trash.util.TrashUtil;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletURL;

import org.osgi.service.component.annotations.Component;

/**
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.css-class-wrapper=portlet-message-boards",
		"com.liferay.portlet.facebook-integration=fbml",
		"com.liferay.portlet.footer-portlet-javascript=/message_boards/js/main.js",
		"com.liferay.portlet.friendly-url-mapping=message-boards",
		"com.liferay.portlet.friendly-url-routes=com/liferay/portlet/messageboards/message-boards-friendly-url-routes.xml",
		"com.liferay.portlet.header-portlet-css=/message_boards/css/main.css",
		"com.liferay.portlet.icon=/icons/message_boards.png",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.preferences-unique-per-layout=false",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.scopeable=true",
		"com.liferay.portlet.use-default-template=false",
		"javax.portlet.display-name=Message Boards",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/message_boards/",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supported-public-render-parameter=tag",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class MessageBoardsPortlet extends MVCPortlet {

	public void banUser(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long banUserId = ParamUtil.getLong(actionRequest, "banUserId");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			MBBan.class.getName(), actionRequest);

		MBBanServiceUtil.addBan(banUserId, serviceContext);
	}

	public void deleteMessage(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long messageId = ParamUtil.getLong(actionRequest, "messageId");

		MBMessageServiceUtil.deleteMessage(messageId);
	}

	public void lockThreads(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long threadId = ParamUtil.getLong(actionRequest, "threadId");

		if (threadId > 0) {
			MBThreadServiceUtil.lockThread(threadId);
		}
		else {
			long[] threadIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "threadIds"), 0L);

			for (int i = 0; i < threadIds.length; i++) {
				MBThreadServiceUtil.lockThread(threadIds[i]);
			}
		}
	}

	public void subscribeMessage(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long messageId = ParamUtil.getLong(actionRequest, "messageId");

		MBMessageServiceUtil.subscribeMessage(messageId);
	}

	public void unbanUser(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long banUserId = ParamUtil.getLong(actionRequest, "banUserId");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			MBBan.class.getName(), actionRequest);

		MBBanServiceUtil.deleteBan(banUserId, serviceContext);
	}

	public void unlockThreads(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long threadId = ParamUtil.getLong(actionRequest, "threadId");

		if (threadId > 0) {
			MBThreadServiceUtil.unlockThread(threadId);
		}
		else {
			long[] threadIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "threadIds"), 0L);

			for (int i = 0; i < threadIds.length; i++) {
				MBThreadServiceUtil.unlockThread(threadIds[i]);
			}
		}
	}

	public void unsubscribeMessage(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		long messageId = ParamUtil.getLong(actionRequest, "messageId");

		MBMessageServiceUtil.unsubscribeMessage(messageId);
	}

	public MBMessage updateMessage(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		UploadException uploadException =
			(UploadException)actionRequest.getAttribute(
				WebKeys.UPLOAD_EXCEPTION);

		if (uploadException != null) {
			if (uploadException.isExceededLiferayFileItemSizeLimit()) {
				throw new LiferayFileItemException();
			}
			else if (uploadException.isExceededSizeLimit()) {
				throw new FileSizeException(uploadException.getCause());
			}

			throw new PortalException(uploadException.getCause());
		}

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long messageId = ParamUtil.getLong(actionRequest, "messageId");

		long groupId = themeDisplay.getScopeGroupId();
		long categoryId = ParamUtil.getLong(actionRequest, "mbCategoryId");
		long threadId = ParamUtil.getLong(actionRequest, "threadId");
		long parentMessageId = ParamUtil.getLong(
			actionRequest, "parentMessageId");
		String subject = ParamUtil.getString(actionRequest, "subject");
		String body = ParamUtil.getString(actionRequest, "body");

		MBGroupServiceSettings mbGroupServiceSettings =
			MBGroupServiceSettings.getInstance(groupId);

		List<ObjectValuePair<String, InputStream>> inputStreamOVPs =
			new ArrayList<>(5);

		try {
			UploadPortletRequest uploadPortletRequest =
				PortalUtil.getUploadPortletRequest(actionRequest);

			for (int i = 1; i <= 5; i++) {
				String fileName = uploadPortletRequest.getFileName(
					"msgFile" + i);
				InputStream inputStream = uploadPortletRequest.getFileAsStream(
					"msgFile" + i);

				if ((inputStream == null) || Validator.isNull(fileName)) {
					continue;
				}

				ObjectValuePair<String, InputStream> inputStreamOVP =
					new ObjectValuePair<>(fileName, inputStream);

				inputStreamOVPs.add(inputStreamOVP);
			}

			boolean question = ParamUtil.getBoolean(actionRequest, "question");
			boolean anonymous = ParamUtil.getBoolean(
				actionRequest, "anonymous");
			double priority = ParamUtil.getDouble(actionRequest, "priority");
			boolean allowPingbacks = ParamUtil.getBoolean(
				actionRequest, "allowPingbacks");

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				MBMessage.class.getName(), actionRequest);

			boolean preview = ParamUtil.getBoolean(actionRequest, "preview");

			serviceContext.setAttribute("preview", preview);

			MBMessage message = null;

			if (messageId <= 0) {
				if (PropsValues.
						CAPTCHA_CHECK_PORTLET_MESSAGE_BOARDS_EDIT_MESSAGE) {

					CaptchaUtil.check(actionRequest);
				}

				if (threadId <= 0) {

					// Post new thread

					message = MBMessageServiceUtil.addMessage(
						groupId, categoryId, subject, body,
						mbGroupServiceSettings.getMessageFormat(),
						inputStreamOVPs, anonymous, priority, allowPingbacks,
						serviceContext);

					if (question) {
						MBThreadLocalServiceUtil.updateQuestion(
							message.getThreadId(), true);
					}
				}
				else {

					// Post reply

					message = MBMessageServiceUtil.addMessage(
						parentMessageId, subject, body,
						mbGroupServiceSettings.getMessageFormat(),
						inputStreamOVPs, anonymous, priority, allowPingbacks,
						serviceContext);
				}
			}
			else {
				List<String> existingFiles = new ArrayList<>();

				for (int i = 1; i <= 5; i++) {
					String path = ParamUtil.getString(
						actionRequest, "existingPath" + i);

					if (Validator.isNotNull(path)) {
						existingFiles.add(path);
					}
				}

				// Update message

				message = MBMessageServiceUtil.updateMessage(
					messageId, subject, body, inputStreamOVPs, existingFiles,
					priority, allowPingbacks, serviceContext);

				if (message.isRoot()) {
					MBThreadLocalServiceUtil.updateQuestion(
						message.getThreadId(), question);
				}
			}

			PermissionChecker permissionChecker =
				themeDisplay.getPermissionChecker();

			boolean subscribe = ParamUtil.getBoolean(
				actionRequest, "subscribe");

			if (!preview && subscribe &&
				MBMessagePermission.contains(
					permissionChecker, message, ActionKeys.SUBSCRIBE)) {

				MBMessageServiceUtil.subscribeMessage(message.getMessageId());
			}

			return message;
		}
		finally {
			for (ObjectValuePair<String, InputStream> inputStreamOVP :
					inputStreamOVPs) {

				InputStream inputStream = inputStreamOVP.getValue();

				StreamUtil.cleanUp(inputStream);
			}
		}
	}

	protected void deleteThreads(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteThreads(actionRequest, false);
	}

	protected void deleteThreads(
			ActionRequest actionRequest, boolean moveToTrash)
		throws PortalException {

		long[] deleteThreadIds = null;

		long threadId = ParamUtil.getLong(actionRequest, "threadId");

		if (threadId > 0) {
			deleteThreadIds = new long[] {threadId};
		}
		else {
			deleteThreadIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "threadIds"), 0L);
		}

		List<TrashedModel> trashedModels = new ArrayList<>();

		for (long deleteThreadId : deleteThreadIds) {
			if (moveToTrash) {
				MBThread thread = MBThreadServiceUtil.moveThreadToTrash(
					deleteThreadId);

				trashedModels.add(thread);
			}
			else {
				MBThreadServiceUtil.deleteThread(deleteThreadId);
			}
		}

		if (moveToTrash && !trashedModels.isEmpty()) {
			TrashUtil.addTrashSessionMessages(actionRequest, trashedModels);

			hideDefaultSuccessMessage(actionRequest);
		}
	}

	protected String getRedirect(
		ActionRequest actionRequest, ActionResponse actionResponse,
		MBMessage message) {

		if (message == null) {
			String redirect = ParamUtil.getString(actionRequest, "redirect");

			return redirect;
		}

		int workflowAction = ParamUtil.getInteger(
			actionRequest, "workflowAction", WorkflowConstants.ACTION_PUBLISH);

		if (workflowAction == WorkflowConstants.ACTION_SAVE_DRAFT) {
			return getSaveAndContinueRedirect(
				actionRequest, actionResponse, message);
		}
		else if (message == null) {
			return ParamUtil.getString(actionRequest, "redirect");
		}

		ActionResponseImpl actionResponseImpl =
			(ActionResponseImpl)actionResponse;

		PortletURL portletURL = actionResponseImpl.createRenderURL();

		portletURL.setParameter(
			"struts_action", "/message_boards/view_message");
		portletURL.setParameter(
			"messageId", String.valueOf(message.getMessageId()));

		return portletURL.toString();
	}

	protected String getSaveAndContinueRedirect(
		ActionRequest actionRequest, ActionResponse actionResponse,
		MBMessage message) {

		String redirect = ParamUtil.getString(actionRequest, "redirect");

		boolean preview = ParamUtil.getBoolean(actionRequest, "preview");

		PortletURL portletURL =
			((ActionResponseImpl)actionResponse).createRenderURL();

		portletURL.setParameter("mvcPath", "edit_message.jsp");
		portletURL.setParameter("redirect", redirect);
		portletURL.setParameter(
			"messageId", String.valueOf(message.getMessageId()));
		portletURL.setParameter("preview", String.valueOf(preview));

		return portletURL.toString();
	}

	protected void moveThreadsToTrash(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		deleteThreads(actionRequest, true);
	}

}