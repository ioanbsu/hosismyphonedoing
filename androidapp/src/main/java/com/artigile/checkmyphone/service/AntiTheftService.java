package com.artigile.checkmyphone.service;

import android.view.SurfaceView;
import com.artigile.howismyphonedoing.api.model.LockDeviceScreenModel;
import com.artigile.howismyphonedoing.api.model.TakePictureModel;
import com.google.inject.ImplementedBy;

/**
 * User: ioanbsu
 * Date: 7/8/13
 * Time: 7:52 PM
 */
@ImplementedBy(AntiTheftServiceImpl.class)
public interface AntiTheftService {
    void lockDevice(LockDeviceScreenModel lockDeviceScreenModel) throws DeviceAdminIsNotEnabledException;

    void wipeDevice() throws DeviceAdminIsNotEnabledException;

    boolean isAntiTheftEnabled();

    void takePicture(TakePictureModel lockDeviceScreenModel) throws DeviceHasNoCameraException;
}
