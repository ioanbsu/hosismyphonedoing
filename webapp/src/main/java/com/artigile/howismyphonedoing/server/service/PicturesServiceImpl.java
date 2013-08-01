package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.api.model.IPictureReadyModel;
import com.artigile.howismyphonedoing.server.dao.PicturesFromDeviceDao;
import com.artigile.howismyphonedoing.server.entity.PicturesFromDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 7/16/13
 * Time: 5:18 PM
 */
@Service
public class PicturesServiceImpl implements PicturesService {
    @Autowired
    private PicturesFromDeviceDao picturesFromDeviceDao;

    @Override
    public String storePicture(IPictureReadyModel pictureReadyModel, String deviceUuid) {
        PicturesFromDevice picturesFromDevice = new PicturesFromDevice();
        picturesFromDevice.setPictureData(pictureReadyModel.getPictureData());
        picturesFromDevice.setDeviceUuid(deviceUuid);
        String pictureUuid = deviceUuid + System.nanoTime();
        picturesFromDevice.setPictureUuid(pictureUuid);
        picturesFromDevice.setPictureName(SimpleDateFormat.getDateTimeInstance().format(new Date()));
        picturesFromDeviceDao.savePicture(picturesFromDevice);
        return pictureUuid;
    }

    @Override
    public PicturesFromDevice getPicture(String pictureUuid) {
        return picturesFromDeviceDao.getPictureByUuid(pictureUuid);
    }

    @Override
    public List<PicturesFromDevice> getPicturesByDeviceUid(String deviceId) {
         return picturesFromDeviceDao.getPicturesByDeviceUid(deviceId);
    }
}
