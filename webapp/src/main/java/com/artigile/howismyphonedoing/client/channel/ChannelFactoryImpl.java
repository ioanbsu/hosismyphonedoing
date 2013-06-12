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

import javax.inject.Singleton;

/**
 * GWT implementation of {@link ChannelFactory}.
 *
 * Uses the App Engine Channel API JavaScript library to create channels.
 */
@Singleton
public class ChannelFactoryImpl implements ChannelFactory {

  @Override
  public final native Channel createChannel(String token) /*-{
    return new $wnd.goog.appengine.DevChannel(token);
  }-*/;

}