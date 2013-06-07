package com.artigile.howismyphonedoing.server.servlet;

import com.artigile.howismyphonedoing.api.CommonContants;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.server.service.WebApMessageReceiver;
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
public class MessageCommunicationServlet extends AbstractServlet {

    // change to true to allow GET calls
    static final boolean DEBUG = true;
    protected final Logger logger = Logger.getLogger(getClass().getName());


    @Autowired
    private WebApMessageReceiver messageProcessor;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uuid = request.getParameter(CommonContants.UUID);
        messageProcessor.processMessage(uuid, MessageType.valueOf(request.getParameter(CommonContants.MESSAGE_EVENT_TYPE)), request.getParameter(CommonContants.SERIALIZED_OBJECT));
        return null;
    }

}
