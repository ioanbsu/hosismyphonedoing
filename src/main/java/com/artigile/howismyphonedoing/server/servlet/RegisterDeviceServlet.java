package com.artigile.howismyphonedoing.server.servlet;

import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.service.TestService;
import com.artigile.howismyphonedoing.server.service.UserAndDeviceService;
import com.artigile.howismyphonedoing.server.service.cloudutil.PhoneDatastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        //for testing only registering the device ID.
        String regId = phoneId;//getParameter(request, PARAMETER_REG_ID);
        String userEmail = "ioanbsu1@gmail.com";//getParameter(request, USER_EMAIL_ID);
        UserDevice userDevice = new UserDevice();
        userDevice.setRegisteredId(regId);
        userAndDeviceService.createOrUpdateUserDevice(userEmail, userDevice);
        PhoneDatastore.register(regId);
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String regId = phoneId;//getParameter(request, PARAMETER_REG_ID);
        String userEmail = "ioanbsu1@gmail.com";//getParameter(request, USER_EMAIL_ID);
        UserDevice userDevice = new UserDevice();
        userDevice.setRegisteredId(regId);
        userAndDeviceService.createOrUpdateUserDevice(userEmail, userDevice);
        PhoneDatastore.register(regId);
        setSuccess(response);
        System.out.println(testService);
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
