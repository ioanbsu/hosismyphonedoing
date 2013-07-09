package com.artigile.howismyphonedoing.client.mvp.settings.widget;

import com.artigile.howismyphonedoing.api.model.DeviceSettingsModel;
import com.artigile.howismyphonedoing.api.model.IDeviceSettingsModel;
import com.artigile.howismyphonedoing.api.model.RingerMode;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.service.DebugUtil;
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
public class DeviceSettingsWidget extends Composite implements TakesValue<IDeviceSettingsModel> {

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
    @UiField
    RadioButton wifiEnabled;
    @UiField
    RadioButton wifiDisabled;
    @UiField
    RadioButton bluetoothEnabled;
    @UiField
    RadioButton bluetoothDisabled;
    @UiField
    Image loadingIcon;
    @UiField
    Button displayLogs;
    @UiField
    Button hideLogs;
    @Inject
    private Messages messages;
    private SaveSettingsListener saveSettingsListener;

    @Inject
    public DeviceSettingsWidget(Binder binder) {
        initWidget(binder.createAndBindUi(this));
        displayLogs.setVisible(DebugUtil.isDebugMode());
        hideLogs.setVisible(DebugUtil.isDebugMode());
    }

    @Override
    public IDeviceSettingsModel getValue() {
        IDeviceSettingsModel deviceSettingsModel = new DeviceSettingsModel();
        if (ringerModeSilent.getValue()) {
            deviceSettingsModel.setRingerMode(RingerMode.RINGER_MODE_SILENT);
        } else if (ringerModeSilentNormal.getValue()) {
            deviceSettingsModel.setRingerMode(RingerMode.RINGER_MODE_NORMAL);
        } else if (ringerModeSilentVibrate.getValue()) {
            deviceSettingsModel.setRingerMode(RingerMode.RINGER_MODE_VIBRATE);
        }
        if (wifiEnabled.getValue()) {
            deviceSettingsModel.setWifiEnabled(true);
        } else if (wifiDisabled.getValue()) {
            deviceSettingsModel.setWifiEnabled(false);
        }
        if (bluetoothEnabled.getValue()) {
            deviceSettingsModel.setBluetoothEnabled(true);
        } else if (bluetoothDisabled.getValue()) {
            deviceSettingsModel.setBluetoothEnabled(false);
        }
        return deviceSettingsModel;
    }

    @Override
    public void setValue(IDeviceSettingsModel value) {
        if (value != null) {
            ringerModeSilentNormal.setValue(value.getRingerMode() == RingerMode.RINGER_MODE_NORMAL);
            ringerModeSilent.setValue(value.getRingerMode() == RingerMode.RINGER_MODE_SILENT);
            ringerModeSilentVibrate.setValue(value.getRingerMode() == RingerMode.RINGER_MODE_VIBRATE);
            wifiEnabled.setValue(value.isWifiEnabled());
            wifiDisabled.setValue(!value.isWifiEnabled());
            bluetoothEnabled.setValue(value.isBluetoothEnabled());
            bluetoothDisabled.setValue(!value.isBluetoothEnabled());
        } else {
            ringerModeSilentNormal.setValue(false);
            ringerModeSilent.setValue(false);
            ringerModeSilent.setValue(false);
            wifiEnabled.setValue(false);
            wifiDisabled.setValue(false);
            bluetoothEnabled.setValue(false);
            bluetoothDisabled.setValue(false);
        }
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

    @UiHandler("displayLogs")
    void onDisplayLogsClicked(ClickEvent clickEvent) {
        if (saveSettingsListener != null) {
            saveSettingsListener.onDisplayLogsClicked();
        }
    }

    @UiHandler("hideLogs")
    void onHideLogsClicked(ClickEvent clickEvent) {
        if (saveSettingsListener != null) {
            saveSettingsListener.onHideLogsClicked();
        }
    }

    public void showLoading(boolean isLoading) {
        loadingIcon.setVisible(isLoading);
    }

    public void enable(boolean enabled) {
        selectDeviceMsg.setVisible(!enabled);
        settingsPanel.setVisible(enabled);
    }

    public static interface Binder extends UiBinder<FlowPanel, DeviceSettingsWidget> {
    }

    public static interface SaveSettingsListener {
        void onSaveClicked();

        void onDisplayLogsClicked();

        void onHideLogsClicked();
    }
}