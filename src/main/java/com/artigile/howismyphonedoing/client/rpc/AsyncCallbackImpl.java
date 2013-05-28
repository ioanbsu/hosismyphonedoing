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
