package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * Date: 7/14/13
 * Time: 11:07 PM
 *
 * @author ioanbsu
 */

public interface ITakePictureModel extends Serializable {
    CameraType getCameraType();

    void setCameraType(CameraType cameraType);

    boolean isHighQuality();

    void setHighQuality(boolean highQuality);
}
