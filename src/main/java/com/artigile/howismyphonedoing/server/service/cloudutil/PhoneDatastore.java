package com.artigile.howismyphonedoing.server.service.cloudutil;

/**
 * User: ioanbsu
 * Date: 5/23/13
 * Time: 6:12 PM
 */

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Simple implementation of a data store using standard Java collections.
 * <p/>
 * This class is thread-safe but not persistent (it will lost the data when the
 * app is restarted) - it is meant just as an example.
 */
@Deprecated
public final class PhoneDatastore {

    private static final List<String> regIds = new ArrayList<String>();
    private static final Logger logger =
            Logger.getLogger(PhoneDatastore.class.getName());

    private PhoneDatastore() {
        throw new UnsupportedOperationException();
    }

    /**
     * Registers a device.
     */
    public static void register(String regId) {
        logger.info("Registering " + regId);
        synchronized (regIds) {
            regIds.add(regId);
        }
    }

    /**
     * Unregisters a device.
     */
    public static void unregister(String regId) {
        logger.info("Unregistering " + regId);
        synchronized (regIds) {
            regIds.remove(regId);
        }
    }

    /**
     * Updates the registration id of a device.
     */
    public static void updateRegistration(String oldId, String newId) {
        logger.info("Updating " + oldId + " to " + newId);
        synchronized (regIds) {
            regIds.remove(oldId);
            regIds.add(newId);
        }
    }

    /**
     * Gets all registered devices.
     */
    public static List<String> getDevices() {
        synchronized (regIds) {
            return new ArrayList<String>(regIds);
        }
    }

}
