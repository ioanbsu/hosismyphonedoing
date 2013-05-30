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