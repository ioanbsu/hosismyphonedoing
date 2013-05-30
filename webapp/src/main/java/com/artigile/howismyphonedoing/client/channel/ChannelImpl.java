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