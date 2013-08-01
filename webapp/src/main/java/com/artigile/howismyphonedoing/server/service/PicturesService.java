package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.api.model.IPictureReadyModel;
import com.artigile.howismyphonedoing.server.entity.PicturesFromDevice;

import java.util.List;

/**
 * User: ioanbsu
 * Date: 7/16/13
 * Time: 5:16 PM
 */
public interface PicturesService {

    /**
     * Stores picture into the datastore and returns uuid of the picture
     * @param pictureReadyModel model of the picture returned from the device
     * @param deviceUuid device uuid
     * @return stored picture uuid.
     */
    String storePicture(IPictureReadyModel pictureReadyModel,String deviceUuid);

    PicturesFromDevice getPicture(String pictureUuid);

    List<PicturesFromDevice> getPicturesByDeviceUid(String deviceId);

    void removePicture(String pictureId);

    void removePicturesFromTheDevice(String deviceId);
}
