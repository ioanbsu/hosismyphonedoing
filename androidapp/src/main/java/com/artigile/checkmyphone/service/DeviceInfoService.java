package com.artigile.checkmyphone.service;

import android.os.Build;
import com.artigile.howismyphonedoing.api.model.DeviceModel;
import com.artigile.howismyphonedoing.api.model.IDeviceModel;

import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/14/13
 * Time: 8:54 AM
 */
@Singleton
public class DeviceInfoService {


    public IDeviceModel buildPhoneModel() {
        IDeviceModel deviceModel = new DeviceModel();
        deviceModel.setBoard(Build.BOARD);
        deviceModel.setModel(Build.MODEL);
        deviceModel.setBootLoader(Build.BOOTLOADER);
        deviceModel.setBrand(Build.BRAND);
        deviceModel.setCpuAbi(Build.CPU_ABI);
        deviceModel.setCpuAbi2(Build.CPU_ABI2);
        deviceModel.setDevice(Build.DEVICE);
        deviceModel.setDisplay(Build.DISPLAY);
        deviceModel.setFingerprint(Build.FINGERPRINT);
        deviceModel.setHardware(Build.HARDWARE);
        deviceModel.setHost(Build.HOST);
        deviceModel.setId(Build.ID);
        deviceModel.setManufacturer(Build.MANUFACTURER);
        deviceModel.setModel(Build.MODEL);
        deviceModel.setProduct(Build.PRODUCT);
        deviceModel.setSerial(Build.SERIAL);
        deviceModel.setTags(Build.TAGS);
        deviceModel.setTime(Build.TIME);
        deviceModel.setType(Build.TYPE);
        deviceModel.setUnknown(Build.UNKNOWN);
        deviceModel.setUser(Build.USER);
        deviceModel.setRadioVersion(Build.getRadioVersion());
        return deviceModel;
    }
}
