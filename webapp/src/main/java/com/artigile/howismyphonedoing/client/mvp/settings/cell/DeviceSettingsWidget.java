package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.api.model.DeviceSettingsModel;
import com.artigile.howismyphonedoing.api.model.RingerMode;
import com.artigile.howismyphonedoing.client.Messages;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.*;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 5:32 PM
 */
@Singleton
public class DeviceSettingsWidget extends Composite implements TakesValue<DeviceSettingsModel> {

    @UiField
    RadioButton ringerModeSilent;
    @UiField
    RadioButton ringerModeSilentNormal;
    @UiField
    RadioButton ringerModeSilentVibrate;
    @UiField
    Button saveButton;
    @UiField
    HTMLPanel settingsPanel;
    @UiField
    Label selectDeviceMsg;
    @Inject
    private Messages messages;
    private SaveSettingsListener saveSettingsListener;

    @Inject
    public DeviceSettingsWidget(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public DeviceSettingsModel getValue() {
        DeviceSettingsModel deviceSettingsModel = new DeviceSettingsModel();
        if (ringerModeSilent.getValue()) {
            deviceSettingsModel.setRingerMode(RingerMode.RINGER_MODE_SILENT);
        } else if (ringerModeSilentNormal.getValue()) {
            deviceSettingsModel.setRingerMode(RingerMode.RINGER_MODE_NORMAL);
        } else if (ringerModeSilentVibrate.getValue()) {
            deviceSettingsModel.setRingerMode(RingerMode.RINGER_MODE_VIBRATE);
        }
        return deviceSettingsModel;
    }

    @Override
    public void setValue(DeviceSettingsModel value) {
        ringerModeSilentNormal.setValue(value.getRingerMode() == RingerMode.RINGER_MODE_NORMAL);
        ringerModeSilent.setValue(value.getRingerMode() == RingerMode.RINGER_MODE_SILENT);
    }

    public void setSaveSettingsListener(SaveSettingsListener saveSettingsListener) {
        this.saveSettingsListener = saveSettingsListener;
    }

    @UiHandler("saveButton")
    void onSaveButtonClicked(ClickEvent clickEvent) {
        if (saveSettingsListener != null) {
            saveSettingsListener.onSaveClicked();
        }
    }

    public void enable(boolean enabled) {
        selectDeviceMsg.setVisible(!enabled);
        settingsPanel.setVisible(enabled);
    }

    public static interface Binder extends UiBinder<FlowPanel, DeviceSettingsWidget> {
    }

    public static interface SaveSettingsListener {
        void onSaveClicked();
    }
}