package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.CameraType;
import com.artigile.howismyphonedoing.api.model.IDeviceSettingsModel;
import com.artigile.howismyphonedoing.api.model.IPictureReadyModel;
import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceInfoCell;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceListCell;
import com.artigile.howismyphonedoing.client.mvp.settings.widget.AntiTheftWidget;
import com.artigile.howismyphonedoing.client.mvp.settings.widget.DeviceSettingsWidget;
import com.artigile.howismyphonedoing.shared.entity.DeviceInfoWithLoadingInfoEntity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellWidget;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ProvidesKey;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    CellList<DeviceInfoWithLoadingInfoEntity> addableDevicesList;
    @UiField(provided = true)
    CellWidget<IUserDeviceModel> deviceInfo;
    @UiField
    Button refreshDeviceInfo;
    @UiField(provided = true)
    DeviceSettingsWidget deviceSettings;
    @UiField(provided = true)
    AntiTheftWidget antiTheftWidget;
    @UiField
    TabLayoutPanel tabLayuotPanel;
    @UiField
    Button refreshDevicesList;
    @UiField
    Image deviceListLoading;
    @UiField
    Label noDeviceFoundText;
    private SettingsPresenter presenter;


    @Inject
    public SettingsView(Binder binder, DeviceInfoCell deviceInfoCell, DeviceListCell deviceListCell, DeviceSettingsWidget deviceSettings, AntiTheftWidget antiTheftWidget) {
        this.antiTheftWidget = antiTheftWidget;
        this.deviceSettings = deviceSettings;
        antiTheftWidget.setAntiTheftActionListener(initAntiTheftListener());
        deviceSettings.setSaveSettingsListener(initSaveSettingsListener());
        deviceInfo = new CellWidget<IUserDeviceModel>(deviceInfoCell);
        addableDevicesList = new CellList<DeviceInfoWithLoadingInfoEntity>(deviceListCell, getUserDeviceModelProvidesKey());
        addableDevicesList.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        binder.createAndBindUi(this);
    }

    private AntiTheftWidget.AntiTheftActionListener initAntiTheftListener() {
        return new AntiTheftWidget.AntiTheftActionListener() {
            @Override
            public void onLockDeviceClicked() {
                presenter.onLockDeviceClicked();
            }

            @Override
            public void onTakePictureClicked() {
                presenter.onTakePictureClicked();
            }
        };
    }

    public CellList<DeviceInfoWithLoadingInfoEntity> getDevicesListView() {
        return addableDevicesList;
    }

    public IUserDeviceModel getDeviceInfoModel() {
        return deviceInfo.getValue();
    }

    public void setDeviceInfoModel(IUserDeviceModel model) {
        deviceInfo.setValue(model);
        antiTheftWidget.deviceHadBeenSelected(model);
    }

    public IDeviceSettingsModel getDeviceSettingsModel() {
        return deviceSettings.getValue();
    }

    public void setDeviceSettingsModel(IDeviceSettingsModel iDeviceSettingsModel) {
        deviceSettings.setValue(iDeviceSettingsModel);
    }

    private DeviceSettingsWidget.SaveSettingsListener initSaveSettingsListener() {
        return new DeviceSettingsWidget.SaveSettingsListener() {
            @Override
            public void onSaveClicked() {
                presenter.onSaveDeviceConfigClicked();
            }

            @Override
            public void onDisplayLogsClicked() {
                presenter.displayLogsOnSelectedDevice();
            }

            @Override
            public void onHideLogsClicked() {
                presenter.hideLogsOnSelectedDevice();
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

    @UiHandler("refreshDevicesList")
    void onRefreshDevicesList(ClickEvent event) {
        presenter.onRefreshDevicesListClicked();
    }

    protected ProvidesKey<DeviceInfoWithLoadingInfoEntity> getUserDeviceModelProvidesKey() {
        return new ProvidesKey<DeviceInfoWithLoadingInfoEntity>() {
            @Override
            public Object getKey(DeviceInfoWithLoadingInfoEntity item) {
                return item.getiUserDeviceModel().getDeviceId();
            }
        };
    }

    public DeviceSettingsWidget getSettingsView() {
        return deviceSettings;
    }

    public void showLoading(boolean isLoading) {
        deviceSettings.showLoading(isLoading);
    }

    public String getNewPinCode() {
        return antiTheftWidget.getNewPinCode();
    }

    public void resetNewPinCodeTextBox() {
        antiTheftWidget.resetNewPinCode();
    }

    public void showDeviceLockIsInProgress(boolean isDeviceLockIsInProgress) {
        antiTheftWidget.showDeviceLoading(isDeviceLockIsInProgress);
    }

    public void showDeviceListLoading(boolean isShowing) {
        deviceListLoading.setVisible(isShowing);
    }

    public void displayNoDevicesFoundLabel(boolean isDevicesListEmpty) {
        noDeviceFoundText.setVisible(isDevicesListEmpty);
    }

    public CameraType getCameraType() {
        return antiTheftWidget.getCameraType();
    }

    public boolean isHighQuality() {
        return antiTheftWidget.isHighQuality();
    }

    public void pictureFromDeviceReceived(IPictureReadyModel picture) {
        antiTheftWidget.pictureFromThDeviceReceived(picture);
    }

    public void enable(boolean isEnabled) {
        deviceSettings.enableControlls(isEnabled);
        antiTheftWidget.enableControls(isEnabled);
    }


    public static interface Binder extends UiBinder<DialogBox, SettingsView> {
    }

}
