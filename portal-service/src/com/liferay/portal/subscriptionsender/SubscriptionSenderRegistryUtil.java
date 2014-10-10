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

package com.liferay.portal.subscriptionsender;

import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceRegistration;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;
import com.liferay.registry.collections.ServiceRegistrationMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author Roberto Díaz
 */
public class SubscriptionSenderRegistryUtil {

	public static SubscriptionSender getSubscriptionSender(String className) {
		return _instance._getSubscriptionSender(className);
	}

	public static List<SubscriptionSender> getSubscriptionSenders() {
		return _instance._getSubscriptionSenders();
	}

	public static void register(List<SubscriptionSender> subscriptionSenders) {
		for (SubscriptionSender subscriptionSender : subscriptionSenders) {
			register(subscriptionSender);
		}
	}

	public static void register(SubscriptionSender subscriptionSender) {
		_instance._register(subscriptionSender);
	}

	public static void unregister(
		List<SubscriptionSender> subscriptionSenders) {

		for (SubscriptionSender subscriptionSender : subscriptionSenders) {
			unregister(subscriptionSender);
		}
	}

	public static void unregister(SubscriptionSender subscriptionSender) {
		_instance._unregister(subscriptionSender);
	}

	private SubscriptionSenderRegistryUtil() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceTracker = registry.trackServices(
			SubscriptionSender.class,
			new SubscriptionSenderServiceTrackerCustomizer());

		_serviceTracker.open();
	}

	private SubscriptionSender _getSubscriptionSender(String className) {
		return _subscriptionSenders.get(className);
	}

	private List<SubscriptionSender> _getSubscriptionSenders() {
		return ListUtil.fromMapValues(_subscriptionSenders);
	}

	private void _register(SubscriptionSender subscriptionSender) {
		Registry registry = RegistryUtil.getRegistry();

		ServiceRegistration<SubscriptionSender> serviceRegistration =
			registry.registerService(
				SubscriptionSender.class, subscriptionSender);

		_serviceRegistrations.put(subscriptionSender, serviceRegistration);
	}

	private void _unregister(SubscriptionSender subscriptionSender) {
		ServiceRegistration<SubscriptionSender> serviceRegistration =
			_serviceRegistrations.remove(subscriptionSender);

		if (serviceRegistration != null) {
			serviceRegistration.unregister();
		}
	}

	private static SubscriptionSenderRegistryUtil _instance =
		new SubscriptionSenderRegistryUtil();

	private ServiceRegistrationMap<SubscriptionSender> _serviceRegistrations =
		new ServiceRegistrationMap<SubscriptionSender>();
	private ServiceTracker<SubscriptionSender, SubscriptionSender>
		_serviceTracker;
	private Map<String, SubscriptionSender> _subscriptionSenders =
		new ConcurrentSkipListMap<String, SubscriptionSender>();

	private class SubscriptionSenderServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<SubscriptionSender, SubscriptionSender> {

		@Override
		public SubscriptionSender addingService(
			ServiceReference<SubscriptionSender> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			SubscriptionSender subscriptionSender = registry.getService(
				serviceReference);

			_subscriptionSenders.put(
				subscriptionSender.getServiceName(), subscriptionSender);

			return subscriptionSender;
		}

		@Override
		public void modifiedService(
			ServiceReference<SubscriptionSender> serviceReference,
			SubscriptionSender subscriptionSender) {
		}

		@Override
		public void removedService(
			ServiceReference<SubscriptionSender> serviceReference,
			SubscriptionSender subscriptionSender) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);

			_subscriptionSenders.remove(subscriptionSender.getClassName());
		}

	}

}