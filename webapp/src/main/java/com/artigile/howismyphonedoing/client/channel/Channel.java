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
 * AppEngine Channel API Channel.
 *
 * Channels are created by the {@link ChannelFactory}.
 */
public interface Channel {

  /**
   * Open a socket on this channel and attaches the given listener.
   */
  public Socket open(SocketListener listener);

}