/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author IoaN, 5/28/13 9:30 PM
 */
@Repository
@Transactional(readOnly = true)
public class UserAndDeviceDaoImpl implements UserAndDeviceDao {

    @Autowired
    @Qualifier("pmfTransactionAware")
    private PersistenceManagerFactory pmfTransationAware;

    @Override
    public void register(UserDevice userDevice) {
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        try {
            pm.makePersistent(userDevice);
        } finally {
            pm.close();
        }
    }

    @Override
    public String unregister(String registeredDeviceId) {
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        String userEmail = null;
        try {
            UserDevice userDevice = pm.getObjectById(UserDevice.class, registeredDeviceId);
            if (userDevice != null) {
                userEmail = userDevice.getUserEmail();
                pm.deletePersistent(userDevice);
            }
        } finally {
            pm.close();
        }
        return userEmail;
    }

    @Override
    public void updateRegistration(String oldDeviceRegistrationId, String newDeviceRegistrationId) {
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        try {
            Query query = pm.newQuery(UserDevice.class, "deviceCloudRegistrationId == deviceCloudRegistrationIdParam");
            query.declareParameters("String deviceCloudRegistrationIdParam");
            UserDevice userDevice = (UserDevice) query.execute(oldDeviceRegistrationId);
            userDevice.setUuid(newDeviceRegistrationId);
            pm.makePersistent(userDevice);
        } finally {
            pm.close();
        }
    }

    @Override
    public Set<UserDevice> getDevices(String userEmail) {
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        try {
            Query query = pm.newQuery(UserDevice.class, "userEmail == emailParam");
            query.declareParameters("String emailParam");
            List<UserDevice> userDevices = (List<UserDevice>) query.execute(userEmail);
            return new HashSet<UserDevice>(userDevices);
        } finally {
            pm.close();
        }

    }

    @Override
    public UserDevice getById(String uuid) {
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        try {
            return pm.getObjectById(UserDevice.class, uuid);
        } finally {
            pm.close();
        }
    }

    @Override
    public void removeAllUserDevices(String userEmailFromSession) {
        Set<UserDevice> userDevices = getDevices(userEmailFromSession);
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        try {
            pm.deletePersistentAll(userDevices);
        } finally {
            pm.close();
        }
    }

    public void cleanupDatabase() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        com.google.appengine.api.datastore.Query mydeleteq = new com.google.appengine.api.datastore.Query();
        PreparedQuery pq = datastore.prepare(mydeleteq);
        for (Entity result : pq.asIterable()) {
            try {
                datastore.delete(result.getKey());
            } catch (IllegalArgumentException e) {
            }
        }
    }

}
