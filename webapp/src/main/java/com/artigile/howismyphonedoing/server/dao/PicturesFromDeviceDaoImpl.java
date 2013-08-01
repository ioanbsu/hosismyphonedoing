package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.entity.PicturesFromDevice;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.List;


/**
 * User: ioanbsu
 * Date: 7/16/13
 * Time: 5:20 PM
 */
@Repository
@Transactional(readOnly = true)
public class PicturesFromDeviceDaoImpl implements PicturesFromDeviceDao {

    @Autowired
    @Qualifier("pmfTransactionAware")
    private PersistenceManagerFactory pmfTransationAware;

    @Override
    public void savePicture(PicturesFromDevice picturesFromDevice) {
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        try {
            pm.makePersistent(picturesFromDevice);
        } finally {
            pm.close();
        }
    }

    @Override
    public PicturesFromDevice getPictureByUuid(String uuid) {
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        try {
            return pm.getObjectById(PicturesFromDevice.class, uuid);
        } finally {
            pm.close();
        }
    }

    @Override
    public List<PicturesFromDevice> getPicturesByDeviceUid(String deviceId) {
        PersistenceManager pm = pmfTransationAware.getPersistenceManager();
        try {
            Query query = pm.newQuery(PicturesFromDevice.class, "deviceUuid == deviceUuidParam");
            query.declareParameters("String deviceUuidParam");
            List<PicturesFromDevice> userDevices = (List<PicturesFromDevice>) query.execute(deviceId);
            return userDevices;
        } finally {
            pm.close();
        }
    }
}
