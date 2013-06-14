package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/14/13
 * Time: 8:52 AM
 */
public interface IDeviceModel extends Serializable{
    String getBoard();

    void setBoard(String board);

    String getBootLoader();

    void setBootLoader(String bootLoader);

    String getBrand();

    void setBrand(String brand);

    String getCpuAbi();

    void setCpuAbi(String cpuAbi);

    String getCpuAbi2();

    void setCpuAbi2(String cpuAbi2);

    String getDevice();

    void setDevice(String device);

    String getDisplay();

    void setDisplay(String display);

    String getFingerprint();

    void setFingerprint(String fingerprint);

    String getHardware();

    void setHardware(String hardware);

    String getHost();

    void setHost(String host);

    String getId();

    void setId(String id);

    String getManufacturer();

    void setManufacturer(String manufacturer);

    String getModel();

    void setModel(String model);

    String getProduct();

    void setProduct(String product);

    String getSerial();

    void setSerial(String serial);

    String getTags();

    void setTags(String tags);

    Long getTime();

    void setTime(Long time);

    String getType();

    void setType(String type);

    String getUnknown();

    void setUnknown(String unknown);

    String getUser();

    void setUser(String user);

    String getRadioVersion();

    void setRadioVersion(String radioVersion);
}
