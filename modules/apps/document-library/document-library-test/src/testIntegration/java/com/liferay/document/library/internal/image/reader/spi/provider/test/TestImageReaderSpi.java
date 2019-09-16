package com.liferay.document.library.internal.image.reader.spi.provider.test;

import java.io.IOException;

import java.util.Locale;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;

public class TestImageReaderSpi extends ImageReaderSpi {

	@Override
	public boolean canDecodeInput(Object source) throws IOException {
		return false;
	}

	@Override
	public ImageReader createReaderInstance(Object extension)
		throws IOException {

		return null;
	}

	@Override
	public String getDescription(Locale locale) {
		return null;
	}

}