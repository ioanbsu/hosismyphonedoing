package com.artigile.howismyphonedoing.api.model;

/**
 * Date: 7/14/13
 * Time: 11:06 PM
 *
 * @author ioanbsu
 */

public class TakePictureModel implements ITakePictureModel {

    private CameraType cameraType;

    private boolean highQuality;

    @Override
    public CameraType getCameraType() {
        return cameraType;
    }

    @Override
    public void setCameraType(CameraType cameraType) {
        this.cameraType = cameraType;
    }

    @Override
    public boolean isHighQuality() {
        return highQuality;
    }

    @Override
    public void setHighQuality(boolean highQuality) {
        this.highQuality = highQuality;
    }
}
