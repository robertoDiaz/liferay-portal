<%--
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
--%>

<%@ include file="/document_library/init.jsp" %>

<%
String navigation = ParamUtil.getString(request, "navigation", "home");

String currentFolder = ParamUtil.getString(request, "curFolder");
String deltaFolder = ParamUtil.getString(request, "deltaFolder");

long folderId = GetterUtil.getLong((String)request.getAttribute("view.jsp-folderId"));

Folder folder = null;

if (folderId > 0) {
	folder = DLAppServiceUtil.getFolder(folderId);
}

long repositoryId = GetterUtil.getLong((String)request.getAttribute("view.jsp-repositoryId"));

long searchRepositoryId = ParamUtil.getLong(request, "searchRepositoryId");

if (searchRepositoryId == 0) {
	searchRepositoryId = scopeGroupId;
}

long fileEntryTypeId = ParamUtil.getLong(request, "fileEntryTypeId", -1);

String dlFileEntryTypeName = LanguageUtil.get(request, "basic-document");

int status = WorkflowConstants.STATUS_APPROVED;

if (permissionChecker.isContentReviewer(user.getCompanyId(), scopeGroupId)) {
	status = WorkflowConstants.STATUS_ANY;
}

long categoryId = ParamUtil.getLong(request, "categoryId");
String tagName = ParamUtil.getString(request, "tag");

boolean useAssetEntryQuery = (categoryId > 0) || Validator.isNotNull(tagName);

DLPortletInstanceSettingsHelper dlPortletInstanceSettingsHelper = new DLPortletInstanceSettingsHelper(dlRequestHelper);

String displayStyle = GetterUtil.getString((String)request.getAttribute("view.jsp-displayStyle"));

PortletURL portletURL = liferayPortletResponse.createRenderURL();

portletURL.setParameter("mvcRenderCommandName", (folderId == DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) ? "/document_library/view" : "/document_library/view_folder");
portletURL.setParameter("navigation", navigation);
portletURL.setParameter("curFolder", currentFolder);
portletURL.setParameter("deltaFolder", deltaFolder);
portletURL.setParameter("folderId", String.valueOf(folderId));

SearchContainer dlSearchContainer = new SearchContainer(liferayPortletRequest, null, null, "curEntry", SearchContainer.DEFAULT_DELTA, portletURL, null, null);

EntriesChecker entriesChecker = new EntriesChecker(liferayPortletRequest, liferayPortletResponse);

entriesChecker.setCssClass("entry-selector");
entriesChecker.setRememberCheckBoxStateURLRegex("^(?!.*" + liferayPortletResponse.getNamespace() + "redirect).*(folderId=" + String.valueOf(folderId) + ")");

EntriesMover entriesMover = new EntriesMover(scopeGroupId, repositoryId);

String[] entryColumns = dlPortletInstanceSettingsHelper.getEntryColumns();

dlSearchContainer.setHeaderNames(ListUtil.fromArray(entryColumns));

String orderByCol = GetterUtil.getString((String)request.getAttribute("view.jsp-orderByCol"));
String orderByType = GetterUtil.getString((String)request.getAttribute("view.jsp-orderByType"));

boolean orderByModel = false;

if (navigation.equals("home")) {
	orderByModel = true;
}

OrderByComparator<?> orderByComparator = DLUtil.getRepositoryModelOrderByComparator(orderByCol, orderByType, orderByModel);

if (navigation.equals("recent")) {
	orderByComparator = new RepositoryModelModifiedDateComparator();
}

dlSearchContainer.setOrderByCol(orderByCol);
dlSearchContainer.setOrderByComparator(orderByComparator);
dlSearchContainer.setOrderByType(orderByType);

List results = null;
int total = 0;

String keywords = ParamUtil.getString(request, "keywords");

