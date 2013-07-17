package com.artigile.checkmyphone.service;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import com.artigile.checkmyphone.TakePictureActivity;
import com.artigile.howismyphonedoing.api.model.TakePictureModel;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date: 7/14/13
 * Time: 8:21 PM
 *
 * @author ioanbsu
 */
@Singleton
public class CameraService {
    @Inject
    private Context context;
    private Camera camera;
    private CameraHolderListener initCameraListener;


    public void takePicture(TakePictureModel takePictureModel) throws DeviceHasNoCameraException {
        ActivityAndBroadcastUtils.startCameraActivity(context,takePictureModel);
    }


    /**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera(Camera mCamera) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }

    Camera.PictureCallback mCall = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(getOutputMediaFile());
                outStream.write(data);
                outStream.close();
                stopPreviewAndFreeCamera(camera);
                if (initCameraListener != null) {
                    initCameraListener.initCamera();
                }
            } catch (FileNotFoundException e) {
                Log.d("CAMERA", e.getMessage());
            } catch (IOException e) {
                Log.d("CAMERA", e.getMessage());
            }

        }
    };


    public File getOutputMediaFile() {
        File mediaStorageDir = getMagicMazeVideosDir();
        if (mediaStorageDir == null) return null;


        // Create a media file name
        File mediaFile;
        String filePath = mediaStorageDir.getPath() + File.separator + "/" + new Date().getTime() + SimpleDateFormat.getDateTimeInstance().format(new Date()) + ".jpg";
        filePath = filePath.replace(" ", "_").replace(",", "_").replace(":", "_");
        mediaFile = new File(filePath);

        return mediaFile;
    }

    public File getMagicMazeVideosDir() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "HowIsMyPhoneDoing");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MagicMaze", "failed to create directory");
                return null;
            }
        }
        return mediaStorageDir;
    }


    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setInitCameraListener(CameraHolderListener initCameraListener) {
        this.initCameraListener = initCameraListener;
    }
}
