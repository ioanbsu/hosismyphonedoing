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

import com.artigile.howismyphonedoing.client.exception.UserNotLoggedInException;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author IoaN, 5/26/13 10:35 AM
 */
@RemoteServiceRelativePath("../remoteService/authService")
public interface AuthRpcService extends RemoteService {

    StateAndChanelEntity getLoggedInUserAndCreateChannel() throws UserNotLoggedInException;

    StateAndChanelEntity validateGooglePlusCallback(GooglePlusAuthenticatedUser googlePlusAuthenticatedUser) throws Exception;

    void logout();

    String refreshStateToken();
}
