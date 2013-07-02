package com.artigile.howismyphonedoing.client.mvp.settings.widget;

import com.artigile.howismyphonedoing.client.rpc.MessageRpcServiceAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 7/1/13
 * Time: 8:22 PM
 *
 * @author ioanbsu
 */
@Singleton
public class AntiTheftWidget extends Composite {

    @UiField
    Button lockTheDevice;

    private AntiTheftActionLkstener antiTheftActionLkstener;

    @Inject
    private MessageRpcServiceAsync messageRpcServiceAsync;

    @Inject
    public AntiTheftWidget(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @UiHandler("lockTheDevice")
    void onLockTheDeviceClicked(ClickEvent clickEvent) {
        if (antiTheftActionLkstener != null) {
            antiTheftActionLkstener.onLockDeviceClicked();
        }
    }

    public void setAntiTheftActionLkstener(AntiTheftActionLkstener antiTheftActionLkstener) {
        this.antiTheftActionLkstener = antiTheftActionLkstener;
    }

    public static interface Binder extends UiBinder<FlowPanel, AntiTheftWidget> {
    }

    public static interface AntiTheftActionLkstener {
        void onLockDeviceClicked();
    }

}
