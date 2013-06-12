package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcService;
import com.artigile.howismyphonedoing.server.service.UserInfoService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserInfoService userInfoService;

    @Override
    public List<UserDeviceModel> getUsersDevicesList() {
        String userInSession=getUserEmailFromSession();
        logger.info("Getting list of devices for user: "+userInSession);
        return userInfoService.getUsersDevicesList(userInSession);
    }
}
