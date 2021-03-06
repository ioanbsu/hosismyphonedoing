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

import com.google.inject.ImplementedBy;

/**
 * Creates new {@link Channel}s.
 *
 * The App Engine Channel API script must be installed before the GWT application, per the
 * instructions at http://code.google.com/appengine/docs/java/channel/javascript.html
 */
@ImplementedBy(ChannelFactoryImpl.class)
public interface ChannelFactory {

  /**
   * Creates a new {@code Channel} object with the given token. The token must be a valid Channel
   * API token created by App Engine's channel service.
   *
   * @param token
   * @return a new {@code Channel}
   */
  Channel createChannel(String token);

}