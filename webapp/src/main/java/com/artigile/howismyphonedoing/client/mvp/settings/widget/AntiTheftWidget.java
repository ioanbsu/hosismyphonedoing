package com.artigile.howismyphonedoing.client.mvp.settings.widget;

import com.artigile.howismyphonedoing.api.model.CameraType;
import com.artigile.howismyphonedoing.api.model.IPictureReadyModel;
import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageType;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.PictureCell;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcServiceAsync;
import com.artigile.howismyphonedoing.client.service.CommonUiUtil;
import com.artigile.howismyphonedoing.client.widget.DisplayPictureWindow;
import com.artigile.howismyphonedoing.client.widget.MessageWindow;
import com.artigile.howismyphonedoing.client.widget.YesNoWindow;
import com.artigile.howismyphonedoing.shared.entity.PictureCellEntity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Date: 7/1/13
 * Time: 8:22 PM
 *
 * @author ioanbsu
 */
@Singleton
public class AntiTheftWidget extends Composite {

    final private ListDataProvider<PictureCellEntity> picturesCellListDataProvider = new ListDataProvider<PictureCellEntity>();
    final private SingleSelectionModel<PictureCellEntity> pictureSelectionModel = new SingleSelectionModel<PictureCellEntity>();
    @UiField
    Button lockTheDevice;
    @UiField
    PasswordTextBox newPinCode;
    @UiField
    Image deviceLockingLoading;
    @UiField
    Image takePicture;
    @UiField
    CheckBox highQuality;
    @UiField
    RadioButton frontCamera;
    @UiField
    RadioButton backCamera;
    @UiField(provided = true)
    CellList<PictureCellEntity> picturesList;
    @UiField
    Image refreshPicturesList;
    @UiField
    Image deleteSelectedPicture;
    @UiField
    Image deleteAllPictures;
    @UiField
    Image viewFullSize;
    @UiField
    FlowPanel pictureLoadingPanel;
    @UiField
    Label hidePicture;
    @UiField
    Label selectDeviceMsg;
    @UiField
    HTMLPanel antiTheftPanel;
    @UiField
    Button wipeDevice;
    @Inject
    private MessageWindow messageWindow;
    @Inject
    private Messages messages;
    private AntiTheftActionListener antiTheftActionListener;
    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;
    @Inject
    private PictureCell pictureCell;
    @Inject
    private UserInfoRpcServiceAsync userInfoRpcServiceAsync;
    private IUserDeviceModel selectedModel;
    @Inject
    private CommonUiUtil commonUiUtil;
    @Inject
    private DisplayPictureWindow displayPictureWindow;
    @Inject
    private YesNoWindow yesNoWindow;


    @Inject
    public AntiTheftWidget(Binder binder, PictureCell pictureCell) {
        picturesList = new CellList<PictureCellEntity>(pictureCell);
        initWidget(binder.createAndBindUi(this));
        frontCamera.setValue(true);
        picturesList.setSelectionModel(pictureSelectionModel);
        picturesCellListDataProvider.addDataDisplay(picturesList);
        initHandlers();
    }

    private void initHandlers() {
        pictureSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {

            }
        });
    }

    @UiHandler("lockTheDevice")
    void onLockTheDeviceClicked(ClickEvent clickEvent) {
        if (antiTheftActionListener != null) {
            antiTheftActionListener.onLockDeviceClicked();
        }
    }

    @UiHandler("takePicture")
    void onTakePictureClicked(ClickEvent clickEvent) {
        if (antiTheftActionListener != null) {
            pictureLoadingPanel.setVisible(true);
            antiTheftActionListener.onTakePictureClicked();
        }
    }

    @UiHandler("refreshPicturesList")
    void onRefreshPicturesListClickedHandler(ClickEvent clickEvent) {
        refreshPicturesList();
    }

    @UiHandler("deleteSelectedPicture")
    void onDeleteSelectedPicture(ClickEvent clickEvent) {
        if (pictureSelected()) {
            userInfoRpcServiceAsync.removePicture(pictureSelectionModel.getSelectedObject().getPictureId(), new AsyncCallbackImpl<Void>() {

            });
            picturesCellListDataProvider.getList().remove(pictureSelectionModel.getSelectedObject());
            pictureSelectionModel.setSelected(null, true);
        }
    }

    @UiHandler("deleteAllPictures")
    void onDeleteAllPictures(ClickEvent clickEvent) {
        if (selectedModel != null) {
            userInfoRpcServiceAsync.removeAllPicturesFromDevice(selectedModel.getDeviceId(), new AsyncCallbackImpl<Void>() {

            });
            picturesCellListDataProvider.getList().clear();
            pictureSelectionModel.setSelected(null, true);
        }
    }

    @UiHandler("viewFullSize")
    void onViewFullSize(ClickEvent clickEvent) {
        if (pictureSelected()) {
            displayPictureWindow.show(commonUiUtil.getPictureUrl(pictureSelectionModel.getSelectedObject().getPictureId(), false));
        }
    }

    @UiHandler("hidePicture")
    void onHidePicture(ClickEvent clickEvent) {
        pictureLoadingPanel.setVisible(false);
    }

    @UiHandler("wipeDevice")
    void onWipeDevice(ClickEvent clickEvent) {
        if (selectedModel != null) {
            yesNoWindow.show(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    messageRpcServiceAsync.sendMessageToDevice(MessageType.WIPE_DEVICE, selectedModel.getDeviceId(), "", new AsyncCallbackImpl<String>() {
                    });
                }
            }, null, messages.device_settings_anti_theft_wipe_device_prompt());
        } else {
            messageWindow.show(messages.device_settings_select_the_device_window_label());
        }
    }

    public void setAntiTheftActionListener(AntiTheftActionListener antiTheftActionListener) {
        this.antiTheftActionListener = antiTheftActionListener;
    }

    public String getNewPinCode() {
        return newPinCode.getText();
    }

    public void resetNewPinCode() {
        newPinCode.setText("");
    }

    public void showDeviceLoading(boolean isLoading) {
        deviceLockingLoading.setVisible(isLoading);
    }

    public CameraType getCameraType() {
        if (frontCamera.getValue()) {
            return CameraType.FRONT;
        } else if (backCamera.getValue()) {
            return CameraType.BACK;
        }
        return CameraType.FRONT;
    }

    public boolean isHighQuality() {
        return highQuality.getValue();
    }

    public void deviceHadBeenSelected(IUserDeviceModel model) {
        this.selectedModel = model;
        refreshPicturesList();
    }

    private void refreshPicturesList() {
        if (selectedModel != null) {
            userInfoRpcServiceAsync.getPicturesFromDevice(selectedModel.getDeviceId(), new AsyncCallbackImpl<List<PictureCellEntity>>() {
                @Override
                public void success(List<PictureCellEntity> result) {
                    picturesCellListDataProvider.setList(result);
                }
            });
        }
    }

    public void pictureFromThDeviceReceived(IPictureReadyModel picture) {
        pictureLoadingPanel.setVisible(false);
        refreshPicturesList();
    }

    public void enableControls(boolean enabled) {
        selectDeviceMsg.setVisible(!enabled);
        antiTheftPanel.setVisible(enabled);
    }

    public boolean pictureSelected() {
        if (pictureSelectionModel.getSelectedObject() == null) {
            messageWindow.show(messages.device_settings_select_a_picture());
            return false;
        }
        return true;
    }

    public static interface Binder extends UiBinder<FlowPanel, AntiTheftWidget> {
    }

    public static interface AntiTheftActionListener {
        void onLockDeviceClicked();

        void onTakePictureClicked();

    }


}
