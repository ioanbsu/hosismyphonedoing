package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.gcmserver.Message;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author IoaN, 5/23/13 9:25 PM
 */
public interface MessagingService {

    void sendMessage(Set<UserDevice> devices, Message message) throws IOException;
}
