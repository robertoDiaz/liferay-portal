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

package com.liferay.liferaygen.document.library.internal;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.liferaygen.BaseLiferayGenAction;
import com.liferay.liferaygen.LiferayGenAction;
import com.liferay.liferaygen.action.config.LiferayGenActionConfig;
import com.liferay.liferaygen.constants.LiferayGenConfigConstants;
import com.liferay.liferaygen.util.LiferayGenConfigHandler;
import com.liferay.liferaygen.util.LiferayGenParameterHandler;
import com.liferay.liferaygen.util.LiferayGenQueryHandler;
import com.liferay.liferaygen.value.generator.LiferayGenValueGenerator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.MimeTypesUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(
	immediate = true,
	properties = "liferaygen.action.class.name=com.liferay.liferaygen.document.library.internal.CreateDLFileEntryLiferayGenAction",
	service = LiferayGenAction.class
)
public class CreateDLFileEntryLiferayGenAction extends BaseLiferayGenAction {

	@Override
	public String doGetDescription() {
		return "Creates a random document inside document library";
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, Object> doGetParametersDefaultValues() {
		return new TreeMap<String, Object>() {
			{
				put("nonDefaultParentFolderRatio", 0L);
			}
		};
	}

	@Override
	@SuppressWarnings("serial")
	public Map<String, String> doGetParametersDescription() {
		return new TreeMap<String, String>() {
			{
				put(
					"nonDefaultParentFolderRatio",
					"Probability percentage of creating a document in a " +
						"random child folder instead of using default parent");
				put(LiferayGenActionConfig.TARGET, "Parent folder");
			}
		};
	}

	@Override
	public Class<? extends ClassedModel> getEntityModel() {
		return DLFolder.class;
	}

	@Override
	public String getEntityProperties() {
		return "folderId";
	}

	@Override
	public String getName() {
		return "CreateDLFileEntryLiferayGenAction";
	}

	@Override
	protected void doRun() {
		Map<String, Object> parameters = getParameters();

		long repositoryId = GetterUtil.getLong(
			parameters.get(LiferayGenConfigConstants.GROUP_ID));
		int nonDefaultParentFolderRatio =
			_liferayGenParameterHandler.getParamAsIntegerPercentage(
				parameters, "nonDefaultParentFolderRatio");

		long parentFolderId = DLFolderConstants.DEFAULT_PARENT_FOLDER_ID;

		LiferayGenValueGenerator liferayGenValueGenerator =
			new LiferayGenValueGenerator(
				_companyLocalService, _liferayGenQueryHandler, _portal,
				_portletLocalService);

		if ((nonDefaultParentFolderRatio > 0) &&
			liferayGenValueGenerator.getBoolean(nonDefaultParentFolderRatio)) {

			parentFolderId = MapUtil.getLong(
				parameters, LiferayGenActionConfig.TARGET,
				DLFolderConstants.DEFAULT_PARENT_FOLDER_ID);
		}

		String fileExtension =
			liferayGenValueGenerator.getRandomFileExtension();

		int fileNameLength = liferayGenValueGenerator.getRandomIntegerFromRange(
			10, 20);

		String fileName = liferayGenValueGenerator.getLowerCaseWord(
			fileNameLength);

		String sourceFileName = fileName + StringPool.PERIOD + fileExtension;

		String mimeType = MimeTypesUtil.getContentType(sourceFileName);

		String title = liferayGenValueGenerator.getLowerCaseWord(
			liferayGenValueGenerator.getRandomIntegerFromRange(12, 20));
		String description = liferayGenValueGenerator.getLowerCaseText(
			liferayGenValueGenerator.getRandomIntegerFromRange(20, 65));
		String changeLog = liferayGenValueGenerator.getLowerCaseText(
			liferayGenValueGenerator.getRandomIntegerFromRange(20, 65));

		byte[] bytes = liferayGenValueGenerator.getRandomFileContent(
			sourceFileName);

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		boolean addGuestPermissions = serviceContext.isAddGuestPermissions();
		boolean addGroupPermissions = serviceContext.isAddGroupPermissions();

		try {
			serviceContext.setAddGuestPermissions(true);
			serviceContext.setAddGroupPermissions(true);

			_dlAppLocalService.addFileEntry(
				liferayGenValueGenerator.getRandomUserIdFromCache(),
				repositoryId, parentFolderId, sourceFileName, mimeType, title,
				description, changeLog, bytes, serviceContext);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
		finally {
			serviceContext.setAddGuestPermissions(addGuestPermissions);
			serviceContext.setAddGroupPermissions(addGroupPermissions);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		CreateDLFileEntryLiferayGenAction.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private LiferayGenConfigHandler _liferayGenConfigHandler;

	@Reference
	private LiferayGenParameterHandler _liferayGenParameterHandler;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

}