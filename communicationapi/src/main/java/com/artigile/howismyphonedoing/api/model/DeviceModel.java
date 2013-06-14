package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 5/29/13
 * Time: 11:57 AM
 */
public class DeviceModel implements  IDeviceModel {
    private String board;
    private String bootLoader;
    private String brand;
    private String cpuAbi;
    private String cpuAbi2;
    private String device;
    private String display;
    private String fingerprint;
    private String hardware;
    private String host;
    private String id;
    private String manufacturer;
    private String model;
    private String product;
    private String serial;
    private String tags;
    private Long time;
    private String type;
    private String unknown;
    private String user;
    private String radioVersion;

    @Override
    public String getBoard() {
        return board;
    }

    @Override
    public void setBoard(String board) {
        this.board = board;
    }

    @Override
    public String getBootLoader() {
        return bootLoader;
    }

    @Override
    public void setBootLoader(String bootLoader) {
        this.bootLoader = bootLoader;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String getCpuAbi() {
        return cpuAbi;
    }

    @Override
    public void setCpuAbi(String cpuAbi) {
        this.cpuAbi = cpuAbi;
    }

    @Override
    public String getCpuAbi2() {
        return cpuAbi2;
    }

    @Override
    public void setCpuAbi2(String cpuAbi2) {
        this.cpuAbi2 = cpuAbi2;
    }

    @Override
    public String getDevice() {
        return device;
    }

    @Override
    public void setDevice(String device) {
        this.device = device;
    }

    @Override
    public String getDisplay() {
        return display;
    }

    @Override
    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String getFingerprint() {
        return fingerprint;
    }

    @Override
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    @Override
    public String getHardware() {
        return hardware;
    }

    @Override
    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String getSerial() {
        return serial;
    }

    @Override
    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public String getTags() {
        return tags;
    }

    @Override
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public Long getTime() {
        return time;
    }

    @Override
    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getUnknown() {
        return unknown;
    }

    @Override
    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String getRadioVersion() {
        return radioVersion;
    }

    @Override
    public void setRadioVersion(String radioVersion) {
        this.radioVersion = radioVersion;
    }


}
