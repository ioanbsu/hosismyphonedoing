package com.artigile.howismyphonedoing.server.entity;


import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author IoaN, 5/28/13 9:25 PM
 */
@PersistenceCapable
public class UserDevice {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String uuid;
    @Persistent
    private String deviceCloudRegistrationId;
    @Persistent
    private String userEmail;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDeviceCloudRegistrationId() {
        return deviceCloudRegistrationId;
    }

    public void setDeviceCloudRegistrationId(String deviceCloudRegistrationId) {
        this.deviceCloudRegistrationId = deviceCloudRegistrationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDevice that = (UserDevice) o;

        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
