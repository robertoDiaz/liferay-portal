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

package com.liferay.document.library.repository.dropbox.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.util.Accessor;
import com.liferay.portal.model.PersistedModel;

/**
 * The extended model interface for the DropboxEntry service. Represents a row in the &quot;DropboxEntry&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see DropboxEntryModel
 * @see com.liferay.document.library.repository.dropbox.model.impl.DropboxEntryImpl
 * @see com.liferay.document.library.repository.dropbox.model.impl.DropboxEntryModelImpl
 * @generated
 */
@ProviderType
public interface DropboxEntry extends DropboxEntryModel, PersistedModel {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to {@link com.liferay.document.library.repository.dropbox.model.impl.DropboxEntryImpl} and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<DropboxEntry, Long> ENTRY_ID_ACCESSOR = new Accessor<DropboxEntry, Long>() {
			@Override
			public Long get(DropboxEntry dropboxEntry) {
				return dropboxEntry.getEntryId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<DropboxEntry> getTypeClass() {
				return DropboxEntry.class;
			}
		};
}