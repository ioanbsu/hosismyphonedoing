package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.server.dao.UserAndDeviceDao;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/11/13
 * Time: 4:38 PM
 */
@Service
public class UserInfoService {

    @Autowired
    private UserAndDeviceDao userAndDeviceDao;

    public List<UserDeviceModel> getUsersDevicesList(String userEmail) {
        List<UserDeviceModel> userDeviceModelList = new ArrayList<UserDeviceModel>();
        for (UserDevice userDevice : userAndDeviceDao.getDevices(userEmail)) {
            UserDeviceModel userDeviceModel = new UserDeviceModel();
            userDeviceModel.setDeviceId(userDevice.getUuid());
            userDeviceModelList.add(userDeviceModel);
        }
        return userDeviceModelList;
    }
}
