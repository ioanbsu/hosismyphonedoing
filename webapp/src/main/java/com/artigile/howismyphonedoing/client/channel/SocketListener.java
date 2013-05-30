package com.artigile.howismyphonedoing.client.channel;

/**
 * Listens for events on a {@link Socket}.
 */
public interface SocketListener {

  /**
   * Called when the socket is ready to receive messages.
   */
  void onOpen();

  /**
   * Called when the socket receives a message.
   */
  void onMessage(String message);

  /**
   * Called when an error occurs on the socket.
   */
  void onError(ChannelError error);

  /**
   * Called when the socket is closed. When the socket is closed, it cannot be reopened.
   */
  void onClose();

}