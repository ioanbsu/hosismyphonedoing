package com.artigile.howismyphonedoing.client.mvp.settings.widget;

import com.artigile.howismyphonedoing.api.model.CameraType;
import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.client.Messages;
import com.artigile.howismyphonedoing.client.mvp.settings.cell.PictureCell;
import com.artigile.howismyphonedoing.client.rpc.AsyncCallbackImpl;
import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.artigile.howismyphonedoing.client.rpc.UserInfoRpcServiceAsync;
import com.artigile.howismyphonedoing.client.widget.MessageWindow;
import com.artigile.howismyphonedoing.shared.entity.PictureCellEntity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;

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
    @UiField
    Button lockTheDevice;
    @UiField
    PasswordTextBox newPinCode;
    @UiField
    Image deviceLockingLoading;
    @UiField
    Button takePicture;
    @UiField
    CheckBox highQuality;
    @UiField
    RadioButton frontCamera;
    @UiField
    RadioButton backCamera;
    @UiField(provided = true)
    CellList<PictureCellEntity> picturesList;
    @UiField
    Button refreshPicturesList;
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
    public AntiTheftWidget(Binder binder, PictureCell pictureCell) {
        picturesList = new CellList<PictureCellEntity>(pictureCell);
        initWidget(binder.createAndBindUi(this));
        frontCamera.setValue(true);
        picturesCellListDataProvider.addDataDisplay(picturesList);
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
            antiTheftActionListener.onTakePictureClicked();
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
        refreshDevicesList();
    }

    private void refreshDevicesList() {
        if (selectedModel != null) {
            userInfoRpcServiceAsync.getPicturesFromDevice(selectedModel.getDeviceId(), new AsyncCallbackImpl<List<PictureCellEntity>>() {
                @Override
                public void success(List<PictureCellEntity> result) {
                    picturesCellListDataProvider.setList(result);
                }
            });
        }
    }

    @UiHandler("refreshPicturesList")
    void onRefreshPicturesListClickedHandler(ClickEvent clickEvent) {
        refreshDevicesList();
    }

    public static interface Binder extends UiBinder<HTMLPanel, AntiTheftWidget> {
    }

    public static interface AntiTheftActionListener {
        void onLockDeviceClicked();

        void onTakePictureClicked();

    }


}
