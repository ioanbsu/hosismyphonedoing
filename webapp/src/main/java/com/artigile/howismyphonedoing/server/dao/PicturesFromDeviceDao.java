package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.entity.PicturesFromDevice;

import java.util.List;

/**
 * User: ioanbsu
 * Date: 7/16/13
 * Time: 5:20 PM
 */
public interface PicturesFromDeviceDao {

    void savePicture(PicturesFromDevice picturesFromDevice);

    PicturesFromDevice getPictureByUuid(String uuid);

    List<PicturesFromDevice> getPicturesByDeviceUid(String deviceId);

    void removePicture(String pictureId);

    void removePicturesFromTheDevice(String deviceId);
}
