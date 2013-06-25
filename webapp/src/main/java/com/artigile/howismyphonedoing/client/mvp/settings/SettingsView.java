package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceInfoCell;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceListCell;
import com.artigile.howismyphonedoing.client.service.DebugUtil;
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

    private SettingsPresenter presenter;
    @UiField
    DialogBox mainDialogBox;
    @UiField
    Button closeSettings;
    @UiField(provided = true)
    CellList<IUserDeviceModel> addableDevicesList;
    @UiField(provided = true)
    CellWidget<IUserDeviceModel> deviceInfo;
    @UiField
    Button refreshDeviceInfo;

    protected ListDataProvider<IUserDeviceModel> dataProvider = new ListDataProvider<IUserDeviceModel>();
    final SingleSelectionModel<IUserDeviceModel> selectionModel = new SingleSelectionModel<IUserDeviceModel>();


    @Inject
    public SettingsView(Binder binder,DeviceInfoCell deviceInfoCell) {
        deviceInfo = new CellWidget<IUserDeviceModel>(deviceInfoCell);
        addableDevicesList = new CellList<IUserDeviceModel>(new DeviceListCell(), getUserDeviceModelProvidesKey());
        addableDevicesList.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        dataProvider.addDataDisplay(addableDevicesList);
        addableDevicesList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                deviceInfo.setValue(selectionModel.getSelectedObject());
                presenter.requestRefreshDeviceInfo(selectionModel.getSelectedObject());
            }
        });
        binder.createAndBindUi(this);
    }

    @Override
    public void setPresenter(SettingsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public SettingsPresenter getPresenter() {
        return presenter;
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
        presenter.requestRefreshDeviceInfo(selectionModel.getSelectedObject());
    }


    protected ProvidesKey<IUserDeviceModel> getUserDeviceModelProvidesKey() {
        return new ProvidesKey<IUserDeviceModel>() {
            @Override
            public Object getKey(IUserDeviceModel item) {
                return item.getDeviceId();
            }
        };
    }

    public void updateDeviceDetails(IUserDeviceModel deviceDetails) {
        for (IUserDeviceModel userDeviceModel : dataProvider.getList()) {
            if (userDeviceModel.getDeviceId().equals(deviceDetails.getDeviceId())) {
                userDeviceModel.setBatteryLevel(deviceDetails.getBatteryLevel());
                userDeviceModel.setBatteryStatusType(deviceDetails.getBatteryStatusType());
                userDeviceModel.setBatteryPluggedType(deviceDetails.getBatteryPluggedType());
                userDeviceModel.setBatteryHealthType(deviceDetails.getBatteryHealthType());
            }
        }
        if (deviceInfo.getValue().getDeviceId().equals(deviceDetails.getDeviceId())) {
            deviceInfo.setValue(deviceDetails);
        }
    }

    public void setDevicesList(List<UserDeviceModel> result) {
        dataProvider.getList().clear();
        dataProvider.getList().addAll(result);
    }

    public static interface Binder extends UiBinder<DialogBox, SettingsView> {
    }

}
