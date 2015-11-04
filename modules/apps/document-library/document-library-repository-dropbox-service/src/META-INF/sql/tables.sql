create table DropboxEntry (
	uuid_ VARCHAR(75) null,
	entryId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	repositoryId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	changeLog VARCHAR(75) null,
	description VARCHAR(75) null,
	name VARCHAR(75) null,
	path_ STRING null,
	parentPath STRING null,
	rev VARCHAR(75) null,
	size_ LONG,
	type_ INTEGER
);

create table DropboxRevision (
	uuid_ VARCHAR(75) null,
	revisionId LONG not null primary key,
	createDate DATE null,
	entryId LONG,
	path_ STRING null,
	repositoryId LONG,
	rev VARCHAR(75) null,
	size_ LONG
);