/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author IoaN, 5/26/13 10:41 AM
 */
public abstract class AsyncCallbackImpl<T> implements AsyncCallback<T> {

    public void success(T result) {

    }

    public void failure(Throwable caught) {

    }

    @Override
    public final void onFailure(Throwable caught) {
        failure(caught);
    }

    @Override
    public final void onSuccess(T result) {
        success(result);
    }
}
