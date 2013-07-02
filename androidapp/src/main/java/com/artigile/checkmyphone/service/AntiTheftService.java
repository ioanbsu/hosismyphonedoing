package com.artigile.checkmyphone.service;

import android.app.admin.DevicePolicyManager;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 7/1/13
 * Time: 8:38 PM
 *
 * @author ioanbsu
 */

@Singleton
public class AntiTheftService {

    @Inject
    private Context context;

    public void lockDevice() {
        DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDPM.lockNow();
    }
}
