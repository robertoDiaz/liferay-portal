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

package com.liferay.liferaygen.web.internal.value.generator;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.liferaygen.web.internal.util.LiferayGenQueryHandler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.Conjunction;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.ClassedModel;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutTypePortlet;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletPreferences;
import com.liferay.portal.kernel.model.ResourcedModel;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import com.liferay.portal.kernel.util.WebKeys;
import com.maximeroussy.invitrode.WordGenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.math3.random.RandomData;
import org.apache.commons.math3.random.RandomDataImpl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.mock.web.portlet.MockActionRequest;

/**
 * @author Jorge Díaz
 * @author Alberto Chaparro
 * @author Daniel Couso
 * @author Roberto Díaz
 */
@Component(immediate = true, service = LiferayGenValueGenerator.class)
public class LiferayGenValueGenerator {

	public boolean canAddPortlet(Portlet portlet, Layout layout)
		throws PortalException {

		if (portlet.isSystem() || portlet.isUndeployedPortlet()) {
			return false;
		}

		List<String> resourceActions = ResourceActionsUtil.getResourceActions(
			portlet.getPortletId());

		if (!resourceActions.contains(ActionKeys.ADD_TO_PAGE)) {
			return false;
		}

		if (!portlet.isInstanceable() && (layout != null)) {
			LayoutTypePortlet ltp = (LayoutTypePortlet)layout.getLayoutType();

			if (ltp.hasPortletId(portlet.getPortletId())) {
				return false;
			}
		}

		return true;
	}

	public List<Long> getAllUserIdsFromCache() {
		List<Long> userIdsCurrentCompany = _userIdsCache.get(
			CompanyThreadLocal.getCompanyId());

		return new ArrayList<>(userIdsCurrentCompany);
	}

	public List<String> getAvailableImageIOFormats() {
		if (_availableImageIOFormats == null) {
			List<String> temp = new ArrayList<>();

			for (String formatName : ImageIO.getWriterFormatNames()) {
				String lowerCaseFormatName = StringUtil.toLowerCase(formatName);

				if (temp.contains(lowerCaseFormatName)) {
					continue;
				}

				if (ArrayUtil.contains(_EXTENSIONS, lowerCaseFormatName)) {
					temp.add(lowerCaseFormatName);
				}
			}

			_availableImageIOFormats = Collections.unmodifiableList(temp);
		}

		return _availableImageIOFormats;
	}

	public boolean getBoolean() {
		return _RAND.nextBoolean();
	}

	public boolean getBoolean(int truePercentage)
		throws IllegalArgumentException {

		if ((truePercentage < 0) || (truePercentage > 100)) {
			throw new IllegalArgumentException(
				truePercentage + " is not a valid percentage value");
		}

		if (_RAND.nextInt(100) < truePercentage) {
			return true;
		}

		return false;
	}

	public String getCSV(int size, int columns) {
		StringBundler sb = new StringBundler(500);

		while (sb.length() < size) {
			for (int i = 0; i < columns; i++) {
				if (i == 0) {
					if (sb.length() > 0) {
						sb.append('\n');
					}
				}
				else {
					sb.append(',');
				}

				int wordSize = getRandomIntegerFromRange(5, 30);

				sb.append(getLowerCaseWord(wordSize));
			}
		}

		return sb.toString();
	}

	public byte[] getImageText(String text) {
		return getImageText("png", text, "Arial", 48);
	}

	public byte[] getImageText(String formatName, String text) {
		return getImageText(formatName, text, "Arial", 48);
	}

	public byte[] getImageText(
		String formatName, String text, String fontType, int fontSize) {

		if (!isAvailableImageIOFormat(formatName)) {
			return null;
		}

		RenderedImage image = getRenderedImageFromText(
			text, fontType, fontSize);

		return convertRenderedImageToBytes(formatName, image);
	}

	public String getLatinName(int length) {
		String randomString = getLowerCaseWord(length);

		return StringUtil.toUpperCase(randomString.substring(0, 1)) +
			randomString.substring(1);
	}

	public String getLowerCaseText(int length) {
		return getLowerCaseText(length, StringPool.SPACE);
	}

