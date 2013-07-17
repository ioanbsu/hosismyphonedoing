package com.artigile.checkmyphone.service;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import com.artigile.checkmyphone.service.admin.DeviceAdminReceiverImpl;
import com.artigile.howismyphonedoing.api.MessageParser;
import com.artigile.howismyphonedoing.api.model.*;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * Date: 7/1/13
 * Time: 8:38 PM
 *
 * @author ioanbsu
 */

@Singleton
public class AntiTheftServiceImpl implements AntiTheftService {
    //    private final BroadcastReceiver mHandleMessageReceiver;
    @Inject
    private Context context;
    @Inject
    private CameraService cameraService;
    @Inject
    private AndroidMessageSender messageSender;
    @Inject
    private MessageParser messageParser;
    private EventBus eventBus;
    private boolean takePictureIsInProgress;


    @Inject
    public AntiTheftServiceImpl(EventBus eventBus) {
        this.eventBus = eventBus;
        /*mHandleMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("AntiTheftServiceImpl", "Picture data received in Anti theft service broadcast.");
                takePictureIsInProgress = false;
                byte[] data = intent.getExtras().getByteArray(MESSAGE);
                sendPictureReadyMessage(data);
            }
        };*/
        eventBus.register(new PictureReadyListener());
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
    public void takePicture(TakePictureModel takePictureModel) throws DeviceHasNoCameraException {
        if (!takePictureIsInProgress) {
//            context.registerReceiver(mHandleMessageReceiver, new IntentFilter(PICTURE_TAKEN_ACTION));
            cameraService.takePicture(takePictureModel);
            takePictureIsInProgress = true;
        } else {
            Log.i("AntiTheftServiceImpl", "Skipping picture shot since picture taking is in progress...");
        }
    }

    private void sendPictureReadyMessage(byte[] data) {
        IPictureReadyModel pictureReadyModel = new PictureReadyModel();
        pictureReadyModel.setPictureData(data);
        try {
            messageSender.processMessage(MessageType.PICTURE_READY, messageParser.serialize(pictureReadyModel), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class PictureReadyListener {
        @Subscribe
        public void pictureReady(PictureReadyEvent event) {
            sendPictureReadyMessage(event.getPictureBytes());
        }
    }


}
