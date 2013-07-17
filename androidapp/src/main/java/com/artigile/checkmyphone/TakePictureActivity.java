package com.artigile.checkmyphone;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.artigile.checkmyphone.service.CameraService;
import com.artigile.checkmyphone.service.CommonUtilities;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

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
 * Time: 10:09 PM
 *
 * @author ioanbsu
 */
@Singleton
public class TakePictureActivity extends RoboActivity implements SurfaceHolder.Callback {
    private final Camera.PictureCallback mCall;
    @InjectView(R.id.cameraRecordPreviewTp)
    private SurfaceView cameraSurfaceView;
    @Inject
    private CommonUtilities commonUtilities;
    private Camera mCamera;

    public TakePictureActivity() {
        mCall = new Camera.PictureCallback() {

            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(getOutputMediaFile());
                    outStream.write(data);
                    outStream.close();
                    stopPreviewAndFreeCamera();
                    new AsyncTask<byte[], Void, Void>() {

                        @Override
                        protected Void doInBackground(byte[]... params) {
                            commonUtilities.firePictureTakenEvent(TakePictureActivity.this, params[0]);
                            return null;
                        }
                    }.doInBackground(data);
                    finish();
                } catch (FileNotFoundException e) {
                    Log.d("CAMERA", e.getMessage());
                } catch (IOException e) {
                    Log.d("CAMERA", e.getMessage());
                }

            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraSurfaceView.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCameraAndTakePicture();
    }

    private void initCameraAndTakePicture() {
        Log.i(CameraService.class.getName(), "Surface Created");
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            mCamera = Camera.open();
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(cameraSurfaceView.getHolder());
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(640, 480);
                //set camera parameters
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mCamera.takePicture(null, null, mCall);
                    }
                }, 500);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPreviewAndFreeCamera();
    }

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

    /**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            mCamera.release();
        }
    }
}
