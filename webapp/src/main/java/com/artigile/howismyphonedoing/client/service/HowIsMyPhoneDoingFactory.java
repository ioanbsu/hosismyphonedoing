package com.artigile.howismyphonedoing.client.service;

import com.artigile.howismyphonedoing.api.model.IDeviceLocationModel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * User: ioanbsu
 * Date: 6/7/13
 * Time: 2:53 PM
 */
public interface HowIsMyPhoneDoingFactory extends AutoBeanFactory {
    AutoBean<IDeviceLocationModel> deviceLocationModel();
}
