package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 7/16/13
 * Time: 5:41 PM
 */
public interface IPictureReadyModel extends Serializable {
    byte[] getPictureData();

    void setPictureData(byte[] pictureData);

    String getPictureId();

    void setPictureId(String pictureId);
}
