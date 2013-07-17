package com.artigile.checkmyphone.service;

import android.app.admin.DevicePolicyManager;
import android.content.*;
import com.artigile.checkmyphone.service.admin.DeviceAdminReceiverImpl;
import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.model.*;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

import static com.artigile.checkmyphone.service.CommonUtilities.MESSAGE;
import static com.artigile.checkmyphone.service.CommonUtilities.PICTURE_TAKEN_ACTION;

/**
 * Date: 7/1/13
 * Time: 8:38 PM
 *
 * @author ioanbsu
 */

@Singleton
public class AntiTheftServiceImpl implements AntiTheftService {
    private final BroadcastReceiver mHandleMessageReceiver;
    @Inject
    private Context context;
    @Inject
    private CameraService cameraService;
    @Inject
    private AndroidMessageSender messageSender;
    @Inject
    private MessageParser messageParser;


    public AntiTheftServiceImpl() {
        mHandleMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                byte[] data = intent.getExtras().getByteArray(MESSAGE);
                IPictureReadyModel pictureReadyModel = new PictureReadyModel();
                pictureReadyModel.setPictureData(data);
                try {
                    messageSender.processMessage(MessageType.PICTURE_READY, messageParser.serialize(pictureReadyModel), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

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

    @Override
    public void takePicture(TakePictureModel lockDeviceScreenModel) throws DeviceHasNoCameraException {
        context.registerReceiver(mHandleMessageReceiver, new IntentFilter(PICTURE_TAKEN_ACTION));
        cameraService.takePicture();

    }


}
