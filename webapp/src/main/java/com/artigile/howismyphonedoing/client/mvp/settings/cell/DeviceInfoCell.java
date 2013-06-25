package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.api.model.battery.BatteryHealthType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryPluggedType;
import com.artigile.howismyphonedoing.api.model.battery.BatteryStatusType;
import com.artigile.howismyphonedoing.client.Messages;
import com.google.common.base.Joiner;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiRenderer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;

/**
 * Date: 6/23/13
 * Time: 9:31 AM
 *
 * @author ioanbsu
 */
@Singleton
public class DeviceInfoCell extends AbstractCell<IUserDeviceModel> {
    private MyUiRenderer renderer = GWT.create(MyUiRenderer.class);

    @Inject
    private Messages messages;

    private Templates templates = GWT.create(Templates.class);

    @Override
    public void render(Context context, IUserDeviceModel deviceModelData, SafeHtmlBuilder sb) {
        if (deviceModelData == null) {
            return;
        }
        String batteryInfo = getDeviceBatteryInfo(deviceModelData);

        String color = calculateColor(deviceModelData);
        String width = calculateWidth(deviceModelData);
        renderer.render(sb, batteryInfo, color, SafeHtmlUtils.fromString(deviceModelData.getBatteryLevel() + "").asString(),
                width, SafeHtmlUtils.fromString(batteryInfo).asString());
    }

    private String getDeviceBatteryInfo(IUserDeviceModel deviceModelData) {
        ArrayList<String> batteryInfoList = new ArrayList<String>();
        BatteryHealthType batteryHealthType = deviceModelData.getBatteryHealthType();
        if (batteryHealthType != null) {
            if (batteryHealthType == BatteryHealthType.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                batteryInfoList.add(messages.device_settings_battery_health_unspecified_failure());
            } else if (batteryHealthType == BatteryHealthType.BATTERY_HEALTH_DEAD) {
                batteryInfoList.add(messages.device_settings_battery_health_dead());
            } else if (batteryHealthType == BatteryHealthType.BATTERY_HEALTH_UNKNOWN) {
                batteryInfoList.add(messages.device_settings_battery_health_unknown());
            } else if (batteryHealthType == BatteryHealthType.BATTERY_HEALTH_OVERHEAT) {
                batteryInfoList.add(messages.device_settings_battery_health_overheat());
            } else if (batteryHealthType == BatteryHealthType.BATTERY_HEALTH_OVER_VOLTAGE) {
                batteryInfoList.add(messages.device_settings_battery_health_over_voltage());
            } else if (batteryHealthType == BatteryHealthType.BATTERY_HEALTH_COLD) {
                batteryInfoList.add(messages.device_settings_battery_health_cold());
            } else if (batteryHealthType == BatteryHealthType.BATTERY_HEALTH_GOOD) {
                batteryInfoList.add(messages.device_settings_battery_health_good());
            }
        }

        BatteryPluggedType batteryPluggedType = deviceModelData.getBatteryPluggedType();
        if (batteryPluggedType != null) {
            if (batteryPluggedType == BatteryPluggedType.BATTERY_PLUGGED_USB) {
                batteryInfoList.add(messages.device_settings_battery_plugged_usb());
            } else if (batteryPluggedType == BatteryPluggedType.BATTERY_PLUGGED_AC) {
                batteryInfoList.add(messages.device_settings_battery_plugged_ac());
            } else if (batteryPluggedType == BatteryPluggedType.BATTERY_PLUGGED_WIRELESS) {
                batteryInfoList.add(messages.device_settings_battery_plugged_wireless());
            }
        }
        BatteryStatusType batteryStatusType = deviceModelData.getBatteryStatusType();
        if (batteryStatusType != null) {
            if (batteryStatusType == BatteryStatusType.BATTERY_STATUS_FULL) {
                batteryInfoList.add(messages.device_settings_battery_status_full());
            } else if (batteryStatusType == BatteryStatusType.BATTERY_STATUS_NOT_CHARGING) {
                batteryInfoList.add(messages.device_settings_battery_status_not_charging());
            } else if (batteryStatusType == BatteryStatusType.BATTERY_STATUS_UNKNOWN) {
                batteryInfoList.add(messages.device_settings_battery_status_unknown());
            } else if (batteryStatusType == BatteryStatusType.BATTERY_STATUS_DISCHARGING) {
                batteryInfoList.add(messages.device_settings_battery_status_discharging());
            } else if (batteryStatusType == BatteryStatusType.BATTERY_STATUS_CHARGING) {
                batteryInfoList.add(messages.device_settings_battery_status_charging());
            }
        }
        return Joiner.on("\n").join(batteryInfoList);
    }

    private String calculateWidth(IUserDeviceModel deviceModelData) {
        if (deviceModelData != null) {
            return templates.widthTemplate(SafeHtmlUtils.fromString(deviceModelData.getBatteryLevel() / 100 * 96 + "")).asString();
        }
        return "";
    }

    private String calculateColor(IUserDeviceModel value) {

        if (value.getBatteryLevel() != null) {
            double maxColorIntencity = 200.;
            int green = (int) (maxColorIntencity * value.getBatteryLevel() / 100);
            int red = (int) (maxColorIntencity - (maxColorIntencity * value.getBatteryLevel() / 100));
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
