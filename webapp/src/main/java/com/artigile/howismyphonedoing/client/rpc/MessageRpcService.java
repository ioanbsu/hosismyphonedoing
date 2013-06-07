package com.artigile.howismyphonedoing.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../remoteService/mainHowIsMyPhoneDoing")
public interface MessageRpcService extends RemoteService {
  String sendSimpleTextMessage(String name) throws IllegalArgumentException;

    String getPhoneInfo();

    String getPhoneLocation();

}
