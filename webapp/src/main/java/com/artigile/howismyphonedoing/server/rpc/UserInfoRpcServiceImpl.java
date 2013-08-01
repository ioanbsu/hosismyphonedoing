/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.exception.UserHasNoDevicesException;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcService;
import com.artigile.howismyphonedoing.server.entity.PicturesFromDevice;
import com.artigile.howismyphonedoing.server.service.PicturesService;
import com.artigile.howismyphonedoing.server.service.UserService;
import com.artigile.howismyphonedoing.shared.entity.PictureCellEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 4:30 PM
 */
@Service
public class UserInfoRpcServiceImpl extends AbstractRpcService implements UserInfoRpcService {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private UserService userService;
    @Autowired
    private PicturesService picturesService;

    @Override
    public List<UserDeviceModel> getUsersDevicesList() throws UserHasNoDevicesException {
        String userInSession = getUserEmailFromSession();
        logger.info("Getting list of devices for user: " + userInSession);
        List<UserDeviceModel> userDeviceModelList = userService.getUsersDevicesList(userInSession);
        if (userDeviceModelList == null || userDeviceModelList.isEmpty()) {
            throw new UserHasNoDevicesException();
        }
        return userDeviceModelList;
    }

    @Override
    public List<PictureCellEntity> getPicturesFromDevice(String deviceId) {
        List<PicturesFromDevice> pictures = picturesService.getPicturesByDeviceUid(deviceId);
        List<PictureCellEntity> pictureCellEntities = new ArrayList<>();
        for (PicturesFromDevice picture : pictures) {
            PictureCellEntity pictureCellEntity = new PictureCellEntity();
            String pictureName = picture.getPictureName();
            if (pictureName == null) {
                pictureName = "no_name";
            }
            pictureCellEntity.setPictureName(pictureName);
            pictureCellEntity.setPictureId(picture.getPictureUuid());
            pictureCellEntities.add(pictureCellEntity);
        }
        return pictureCellEntities;
    }


}
