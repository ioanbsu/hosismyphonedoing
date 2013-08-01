package com.artigile.howismyphonedoing.shared.entity;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 9:51 AM
 */
public class DeviceInfoWithLoadingInfoEntity implements Serializable{

    private IUserDeviceModel iUserDeviceModel;

    private LoadingState loadingState=LoadingState.UNKNOWN;

    public IUserDeviceModel getiUserDeviceModel() {
        return iUserDeviceModel;
    }

    public void setiUserDeviceModel(IUserDeviceModel iUserDeviceModel) {
        this.iUserDeviceModel = iUserDeviceModel;
    }

    public LoadingState getLoadingState() {
        return loadingState;
    }

    public void setLoadingState(LoadingState loadingState) {
        this.loadingState = loadingState;
    }

    public static enum LoadingState{
        LOADING,
        LOADED,
        UNKNOWN
    }
}
