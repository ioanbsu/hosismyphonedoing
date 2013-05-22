package com.artigile.howismyphonedoing.server.service;

import org.springframework.stereotype.Component;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * @author IoaN, 5/21/13 10:32 PM
 */
@Component
public class PersistenceFactory {

    private final PersistenceManagerFactory pmfInstance =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");

    public PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
