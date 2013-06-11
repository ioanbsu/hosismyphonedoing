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

    StateAndChanelEntity getLoggedInUser() throws UserNotLoggedInException;

    StateAndChanelEntity validateGooglePlusCallback(GooglePlusAuthenticatedUser googlePlusAuthenticatedUser) throws Exception;

    void logout();
}
