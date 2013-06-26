package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.api.model.DeviceSettings;
import com.artigile.howismyphonedoing.api.model.RingerMode;
import com.artigile.howismyphonedoing.client.Messages;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 5:32 PM
 */
@Singleton
public class DeviceSettingsWidget extends Composite implements TakesValue<DeviceSettings> {

    @UiField
    RadioButton ringerModeSilent;
    @UiField
    RadioButton ringerModeSilentNormal;
    @UiField
    RadioButton ringerModeSilentVibrate;
    @UiField
    Button saveButton;
    @Inject
    private Messages messages;
    private SaveSettingsListener saveSettingsListener;

    @Inject
    public DeviceSettingsWidget(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public DeviceSettings getValue() {
        DeviceSettings deviceSettings = new DeviceSettings();
        if (ringerModeSilent.getValue()) {
            deviceSettings.setRingerMode(RingerMode.RINGER_MODE_SILENT);
        } else if (ringerModeSilentNormal.getValue()) {
            deviceSettings.setRingerMode(RingerMode.RINGER_MODE_NORMAL);
        } else if (ringerModeSilentVibrate.getValue()) {
            deviceSettings.setRingerMode(RingerMode.RINGER_MODE_VIBRATE);
        }
        return deviceSettings;
    }

    @Override
    public void setValue(DeviceSettings value) {
        ringerModeSilentNormal.setValue(value.getRingerMode() == RingerMode.RINGER_MODE_NORMAL);
        ringerModeSilent.setValue(value.getRingerMode() == RingerMode.RINGER_MODE_SILENT);
    }

    public void setSaveSettingsListener(SaveSettingsListener saveSettingsListener) {
        this.saveSettingsListener = saveSettingsListener;
    }

    @UiHandler("saveButton")
    void onSaveButtonClicked(ClickEvent clickEvent) {
        if (saveSettingsListener != null) {
            saveSettingsListener.onSaveClicked(getValue());
        }
    }

    public static interface Binder extends UiBinder<HTMLPanel, DeviceSettingsWidget> {
    }

    public static interface SaveSettingsListener {
        void onSaveClicked(DeviceSettings deviceSettings);
    }
}