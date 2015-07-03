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

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.repository.model.FileEntry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roberto Díaz
 */
public class FileExtensionUtil {

	public static String processFileName(
			String fileName, Retrieve<FileEntry> retrieve)
		throws Exception {

		FileEntry existingFileEntry = retrieve.get(fileName);

		while (existingFileEntry != null) {
			Pattern extensionPattern = Pattern.compile(EXTENSION_REGEXP);

			Pattern digitPattern = Pattern.compile(DIGIT_REGEXP);

			Matcher matcher = extensionPattern.matcher(fileName);

			String extension = StringPool.BLANK;

			if (matcher.find()) {
				extension = matcher.group(0);

				fileName = fileName.replaceAll(
					EXTENSION_REGEXP, StringPool.BLANK);
			}

			matcher = digitPattern.matcher(fileName);

			String suffix = "(1)";

			if (matcher.find()) {
				String digit = matcher.group(0);

				String value = digit.substring(1, digit.length() - 1);

				if (Validator.isNotNull(value)) {
					suffix =
						StringPool.OPEN_PARENTHESIS +
							(Integer.valueOf(value) + 1) +
							StringPool.CLOSE_PARENTHESIS;
				}

				fileName = fileName.replaceAll(DIGIT_REGEXP, StringPool.BLANK);
			}

			fileName = fileName + suffix + extension;

			existingFileEntry = retrieve.get(fileName);
		}

		return fileName;
	}

	public static final String DIGIT_REGEXP = "\\(\\d+\\)";

	public static final String EXTENSION_REGEXP = "\\.\\w+$";

}