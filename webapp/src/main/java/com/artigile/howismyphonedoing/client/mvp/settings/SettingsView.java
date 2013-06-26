package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.DeviceSettings;
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
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;
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

    final SingleSelectionModel<DeviceInfoWithLoadingInfo> selectionModel = new SingleSelectionModel<DeviceInfoWithLoadingInfo>();
    protected ListDataProvider<DeviceInfoWithLoadingInfo> dataProvider = new ListDataProvider<DeviceInfoWithLoadingInfo>();
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
        dataProvider.addDataDisplay(addableDevicesList);
        addableDevicesList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                deviceInfo.setValue(selectionModel.getSelectedObject().getiUserDeviceModel());
                if (selectionModel.getSelectedObject().getiUserDeviceModel().getBatteryLevel() == null) {
                    requestDeviceInfoUpdate();
                }
            }
        });
        binder.createAndBindUi(this);
    }

    private DeviceSettingsWidget.SaveSettingsListener initSaveSettingsListener() {
        return new DeviceSettingsWidget.SaveSettingsListener() {
            @Override
            public void onSaveClicked(DeviceSettings deviceSettings) {
                presenter.onSaveDeviceConfigClicked(selectionModel.getSelectedObject().getiUserDeviceModel(), deviceSettings);
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
        requestDeviceInfoUpdate();
    }

    private void requestDeviceInfoUpdate() {
        presenter.requestRefreshDeviceInfo(selectionModel.getSelectedObject().getiUserDeviceModel());
        selectionModel.getSelectedObject().setLoadingState(DeviceInfoWithLoadingInfo.LoadingState.LOADING);
        addableDevicesList.redraw();
    }

    protected ProvidesKey<DeviceInfoWithLoadingInfo> getUserDeviceModelProvidesKey() {
        return new ProvidesKey<DeviceInfoWithLoadingInfo>() {
            @Override
            public Object getKey(DeviceInfoWithLoadingInfo item) {
                return item.getiUserDeviceModel().getDeviceId();
            }
        };
    }

    public void updateDeviceDetails(IUserDeviceModel deviceDetails) {
        for (DeviceInfoWithLoadingInfo userDeviceModel : dataProvider.getList()) {
            if (userDeviceModel.getiUserDeviceModel().getDeviceId().equals(deviceDetails.getDeviceId())) {
                mergeDeviceInfo(deviceDetails, userDeviceModel.getiUserDeviceModel());
                userDeviceModel.setLoadingState(DeviceInfoWithLoadingInfo.LoadingState.LOADED);
                addableDevicesList.redraw();
            }
        }
        if (deviceInfo.getValue().getDeviceId().equals(deviceDetails.getDeviceId())) {
            deviceInfo.setValue(deviceDetails);
        }
    }

    public void setDevicesList(List<UserDeviceModel> result) {
        DeviceInfoWithLoadingInfo selectedObject = selectionModel.getSelectedObject();
        DeviceInfoWithLoadingInfo newSelected = null;

        List<DeviceInfoWithLoadingInfo> existentList = dataProvider.getList();
        dataProvider.getList().clear();
        List<DeviceInfoWithLoadingInfo> deviceInfoWithLoadingInfoList = new ArrayList<DeviceInfoWithLoadingInfo>();
        for (UserDeviceModel userDeviceModel : result) {
            DeviceInfoWithLoadingInfo deviceInfoWithLoadingInfo = new DeviceInfoWithLoadingInfo();

            deviceInfoWithLoadingInfo.setiUserDeviceModel(userDeviceModel);
            deviceInfoWithLoadingInfo.setLoadingState(DeviceInfoWithLoadingInfo.LoadingState.UNKNOWN);
            for (DeviceInfoWithLoadingInfo deviceInfoWithLoadingInfoInList : existentList) {
                if (deviceInfoWithLoadingInfoInList.getiUserDeviceModel().getDeviceId().equals(userDeviceModel)) {
                    mergeDeviceInfo(deviceInfoWithLoadingInfoInList.getiUserDeviceModel(), userDeviceModel);
                    deviceInfoWithLoadingInfo.setLoadingState(deviceInfoWithLoadingInfoInList.getLoadingState());
                    break;
                }
            }
            if (selectedObject != null && selectedObject.getiUserDeviceModel().getDeviceId().equals(userDeviceModel.getDeviceId())) {
                newSelected = deviceInfoWithLoadingInfo;
            } else {

            }
            deviceInfoWithLoadingInfoList.add(deviceInfoWithLoadingInfo);
        }
        dataProvider.getList().addAll(deviceInfoWithLoadingInfoList);
        if (newSelected != null) {
            selectionModel.setSelected(newSelected, true);
        }
    }


    private void mergeDeviceInfo(IUserDeviceModel deviceDetailsFrom, IUserDeviceModel deviceDetailsTo) {
        deviceDetailsTo.setBatteryLevel(deviceDetailsFrom.getBatteryLevel());
        deviceDetailsTo.setBatteryStatusType(deviceDetailsFrom.getBatteryStatusType());
        deviceDetailsTo.setBatteryPluggedType(deviceDetailsFrom.getBatteryPluggedType());
        deviceDetailsTo.setBatteryHealthType(deviceDetailsFrom.getBatteryHealthType());
        deviceDetailsTo.setOperator(deviceDetailsFrom.getOperator());
        deviceDetailsTo.setNetworkType(deviceDetailsFrom.getNetworkType());
        deviceDetailsTo.setWifiEnabled(deviceDetailsFrom.isWifiEnabled());
        deviceDetailsTo.setBluetoothEnabled(deviceDetailsFrom.isBluetoothEnabled());
    }


    public static interface Binder extends UiBinder<DialogBox, SettingsView> {
    }

}
