package com.artigile.howismyphonedoing.client.service;

import com.artigile.howismyphonedoing.api.model.*;
import com.artigile.howismyphonedoing.shared.RpcConstants;
import com.artigile.howismyphonedoing.shared.WebAppMessageProcessor;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.widget.DisplayPictureWindow;
import com.artigile.howismyphonedoing.shared.WebAppAndClientConstants;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Logger;

/**
 * Date: 6/22/13
 * Time: 7:29 PM
 *
 * @author ioanbsu
 */
@EventHandler
@Singleton
public class UiMessageReceivedProcessor extends BaseEventHandler<MainEventBus> implements WebAppMessageProcessor<IUserDeviceModel> {

    public static final Logger logger = Logger.getLogger(UiMessageReceivedProcessor.class.getName());
    @Inject
    private HowIsMyPhoneDoingAutoBeansFactory howIsMyPhoneDoingBeansFactory;
    @Inject
    private DisplayPictureWindow displayPictureWindow;
    @Inject
    private CommonUiUtil commonUiUtil;

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
            eventBus.deviceHadBeenLocked(userDevice);
        } else if (messageType == MessageType.DEVICE_ADMIN_IS_NOT_ENABLED) {
            eventBus.deviceAdminIsNotEnabled(userDevice);
        } else if (messageType == MessageType.DEVICE_SETTINGS_UPDATE) {

        } else if (messageType == MessageType.PICTURE_READY) {
            AutoBean<IPictureReadyModel> deviceDetails = AutoBeanCodex.decode(howIsMyPhoneDoingBeansFactory, IPictureReadyModel.class, serializedObject);
            String pictureUrl= commonUiUtil.getPictureUrl(deviceDetails.as().getPictureId(),false);
            logger.info("Picture received, url:" + pictureUrl);
            displayPictureWindow.show(pictureUrl);

        }
        return "succeeded";
    }

    public void onMessageFromServerReceived(String encodedData) {
        AutoBean<IResponseFromServer> responseFromServerAutoBean = AutoBeanCodex.decode(howIsMyPhoneDoingBeansFactory, IResponseFromServer.class, encodedData);
        IResponseFromServer responseFromServer = responseFromServerAutoBean.as();
        processMessage(responseFromServer.getUserDeviceModel(), responseFromServer.getMessageType(), responseFromServer.getSerializedObject());

    }

}
