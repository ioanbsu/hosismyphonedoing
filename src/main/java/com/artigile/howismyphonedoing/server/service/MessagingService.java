package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.server.gcmserver.Message;

/**
 * @author IoaN, 5/23/13 9:25 PM
 */
public interface MessagingService {

    void sendMessage(String deviceId, Message message);
}
