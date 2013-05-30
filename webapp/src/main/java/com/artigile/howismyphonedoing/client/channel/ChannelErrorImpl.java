package com.artigile.howismyphonedoing.client.channel;


import com.google.gwt.core.client.JavaScriptObject;

/**
 * GWT implementation of {@link ChannelError}.
 */
public class ChannelErrorImpl extends JavaScriptObject implements ChannelError {

  protected ChannelErrorImpl() {
  }

  @Override
  public final native String getDescription() /*-{
    return this.description;
  }-*/;

  @Override
  public final native String getCode() /*-{
    return this.code;
  }-*/;

}