	public String getLowerCaseText(int minLength, int maxLength) {
		int length = getRandomIntegerFromRange(minLength, maxLength);

		return getLowerCaseText(length, StringPool.SPACE);
	}

	public String getLowerCaseText(int length, String separator) {
		String text = StringPool.BLANK;

		while (length > 0) {
			int wordLength = length;

			if (!separator.isEmpty() || (length > 15)) {
				wordLength = Math.min(length, getRandomIntegerFromRange(4, 15));
			}

			length = length - wordLength - separator.length();

			if ((length == 1) || (length == 2)) {
				wordLength = wordLength + length - 3;
				length = 3;
			}

			if ((wordLength == 1) || (wordLength == 2)) {
				wordLength = 3;
			}

			if (Validator.isNotNull(text)) {
				text = text + separator;
			}

			WordGenerator wordGenerator = new WordGenerator();

			try {
				text =
					text +
						StringUtil.toLowerCase(
							wordGenerator.newWord(wordLength));
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(e, e);
				}
			}
		}

		return text;
	}

	public String getLowerCaseWord(int length) {
		return getLowerCaseText(length, StringPool.BLANK);
	}

	public MockActionRequest getMockActionRequest(
		HttpServletRequest request, Portlet portlet,
		PortletPreferences portletPreferences)
		throws Exception {

		MockActionRequest actionRequest = new MockActionRequest();

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		Layout layout = (Layout)request.getAttribute(WebKeys.LAYOUT);

		String portletId = StringPool.BLANK;

		if (portlet != null) {
			portletId = portlet.getPortletId();
		}

		actionRequest.setAttribute(WebKeys.CTX, getMockServletContext());
		actionRequest.setAttribute(WebKeys.CURRENT_URL, StringPool.BLANK);
		actionRequest.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
		actionRequest.setAttribute(WebKeys.LAYOUT, layout);
		actionRequest.setAttribute(WebKeys.THEME, layout.getTheme());
		actionRequest.setAttribute("COLOR_SCHEME", layout.getColorScheme());
		actionRequest.setParameter("portletResource", portletId);
		actionRequest.setPreferences(portletPreferences);

		return actionRequest;
	}

	public MockHttpServletRequest getMockHttpServletRequest(
			Layout layout, User user, Locale locale)
		throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();

		ThemeDisplay themeDisplay = getMockThemeDisplay(
			layout, user, locale, request);

		request.setAttribute(WebKeys.CTX, getMockServletContext());
		request.setAttribute(WebKeys.CURRENT_URL, StringPool.BLANK);
		request.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
		request.setAttribute(WebKeys.LAYOUT, layout);
		request.setAttribute(WebKeys.THEME, layout.getTheme());
		request.setAttribute("COLOR_SCHEME", layout.getColorScheme());

		return request;
	}

	public ServletContext getMockServletContext() {
		return _MOCK_SERVLET_CONTEXT;
	}

	public ThemeDisplay getMockThemeDisplay(
			Layout layout, User user, Locale locale,
			MockHttpServletRequest request)
		throws Exception {

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(user);

		Company company = _companyLocalService.getCompany(
			layout.getCompanyId());

		long scopeGroupId = layout.getGroupId();

		TimeZone timeZone = user.getTimeZone();

		if (timeZone == null) {
			timeZone = company.getTimeZone();
		}

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setSecure(request.isSecure());
		themeDisplay.setServerName(request.getServerName());
		themeDisplay.setServerPort(request.getServerPort());
		themeDisplay.setCompany(company);
		themeDisplay.setLayout(layout);
		themeDisplay.setLayoutTypePortlet(
			(LayoutTypePortlet)layout.getLayoutType());
		themeDisplay.setLanguageId(LocaleUtil.toLanguageId(locale));
		themeDisplay.setLocale(locale);
		themeDisplay.setLookAndFeel(layout.getTheme(), layout.getColorScheme());
		themeDisplay.setUser(user);
		themeDisplay.setRealUser(user);
		themeDisplay.setScopeGroupId(scopeGroupId);
		themeDisplay.setTimeZone(timeZone);
		themeDisplay.setPathMain(_portal.getPathMain());
		themeDisplay.setPermissionChecker(permissionChecker);

		return themeDisplay;
	}

	public byte[] getPDF(int length) {
		String text = getLowerCaseText(length);

		ByteArrayOutputStream byteArrayOutputStream =
			new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(byteArrayOutputStream);

		PdfDocument pdf = new PdfDocument(writer);

		Document document = new Document(pdf);

		document.add(new Paragraph(text));

		document.close();

		return byteArrayOutputStream.toByteArray();
	}

	public byte[] getRandomBytes(int size) {
		byte[] array = new byte[size];

		_RAND.nextBytes(array);

		return array;
	}

	public long getRandomClassPK(String className) {
		return getRandomClassPK(className, 0, 0);
	}

	public long getRandomClassPK(
		String className, long companyId, long groupId) {

		try {
			Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

			Criterion criterionCompanyId = null;

			if (companyId > 0) {
				criterionCompanyId = RestrictionsFactoryUtil.eq(
					"companyId", companyId);

				conjunction.add(criterionCompanyId);
			}

			if (groupId > 0) {
				Criterion criterionGroupId = RestrictionsFactoryUtil.eq(
					"groupId", groupId);

				conjunction.add(criterionGroupId);
			}

			Object obj;

			try {
				obj = getRandomObject(className, conjunction);
			}
			catch (Exception e) {
				Throwable rootException = e;

				while (rootException.getCause() != null) {
					rootException = rootException.getCause();
				}

				String message = rootException.getMessage();

				if (message.contains("could not resolve property: groupId")) {
					obj = getRandomObject(className, criterionCompanyId);
				}
				else {
					throw e;
				}
			}

			if (obj instanceof ResourcedModel) {
				ResourcedModel resourcedModel = (ResourcedModel)obj;

				return resourcedModel.getResourcePrimKey();
			}

			if (obj instanceof ClassedModel) {
				ClassedModel classedModel = (ClassedModel)obj;

				return (Long)classedModel.getPrimaryKeyObj();
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		return 0;
	}

	public Date getRandomDate() {
		return getRandomDate(new Date(0), new Date(Long.MAX_VALUE));
	}

	public Date getRandomDate(Date startDate, Date endDate) {
		return new Date(_RD.nextLong(startDate.getTime(), endDate.getTime()));
	}

	public Double getRandomDoubleFromRange(double minValue, double maxValue) {
		return _RD.nextUniform(minValue, maxValue);
	}

	public byte[] getRandomFileContent(String fileName) {
		String fileExtension = FileUtil.getExtension(fileName);

		/* Try generating image using ImageIO library */
		byte[] bytes = getImageText(fileExtension, fileName);

		if (bytes != null) {
			return bytes;
		}

		int size = getRandomIntegerFromRange(5000, 10000);

		/* If file extension is txt or pdf, we generate a text or pdf file */
		if ("txt".equals(fileExtension)) {
			String text = getLowerCaseText(size);

			return text.getBytes();
		}

		if ("csv".equals(fileExtension)) {
			int columns = getRandomIntegerFromRange(10, 20);

			String text = getCSV(size, columns);

			return text.getBytes();
		}

		if ("pdf".equals(fileExtension)) {
			return getPDF(size);
		}

		/* Other formats: random byte array */

		return getRandomBytes(size * 16);
	}

	public String getRandomFileExtension() {
		return getRandomObjectFromArray(_EXTENSIONS);
	}

	public Float getRandomFloatFromRange(float minValue, float maxValue) {
		return (float)_RD.nextUniform((double)minValue, (double)maxValue);
	}

	public byte[] getRandomImage(String formatName, int width, int height) {
		RenderedImage image = getRenderedImageRandom(width, height);

		return convertRenderedImageToBytes(formatName, image);
	}

	public DLFileEntry getRandomImageFromDL(Criterion criterion)
		throws Exception {

		Criterion extensionCriterion = RestrictionsFactoryUtil.in(
			"extension", getAvailableImageIOFormats());

		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		conjunction.add(criterion);
		conjunction.add(extensionCriterion);

		return (DLFileEntry)getRandomObject(
			DLFileEntry.class.getName(), conjunction);
	}

	public int getRandomIntegerFromRange(int minValue, int maxValue) {
		if (minValue == maxValue) {
			return minValue;
		}

		return _RD.nextInt(minValue, maxValue);
	}

	public Long getRandomLongFromRange(long minValue, long maxValue) {
		if (minValue == maxValue) {
			return minValue;
		}

		return _RD.nextLong(minValue, maxValue);
	}

	public ClassedModel getRandomObject(String className, Criterion criterion)
		throws Exception {

		return (ClassedModel)getRandomObjectProperties(
			className, null, criterion);
	}

	public <T> T getRandomObjectFromArray(T[] array) {
		if ((array == null) || (array.length == 0)) {
			return null;
		}

		if (array.length == 1) {
			return array[0];
		}

		return array[_RAND.nextInt(array.length)];
	}

	public <T> T getRandomObjectFromList(List<T> list) {
		if ((list == null) || list.isEmpty()) {
			return null;
		}

		if (list.size() == 1) {
			return list.get(0);
		}

		return list.get(_RAND.nextInt(list.size()));
	}

	public Object getRandomObjectProperties(
			String className, String properties, Criterion criterion)
		throws Exception {

		int count = (int)_liferayGenQueryHandler.executeEntityModelQueryCount(
			className, criterion);

		if (count == 0) {
			return null;
		}

		int randomPos = getRandomIntegerFromRange(0, count - 1);

		List<Object> objects = _liferayGenQueryHandler.executeEntityModelQuery(
			className, properties, criterion, randomPos, randomPos + 1);

		return objects.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<ClassedModel> getRandomObjects(
			String className, Criterion criterion, long number)
		throws Exception {

		return (List<ClassedModel>)getRandomObjectsProperties(
			className, null, criterion, number);
	}

	public <T> List<T> getRandomObjectsFromArray(T[] array, long number) {
		return getRandomObjectsFromList(Arrays.asList(array), number, false);
	}

	public <T> List<T> getRandomObjectsFromArray(
		T[] array, long number, boolean allowDuplicates) {

		return getRandomObjectsFromList(
			Arrays.asList(array), number, allowDuplicates);
	}

	public <T> List<T> getRandomObjectsFromList(List<T> list, long number) {
		return getRandomObjectsFromList(list, number, false);
	}

	public <T> List<T> getRandomObjectsFromList(
		List<T> list, long number, boolean allowDuplicates) {

		if ((number == 0) || (list == null) || list.isEmpty()) {
			return Collections.emptyList();
		}

		if (number == 1) {
			return Collections.singletonList(getRandomObjectFromList(list));
		}

		List<T> copy = list;

		if (!allowDuplicates) {
			copy = new ArrayList<>(list);
		}

		List<T> selected = new ArrayList<>();

		while (selected.size() < number) {
			T randomObject;

			if (allowDuplicates) {
				randomObject = getRandomObjectFromList(copy);
			}
			else {
				randomObject = removeRandomObjectFromList(copy);
			}

			if (randomObject == null) {
				break;
			}

			selected.add(randomObject);
		}

		return selected;
	}

	public List<?> getRandomObjectsProperties(
		String className, String properties, Criterion criterion, long number) {

		if (number == 0) {
			return Collections.emptyList();
		}

		int count = (int)_liferayGenQueryHandler.executeEntityModelQueryCount(
			className, criterion);

		if (count == 0) {
			return Collections.emptyList();
		}

		List<Object> result = new ArrayList<>();

		for (long i = 0; i < number; i++) {
			int randomPos = getRandomIntegerFromRange(0, count - 1);

			List<Object> objects =
				_liferayGenQueryHandler.executeEntityModelQuery(
					className, properties, criterion, randomPos, randomPos + 1);

			result.addAll(objects);
		}

		return result;
	}

	public Portlet getRandomPortlet(Layout layout) throws PortalException {
		if (layout == null) {
			return null;
		}

		return getRandomPortlet(layout.getCompanyId(), layout);
	}

	public Portlet getRandomPortlet(long companyId) throws PortalException {
		return getRandomPortlet(companyId, null);
	}

	public Portlet getRandomPortlet(long companyId, Layout layout)
		throws PortalException {

		List<Portlet> portlets = _portletLocalService.getPortlets(
			companyId, false, false);

		while (true) {
			Portlet portlet = removeRandomObjectFromList(portlets);

			if (portlet == null) {
				return null;
			}

			if (canAddPortlet(portlet, layout)) {
				return portlet;
			}
		}
	}

	public Short getRandomShortFromRange(short minValue, short maxValue) {
		return (short)_RD.nextInt(minValue, maxValue);
	}

	/**
	 * It is better to always use getLowerCaseWord method unless you want a
	 * random alphanumeric string with random characters
	 */
	public String getRandomString(int length) {
		return getRandomString(length, _ALPHANUM_LEXICON);
	}

	/**
	 * It is better to always use getLowerCaseWord method unless you want a
	 * random alphanumeric string with random characters
	 */
	public String getRandomString(int length, String lexicon) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < length; i++) {
			builder.append(lexicon.charAt(_RAND.nextInt(lexicon.length())));
		}

		return builder.toString();
	}

	public long getRandomUserIdFromCache() {
		List<Long> result = getRandomUserIdsFromCache(1L);

		if (result.isEmpty()) {
			return -1L;
		}

		return result.get(0);
	}

	public List<Long> getRandomUserIdsFromCache(long number) {
		if (_userIdsCache == null) {
			resetUserIdsCache();
		}

		List<Long> userIdsCurrentCompany = _userIdsCache.get(
			CompanyThreadLocal.getCompanyId());

		return getRandomObjectsFromList(userIdsCurrentCompany, number);
	}

	public Map<Locale, String> getRandomValuesLocalizationMap(
		Locale[] locales, int valuesLength) {

		Map<Locale, String> localizationMap = new HashMap<>();

		for (Locale randomLocale : locales) {
			localizationMap.put(randomLocale, getLowerCaseText(valuesLength));
		}

		return localizationMap;
	}

	public String getUpperCaseWord(int length) {
		return StringUtil.toUpperCase(getLowerCaseWord(length));
	}

	public boolean isAvailableImageIOFormat(String formatName) {
		formatName = StringUtil.toLowerCase(formatName);

		return getAvailableImageIOFormats().contains(formatName);
	}

	public <T> T removeRandomObjectFromList(List<T> list) {
		if ((list == null) || list.isEmpty()) {
			return null;
		}

		return list.remove(_RAND.nextInt(list.size()));
	}

	public void resetCaches() {
		resetUserIdsCache();
	}

	public void setServletContext(ServletContext servletContext) {
		if (servletContext == null) {
			return;
		}

		Enumeration<String> attributesEnumeration =
			servletContext.getAttributeNames();

		while (attributesEnumeration.hasMoreElements()) {
			String attribute = attributesEnumeration.nextElement();

			Object value = servletContext.getAttribute(attribute);

			_MOCK_SERVLET_CONTEXT.setAttribute(attribute, value);
		}

		Enumeration<String> initParametersEnumeration =
			servletContext.getInitParameterNames();

		while (initParametersEnumeration.hasMoreElements()) {
			String attribute = initParametersEnumeration.nextElement();

			Object value = servletContext.getInitParameter(attribute);

			_MOCK_SERVLET_CONTEXT.setAttribute(attribute, value);
		}

		_MOCK_SERVLET_CONTEXT.setContextPath(servletContext.getContextPath());
		_MOCK_SERVLET_CONTEXT.setServletContextName(
			servletContext.getServletContextName());
	}

	protected byte[] convertRenderedImageToBytes(
		String formatName, RenderedImage image) {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, formatName, os);

			return os.toByteArray();
		}
		catch (IOException ioe) {
			_log.error(ioe, ioe);
		}
		finally {
			if (os != null) {
				try {
					os.close();
				}
				catch (IOException ioe) {
					_log.error(ioe, ioe);
				}
			}
		}

		return null;
	}

	protected List<Long> getAllUserIds(long companyId) {
		Conjunction conjunction = RestrictionsFactoryUtil.conjunction();

		conjunction.add(RestrictionsFactoryUtil.eq("defaultUser", false));
		conjunction.add(RestrictionsFactoryUtil.eq("companyId", companyId));
		conjunction.add(RestrictionsFactoryUtil.eq("status", 0));

		return (List<Long>)_liferayGenQueryHandler.executeEntityModelQuery(
			User.class.getName(), "userId", conjunction);
	}

	protected RenderedImage getRenderedImageFromText(
		String text, String fontType, int fontSize) {

		/*
		Because font metrics is based on a graphics context, we need to create
		a small, temporary image so we can ascertain the width and height
		of the final image
		 */
		BufferedImage img = new BufferedImage(
			1, 1, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = img.createGraphics();

		Font font = new Font(fontType, Font.PLAIN, fontSize);

		g2d.setFont(font);

		FontMetrics fm = g2d.getFontMetrics();

		int width = fm.stringWidth(text);
		int height = fm.getHeight();

		g2d.dispose();

		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		g2d = img.createGraphics();

		g2d.setRenderingHint(
			RenderingHints.KEY_ALPHA_INTERPOLATION,
			RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(
			RenderingHints.KEY_COLOR_RENDERING,
			RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(
			RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(
			RenderingHints.KEY_FRACTIONALMETRICS,
			RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(
			RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(
			RenderingHints.KEY_STROKE_CONTROL,
			RenderingHints.VALUE_STROKE_PURE);
		g2d.setFont(font);

		fm = g2d.getFontMetrics();
		g2d.setColor(Color.BLACK);
		g2d.setBackground(Color.GRAY);
		g2d.drawString(text, 0, fm.getAscent());
		g2d.dispose();

		return img;
	}

	protected BufferedImage getRenderedImageRandom(int width, int height) {

		// create buffered image object img

		BufferedImage img = new BufferedImage(
			width, height, BufferedImage.TYPE_INT_ARGB);

		// ByteArray OS

		// create random image pixel by pixel

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int r = (int)(Math.random() * 256); // red
				int g = (int)(Math.random() * 256); // green
				int b = (int)(Math.random() * 256); // blue

				Color rgb = new Color(r, g, b);

				img.setRGB(x, y, rgb.getRGB());
			}
		}

		return img;
	}

	protected void resetUserIdsCache() {
		_userIdsCache = _getUserIdsMap();
	}

	private Map<Long, List<Long>> _getUserIdsMap() {
		List<Long> companyIds =
			(List<Long>)_liferayGenQueryHandler.executeEntityModelQuery(
				Company.class.getName(), "companyId");

		Map<Long, List<Long>> userIdsMap = new ConcurrentHashMap<>();

		for (Long companyId : companyIds) {
			List<Long> userIdsFromCompany = getAllUserIds(companyId);

			userIdsMap.put(companyId, userIdsFromCompany);
		}

		return userIdsMap;
	}

	private static final String _ALPHANUM_LEXICON =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz12345674890";

	private static final String[] _EXTENSIONS = {
		"txt", "csv", "pdf", "doc", "docx", "rtf", "odt", "mp3", "ogg", "wav",
		"wma", "odp", "ppt", "pptx", "ods", "xls", "xlsx", "avi", "mp4", "mpg",
		"ogv", "wmv", "gif", "jpeg", "jpg", "png", "gif", "jpeg", "jpg", "png",
		"txt", "csv", "pdf", "gif", "jpeg", "jpg", "png", "gif", "jpeg", "jpg",
		"png", "txt", "csv", "pdf", "gif", "jpeg", "jpg", "png", "gif", "jpeg",
		"jpg", "png", "txt", "csv", "pdf"
	};

	private static final MockServletContext _MOCK_SERVLET_CONTEXT =
		new MockServletContext();

	private static final Random _RAND = new Random();

	private static final RandomData _RD = new RandomDataImpl();

	private static final Log _log = LogFactoryUtil.getLog(
		LiferayGenValueGenerator.class);

	private List<String> _availableImageIOFormats;

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private LiferayGenQueryHandler _liferayGenQueryHandler;

	@Reference
	private Portal _portal;

	@Reference
	private PortletLocalService _portletLocalService;

	private Map<Long, List<Long>> _userIdsCache;

}