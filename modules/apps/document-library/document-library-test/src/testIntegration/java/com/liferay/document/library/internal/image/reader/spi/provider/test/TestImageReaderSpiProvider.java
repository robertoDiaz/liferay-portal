package com.liferay.document.library.internal.image.reader.spi.provider.test;

import com.liferay.document.library.image.reader.spi.provider.ImageReaderSpiProvider;

import javax.imageio.spi.ImageReaderSpi;

public class TestImageReaderSpiProvider implements ImageReaderSpiProvider {

	@Override
	public ImageReaderSpi getImageReaderSpi() throws Exception {
		return new TestImageReaderSpi();
	}

}
