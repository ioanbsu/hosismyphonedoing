package com.artigile.howismyphonedoing.server.servlet;

import com.artigile.howismyphonedoing.server.service.TestService;
import com.artigile.howismyphonedoing.server.service.cloudutil.PhoneDatastore;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
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
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private TestService testService;
    private String phoneId = "APA91bE1gYlPjxFqDx692-oFLvG9jlQ0CN_g-Shp3yHqGDtr0jeDWEb3sMkEMUoaquVAKK5LNNm2wOvKhFLUPYNDtTYj8xnwTh0UzODr7Ff6csX_Ewb0dKox439k25YXtlPgJomko77ES5NWvbk9-aX3T0Lr9cKpMFM-Dn4POsL4d0amTHFgZjE";

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String regId = getParameter(request, PARAMETER_REG_ID);
        PhoneDatastore.register(regId);
        setSuccess(response);

        System.out.println(testService);
        return null;
    }

    protected String getParameter(HttpServletRequest req, String parameter)
            throws ServletException {
        String value = req.getParameter(parameter);
        if (Strings.isNullOrEmpty(value)) {
            if (DEBUG) {
                StringBuilder parameters = new StringBuilder();
                @SuppressWarnings("unchecked")
                Enumeration<String> names = req.getParameterNames();
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    String param = req.getParameter(name);
                    parameters.append(name).append("=").append(param).append("\n");
                }
                logger.fine("parameters: " + parameters);
            }
            throw new ServletException("Parameter " + parameter + " not found");
        }
        return value.trim();
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
