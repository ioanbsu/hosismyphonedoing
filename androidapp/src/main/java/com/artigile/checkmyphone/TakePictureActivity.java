package com.artigile.checkmyphone;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.IOException;


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
                stopPreviewAndFreeCamera();
                new AsyncTask<byte[], Void, Void>() {

                    @Override
                    protected Void doInBackground(byte[]... params) {
                        commonUtilities.firePictureTakenEvent(TakePictureActivity.this, params[0]);
                        return null;
                    }
                }.doInBackground(data);
                finish();
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

    /**
     * When this function returns, mCamera will be null.
     */
    private void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            mCamera.release();
        }
    }
}
