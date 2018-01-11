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

package com.liferay.portal.kernel.servlet.taglib.ui;

import com.liferay.portal.kernel.exception.ImageResolutionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageTool;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * @author Roberto Díaz
 */
public class ImageSelectorProcessor {

	public ImageSelectorProcessor(byte[] bytes) {
		_bytes = bytes;
	}

	public byte[] cropImage(String cropRegion)
		throws IOException, PortalException {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(cropRegion);

		int height = jsonObject.getInt("height");
		int width = jsonObject.getInt("width");
		int x = jsonObject.getInt("x");
		int y = jsonObject.getInt("y");

		if ((x > 0) || (y > 0) || (width > 0) || (height > 0)) {
			ImageBag imageBag = ImageToolUtil.read(_bytes);

			RenderedImage renderedImage = imageBag.getRenderedImage();

			if (imageBag.getType().equals(ImageTool.TYPE_GIF)) {
				return _cropGif(_bytes, height, width, x, y);
			}
			else {
				renderedImage = ImageToolUtil.crop(
					renderedImage, height, width, x, y);
			}

			return ImageToolUtil.getBytes(renderedImage, imageBag.getType());
		}

		return _bytes;
	}

	public byte[] scaleImage(int width)
		throws ImageResolutionException, IOException {

		ImageBag imageBag = ImageToolUtil.read(_bytes);

		RenderedImage renderedImage = imageBag.getRenderedImage();

		renderedImage = ImageToolUtil.scale(renderedImage, width);

		return ImageToolUtil.getBytes(renderedImage, imageBag.getType());
	}

	private byte[] _cropGif(
		byte[] imageData, int height, int width, int x, int y) {

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			ImageOutputStream ios = ImageIO.createImageOutputStream(os);

			ImageInputStream imageInputStream = ImageIO.createImageInputStream(
				new ByteArrayInputStream(imageData));

			ImageReader imageReader = (ImageReader)
				ImageIO.getImageReadersByFormatName(ImageTool.TYPE_GIF).next();

			ImageWriter imageWriter = (ImageWriter)
				ImageIO.getImageWritersByFormatName(ImageTool.TYPE_GIF).next();

			imageReader.setInput(imageInputStream, false);

			imageWriter.setOutput(ios);
			imageWriter.prepareWriteSequence(null);

			IIOMetadata imageMetaData = imageReader.getImageMetadata(0);
			int numberOfFrames = imageReader.getNumImages(true);
			BufferedImage master = null;

			for (int i = 0; i < numberOfFrames; i++) {
				BufferedImage image = imageReader.read(i);

				master = new BufferedImage(
					width, height, BufferedImage.TYPE_INT_ARGB);

				master.getGraphics().drawImage(image, x, y, null);

				IIOImage iioImage = new IIOImage(master, null, imageMetaData);

				imageWriter.writeToSequence(iioImage, null);
			}

			imageWriter.endWriteSequence();

			ios.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return os.toByteArray();
	}

	private final byte[] _bytes;

}