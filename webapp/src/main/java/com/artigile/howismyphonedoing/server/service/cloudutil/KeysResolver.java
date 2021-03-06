/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.service.cloudutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 5/23/13
 * Time: 6:38 PM
 */
@Service
public class KeysResolver {

    public static final String REPLACE_KEY_WORD = "REPLACE!!!";
    private static final String CLIENT_ID_KEY = "google.api.client.id";
    private static final String CLIENT_SECRET_KEY = "google.api.client.secret";
    private static final String PHONE_API_KEY = "phone.api.key";
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private String clientId;
    private String clientSecret;
    private String phoneApiKey;
    @Autowired
    private Environment env;

    @PostConstruct
    public void init() throws IOException {
        clientId = env.getProperty(CLIENT_ID_KEY);
        clientSecret = env.getProperty(CLIENT_SECRET_KEY);
        phoneApiKey = env.getProperty(PHONE_API_KEY);
        if (REPLACE_KEY_WORD.equals(clientId) || REPLACE_KEY_WORD.equals(clientSecret) || REPLACE_KEY_WORD.equals(phoneApiKey)) {
            logger.warning("Not all keys and google api secret values specified in configuration, application won't start.");
            throw new IllegalArgumentException("The client KEY or client secret should be configured in pom.xml file OR in settings.xml(preffered) file.");
        }

    }

    public String createStateKey() {
        // Create a state token to prevent request forgery.
        // Store it in the session for later validation.
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    public String getPhoneApiKey() {
        return phoneApiKey;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
