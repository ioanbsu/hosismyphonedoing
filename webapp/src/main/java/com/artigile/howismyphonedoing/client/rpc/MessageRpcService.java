/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.rpc;

import com.artigile.howismyphonedoing.api.model.MessageToDeviceModel;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../remoteService/mainHowIsMyPhoneDoing")
public interface MessageRpcService extends RemoteService {
  String sendMessageToDevice(MessageToDeviceModel name) throws Exception;

    String getPhoneInfo();

    String getPhoneLocation();

    String removeAllEntities();
}
