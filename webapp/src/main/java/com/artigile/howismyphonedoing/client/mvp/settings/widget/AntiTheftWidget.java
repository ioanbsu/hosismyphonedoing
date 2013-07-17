package com.artigile.howismyphonedoing.client.mvp.settings.widget;

import com.artigile.howismyphonedoing.api.model.CameraType;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.client.widget.MessageWindow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 7/1/13
 * Time: 8:22 PM
 *
 * @author ioanbsu
 */
@Singleton
public class AntiTheftWidget extends Composite {

    @UiField
    Button lockTheDevice;
    @UiField
    PasswordTextBox newPinCode;
    @UiField
    Image deviceLockingLoading;
    @UiField
    Button takePicture;
    @UiField
    CheckBox highQuality;
    @UiField
    RadioButton frontCamera;
    @UiField
    RadioButton backCamera;
    @Inject
    private MessageWindow messageWindow;
    @Inject
    private Messages messages;
    private AntiTheftActionListener antiTheftActionListener;
    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;

    @Inject
    public AntiTheftWidget(Binder binder) {
        initWidget(binder.createAndBindUi(this));
        frontCamera.setValue(true);
    }

    @UiHandler("lockTheDevice")
    void onLockTheDeviceClicked(ClickEvent clickEvent) {
        if (antiTheftActionListener != null) {
            antiTheftActionListener.onLockDeviceClicked();
        }
    }

    @UiHandler("takePicture")
    void onTakePictureClicked(ClickEvent clickEvent) {
        if (antiTheftActionListener != null) {
            antiTheftActionListener.onTakePictureClicked();
        }
    }

    public void setAntiTheftActionListener(AntiTheftActionListener antiTheftActionListener) {
        this.antiTheftActionListener = antiTheftActionListener;
    }

    public String getNewPinCode() {
        return newPinCode.getText();
    }

    public void resetNewPinCode() {
        newPinCode.setText("");
    }

    public void showDeviceLoading(boolean isLoading) {
        deviceLockingLoading.setVisible(isLoading);
    }

    public CameraType getCameraType() {
        if (frontCamera.getValue()) {
            return CameraType.FRONT;
        } else if (backCamera.getValue()) {
            return CameraType.BACK;
        }
        return CameraType.FRONT;
    }

    public boolean isHighQuality() {
        return highQuality.getValue();
    }

    public static interface Binder extends UiBinder<HTMLPanel, AntiTheftWidget> {
    }


    public static interface AntiTheftActionListener {
        void onLockDeviceClicked();

        void onTakePictureClicked();

    }


}
