package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;

/**
 * Date: 6/23/13
 * Time: 9:31 AM
 *
 * @author ioanbsu
 */

public class DeviceInfoCell extends AbstractCell<IUserDeviceModel> {
    private MyUiRenderer renderer = GWT.create(MyUiRenderer.class);

    @Override
    public void render(Context context, IUserDeviceModel value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        StringBuilder batteryInfo = new StringBuilder();

        if (value.getBatteryLevel() != null) {
            batteryInfo.append(value.getBatteryLevel()).append(" ");
        }
        if (value.getBatteryHealthType() != null) {
            batteryInfo.append(value.getBatteryHealthType()).append(" ");
        }
        if (value.getBatteryPluggedType() != null) {
            batteryInfo.append(value.getBatteryPluggedType()).append(" ");
        }
        if (value.getBatteryStatusType() != null) {
            batteryInfo.append(value.getBatteryStatusType()).append(" ");
        }
        renderer.render(sb, batteryInfo.toString());
    }


    interface MyUiRenderer extends UiRenderer {
        void render(SafeHtmlBuilder sb, String batteryInfo);
    }
}
