package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.api.model.UserDeviceModel;
import com.artigile.howismyphonedoing.client.resources.CellStyles;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiRenderer;

/**
 * Date: 6/22/13
 * Time: 8:30 PM
 *
 * @author ioanbsu
 */

public class DeviceListCell extends AbstractCell<UserDeviceModel> {

    interface MyUiRenderer extends UiRenderer {
        void render(SafeHtmlBuilder sb, String deviceName);

        CellStyles getCellStyles();
    }

    private MyUiRenderer renderer = GWT.create(MyUiRenderer.class);


    public DeviceListCell() {
        super(BrowserEvents.CLICK, BrowserEvents.CHANGE);
    }



    @Override
    public void render(Context context, UserDeviceModel value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        renderer.render(sb, value.getHumanReadableName());
    }


}