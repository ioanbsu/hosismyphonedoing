package com.artigile.howismyphonedoing.server.servlet;

import com.artigile.howismyphonedoing.api.EventType;
import com.artigile.howismyphonedoing.api.model.PhoneModel;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.service.TestService;
import com.artigile.howismyphonedoing.server.service.UserAndDeviceService;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gson.Gson;
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
    private static final String PARAMETER_REG_ID = "regId";
    private static final String USER_EMAIL_ID = "userEmail";
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private TestService testService;
    @Autowired
    private UserAndDeviceService userAndDeviceService;
    private String phoneId = "APA91bE1gYlPjxFqDx692-oFLvG9jlQ0CN_g-Shp3yHqGDtr0jeDWEb3sMkEMUoaquVAKK5LNNm2wOvKhFLUPYNDtTYj8xnwTh0UzODr7Ff6csX_Ewb0dKox439k25YXtlPgJomko77ES5NWvbk9-aX3T0Lr9cKpMFM-Dn4POsL4d0amTHFgZjE";

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String eventType = request.getParameter(EventType.TYPE);
        String userEmail = "ioanbsu1@gmail.com";//getParameter(request, USER_EMAIL_ID);
        if (EventType.PHONE_INFO.equals(eventType)) {
            String deviceInfo = request.getParameter("phoneInfo");
            ChannelService channelService = ChannelServiceFactory.getChannelService();
            channelService.sendMessage(new ChannelMessage(userEmail, deviceInfo));
        } else {
            String regId = phoneId;//getParameter(request, PARAMETER_REG_ID);
            UserDevice userDevice = new UserDevice();
            userDevice.setUserEmail(userEmail);
            userDevice.setRegisteredId(regId);
            userAndDeviceService.register(userDevice);
            setSuccess(response);
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
