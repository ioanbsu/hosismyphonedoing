package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.stereotype.Service;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author IoaN, 5/28/13 9:30 PM
 */
@Service
public class UserAndDeviceDaoImpl implements UserAndDeviceDao {

    private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    @Override
    public void register(UserDevice userDevice) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            pm.makePersistent(userDevice);
        } finally {
            pm.close();
        }
    }

    @Override
    public void unregister(String registeredDeviceId) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            UserDevice userDevice = pm.getObjectById(UserDevice.class, registeredDeviceId);
            if (userDevice != null) {
                pm.deletePersistent(userDevice);
            }
        } finally {
            pm.close();
        }
    }

    @Override
    public void updateRegistration(String oldDeviceRegistrationId, String newDeviceRegistrationId) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
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
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        Query query = pm.newQuery(UserDevice.class, "userEmail == emailParam");
        query.declareParameters("String emailParam");
        List<UserDevice> userDevices = (List<UserDevice>) query.execute(userEmail);
        return new HashSet<>(userDevices);

    }

    @Override
    public UserDevice getById(String uuid) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            return pm.getObjectById(UserDevice.class, uuid);
        } finally {
            pm.close();
        }
    }
}