if (Validator.isNotNull(keywords)) {
	SearchContext searchContext = SearchContextFactory.getInstance(request);

	searchContext.setAttribute("paginationType", "regular");

	searchContext.setAttribute("searchRepositoryId", searchRepositoryId);
	searchContext.setEnd(dlSearchContainer.getEnd());

	long searchFolderId = ParamUtil.getLong(request, "searchFolderId");

	if (searchFolderId > 0) {
		folder = DLAppServiceUtil.getFolder(searchFolderId);
	}

	searchContext.setFolderIds(new long[] {searchFolderId});
	searchContext.setIncludeDiscussions(true);
	searchContext.setKeywords(keywords);

	QueryConfig queryConfig = new QueryConfig();

	queryConfig.setSearchSubfolders(true);

	searchContext.setQueryConfig(queryConfig);

	searchContext.setStart(dlSearchContainer.getStart());

	Hits hits = DLAppServiceUtil.search(searchRepositoryId, searchContext);

	total = hits.getLength();

	dlSearchContainer.setTotal(total);

	results = SearchResultUtil.getSearchResults(hits, locale);
}
else if (fileEntryTypeId >= 0) {
	Indexer indexer = IndexerRegistryUtil.getIndexer(DLFileEntryConstants.getClassName());

	if (fileEntryTypeId > 0) {
		DLFileEntryType dlFileEntryType = DLFileEntryTypeLocalServiceUtil.getFileEntryType(fileEntryTypeId);

		dlFileEntryTypeName = dlFileEntryType.getName(locale);
	}

	SearchContext searchContext = SearchContextFactory.getInstance(request);

	searchContext.setAttribute("paginationType", "none");
	searchContext.setEnd(dlSearchContainer.getEnd());

	if (orderByCol.equals("creationDate")) {
		orderByCol = "createDate";
	}
	else if (orderByCol.equals("readCount")) {
		orderByCol = "downloads";
	}
	else if (orderByCol.equals("modifiedDate")) {
		orderByCol = "modified";
	}

	Sort sort = new Sort(orderByCol, !StringUtil.equalsIgnoreCase(orderByType, "asc"));

	searchContext.setSorts(sort);

	searchContext.setStart(dlSearchContainer.getStart());

	Hits hits = indexer.search(searchContext);

	total = hits.getLength();

	dlSearchContainer.setTotal(total);

	Document[] docs = hits.getDocs();

	results = new ArrayList(docs.length);

	for (Document doc : docs) {
		long fileEntryId = GetterUtil.getLong(doc.get(Field.ENTRY_CLASS_PK));

		FileEntry fileEntry = null;

		try {
			fileEntry = DLAppLocalServiceUtil.getFileEntry(fileEntryId);
		}
		catch (Exception e) {
			if (_log.isWarnEnabled()) {
				_log.warn("Documents and Media search index is stale and contains file entry {" + fileEntryId + "}");
			}

			continue;
		}

		results.add(fileEntry);
	}
}
else {
	if (navigation.equals("home")) {
		if (useAssetEntryQuery) {
			long[] classNameIds = {PortalUtil.getClassNameId(DLFileEntryConstants.getClassName()), PortalUtil.getClassNameId(DLFileShortcutConstants.getClassName())};

			AssetEntryQuery assetEntryQuery = new AssetEntryQuery(classNameIds, dlSearchContainer);

			assetEntryQuery.setEnablePermissions(true);
			assetEntryQuery.setExcludeZeroViewCount(false);

			total = AssetEntryServiceUtil.getEntriesCount(assetEntryQuery);

			dlSearchContainer.setTotal(total);

			results = AssetEntryServiceUtil.getEntries(assetEntryQuery);
		}
		else {
			total = DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcutsCount(repositoryId, folderId, status, true);

			dlSearchContainer.setTotal(total);

			results = DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcuts(repositoryId, folderId, status, true, dlSearchContainer.getStart(), dlSearchContainer.getEnd(), dlSearchContainer.getOrderByComparator());
		}
	}
	else if (navigation.equals("mine") || navigation.equals("recent")) {
		long groupFileEntriesUserId = 0;

		if (navigation.equals("mine") && themeDisplay.isSignedIn()) {
			groupFileEntriesUserId = user.getUserId();

			status = WorkflowConstants.STATUS_ANY;
		}

		total = DLAppServiceUtil.getGroupFileEntriesCount(repositoryId, groupFileEntriesUserId, folderId, null, status);

		dlSearchContainer.setTotal(total);

		results = DLAppServiceUtil.getGroupFileEntries(repositoryId, groupFileEntriesUserId, folderId, null, status, dlSearchContainer.getStart(), dlSearchContainer.getEnd(), dlSearchContainer.getOrderByComparator());
	}
}

