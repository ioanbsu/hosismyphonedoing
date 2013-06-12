/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.channel;


import com.google.gwt.core.client.JavaScriptObject;

/**
 * GWT implementation of {@link Channel}.
 */
public class ChannelImpl extends JavaScriptObject implements Channel {

  protected ChannelImpl() {
  }

  @Override
  public final native Socket open(SocketListener listener) /*-{
    var socket = this.open();

    socket.onopen = function(event) {
      listener.@com.artigile.howismyphonedoing.client.channel.SocketListener::onOpen()();
    };

    socket.onmessage = function(event) {
      listener.@com.artigile.howismyphonedoing.client.channel.SocketListener::onMessage(Ljava/lang/String;)(event.data);
    };

    socket.onerror = function(error) {
      listener.@com.artigile.howismyphonedoing.client.channel.SocketListener::onError(Lcom/artigile/howismyphonedoing/client/channel/ChannelError;)(error);
    };

    socket.onclose = function() {
      listener.@com.artigile.howismyphonedoing.client.channel.SocketListener::onClose()();
    };

    return socket;
  }-*/;

}