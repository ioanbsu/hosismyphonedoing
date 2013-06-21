/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.service;

import com.artigile.howismyphonedoing.api.model.*;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * User: ioanbsu
 * Date: 6/7/13
 * Time: 2:53 PM
 */
public interface HowIsMyPhoneDoingAutoBeansFactory extends AutoBeanFactory {
    AutoBean<IResponseFromServer> responseFromServer();

    AutoBean<IDeviceLocationModel> deviceLocationModel();

    AutoBean<IDeviceRegistrationModel> deviceRegistrationModel();

    AutoBean<IMessageNotSupportedByDeviceResponseModel> messageTypNotSupportedResponseModel();

    AutoBean<IMessageToDeviceModel> messageToDeviceModel();
}
