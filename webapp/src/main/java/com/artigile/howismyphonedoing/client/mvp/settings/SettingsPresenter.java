package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.*;
import com.artigile.howismyphonedoing.client.MainEventBus;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceInfoWithLoadingInfo;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.HowIsMyPhoneDoingAutoBeansFactory;
import com.artigile.howismyphonedoing.client.widget.MessageWindow;
import com.artigile.howismyphonedoing.client.widget.YesNoWindow;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 6/22/13
 * Time: 7:53 PM
 *
 * @author ioanbsu
 */
@Singleton
@Presenter(view = SettingsView.class)
public class SettingsPresenter extends BasePresenter<SettingsView, MainEventBus> {

    final private SingleSelectionModel<DeviceInfoWithLoadingInfo> selectionModel = new SingleSelectionModel<DeviceInfoWithLoadingInfo>();
    final private ListDataProvider<DeviceInfoWithLoadingInfo> devicesListDataProvider = new ListDataProvider<DeviceInfoWithLoadingInfo>();
    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;
    @Inject
    private HowIsMyPhoneDoingAutoBeansFactory howIsMyPhoneDoingAutoBeansFactory;
    @Inject
    private YesNoWindow yesNoWindow;
    @Inject
    private Messages messages;
    @Inject
    private MessageWindow messageWindow;

