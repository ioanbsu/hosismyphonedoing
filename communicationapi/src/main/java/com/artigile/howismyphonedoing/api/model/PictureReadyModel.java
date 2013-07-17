package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 7/16/13
 * Time: 4:52 PM
 */
public class PictureReadyModel implements IPictureReadyModel {

    private byte[] pictureData;

    private String pictureId;

    @Override
    public byte[] getPictureData() {
        return pictureData;
    }

    @Override
    public void setPictureData(byte[] pictureData) {
        this.pictureData = pictureData;
    }

    @Override
    public String getPictureId() {
        return pictureId;
    }

    @Override
    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }
}
