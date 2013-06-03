package com.artigile.howismyphonedoing.server.servlet;

import com.artigile.howismyphonedoing.api.CommonContants;
import com.artigile.howismyphonedoing.api.EventType;
import com.artigile.howismyphonedoing.api.RegistrarionType;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.service.TestService;
import com.artigile.howismyphonedoing.server.dao.UserAndDeviceDao;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 5/23/13
 * Time: 6:01 PM
 */
@Service
public class RegisterDeviceServlet extends AbstractServlet {

    // change to true to allow GET calls
    static final boolean DEBUG = true;
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private TestService testService;
    @Autowired
    private UserAndDeviceDao userAndDeviceDao;
    private String phoneId = "APA91bE1gYlPjxFqDx692-oFLvG9jlQ0CN_g-Shp3yHqGDtr0jeDWEb3sMkEMUoaquVAKK5LNNm2wOvKhFLUPYNDtTYj8xnwTh0UzODr7Ff6csX_Ewb0dKox439k25YXtlPgJomko77ES5NWvbk9-aX3T0Lr9cKpMFM-Dn4POsL4d0amTHFgZjE";

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userEmail = request.getParameter(CommonContants.USER_EMAIL);
        String regId = request.getParameter(CommonContants.REG_ID);

        if (RegistrarionType.REGISTER.of(request.getParameter(CommonContants.REGISTRATION_ACTION_TYPE))) {
            logger.info("Registering new device: " + regId);
            UserDevice userDevice = new UserDevice();
            userDevice.setUserEmail(userEmail);
            userDevice.setRegisteredId(regId);
            userAndDeviceDao.register(userDevice);
            setSuccess(response);
        }
        if (RegistrarionType.UNREGISTER.of(request.getParameter(CommonContants.REGISTRATION_ACTION_TYPE))) {
            logger.info("Unregistering device: " + regId);
            userAndDeviceDao.unregister(regId);
            setSuccess(response);
        }
        String eventType = request.getParameter(CommonContants.MESSAGE_EVENT_TYPE);
        if (EventType.PHONE_INFO.of(eventType)) {
            logger.info("Parsing info about phone.");
            String deviceInfo = request.getParameter("phoneInfo");
            ChannelService channelService = ChannelServiceFactory.getChannelService();
            channelService.sendMessage(new ChannelMessage(userEmail, deviceInfo));
        }
        return null;
    }

    protected void setSuccess(HttpServletResponse resp) {
        setSuccess(resp, 0);
    }

    protected void setSuccess(HttpServletResponse resp, int size) {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/plain");
        resp.setContentLength(size);
    }


}
