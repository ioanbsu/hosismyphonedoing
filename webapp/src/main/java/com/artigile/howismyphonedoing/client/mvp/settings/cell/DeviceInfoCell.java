package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.api.model.IUserDeviceModel;
import com.artigile.howismyphonedoing.api.model.NetworkType;
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
            sb.append(templates.selectDevice(SafeHtmlUtils.fromTrustedString(messages.device_settings_select_device_label())));
            return;
        }
        String batteryInfo = getDeviceBatteryInfo(deviceModelData);

        String batteryLevelBarColor = calculateColor(deviceModelData);
        String width = calculateWidth(deviceModelData);
        String batteryLevel = deviceModelData.getBatteryLevel() == null ? messages.device_settings_data_is_loading() : messages.device_settings_battery_level_template(deviceModelData.getBatteryLevel() + "");
        SafeHtml chargingShadow = SafeHtmlUtils.EMPTY_SAFE_HTML;
        if (deviceModelData.getBatteryStatusType() != null && deviceModelData.getBatteryStatusType() == BatteryStatusType.BATTERY_STATUS_CHARGING) {
            batteryLevel += " " + messages.device_settings_battery_charging_icon();
            chargingShadow = templates.deviceCharging();
        } else if (deviceModelData.getBatteryStatusType() != null && deviceModelData.getBatteryStatusType() == BatteryStatusType.BATTERY_STATUS_DISCHARGING) {
            chargingShadow = templates.deviceDischarging();
            batteryLevel += " " + messages.device_settings_battery_discharghing_icon();
        }
        String wifiEnabled = messages.device_settings_data_is_loading();
        if (deviceModelData.getiDeviceSettingsModel() != null) {
            wifiEnabled = deviceModelData.getiDeviceSettingsModel().isWifiEnabled() ? messages.device_settings_wifi_enabled_label() : messages.device_settings_wifi_disabled_label();
        }
        String bluetoothEnabled = messages.device_settings_data_is_loading();
        ;
        if (deviceModelData.getiDeviceSettingsModel() != null) {
            bluetoothEnabled = deviceModelData.getiDeviceSettingsModel().isBluetoothEnabled() ? messages.device_settings_bluetooth_enabled_label() : messages.device_settings_bluetooth_disabled_label();
        }
        String operator = deviceModelData.getOperator() == null ? messages.device_settings_operator_unknown() : deviceModelData.getOperator();
        String networkType = parseNetworkType(deviceModelData.getNetworkType());
        renderer.render(sb,
                batteryInfo,
                operator,
                networkType,
                wifiEnabled,
                bluetoothEnabled,
                batteryLevelBarColor,
                SafeHtmlUtils.fromTrustedString(batteryLevel).asString(),
                width, SafeHtmlUtils.fromString(batteryInfo).asString(),
                chargingShadow.asString());
    }

    private String parseNetworkType(NetworkType networkType) {
        if (networkType == NetworkType.NETWORK_TYPE_1xRTT) {
            return messages.device_settings_network_type_network_type_1xrtt();
        } else if (networkType == NetworkType.NETWORK_TYPE_CDMA) {
            return messages.device_settings_network_type_network_type_cdma();
        } else if (networkType == NetworkType.NETWORK_TYPE_EDGE) {
            return messages.device_settings_network_type_network_type_edge();
        } else if (networkType == NetworkType.NETWORK_TYPE_EHRPD) {
            return messages.device_settings_network_type_network_type_ehrpd();
        } else if (networkType == NetworkType.NETWORK_TYPE_EVDO_0) {
            return messages.device_settings_network_type_network_type_evdo_0();
        } else if (networkType == NetworkType.NETWORK_TYPE_EVDO_A) {
            return messages.device_settings_network_type_network_type_evdo_a();
        } else if (networkType == NetworkType.NETWORK_TYPE_EVDO_B) {
            return messages.device_settings_network_type_network_type_evdo_b();
        } else if (networkType == NetworkType.NETWORK_TYPE_GPRS) {
            return messages.device_settings_network_type_network_type_gprs();
        } else if (networkType == NetworkType.NETWORK_TYPE_HSDPA) {
            return messages.device_settings_network_type_network_type_hsdpa();
        } else if (networkType == NetworkType.NETWORK_TYPE_HSPA) {
            return messages.device_settings_network_type_network_type_hspa();
        } else if (networkType == NetworkType.NETWORK_TYPE_HSPAP) {
            return messages.device_settings_network_type_network_type_hspap();
        } else if (networkType == NetworkType.NETWORK_TYPE_HSUPA) {
            return messages.device_settings_network_type_network_type_hsupa();
        } else if (networkType == NetworkType.NETWORK_TYPE_IDEN) {
            return messages.device_settings_network_type_network_type_iden();
        } else if (networkType == NetworkType.NETWORK_TYPE_UMTS) {
            return messages.device_settings_network_type_network_type_umts();
        } else if (networkType == NetworkType.NETWORK_TYPE_UNKNOWN) {
            return messages.device_settings_network_type_network_type_unknown();
        }
        return messages.device_settings_network_type_network_type_unknown();
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
        if (deviceModelData != null && deviceModelData.getBatteryLevel() != null) {
            return templates.widthTemplate(SafeHtmlUtils.fromString(deviceModelData.getBatteryLevel() / 100 * 96 + "")).asString();
        }
        return "";
    }

    private String calculateColor(IUserDeviceModel iUserDeviceModel) {

        if (iUserDeviceModel.getBatteryLevel() != null) {
            double maxColorIntencity = 200.;
            int green = (int) (maxColorIntencity * iUserDeviceModel.getBatteryLevel() / 100);
            int red = (int) (maxColorIntencity - (maxColorIntencity * iUserDeviceModel.getBatteryLevel() / 100));
            SafeHtml safeHtml = templates.colorTemplate(SafeHtmlUtils.fromString(red + ""), SafeHtmlUtils.fromString(green + ""), SafeHtmlUtils.fromTrustedString("0"));
            return safeHtml.asString();
        }
        return "";
    }


    interface MyUiRenderer extends UiRenderer {
        void render(SafeHtmlBuilder sb,
                    String batteryInfo,
                    String operator,
                    String networkType,
                    String wifiEnabled,
                    String bluetoothEnabled, String batteryLevelBarColor, String batteryLevel, String width, String additionalBatteryInfo, String chargingShadow);
    }

    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("background-color: rgb({0},{1},{2})")
        SafeHtml colorTemplate(SafeHtml r, SafeHtml g, SafeHtml b);

        @SafeHtmlTemplates.Template("width: {0}%")
        SafeHtml widthTemplate(SafeHtml width);

        @SafeHtmlTemplates.Template("box-shadow: 0px 0px 5px #ff0")
        SafeHtml deviceCharging();

        @SafeHtmlTemplates.Template("box-shadow: 0px 0px 5px #f00")
        SafeHtml deviceDischarging();

        @SafeHtmlTemplates.Template("<div>{0}</div>")
        SafeHtml selectDevice(SafeHtml safeHtml);
    }

}
