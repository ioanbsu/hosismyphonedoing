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