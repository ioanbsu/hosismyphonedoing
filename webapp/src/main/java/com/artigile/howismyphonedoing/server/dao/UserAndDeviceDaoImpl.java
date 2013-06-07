package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.stereotype.Service;

import javax.jdo.*;
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
    public void updateRegistration(String oldId, String newId) {
        PersistenceManager pm = pmfInstance.getPersistenceManager();
        try {
            UserDevice userDevice = pm.getObjectById(UserDevice.class, oldId);
            userDevice.setRegisteredId(newId);
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
}
