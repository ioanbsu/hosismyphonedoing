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

import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.exception.UserNotLoggedInException;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author IoaN, 5/26/13 10:41 AM
 */
public abstract class AsyncCallbackImpl<T> implements AsyncCallback<T> {


    private AfterRpcResponceHandler afterRpcResponceHandler;
    private MainEventBus eventBus;

    protected AsyncCallbackImpl(MainEventBus eventBus) {
        this(eventBus, null);
    }

    protected AsyncCallbackImpl(MainEventBus eventBus, AfterRpcResponceHandler afterRpcResponceHandler) {
        this.eventBus = eventBus;
        this.afterRpcResponceHandler = afterRpcResponceHandler;
    }

    public void success(T result) {

    }

    public void failure(Throwable caught) {

    }

    @Override
    public final void onFailure(Throwable caught) {
        if (caught instanceof UserNotLoggedInException) {
            eventBus.showLoginWindow(caught.getMessage());
        }
        failure(caught);
        doAfterResponse();

    }

    private void doAfterResponse() {
        if (afterRpcResponceHandler != null) {
            afterRpcResponceHandler.afterResponse();
        }
    }

    @Override
    public final void onSuccess(T result) {
        success(result);
        doAfterResponse();
    }

    public static interface AfterRpcResponceHandler {
        void afterResponse();
    }
}
