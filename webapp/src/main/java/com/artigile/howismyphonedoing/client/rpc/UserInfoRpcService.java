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

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.exception.UserHasNoDevicesException;
import com.artigile.howismyphonedoing.shared.RpcConstants;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 4:30 PM
 */
@RemoteServiceRelativePath(RpcConstants.RPC_ENTRY_POINT + RpcConstants.USER_INFO_SERVICE)
public interface UserInfoRpcService extends RemoteService {


    List<UserDeviceModel> getUsersDevicesList() throws UserHasNoDevicesException;
}
