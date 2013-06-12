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