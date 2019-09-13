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

package com.liferay.document.library.internal.image.reader.spi.provider;

import com.liferay.document.library.image.reader.spi.provider.ImageReaderSpiProvider;
import com.liferay.document.library.image.reader.spi.provider.ImageReaderSpiProviderHandler;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerList;
import com.liferay.osgi.service.tracker.collections.list.ServiceTrackerListFactory;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

import javax.imageio.spi.ImageReaderSpi;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Roberto Díaz
 */
@Component(immediate = true, service = ImageReaderSpiProviderHandler.class)
public class ImageReaderSpiProviderHandlerImpl implements
	ImageReaderSpiProviderHandler {

	@Activate
	@Modified
	protected void activate(BundleContext bundleContext) {
		_serviceTrackerList = ServiceTrackerListFactory.open(
			bundleContext, ImageReaderSpiProvider.class);
	}

	@Deactivate
	protected void deactivate() {
		_serviceTrackerList.close();
	}

	@Override
	public List<ImageReaderSpi> getImageReaderSpis() throws Exception {
		List<ImageReaderSpi> imageReaderSpies = new ArrayList<>();

		for (ImageReaderSpiProvider imageReaderSpiProvider :
				_serviceTrackerList) {

			imageReaderSpies.add(imageReaderSpiProvider.getImageReaderSpi());
		}

		return imageReaderSpies;
	}

	private ServiceTrackerList<ImageReaderSpiProvider, ImageReaderSpiProvider>
		_serviceTrackerList;

}
