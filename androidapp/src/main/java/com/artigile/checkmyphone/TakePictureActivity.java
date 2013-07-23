package com.artigile.checkmyphone;

import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.artigile.checkmyphone.service.ActivityAndBroadcastUtils;
import com.artigile.checkmyphone.service.CameraService;
import com.artigile.checkmyphone.service.PictureReadyEvent;
import com.artigile.howismyphonedoing.api.CompressorUtil;
import com.artigile.howismyphonedoing.api.model.CameraType;
import com.artigile.howismyphonedoing.api.model.TakePictureModel;
import com.google.common.eventbus.EventBus;
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
    private ActivityAndBroadcastUtils commonUtilities;

    @Inject
    private EventBus eventBus;
    private Camera mCamera;
    private TakePictureModel takePictureModel;

    public TakePictureActivity() {
        mCall = new Camera.PictureCallback() {

            public void onPictureTaken(byte[] data, Camera camera) {
                stopPreviewAndFreeCamera();
                new AsyncTask<byte[], Void, Void>() {

                    @Override
                    protected Void doInBackground(byte[]... params) {
                        eventBus.post(new PictureReadyEvent(params[0]));
                        finish();
                        return null;
                    }
                }.doInBackground(CompressorUtil.compressBytes(data));
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture);
        takePictureModel = (TakePictureModel) this.getIntent().getExtras().get(ActivityAndBroadcastUtils.TAKE_PICTURE_CONFIG);
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
            mCamera = getCamera();
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(cameraSurfaceView.getHolder());
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPreviewSize(640, 480);
                parameters.setPictureFormat(ImageFormat.JPEG);
                if (takePictureModel != null) {
                    if (takePictureModel.isHighQuality()) {
                        Camera.Size size = parameters.getSupportedPictureSizes().get(0);
                        parameters.setPictureSize(size.width, size.height);
                        parameters.setJpegQuality(50);
                    }
                }else{
                    parameters.setJpegQuality(1);
                }
                //set camera parameters
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mCamera.takePicture(null, null, mCall);
                    }
                }, 300);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Camera getCamera() {
        if (takePictureModel != null) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            int facing = -1;
            if (takePictureModel.getCameraType() == CameraType.BACK) {
                facing = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else if (takePictureModel.getCameraType() == CameraType.FRONT) {
                facing = Camera.CameraInfo.CAMERA_FACING_FRONT;

            }
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, info);
                if (info.facing == facing) {
                    return Camera.open(i);
                }
            }

        }
        return Camera.open();
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
