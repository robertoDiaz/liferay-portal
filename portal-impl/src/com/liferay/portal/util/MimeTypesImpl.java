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

package com.liferay.portal.util;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MimeTypes;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.net.URL;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.CloseShieldInputStream;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypesReaderMetKeys;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

/**
 * @author Jorge Ferrer
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 */
public class MimeTypesImpl implements MimeTypes, MimeTypesReaderMetKeys {

	public MimeTypesImpl() {
		_detector = new DefaultDetector(
			org.apache.tika.mime.MimeTypes.getDefaultMimeTypes());

		_webImageMimeTypes = SetUtil.fromArray(
			PropsValues.MIME_TYPES_WEB_IMAGES);
	}

	public void afterPropertiesSet() {
		URL url = org.apache.tika.mime.MimeTypes.class.getResource(
			"tika-mimetypes.xml");

		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		URL customMimeTypesUrl = classLoader.getResource(
			"tika/custom-mimetypes.xml");

		try {
			read(url.openStream());
			read(customMimeTypesUrl.openStream());
		}
		catch (Exception e) {
			_log.error("Unable to populate extensions map", e);
		}
	}

	@Override
	public String getContentType(File file) {
		return getContentType(file, file.getName());
	}

	@Override
	public String getContentType(File file, String fileName) {
		if ((file == null) || !file.exists()) {
			return getContentType(fileName);
		}

		InputStream is = null;

		try {
			is = TikaInputStream.get(file);

			return getContentType(is, fileName);
		}
		catch (FileNotFoundException fnfe) {
			return getContentType(fileName);
		}
		finally {
			StreamUtil.cleanUp(is);
		}
	}

	@Override
	public String getContentType(InputStream inputStream, String fileName) {
		if (inputStream == null) {
			return getContentType(fileName);
		}

		String contentType = null;

		TikaInputStream tikaInputStream = null;

		try {
			String extension = _getFileNameExtension(fileName);

			contentType = getExtensionContentType(extension);

			if (contentType == ContentTypes.APPLICATION_OCTET_STREAM) {
				tikaInputStream = TikaInputStream.get(
					new CloseShieldInputStream(inputStream));

				Metadata metadata = new Metadata();

				metadata.set(Metadata.RESOURCE_NAME_KEY, fileName);

				MediaType mediaType = _detector.detect(
					tikaInputStream, metadata);

				contentType = mediaType.toString();
			}

			if (contentType.contains("tika")) {
				if (_log.isDebugEnabled()) {
					_log.debug("Retrieved invalid content type " + contentType);
				}

				contentType = getContentType(fileName);
			}

			if (contentType.contains("tika")) {
				if (_log.isDebugEnabled()) {
					_log.debug("Retrieved invalid content type " + contentType);
				}

				contentType = ContentTypes.APPLICATION_OCTET_STREAM;
			}
		}
		catch (Exception e) {
			_log.error(e, e);

			contentType = ContentTypes.APPLICATION_OCTET_STREAM;
		}
		finally {
			StreamUtil.cleanUp(tikaInputStream);
		}

		return contentType;
	}

	@Override
	public String getContentType(String fileName) {
		if (Validator.isNull(fileName)) {
			return ContentTypes.APPLICATION_OCTET_STREAM;
		}

		String contentType = null;

		try {
			String extension = _getFileNameExtension(fileName);

			contentType = getExtensionContentType(extension);

			if (contentType == ContentTypes.APPLICATION_OCTET_STREAM) {
				Metadata metadata = new Metadata();

				metadata.set(Metadata.RESOURCE_NAME_KEY, fileName);

				MediaType mediaType = _detector.detect(null, metadata);

				contentType = mediaType.toString();
			}

			if (!contentType.contains("tika")) {
				return contentType;
			}
			else if (_log.isDebugEnabled()) {
				_log.debug("Retrieved invalid content type " + contentType);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return ContentTypes.APPLICATION_OCTET_STREAM;
	}

	@Override
	public String getExtensionContentType(String extension) {
		if (Validator.isNull(extension)) {
			return ContentTypes.APPLICATION_OCTET_STREAM;
		}

		for (Map.Entry<String, Set<String>> entry : _extensionsMap.entrySet()) {
			Set<String> valueSet = entry.getValue();

			if (valueSet.contains(".".concat(extension))) {
				return entry.getKey();
			}
		}

		return getContentType("A.".concat(extension));
	}

	@Override
	public Set<String> getExtensions(String contentType) {
		Set<String> extensions = _extensionsMap.get(contentType);

		if (extensions == null) {
			extensions = Collections.emptySet();
		}

		return extensions;
	}

	@Override
	public boolean isWebImage(String mimeType) {
		return _webImageMimeTypes.contains(mimeType);
	}

	protected void read(InputStream stream) throws Exception {
		DocumentBuilderFactory documentBuilderFactory =
			DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder =
			documentBuilderFactory.newDocumentBuilder();

		Document document = documentBuilder.parse(new InputSource(stream));

		Element element = document.getDocumentElement();

		if ((element == null) || !MIME_INFO_TAG.equals(element.getTagName())) {
			throw new SystemException("Invalid configuration file");
		}

		NodeList nodeList = element.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element childElement = (Element)node;

			if (MIME_TYPE_TAG.equals(childElement.getTagName())) {
				readMimeType(childElement);
			}
		}
	}

	protected void readMimeType(Element element) {
		Set<String> mimeTypes = new HashSet<>();

		Set<String> extensions = new HashSet<>();

		String name = element.getAttribute(MIME_TYPE_TYPE_ATTR);

		mimeTypes.add(name);

		NodeList nodeList = element.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);

			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element childElement = (Element)node;

			if (ALIAS_TAG.equals(childElement.getTagName())) {
				String alias = childElement.getAttribute(ALIAS_TYPE_ATTR);

				mimeTypes.add(alias);
			}
			else if (GLOB_TAG.equals(childElement.getTagName())) {
				boolean regex = GetterUtil.getBoolean(
					childElement.getAttribute(ISREGEX_ATTR));

				if (regex) {
					continue;
				}

				String pattern = childElement.getAttribute(PATTERN_ATTR);

				if (!pattern.startsWith("*")) {
					continue;
				}

				String extension = pattern.substring(1);

				if (!extension.contains("*") && !extension.contains("?") &&
					!extension.contains("[")) {

					extensions.add(extension);
				}
			}
		}

		for (String mimeType : mimeTypes) {
			_extensionsMap.put(mimeType, extensions);
		}
	}

	private String _getFileNameExtension(String fileName) {
		if (fileName == null) {
			return null;
		}

		int pos = fileName.lastIndexOf(CharPool.PERIOD);

		if (pos > 0) {
			return StringUtil.toLowerCase(
				fileName.substring(pos + 1, fileName.length()));
		}
		else {
			return StringPool.BLANK;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(MimeTypesImpl.class);

	private final Detector _detector;
	private final Map<String, Set<String>> _extensionsMap = new HashMap<>();
	private final Set<String> _webImageMimeTypes;

}