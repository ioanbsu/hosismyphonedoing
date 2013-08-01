package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.*;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.HowIsMyPhoneDoingAutoBeansFactory;
import com.artigile.howismyphonedoing.client.widget.MessageWindow;
import com.artigile.howismyphonedoing.client.widget.YesNoWindow;
import com.artigile.howismyphonedoing.shared.entity.DeviceInfoWithLoadingInfoEntity;
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
import java.util.logging.Logger;

/**
 * Date: 6/22/13
 * Time: 7:53 PM
 *
 * @author ioanbsu
 */
@Singleton
@Presenter(view = SettingsView.class)
public class SettingsPresenter extends BasePresenter<SettingsView, SettingsEventBus> {

    public static final Logger logger = Logger.getLogger(SettingsPresenter.class.getName());
    final private SingleSelectionModel<DeviceInfoWithLoadingInfoEntity> deviceSelectionModel = new SingleSelectionModel<DeviceInfoWithLoadingInfoEntity>();
    final private ListDataProvider<DeviceInfoWithLoadingInfoEntity> devicesListDataProvider = new ListDataProvider<DeviceInfoWithLoadingInfoEntity>();
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
    @Inject
    private UserInfoRpcServiceAsync userInfoRpcServiceAsync;

    public void onInitApp() {
        logger.info("Settings window initiated.");
        devicesListDataProvider.addDataDisplay(getView().getDevicesListView());
        getView().getDevicesListView().setSelectionModel(deviceSelectionModel);
        deviceSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                getView().setDeviceInfoModel(deviceSelectionModel.getSelectedObject().getiUserDeviceModel());
                getView().setDeviceSettingsModel(deviceSelectionModel.getSelectedObject().getiUserDeviceModel().getiDeviceSettingsModel());
                getView().enable(true);
                if (deviceSelectionModel.getSelectedObject().getiUserDeviceModel().getBatteryLevel() == null) {
                    requestDeviceInfoUpdate();
                }
                eventBus.centerDeviceLocationOnScreen(deviceSelectionModel.getSelectedObject().getiUserDeviceModel());
            }
        });
    }

    public void requestDeviceInfoUpdate() {
        requestRefreshDeviceInfo(deviceSelectionModel.getSelectedObject().getiUserDeviceModel());
        deviceSelectionModel.getSelectedObject().setLoadingState(DeviceInfoWithLoadingInfoEntity.LoadingState.LOADING);
        getView().getDevicesListView().redraw();
    }

    public void onShowSettingsWindow() {
        logger.info("Showing view" + view + " " + getView());
        view.show();
    }

    public void onUserLogout() {
        getView().enable(false);
        getView().setDeviceInfoModel(null);
    }

    public void onPictureFromThePhoneReceived(IPictureReadyModel picture) {
        getView().pictureFromDeviceReceived(picture);
    }

    public void onUsersDevicesListReceived(List<UserDeviceModel> result) {
        DeviceInfoWithLoadingInfoEntity selectedObject = deviceSelectionModel.getSelectedObject();
        DeviceInfoWithLoadingInfoEntity newSelected = null;

        List<DeviceInfoWithLoadingInfoEntity> existentList = new ArrayList<DeviceInfoWithLoadingInfoEntity>(devicesListDataProvider.getList());
        devicesListDataProvider.getList().clear();
        List<DeviceInfoWithLoadingInfoEntity> deviceInfoWithLoadingInfoList = new ArrayList<DeviceInfoWithLoadingInfoEntity>();
        for (UserDeviceModel userDeviceModel : result) {
            DeviceInfoWithLoadingInfoEntity deviceInfoWithLoadingInfo = new DeviceInfoWithLoadingInfoEntity();
            deviceInfoWithLoadingInfo.setiUserDeviceModel(userDeviceModel);
            deviceInfoWithLoadingInfo.setLoadingState(DeviceInfoWithLoadingInfoEntity.LoadingState.UNKNOWN);
            for (DeviceInfoWithLoadingInfoEntity deviceInfoWithLoadingInfoInList : existentList) {
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
            deviceSelectionModel.setSelected(newSelected, true);
            getView().setDeviceSettingsModel(newSelected.getiUserDeviceModel().getiDeviceSettingsModel());
            getView().enable(true);
        } else {
            getView().enable(false);
            getView().setDeviceInfoModel(null);
        }
        getView().displayNoDevicesFoundLabel(devicesListDataProvider.getList().isEmpty());
    }

    public void onDeviceDetailsReceived(IUserDeviceModel deviceDetails) {
        for (DeviceInfoWithLoadingInfoEntity userDeviceModel : devicesListDataProvider.getList()) {
            if (userDeviceModel.getiUserDeviceModel().getDeviceId().equals(deviceDetails.getDeviceId())) {
                mergeDeviceInfo(deviceDetails, userDeviceModel.getiUserDeviceModel());
                userDeviceModel.setLoadingState(DeviceInfoWithLoadingInfoEntity.LoadingState.LOADED);
                getView().getDevicesListView().redraw();
            }
        }
        if (getView().getDeviceInfoModel().getDeviceId().equals(deviceDetails.getDeviceId())) {
            getView().setDeviceInfoModel(deviceDetails);
            getView().setDeviceSettingsModel(deviceDetails.getiDeviceSettingsModel());
        }
    }

    public void onDeviceHadBeenLocked(IUserDeviceModel userDevice) {
        getView().showDeviceLockIsInProgress(false);

    }

    public void onDeviceAdminIsNotEnabled(IUserDeviceModel userDevice) {
        messageWindow.show(messages.device_settings_anti_theft_is_not_enabled(userDevice.getHumanReadableName()));
        getView().showDeviceLockIsInProgress(false);
    }

    public void requestRefreshDeviceInfo(IUserDeviceModel selectedObject) {
        messageRpcServiceAsync.sendMessageToDevice(MessageType.DEVICE_DETAILS_INFO, selectedObject.getDeviceId(), "", new AsyncCallbackImpl<String>() {
        });
    }

    public void onSaveDeviceConfigClicked() {
        DeviceInfoWithLoadingInfoEntity deviceInfoWithLoadingInfo = deviceSelectionModel.getSelectedObject();
        if (deviceSelectionModel.getSelectedObject() == null) {
            Window.alert("Please select device first");
            return;
        }
        if (deviceInfoWithLoadingInfo.getiUserDeviceModel().getNetworkType() == NetworkType.NETWORK_TYPE_UNKNOWN
                && (deviceInfoWithLoadingInfo.getiUserDeviceModel().getiDeviceSettingsModel().isWifiEnabled() && !getView().getDeviceSettingsModel().isWifiEnabled())) {
            yesNoWindow.show(new ClickHandler() {
                                 @Override
                                 public void onClick(ClickEvent event) {
                                     doSave(deviceSelectionModel.getSelectedObject());
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

    private void doSave(DeviceInfoWithLoadingInfoEntity deviceInfoWithLoadingInfo) {
        IDeviceSettingsModel deviceSettingsModel = getView().getDeviceSettingsModel();
        AutoBean<IDeviceSettingsModel> iDeviceSettingsAutoBean = howIsMyPhoneDoingAutoBeansFactory.create(IDeviceSettingsModel.class, deviceSettingsModel);
        String serializedMessage = AutoBeanCodex.encode(iDeviceSettingsAutoBean).getPayload();
        messageRpcServiceAsync.sendMessageToDevice(MessageType.DEVICE_SETTINGS_UPDATE,
                deviceInfoWithLoadingInfo.getiUserDeviceModel().getDeviceId(), serializedMessage, new AsyncCallbackImpl<String>() {
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

    public void displayLogsOnSelectedDevice() {
        messageRpcServiceAsync.sendMessageToDevice(MessageType.DISPLAY_LOGS, deviceSelectionModel.getSelectedObject().getiUserDeviceModel().getDeviceId(), "", new AsyncCallbackImpl<String>() {
        });
    }

    public void hideLogsOnSelectedDevice() {
        messageRpcServiceAsync.sendMessageToDevice(MessageType.HIDE_LOGS, deviceSelectionModel.getSelectedObject().getiUserDeviceModel().getDeviceId(), "", new AsyncCallbackImpl<String>() {
        });
    }

    public void onLockDeviceClicked() {
        String newPinCode = getView().getNewPinCode();
        ILockDeviceScreenModel lockDeviceScreenModel = new LockDeviceScreenModel();
        lockDeviceScreenModel.setNewPinCode(newPinCode);
        AutoBean<ILockDeviceScreenModel> iLockDeviceScreenModelAutoBean = howIsMyPhoneDoingAutoBeansFactory.create(ILockDeviceScreenModel.class, lockDeviceScreenModel);
        String serializedMessage = AutoBeanCodex.encode(iLockDeviceScreenModelAutoBean).getPayload();
        messageRpcServiceAsync.sendMessageToDevice(MessageType.LOCK_DEVICE, deviceSelectionModel.getSelectedObject().getiUserDeviceModel().getDeviceId(), serializedMessage, new AsyncCallbackImpl<String>() {
        });
        getView().resetNewPinCodeTextBox();
        getView().showDeviceLockIsInProgress(true);

    }

    public void onTakePictureClicked() {
        ITakePictureModel takePictureModel = new TakePictureModel();
        AutoBean<ITakePictureModel> iTakePictureModelAutoBean = howIsMyPhoneDoingAutoBeansFactory.create(ITakePictureModel.class, takePictureModel);
        iTakePictureModelAutoBean.as().setCameraType(getView().getCameraType());
        iTakePictureModelAutoBean.as().setHighQuality(getView().isHighQuality());
        String serializedMessage = AutoBeanCodex.encode(iTakePictureModelAutoBean).getPayload();
        logger.info("Picture model: " + serializedMessage);
        messageRpcServiceAsync.sendMessageToDevice(MessageType.TAKE_PICTURE, deviceSelectionModel.getSelectedObject().getiUserDeviceModel().getDeviceId(), serializedMessage, new AsyncCallbackImpl<String>() {
        });
    }

    public void onRefreshDevicesListClicked() {
        getView().showDeviceListLoading(true);
        userInfoRpcServiceAsync.getUsersDevicesList(new AsyncCallbackImpl<List<UserDeviceModel>>() {
            @Override
            public void success(List<UserDeviceModel> result) {
                eventBus.usersDevicesListReceived(result);
                getView().showDeviceListLoading(false);
            }

            @Override
            public void failure(Throwable caught) {
                getView().showDeviceListLoading(false);
            }
        });
    }
}
