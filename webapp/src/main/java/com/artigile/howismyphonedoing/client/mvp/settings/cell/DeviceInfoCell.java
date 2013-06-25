package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.client.Messages;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiRenderer;

/**
 * Date: 6/23/13
 * Time: 9:31 AM
 *
 * @author ioanbsu
 */

public class DeviceInfoCell extends AbstractCell<IUserDeviceModel> {
    private MyUiRenderer renderer = GWT.create(MyUiRenderer.class);

    private Messages messages = GWT.create(Messages.class);

    private Templates templates = GWT.create(Templates.class);


    @Override
    public void render(Context context, IUserDeviceModel deviceModelData, SafeHtmlBuilder sb) {
        if (deviceModelData == null) {
            return;
        }
        StringBuilder batteryInfo = new StringBuilder();

        if (deviceModelData.getBatteryHealthType() != null) {
            batteryInfo.append(deviceModelData.getBatteryHealthType()).append(" ");
        }
        if (deviceModelData.getBatteryPluggedType() != null) {
            batteryInfo.append(deviceModelData.getBatteryPluggedType()).append(" ");
        }
        if (deviceModelData.getBatteryStatusType() != null) {
            batteryInfo.append(deviceModelData.getBatteryStatusType()).append(" ");
        }

        String color = calculateColor(deviceModelData);
        String width = calculateWidth(deviceModelData);
        renderer.render(sb, batteryInfo.toString(), color, SafeHtmlUtils.fromString(deviceModelData.getBatteryLevel() + "").asString(),
                width, SafeHtmlUtils.fromString(batteryInfo.toString()).asString());
    }

    private String calculateWidth(IUserDeviceModel deviceModelData) {
        if (deviceModelData != null) {
            return templates.widthTemplate(SafeHtmlUtils.fromString(deviceModelData.getBatteryLevel()/100*96 + "")).asString();
        }
        return "";
    }

    private String calculateColor(IUserDeviceModel value) {

        if (value.getBatteryLevel() != null) {
            double maxColorIntencity = 200.;
            int green = (int) (maxColorIntencity * value.getBatteryLevel() / 100);
            int red =(int)( maxColorIntencity -  (maxColorIntencity * value.getBatteryLevel() / 100));
            SafeHtml safeHtml = templates.colorTemplate(SafeHtmlUtils.fromString(red + ""), SafeHtmlUtils.fromString(green + ""), SafeHtmlUtils.fromTrustedString("0"));
            return safeHtml.asString();
        }
        return "";
    }


    interface MyUiRenderer extends UiRenderer {
        void render(SafeHtmlBuilder sb, String batteryInfo, String color, String batteryLevel, String width, String additionalBatteryInfo);
    }

    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("background-color: rgb({0},{1},{2})")
        SafeHtml colorTemplate(SafeHtml r, SafeHtml g, SafeHtml b);

        @SafeHtmlTemplates.Template("width: {0}%")
        SafeHtml widthTemplate(SafeHtml width);
    }

}
