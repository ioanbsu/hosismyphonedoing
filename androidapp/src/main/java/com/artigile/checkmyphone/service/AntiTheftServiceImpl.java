package com.artigile.checkmyphone.service;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.provider.MediaStore;
import android.util.Log;
import com.artigile.checkmyphone.service.admin.DeviceAdminReceiverImpl;
import com.artigile.howismyphonedoing.api.model.LockDeviceScreenModel;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

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

    private Camera mCamera;

   private CameraPreview mPreview;

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
    public void takePicture() throws DeviceHasNoCameraException {
        if(mPreview==null){
            mPreview=new CameraPreview(context);
        }
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            throw new DeviceHasNoCameraException();
        } else {
           setCamera(Camera.open());
        }
    }




    public void setCamera(Camera camera) {
        if (mCamera == camera) { return; }

        stopPreviewAndFreeCamera();

        mCamera = camera;

        if (mCamera != null) {
            List<Camera.Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
//            mSupportedPreviewSizes = localSizes;
//            context.requestLayout();

            try {
                mCamera.setPreviewDisplay(mPreview.getmHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
              Important: Call startPreview() to start updating the preview surface. Preview must
              be started before you can take a picture.
              */
            mCamera.startPreview();
        }
    }

    private boolean safeCameraOpen(int id) {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e("blablabla", "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }
    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


/**
  * When this function returns, mCamera will be null.
  */
private void stopPreviewAndFreeCamera() {

    if (mCamera != null) {
        /*
          Call stopPreview() to stop updating the preview surface.
        */
        mCamera.stopPreview();

        /*
          Important: Call release() to release the camera for use by other applications.
          Applications should release the camera immediately in onPause() (and re-open() it in
          onResume()).
        */
        mCamera.release();

        mCamera = null;
    }
}

}
