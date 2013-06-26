package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.DeviceSettingsModel;
import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceInfoCell;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceInfoWithLoadingInfo;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceListCell;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceSettingsWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellWidget;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.view.client.ProvidesKey;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 6/22/13
 * Time: 7:52 PM
 *
 * @author ioanbsu
 */
@Singleton
public class SettingsView implements ReverseViewInterface<SettingsPresenter> {


    @UiField
    DialogBox mainDialogBox;
    @UiField
    Button closeSettings;
    @UiField(provided = true)
    CellList<DeviceInfoWithLoadingInfo> addableDevicesList;
    @UiField(provided = true)
    CellWidget<IUserDeviceModel> deviceInfo;
    @UiField
    Button refreshDeviceInfo;
    @UiField(provided = true)
    DeviceSettingsWidget deviceSettings;
    private SettingsPresenter presenter;


    @Inject
    public SettingsView(Binder binder, DeviceInfoCell deviceInfoCell, DeviceListCell deviceListCell, DeviceSettingsWidget deviceSettings) {
        this.deviceSettings = deviceSettings;
        deviceSettings.setSaveSettingsListener(initSaveSettingsListener());
        deviceInfo = new CellWidget<IUserDeviceModel>(deviceInfoCell);
        addableDevicesList = new CellList<DeviceInfoWithLoadingInfo>(deviceListCell, getUserDeviceModelProvidesKey());
        addableDevicesList.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        binder.createAndBindUi(this);
    }

    public CellList<DeviceInfoWithLoadingInfo> getDevicesListView(){
        return addableDevicesList;
    }

    public CellWidget<IUserDeviceModel> getDeviceInfoCell(){
        return deviceInfo;
    }

    public DeviceSettingsModel getDeviceSettingsModel(){
        return deviceSettings.getValue();
    }
    private DeviceSettingsWidget.SaveSettingsListener initSaveSettingsListener() {
        return new DeviceSettingsWidget.SaveSettingsListener() {
            @Override
            public void onSaveClicked() {
                presenter.onSaveDeviceConfigClicked();
            }
        };
    }

    @Override
    public SettingsPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(SettingsPresenter presenter) {
        this.presenter = presenter;
    }

    public void show() {
        mainDialogBox.center();
    }

    public void hide() {
        mainDialogBox.hide();
    }

    @UiHandler("closeSettings")
    void onCloseSettings(ClickEvent event) {
        hide();
    }

    @UiHandler("refreshDeviceInfo")
    void onRefreshDeviceInfo(ClickEvent event) {
        presenter.requestDeviceInfoUpdate();
    }


    protected ProvidesKey<DeviceInfoWithLoadingInfo> getUserDeviceModelProvidesKey() {
        return new ProvidesKey<DeviceInfoWithLoadingInfo>() {
            @Override
            public Object getKey(DeviceInfoWithLoadingInfo item) {
                return item.getiUserDeviceModel().getDeviceId();
            }
        };
    }

    public DeviceSettingsWidget getSettingsView() {
        return deviceSettings;
    }


    public static interface Binder extends UiBinder<DialogBox, SettingsView> {
    }

}
