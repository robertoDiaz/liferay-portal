create index IX_EF8A863D on DropboxEntry (repositoryId, parentPath[$COLUMN_LENGTH:4000$], type_);
create index IX_9E3929F5 on DropboxEntry (repositoryId, path_[$COLUMN_LENGTH:4000$]);
create index IX_37C0AEB0 on DropboxEntry (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_DE052132 on DropboxEntry (uuid_[$COLUMN_LENGTH:75$], groupId);

create index IX_ACECE6EC on DropboxRevision (repositoryId, entryId, rev[$COLUMN_LENGTH:75$]);
create index IX_44B2A0B7 on DropboxRevision (uuid_[$COLUMN_LENGTH:75$]);