dlSearchContainer.setResults(results);

dlSearchContainer.setEmptyResultsMessage(fileEntryTypeId >= 0 ? LanguageUtil.format(request, "there-are-no-documents-or-media-files-of-type-x", HtmlUtil.escape(dlFileEntryTypeName)) : "there-are-no-documents-or-media-files-in-this-folder");

boolean portletTitleBasedNavigation = GetterUtil.getBoolean(portletConfig.getInitParameter("portlet-title-based-navigation"));

if (portletTitleBasedNavigation && (folderId != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) && (folderId != rootFolderId)) {
	String redirect = ParamUtil.getString(request, "redirect");

	if (Validator.isNotNull(redirect)) {
		portletDisplay.setShowBackIcon(true);
		portletDisplay.setURLBack(redirect);
	}

	renderResponse.setTitle(folder.getName());
}

boolean showSearchInfo = ParamUtil.getBoolean(request, "showSearchInfo");
boolean showRepositoryTabs = ParamUtil.getBoolean(request, "showRepositoryTabs");
%>

<c:if test="<%= showSearchInfo %>">
	<liferay-util:buffer var="searchInfo">
		<div class="search-info">
			<span class="keywords">

				<%
				boolean searchEverywhere = false;

				if ((folder == null) || (folder.getFolderId() == rootFolderId)) {
					searchEverywhere = true;
				}
				%>

				<c:choose>
					<c:when test="<%= !searchEverywhere %>">
						<liferay-ui:message arguments="<%= new Object[] {HtmlUtil.escape(keywords), HtmlUtil.escape(folder.getName())} %>" key="searched-for-x-in-x" translateArguments="<%= false %>" />
					</c:when>
					<c:otherwise>
						<liferay-ui:message arguments="<%= HtmlUtil.escape(keywords) %>" key="searched-for-x-everywhere" translateArguments="<%= false %>" />
					</c:otherwise>
				</c:choose>
			</span>

			<c:if test="<%= folderId != rootFolderId %>">
				<span class="change-search-folder">
					<portlet:renderURL var="changeSearchFolderURL">
						<portlet:param name="mvcRenderCommandName" value="/document_library/search" />
						<portlet:param name="repositoryId" value="<%= String.valueOf(repositoryId) %>" />
						<portlet:param name="searchRepositoryId" value="<%= !searchEverywhere ? String.valueOf(scopeGroupId) : String.valueOf(repositoryId) %>" />
						<portlet:param name="folderId" value="<%= String.valueOf(folderId) %>" />
						<portlet:param name="searchFolderId" value="<%= !searchEverywhere ? String.valueOf(DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) : String.valueOf(folderId) %>" />
						<portlet:param name="keywords" value="<%= keywords %>" />
						<portlet:param name="showRepositoryTabs" value="<% (searchEverywhere) ? Boolean.TRUE.toString() : Boolean.FALSE.toString() %>" />
						<portlet:param name="showSearchInfo" value="<%= Boolean.TRUE.toString() %>" />
					</portlet:renderURL>

					<aui:button href="<%= changeSearchFolderURL %>" value='<%= !searchEverywhere ? "search-everywhere" : "search-in-the-current-folder" %>' />
				</span>
			</c:if>

			<portlet:renderURL var="closeSearchURL">
				<portlet:param name="mvcRenderCommandName" value="/document_library/view" />
			</portlet:renderURL>

			<liferay-ui:icon cssClass="close-search" iconCssClass="icon-remove" id="closeSearch" message="remove" url="<%= closeSearchURL %>" />
		</div>

		<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
			<aui:script>
				Liferay.Util.focusFormField(document.getElementById('<portlet:namespace />keywords'));
			</aui:script>
		</c:if>
	</liferay-util:buffer>

	<div id="<portlet:namespace />searchInfo">
		<%= searchInfo %>
	</div>
