package com.artigile.howismyphonedoing.client.channel;


import com.google.gwt.core.client.JavaScriptObject;

/**
 *  Implementation of {@link Socket}.
 */
public class SocketImpl extends JavaScriptObject implements Socket {

  protected SocketImpl() {
  }

  @Override
  public final native void close() /*-{
    this.close();
  }-*/;

}