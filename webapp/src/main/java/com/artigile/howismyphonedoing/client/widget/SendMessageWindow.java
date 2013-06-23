package com.artigile.howismyphonedoing.client.widget;

import com.artigile.howismyphonedoing.api.model.GwtLocale;
import com.artigile.howismyphonedoing.api.model.IMessageToDeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageToDeviceModel;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.exception.DeviceWasRemovedException;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ProvidesKey;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 10:39 AM
 */
@EventHandler
@Singleton
public class SendMessageWindow extends BaseEventHandler<MainEventBus> {

    @UiField
    Button closeWindow;
    @UiField
    DialogBox dialogBox;
    @UiField
    ListBox languagesList;
    @UiField
    TextBox messageToSend;
    @UiField
    Button sendMessage;
    @UiField(provided = true)
    ValueListBox<UserDeviceModel> devicesValueListBox;
    @UiField
    FlowPanel messagesAuditTrail;
    @UiField
    FlowPanel messageQueuePanel;
    @UiField
    Label clearPendingMsg;
    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;
    @Inject
    private MessageWindow messageWindow;
    @Inject
    private Messages messages;
    private List<UserDeviceModel> userDeviceModels;


    @Inject
    public SendMessageWindow(Binder binder) {
        devicesValueListBox = new ValueListBox<UserDeviceModel>(new AbstractRenderer<UserDeviceModel>() {
            @Override
            public String render(UserDeviceModel object) {
                if (object == null) {
                    return "";
                }
                return object.getHumanReadableName();
            }
        }, new ProvidesKey<UserDeviceModel>() {
            @Override
            public Object getKey(UserDeviceModel item) {
                if (item == null) {
                    return null;
                }
                return item.getDeviceId();
            }
        }
        );
        binder.createAndBindUi(this);
        for (GwtLocale gwtLocale : GwtLocale.values()) {
            languagesList.addItem(gwtLocale.getLanguageName());
        }
    }

    public void show() {
        dialogBox.center();
    }

    public void onUsersDevicesListReceived(List<UserDeviceModel> userDeviceModels) {
        this.userDeviceModels = userDeviceModels;
        boolean devicesListEmpty = userDeviceModels == null || userDeviceModels.isEmpty();
        sendMessage.setEnabled(!devicesListEmpty);
        if (!devicesListEmpty) {
            devicesValueListBox.setValue(userDeviceModels.get(0));
        }
        devicesValueListBox.setAcceptableValues(userDeviceModels);
    }

    public void onMessageDelivered(IMessageToDeviceModel messageDeliveredModel) {
        List<LabelWithId> newMessagesList = new LinkedList<LabelWithId>();
        for (Widget widget : messagesAuditTrail) {
            LabelWithId messageHistoryLabel = (LabelWithId) widget;
            if (!(messageDeliveredModel.getMessageId()).equals(messageHistoryLabel.getId())) {
                newMessagesList.add(messageHistoryLabel);
            }
        }
        messagesAuditTrail.clear();
        for (LabelWithId labelWithId : newMessagesList) {
            messagesAuditTrail.add(labelWithId);
        }
        messageQueuePanel.setVisible(messagesAuditTrail.getWidgetCount() != 0);
    }

    @UiHandler("sendMessage")
    void onSendMessageClicked(ClickEvent clickEvent) {
        sendMessageToTheDevice();
    }

    @UiHandler("clearPendingMsg")
    void clearPendingMsg(ClickEvent clickEvent) {
        messagesAuditTrail.clear();
        messageQueuePanel.setVisible(messagesAuditTrail.getWidgetCount() != 0);
    }

    private void sendMessageToTheDevice() {
        MessageToDeviceModel messageToTheDevice = new MessageToDeviceModel();
        messageToTheDevice.setDeviceId(devicesValueListBox.getValue().getDeviceId());
        messageToTheDevice.setMessage(messageToSend.getText());
        messageToTheDevice.setLocale(GwtLocale.parse(languagesList.getItemText(languagesList.getSelectedIndex())));
        messageToTheDevice.setMessageId(messageToSend.getText() + "_" + new Date().getTime());
        messageToSend.setText("");
        final LabelWithId labelWithId = new LabelWithId(messageToTheDevice.getMessageId());
        labelWithId.setText(devicesValueListBox.getValue().getHumanReadableName() + ": " + messageToTheDevice.getMessage());
        messageQueuePanel.setVisible(true);
        messageRpcServiceAsync.sendMessageToDevice(messageToTheDevice, new AsyncCallbackImpl<String>(eventBus) {

            @Override
            public void success(String result) {
                messagesAuditTrail.add(labelWithId);
            }

            @Override
            public void failure(Throwable caught) {
                if (caught instanceof DeviceWasRemovedException) {
                    messageWindow.show(messages.send_message_window_device_no_longer_exist_label());
                    eventBus.updateDevicesList();
                }
            }
        });
    }

    @UiHandler("messageToSend")
    void onMessageToSendKeyPressHandler(KeyUpEvent keyUpEvent) {
        if (keyUpEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            sendMessageToTheDevice();
        }
    }

    @UiHandler("closeWindow")
    void onCloseWindowClicked(ClickEvent clickEvent) {
        dialogBox.hide();
    }

    interface Binder extends UiBinder<DialogBox, SendMessageWindow> {
    }

    private class LabelWithId extends Label {
        private String id;

        private LabelWithId(String id) {
            this.id = id;
        }

        private String getId() {
            return id;
        }

    }
}