</c:if>

<c:choose>
	<c:when test="<%= showRepositoryTabs %>">

		<%
		PortletURL searchRepositoryURL = liferayPortletResponse.createRenderURL();

		searchRepositoryURL.setParameter("mvcRenderCommandName", "/document_library/search");
		searchRepositoryURL.setParameter("repositoryId", String.valueOf(scopeGroupId));
		searchRepositoryURL.setParameter("searchRepositoryId", String.valueOf(scopeGroupId));
		searchRepositoryURL.setParameter("keywords", keywords);
		searchRepositoryURL.setParameter("showRepositoryTabs", Boolean.TRUE.toString());
		searchRepositoryURL.setParameter("showSearchInfo", Boolean.TRUE.toString());

		String[] tabsUrls = new String[] {searchRepositoryURL.toString()};

		String selectedTab = LanguageUtil.get(request, "local");

		List<Folder> mountFolders = DLAppServiceUtil.getMountFolders(scopeGroupId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		for (Folder mountFolder : mountFolders) {
			if (mountFolder.getRepositoryId() == searchRepositoryId) {
				selectedTab = HtmlUtil.escape(mountFolder.getName());
			}

			searchRepositoryURL.setParameter("repositoryId", String.valueOf(mountFolder.getRepositoryId()));
			searchRepositoryURL.setParameter("searchRepositoryId", String.valueOf(mountFolder.getRepositoryId()));

			tabsUrls = ArrayUtil.append(tabsUrls, searchRepositoryURL.toString());
		}
		%>

		<div class="search-results-container" id="<portlet:namespace />searchResultsContainer">
			<liferay-ui:tabs
				names='<%= LanguageUtil.get(request, "local") + "," + HtmlUtil.escape(ListUtil.toString(mountFolders, "name")) %>'
				refresh="<%= true %>"
				type="tabs nav-tabs-default"
				urls="<%= tabsUrls %>"
				value="<%= selectedTab %>"
			/>
		</div>
	</c:when>
</c:choose>

<div class="document-container" id="<portlet:namespace />entriesContainer">

	<%
	String searchContainerId = ParamUtil.getString(request, "searchContainerId");
	%>

	<liferay-ui:search-container
		id="<%= searchContainerId %>"
		searchContainer="<%= dlSearchContainer %>"
		total="<%= total %>"
		totalVar="dlSearchContainerTotal"
	>
		<liferay-ui:search-container-results
			results="<%= results %>"
			resultsVar="dlSearchContainerResults"
		/>

		<liferay-ui:search-container-row
			className="Object"
			modelVar="result"
		>
			<%@ include file="/document_library/cast_result.jspf" %>

			<c:choose>
				<c:when test="<%= fileEntry != null %>">

					<%
					FileVersion latestFileVersion = fileEntry.getFileVersion();

					if ((user.getUserId() == fileEntry.getUserId()) || permissionChecker.isContentReviewer(user.getCompanyId(), scopeGroupId) || DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.UPDATE)) {
						latestFileVersion = fileEntry.getLatestFileVersion();
					}

					DLViewFileVersionDisplayContext dlViewFileVersionDisplayContext = null;

					if (fileShortcut == null) {
						dlViewFileVersionDisplayContext = dlDisplayContextProvider.getDLViewFileVersionDisplayContext(request, response, fileEntry.getFileVersion());

						row.setPrimaryKey(String.valueOf(fileEntry.getFileEntryId()));
					}
					else {
						dlViewFileVersionDisplayContext = dlDisplayContextProvider.getDLViewFileVersionDisplayContext(request, response, fileShortcut);

						row.setPrimaryKey(String.valueOf(fileShortcut.getFileShortcutId()));
					}

					boolean draggable = false;

					if (DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.DELETE) || DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.UPDATE)) {
						draggable = true;

						if (dlSearchContainer.getRowChecker() == null) {
							dlSearchContainer.setRowChecker(entriesChecker);
						}

						if (dlSearchContainer.getRowMover() == null) {
							dlSearchContainer.setRowMover(entriesMover);
						}
					}

					Map<String, Object> rowData = new HashMap<String, Object>();

					rowData.put("draggable", draggable);
					rowData.put("title", fileEntry.getTitle());

					row.setData(rowData);

					String thumbnailSrc = DLUtil.getThumbnailSrc(fileEntry, latestFileVersion, themeDisplay);
					%>

					<c:choose>
						<c:when test='<%= displayStyle.equals("descriptive") %>'>
							<c:choose>
								<c:when test="<%= fileShortcut != null %>">
									<liferay-ui:search-container-column-icon
										icon="shortcut"
										toggleRowChecker="<%= true %>"
									/>
								</c:when>
								<c:when test="<%= Validator.isNotNull(thumbnailSrc) %>">
									<liferay-ui:search-container-column-image
										src="<%= thumbnailSrc %>"
										toggleRowChecker="<%= true %>"
									/>
								</c:when>
								<c:when test="<%= Validator.isNotNull(latestFileVersion.getExtension()) %>">
									<liferay-ui:search-container-column-text>
										<div class="sticker-default sticker-lg <%= dlViewFileVersionDisplayContext.getCssClassFileMimeType() %>">
											<%= StringUtil.shorten(StringUtil.upperCase(latestFileVersion.getExtension()), 3, StringPool.BLANK) %>
										</div>
									</liferay-ui:search-container-column-text>
								</c:when>
								<c:otherwise>
									<liferay-ui:search-container-column-icon
										icon="documents-and-media"
										toggleRowChecker="<%= true %>"
									/>
								</c:otherwise>
							</c:choose>

							<liferay-ui:search-container-column-jsp
								colspan="<%= 2 %>"
								path="/document_library/view_file_entry_descriptive.jsp"
							/>

							<liferay-ui:search-container-column-jsp
								path="/document_library/file_entry_action.jsp"
							/>
						</c:when>
						<c:when test='<%= displayStyle.equals("icon") %>'>

							<%
							row.setCssClass("entry-card lfr-asset-item");
							%>

							<liferay-ui:search-container-column-text>

								<%
								PortletURL rowURL = liferayPortletResponse.createRenderURL();

								rowURL.setParameter("mvcRenderCommandName", "/document_library/view_file_entry");
								rowURL.setParameter("redirect", HttpUtil.removeParameter(currentURL, liferayPortletResponse.getNamespace() + "ajax"));
								rowURL.setParameter("fileEntryId", String.valueOf(fileEntry.getFileEntryId()));
								%>

								<c:choose>
									<c:when test="<%= Validator.isNull(thumbnailSrc) %>">
										<liferay-frontend:icon-vertical-card
											actionJsp="/document_library/file_entry_action.jsp"
											actionJspServletContext="<%= application %>"
											cssClass="entry-display-style"
											icon="documents-and-media"
											resultRow="<%= row %>"
											rowChecker="<%= entriesChecker %>"
											title="<%= latestFileVersion.getTitle() %>"
											url="<%= rowURL != null ? rowURL.toString() : null %>"
										>
											<%@ include file="/document_library/file_entry_vertical_card.jspf" %>
										</liferay-frontend:icon-vertical-card>
									</c:when>
									<c:otherwise>
										<liferay-frontend:vertical-card
											actionJsp="/document_library/file_entry_action.jsp"
											actionJspServletContext="<%= application %>"
											cssClass="entry-display-style"
											imageUrl="<%= thumbnailSrc %>"
											resultRow="<%= row %>"
											rowChecker="<%= entriesChecker %>"
											title="<%= latestFileVersion.getTitle() %>"
											url="<%= rowURL != null ? rowURL.toString() : null %>"
										>
											<%@ include file="/document_library/file_entry_vertical_card.jspf" %>
										</liferay-frontend:vertical-card>
									</c:otherwise>
								</c:choose>
							</liferay-ui:search-container-column-text>
						</c:when>
						<c:otherwise>
							<c:if test='<%= ArrayUtil.contains(entryColumns, "name") %>'>

								<%
								PortletURL rowURL = liferayPortletResponse.createRenderURL();

								rowURL.setParameter("mvcRenderCommandName", "/document_library/view_file_entry");
								rowURL.setParameter("redirect", HttpUtil.removeParameter(currentURL, liferayPortletResponse.getNamespace() + "ajax"));
								rowURL.setParameter("fileEntryId", String.valueOf(fileEntry.getFileEntryId()));
								%>

								<liferay-ui:search-container-column-text
									cssClass="content-column title-column"
									name="title"
									truncate="<%= true %>"
								>
									<aui:a href="<%= rowURL.toString() %>"><%= latestFileVersion.getTitle() %></aui:a>

									<c:if test="<%= fileEntry.hasLock() %>">
										<span>
											<aui:icon cssClass="icon-monospaced" image="lock" markupView="lexicon" message="locked" />
										</span>
									</c:if>

									<c:if test="<%= fileShortcut != null %>">
										<span>
											<aui:icon cssClass="icon-monospaced" image="shortcut" markupView="lexicon" message="shortcut" />
										</span>
									</c:if>
								</liferay-ui:search-container-column-text>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "size") %>'>
								<liferay-ui:search-container-column-text
									cssClass="size-column"
									name="size"
									value="<%= TextFormatter.formatStorageSize(latestFileVersion.getSize(), locale) %>"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "status") %>'>
								<liferay-ui:search-container-column-status
									cssClass="status-column"
									name="status"
									status="<%= latestFileVersion.getStatus() %>"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "downloads") %>'>
								<liferay-ui:search-container-column-text
									cssClass="downloads-column"
									name="downloads"
									value="<%= String.valueOf(fileEntry.getReadCount()) %>"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "create-date") %>'>
								<liferay-ui:search-container-column-date
									cssClass="create-date-column text-column"
									name="create-date"
									value="<%= fileEntry.getCreateDate() %>"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "modified-date") %>'>
								<liferay-ui:search-container-column-date
									cssClass="modified-date-column text-column"
									name="modified-date"
									value="<%= latestFileVersion.getModifiedDate() %>"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "action") %>'>
								<liferay-ui:search-container-column-jsp
									cssClass="entry-action-column"
									path="/document_library/file_entry_action.jsp"
								/>
							</c:if>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>

					<%
					Map<String, Object> rowData = new HashMap<String, Object>();

					boolean draggable = false;

					if (DLFolderPermission.contains(permissionChecker, curFolder, ActionKeys.DELETE) || DLFolderPermission.contains(permissionChecker, curFolder, ActionKeys.UPDATE)) {
						draggable = true;

						if (dlSearchContainer.getRowChecker() == null) {
							dlSearchContainer.setRowChecker(entriesChecker);
						}

						if (dlSearchContainer.getRowMover() == null) {
							dlSearchContainer.setRowMover(entriesMover);
						}
					}

					rowData.put("draggable", draggable);

					rowData.put("folder", true);
					rowData.put("folder-id", curFolder.getFolderId());
					rowData.put("title", curFolder.getName());

					row.setData(rowData);

					row.setPrimaryKey(String.valueOf(curFolder.getPrimaryKey()));
					%>

					<c:choose>
						<c:when test='<%= displayStyle.equals("descriptive") %>'>
							<liferay-ui:search-container-column-icon
								icon='<%= curFolder.isMountPoint() ? "repository" : "folder" %>'
								toggleRowChecker="<%= true %>"
							/>

							<liferay-ui:search-container-column-jsp
								colspan="<%= 2 %>"
								path="/document_library/view_folder_descriptive.jsp"
							/>

							<liferay-ui:search-container-column-jsp
								path="/document_library/folder_action.jsp"
							/>
						</c:when>
						<c:when test='<%= displayStyle.equals("icon") %>'>

							<%
							row.setCssClass("entry-card lfr-asset-folder");

							PortletURL rowURL = liferayPortletResponse.createRenderURL();

							rowURL.setParameter("mvcRenderCommandName", "/document_library/view_folder");
							rowURL.setParameter("redirect", currentURL);
							rowURL.setParameter("folderId", String.valueOf(curFolder.getFolderId()));
							%>

							<liferay-ui:search-container-column-text colspan="<%= 2 %>">
								<liferay-frontend:horizontal-card
									actionJsp="/document_library/folder_action.jsp"
									actionJspServletContext="<%= application %>"
									resultRow="<%= row %>"
									rowChecker="<%= entriesChecker %>"
									text="<%= HtmlUtil.escape(curFolder.getName()) %>"
									url="<%= rowURL.toString() %>"
								>
									<liferay-frontend:horizontal-card-col>
										<liferay-frontend:horizontal-card-icon
											icon='<%= curFolder.isMountPoint() ? "repository" : "folder" %>'
										/>
									</liferay-frontend:horizontal-card-col>
								</liferay-frontend:horizontal-card>
							</liferay-ui:search-container-column-text>
						</c:when>
						<c:otherwise>
							<c:if test='<%= ArrayUtil.contains(entryColumns, "name") %>'>

								<%
								PortletURL rowURL = liferayPortletResponse.createRenderURL();

								rowURL.setParameter("mvcRenderCommandName", "/document_library/view_folder");
								rowURL.setParameter("redirect", currentURL);
								rowURL.setParameter("folderId", String.valueOf(curFolder.getFolderId()));
								%>

								<liferay-ui:search-container-column-text
									cssClass="content-column title-column"
									href="<%= rowURL %>"
									name="title"
									truncate="<%= true %>"
									value="<%= curFolder.getName() %>"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "size") %>'>
								<liferay-ui:search-container-column-text
									cssClass="size-column"
									name="size"
									value="--"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "status") %>'>
								<liferay-ui:search-container-column-text
									cssClass="status-column"
									name="status"
									value="--"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "downloads") %>'>
								<liferay-ui:search-container-column-text
									cssClass="downloads-column"
									name="downloads"
									value="--"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "create-date") %>'>
								<liferay-ui:search-container-column-date
									cssClass="create-date-column text-column"
									name="create-date"
									value="<%= curFolder.getCreateDate() %>"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "modified-date") %>'>
								<liferay-ui:search-container-column-date
									cssClass="modified-date-column text-column"
									name="modified-date"
									value="<%= curFolder.getModifiedDate() %>"
								/>
							</c:if>

							<c:if test='<%= ArrayUtil.contains(entryColumns, "action") %>'>
								<liferay-ui:search-container-column-jsp
									cssClass="entry-action-column"
									path="/document_library/folder_action.jsp"
								/>
							</c:if>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator displayStyle="<%= displayStyle %>" markupView="lexicon" resultRowSplitter="<%= new DLResultRowSplitter() %>" searchContainer="<%= dlSearchContainer %>" />
	</liferay-ui:search-container>
</div>

<%@ include file="/document_library/version_details.jspf" %>

<%!
private static Log _log = LogFactoryUtil.getLog("com_liferay_document_library_web.document_library.view_entries_jsp");
%>