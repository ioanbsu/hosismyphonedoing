/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.servlet;

import com.artigile.howismyphonedoing.api.CommonConstants;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.server.service.WebAppMessageReceiver;
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
    private WebAppMessageReceiver messageProcessor;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uuid = request.getParameter(CommonConstants.UUID);
        try {
            messageProcessor.processMessage(uuid, MessageType.valueOf(request.getParameter(CommonConstants.MESSAGE_EVENT_TYPE)), request.getParameter(CommonConstants.SERIALIZED_OBJECT));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }

}
