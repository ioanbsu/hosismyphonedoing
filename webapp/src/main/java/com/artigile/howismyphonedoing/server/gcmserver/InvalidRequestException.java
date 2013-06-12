/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */
package com.artigile.howismyphonedoing.server.gcmserver;

import java.io.IOException;

/**
 * Exception thrown when GCM returned an error due to an invalid request.
 * <p/>
 * This is equivalent to GCM posts that return an HTTP error different of 200.
 */
public final class InvalidRequestException extends IOException {

    private final int status;
    private final String description;

    public InvalidRequestException(int status) {
        this(status, null);
    }

    public InvalidRequestException(int status, String description) {
        super(getMessage(status, description));
        this.status = status;
        this.description = description;
    }

    private static String getMessage(int status, String description) {
        StringBuilder base = new StringBuilder("HTTP Status Code: ").append(status);
        if (description != null) {
            base.append("(").append(description).append(")");
        }
        return base.toString();
    }

    /**
     * Gets the HTTP Status Code.
     */
    public int getHttpStatusCode() {
        return status;
    }

    /**
     * Gets the error description.
     */
    public String getDescription() {
        return description;
    }

}
