package com.artigile.howismyphonedoing.client.service;

import com.artigile.howismyphonedoing.api.model.*;
import com.artigile.howismyphonedoing.api.shared.WebAppMessageProcessor;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 6/22/13
 * Time: 7:29 PM
 *
 * @author ioanbsu
 */
@EventHandler
@Singleton
public class UiMessageReceivedProcessor extends BaseEventHandler<MainEventBus> implements WebAppMessageProcessor<IUserDeviceModel> {

    @Inject
    private HowIsMyPhoneDoingAutoBeansFactory howIsMyPhoneDoingBeansFactory;

    @Override
    public String processMessage(IUserDeviceModel userDevice, MessageType messageType, String serializedObject) {
        if (messageType == MessageType.DEVICE_LOCATION_UPDATED) {
            AutoBean<IDeviceLocationModel> phoneLocationModelAutoBean = AutoBeanCodex.decode(howIsMyPhoneDoingBeansFactory, IDeviceLocationModel.class, serializedObject);
            IDeviceLocationModel deviceLocationModel = phoneLocationModelAutoBean.as();
            deviceLocationModel.setDeviceId(userDevice.getDeviceId());
            eventBus.deviceLocationUpdated(deviceLocationModel);
        } else if (messageType == MessageType.DEVICE_INFO) {

        } else if (messageType == MessageType.MESSAGE_TO_DEVICE) {
            AutoBean<IMessageToDeviceModel> messageDeliveredModel = AutoBeanCodex.decode(howIsMyPhoneDoingBeansFactory, IMessageToDeviceModel.class, serializedObject);
            eventBus.messageDelivered(messageDeliveredModel.as());
        } else if (messageType == MessageType.REGISTER_DEVICE) {
            eventBus.updateDevicesList();
        } else if (messageType == MessageType.UNREGISTER_DEVICE) {
            eventBus.updateDevicesList();
        } else if (messageType == MessageType.MESSAGE_TYPE_IS_NOT_SUPPORTED) {
            AutoBean<IMessageNotSupportedByDeviceResponseModel> messageNotSupported = AutoBeanCodex.decode(howIsMyPhoneDoingBeansFactory, IMessageNotSupportedByDeviceResponseModel.class, serializedObject);
            MessageType notSupportedMessageType = MessageType.valueOf(messageNotSupported.as().getMessageType());
            eventBus.messageNotSupported(userDevice, notSupportedMessageType);
        } else if (messageType == MessageType.DEVICE_DETAILS_INFO) {
            AutoBean<IUserDeviceModel> deviceDetails = AutoBeanCodex.decode(howIsMyPhoneDoingBeansFactory, IUserDeviceModel.class, serializedObject);
            deviceDetails.as().setDeviceId(userDevice.getDeviceId());
            deviceDetails.as().setHumanReadableName(userDevice.getHumanReadableName());
            eventBus.deviceDetailsReceived(deviceDetails.as());
        } else if (messageType == MessageType.LOCK_DEVICE) {
            Window.alert("Device screen had been locked: " + userDevice.getHumanReadableName());
        } else if (messageType == MessageType.DEVICE_ADMIN_IS_NOT_ENABLED) {
            Window.alert("Device admin is not enabled on the " + userDevice.getHumanReadableName());
        } else if (messageType == MessageType.DEVICE_SETTINGS_UPDATE) {

        }
        return "succeeded";
    }

    public void onMessageFromServerReceived(String encodedData) {
        AutoBean<IResponseFromServer> responseFromServerAutoBean = AutoBeanCodex.decode(howIsMyPhoneDoingBeansFactory, IResponseFromServer.class, encodedData);
        IResponseFromServer responseFromServer = responseFromServerAutoBean.as();
        processMessage(responseFromServer.getUserDeviceModel(), responseFromServer.getMessageType(), responseFromServer.getSerializedObject());

    }

}
