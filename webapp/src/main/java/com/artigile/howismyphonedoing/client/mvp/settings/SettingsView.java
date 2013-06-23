package com.artigile.howismyphonedoing.client.mvp.settings;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceInfoCell;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.DeviceListCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellWidget;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimplePanel;
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
    CellList<UserDeviceModel> addableDevicesList;
    @UiField
    SimplePanel deviceInfo;
    @UiField
    Button refreshDeviceInfo;

    protected ListDataProvider<UserDeviceModel> dataProvider = new ListDataProvider<UserDeviceModel>();
    final SingleSelectionModel<UserDeviceModel> selectionModel = new SingleSelectionModel<UserDeviceModel>();


    @Inject
    public SettingsView(Binder binder) {
        addableDevicesList = new CellList<UserDeviceModel>(new DeviceListCell(), getUserDeviceModelProvidesKey());
        addableDevicesList.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);
        dataProvider.addDataDisplay(addableDevicesList);

        addableDevicesList.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                presenter.onDeviceSelected(selectionModel.getSelectedObject());
                CellWidget<UserDeviceModel> cellWidget=new CellWidget<UserDeviceModel>(new DeviceInfoCell());
                cellWidget.setValue(selectionModel.getSelectedObject());
                deviceInfo.setWidget(cellWidget);
            }
        });
        binder.createAndBindUi(this);
        initFake();
    }

    private void initFake() {
        List<UserDeviceModel> userDeviceModels = new ArrayList<UserDeviceModel>();
        UserDeviceModel userDeviceModel1 = new UserDeviceModel();
        UserDeviceModel userDeviceModel2 = new UserDeviceModel();
        userDeviceModel1.setDeviceId("1");
        userDeviceModel1.setHumanReadableName("name1");
        userDeviceModel2.setDeviceId("2");
        userDeviceModel2.setHumanReadableName("name2");
        userDeviceModels.add(userDeviceModel1);
        userDeviceModels.add(userDeviceModel2);
        dataProvider.setList(userDeviceModels);
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
        presenter.requestRefreshDeficeInfo(selectionModel.getSelectedObject());
    }


    protected ProvidesKey<UserDeviceModel> getUserDeviceModelProvidesKey() {
        return new ProvidesKey<UserDeviceModel>() {
            @Override
            public Object getKey(UserDeviceModel item) {
                return item.getDeviceId();
            }
        };
    }

    public static interface Binder extends UiBinder<DialogBox, SettingsView> {
    }

}
