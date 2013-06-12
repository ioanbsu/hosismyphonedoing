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
 * App Engine Channel API socket object.
 */
public interface Socket {

  /**
   * Close the socket. The socket cannot be used again after calling close; the server must create
   * a new socket.
   */
  void close();

}