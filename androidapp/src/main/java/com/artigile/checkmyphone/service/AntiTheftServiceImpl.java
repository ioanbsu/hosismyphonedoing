package com.artigile.checkmyphone.service;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import com.artigile.checkmyphone.service.admin.DeviceAdminReceiverImpl;
import com.artigile.howismyphonedoing.api.model.LockDeviceScreenModel;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 7/1/13
 * Time: 8:38 PM
 *
 * @author ioanbsu
 */

@Singleton
public class AntiTheftServiceImpl implements AntiTheftService {

    @Inject
    private Context context;

    @Override
    public void lockDevice(LockDeviceScreenModel lockDeviceScreenModel) throws DeviceAdminIsNotEnabledException {
        DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (isAntiTheftEnabled()) {
            mDPM.lockNow();
            if (!Strings.isNullOrEmpty(lockDeviceScreenModel.getNewPinCode())) {
                mDPM.resetPassword(lockDeviceScreenModel.getNewPinCode(), 0);
            }
        } else {
            throw new DeviceAdminIsNotEnabledException();
        }
    }

    @Override
    public void wipeDevice() throws DeviceAdminIsNotEnabledException {
        DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (isAntiTheftEnabled()) {
            mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);

        } else {
            throw new DeviceAdminIsNotEnabledException();
        }
    }

    @Override
    public boolean isAntiTheftEnabled() {
        ComponentName mDeviceAdminSample = new ComponentName(context, DeviceAdminReceiverImpl.class);
        DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        return mDPM.isAdminActive(mDeviceAdminSample);
    }
}
