package com.artigile.howismyphonedoing.api;

/**
 * User: ioanbsu
 * Date: 5/31/13
 * Time: 7:32 PM
 */
public class CommonConstants {
    /**
     * Used to detect type of message that sent between browser and phone. See @{link MessageType} for more details.
     */
    public static final String MESSAGE_EVENT_TYPE = "messageEventType";
    public static final String UUID = "uuid";
    public static final String SERIALIZED_OBJECT = "serializedObject";
    public static final String MESSAGES_COMMUNICATION_URL = "/messageServlet";
    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
    public final static String SERVER_URL_PARAM_NAME = "remoteServerUrl";

    private CommonConstants() {
    }
}
