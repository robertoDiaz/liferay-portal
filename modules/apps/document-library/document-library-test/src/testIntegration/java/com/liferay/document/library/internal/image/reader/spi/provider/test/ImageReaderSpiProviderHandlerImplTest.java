package com.liferay.document.library.internal.image.reader.spi.provider.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.document.library.image.reader.spi.provider.ImageReaderSpiProvider;
import com.liferay.document.library.image.reader.spi.provider.ImageReaderSpiProviderHandler;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.spi.ImageReaderSpi;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

@RunWith(Arquillian.class)
public class ImageReaderSpiProviderHandlerImplTest {

	@Test
	public void getImageReaderSpis() throws Exception {
		ServiceRegistration<ImageReaderSpiProvider>
			imageReaderSpiProviderServiceRegistration =
				_bundleContext.registerService(
					ImageReaderSpiProvider.class,
					new TestImageReaderSpiProvider(), new Hashtable<>());

		List<ServiceRegistration> serviceRegistrations = new ArrayList<>();

		serviceRegistrations.add(imageReaderSpiProviderServiceRegistration);

		try {
			List<ImageReaderSpi> imageReaderSpis =
				_imageReaderSpiProviderHandler.getImageReaderSpis();

			for (ImageReaderSpi imageReaderSpi : imageReaderSpis) {
				if (imageReaderSpi instanceof TestImageReaderSpi) {
					return;
				}
			}

			Assert.fail();
		}
		finally {
			serviceRegistrations.forEach(ServiceRegistration::unregister);
		}
	}

	@Before
	public void setUp() throws Exception {
		_bundle = FrameworkUtil.getBundle(
			ImageReaderSpiProviderHandlerImplTest.class);

		_bundleContext = _bundle.getBundleContext();

		_serviceReference = _bundleContext.getServiceReference(
			ImageReaderSpiProviderHandler.class);

		_imageReaderSpiProviderHandler = _bundleContext.getService(
			_serviceReference);
	}

	@After
	public void tearDown() {
		_bundleContext.ungetService(_serviceReference);
	}

	private Bundle _bundle;
	private BundleContext _bundleContext;
	private ImageReaderSpiProviderHandler _imageReaderSpiProviderHandler;
	private ServiceReference<ImageReaderSpiProviderHandler> _serviceReference;

}