package com.artigile.howismyphonedoing.api;

import com.artigile.howismyphonedoing.api.model.MessageType;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/7/13
 * Time: 12:12 PM
 */
public interface MessageParser {
    public <T extends Serializable> T parse(MessageType messageType,String object) ;
    public String serialize(Serializable object) ;
}
