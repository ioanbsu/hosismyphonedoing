package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.api.shared.WebAppMessageProcessor;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * This class is for testing only!
 * Date: 6/25/13
 * Time: 3:03 PM
 *
 * @author ioanbsu
 */

@Service
@Qualifier("fakeSender")
public class WebAppMessageFakeSender implements WebAppMessageProcessor<UserDevice> {
    @Override
    public String processMessage(UserDevice userDevice, MessageType messageType, String message) throws Exception {
        return "success";
    }
}
