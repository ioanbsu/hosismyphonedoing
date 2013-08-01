package com.artigile.howismyphonedoing.shared.entity;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 7/31/13
 * Time: 5:33 PM
 */
public class PictureCellEntity implements Serializable {

    private String pictureName;

    private String pictureIconUrl;

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureIconUrl() {
        return pictureIconUrl;
    }

    public void setPictureIconUrl(String pictureIconUrl) {
        this.pictureIconUrl = pictureIconUrl;
    }
}