    public void onInitApp() {
        GWT.log("Settings window initiated.");

        devicesListDataProvider.addDataDisplay(getView().getDevicesListView());
        getView().getDevicesListView().setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                getView().setDeviceInfoModel(selectionModel.getSelectedObject().getiUserDeviceModel());
                getView().setDeviceSettingsModel(selectionModel.getSelectedObject().getiUserDeviceModel().getiDeviceSettingsModel());
                getView().getSettingsView().enable(true);
                if (selectionModel.getSelectedObject().getiUserDeviceModel().getBatteryLevel() == null) {
                    requestDeviceInfoUpdate();
                }
                eventBus.centerDeviceLocationOnScreen(selectionModel.getSelectedObject().getiUserDeviceModel());
            }
        });
    }

    public void requestDeviceInfoUpdate() {
        requestRefreshDeviceInfo(selectionModel.getSelectedObject().getiUserDeviceModel());
        selectionModel.getSelectedObject().setLoadingState(DeviceInfoWithLoadingInfo.LoadingState.LOADING);
        getView().getDevicesListView().redraw();
    }

    public void show() {
        view.show();
    }

    public void onUsersDevicesListReceived(List<UserDeviceModel> result) {
        DeviceInfoWithLoadingInfo selectedObject = selectionModel.getSelectedObject();
        DeviceInfoWithLoadingInfo newSelected = null;

        List<DeviceInfoWithLoadingInfo> existentList = new ArrayList<DeviceInfoWithLoadingInfo>(devicesListDataProvider.getList());
        devicesListDataProvider.getList().clear();
        List<DeviceInfoWithLoadingInfo> deviceInfoWithLoadingInfoList = new ArrayList<DeviceInfoWithLoadingInfo>();
        for (UserDeviceModel userDeviceModel : result) {
            DeviceInfoWithLoadingInfo deviceInfoWithLoadingInfo = new DeviceInfoWithLoadingInfo();
            deviceInfoWithLoadingInfo.setiUserDeviceModel(userDeviceModel);
            deviceInfoWithLoadingInfo.setLoadingState(DeviceInfoWithLoadingInfo.LoadingState.UNKNOWN);
            for (DeviceInfoWithLoadingInfo deviceInfoWithLoadingInfoInList : existentList) {
                if (deviceInfoWithLoadingInfoInList.getiUserDeviceModel().getDeviceId().equals(userDeviceModel.getDeviceId())) {
                    mergeDeviceInfo(deviceInfoWithLoadingInfoInList.getiUserDeviceModel(), userDeviceModel);
                    deviceInfoWithLoadingInfo.setLoadingState(deviceInfoWithLoadingInfoInList.getLoadingState());
                    break;
                }
            }
            if (selectedObject != null && selectedObject.getiUserDeviceModel().getDeviceId().equals(userDeviceModel.getDeviceId())) {
                newSelected = deviceInfoWithLoadingInfo;
            }
            deviceInfoWithLoadingInfoList.add(deviceInfoWithLoadingInfo);
        }
        devicesListDataProvider.getList().addAll(deviceInfoWithLoadingInfoList);
        if (newSelected != null) {
            selectionModel.setSelected(newSelected, true);
            getView().setDeviceSettingsModel(newSelected.getiUserDeviceModel().getiDeviceSettingsModel());
            getView().getSettingsView().enable(true);
        } else {
            getView().getSettingsView().enable(false);
        }
    }

    public void onDeviceDetailsReceived(IUserDeviceModel deviceDetails) {
        for (DeviceInfoWithLoadingInfo userDeviceModel : devicesListDataProvider.getList()) {
            if (userDeviceModel.getiUserDeviceModel().getDeviceId().equals(deviceDetails.getDeviceId())) {
                mergeDeviceInfo(deviceDetails, userDeviceModel.getiUserDeviceModel());
                userDeviceModel.setLoadingState(DeviceInfoWithLoadingInfo.LoadingState.LOADED);
                getView().getDevicesListView().redraw();
            }
        }
        if (getView().getDeviceInfoModel().getDeviceId().equals(deviceDetails.getDeviceId())) {
            getView().setDeviceInfoModel(deviceDetails);
            getView().setDeviceSettingsModel(deviceDetails.getiDeviceSettingsModel());
        }
    }

    public void requestRefreshDeviceInfo(IUserDeviceModel selectedObject) {
        messageRpcServiceAsync.sendMessageToDevice(MessageType.DEVICE_DETAILS_INFO, selectedObject.getDeviceId(), "", new AsyncCallbackImpl<String>(eventBus) {
        });
    }

    public void onSaveDeviceConfigClicked() {
        DeviceInfoWithLoadingInfo deviceInfoWithLoadingInfo = selectionModel.getSelectedObject();
        if (selectionModel.getSelectedObject() == null) {
            Window.alert("Please select device first");
            return;
        }
        if (deviceInfoWithLoadingInfo.getiUserDeviceModel().getNetworkType() == NetworkType.NETWORK_TYPE_UNKNOWN
                && (deviceInfoWithLoadingInfo.getiUserDeviceModel().getiDeviceSettingsModel().isWifiEnabled() && !getView().getDeviceSettingsModel().isWifiEnabled())) {
            yesNoWindow.show(new ClickHandler() {
                                 @Override
                                 public void onClick(ClickEvent event) {
                                     doSave(selectionModel.getSelectedObject());
                                 }
                             }, new ClickHandler() {
                                 @Override
                                 public void onClick(ClickEvent event) {
                                     IDeviceSettingsModel deviceSettingsModel = getView().getDeviceSettingsModel();
                                     deviceSettingsModel.setWifiEnabled(true);
                                     getView().setDeviceSettingsModel(deviceSettingsModel);
                                 }
                             },
                    messages.device_settings_wifi_off_on_wifi_only_device()
            );
        } else {
            doSave(deviceInfoWithLoadingInfo);
        }
    }

    private void doSave(DeviceInfoWithLoadingInfo deviceInfoWithLoadingInfo) {
        IDeviceSettingsModel deviceSettingsModel = getView().getDeviceSettingsModel();
        AutoBean<IDeviceSettingsModel> iDeviceSettingsAutoBean = howIsMyPhoneDoingAutoBeansFactory.create(IDeviceSettingsModel.class, deviceSettingsModel);
        String serializedMessage = AutoBeanCodex.encode(iDeviceSettingsAutoBean).getPayload();
        messageRpcServiceAsync.sendMessageToDevice(MessageType.DEVICE_SETTINGS_UPDATE,
                deviceInfoWithLoadingInfo.getiUserDeviceModel().getDeviceId(), serializedMessage, new AsyncCallbackImpl<String>(eventBus) {
            @Override
            public void success(String result) {
                messageWindow.show(messages.device_settings_settings_updated());
            }
        });
    }

    private void mergeDeviceInfo(IUserDeviceModel deviceDetailsFrom, IUserDeviceModel deviceDetailsTo) {
        deviceDetailsTo.setBatteryLevel(deviceDetailsFrom.getBatteryLevel());
        deviceDetailsTo.setBatteryStatusType(deviceDetailsFrom.getBatteryStatusType());
        deviceDetailsTo.setBatteryPluggedType(deviceDetailsFrom.getBatteryPluggedType());
        deviceDetailsTo.setBatteryHealthType(deviceDetailsFrom.getBatteryHealthType());
        deviceDetailsTo.setOperator(deviceDetailsFrom.getOperator());
        deviceDetailsTo.setNetworkType(deviceDetailsFrom.getNetworkType());
        deviceDetailsTo.setiDeviceSettingsModel(deviceDetailsFrom.getiDeviceSettingsModel());
    }

}
