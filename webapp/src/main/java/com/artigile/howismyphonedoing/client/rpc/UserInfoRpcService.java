package com.artigile.howismyphonedoing.client.rpc;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 4:30 PM
 */
@RemoteServiceRelativePath("../remoteService/userInfoService")
public interface UserInfoRpcService extends RemoteService{


    List<UserDeviceModel> getUsersDevicesList();
}
