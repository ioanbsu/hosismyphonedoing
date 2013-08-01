package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.client.resources.Images;
import com.artigile.howismyphonedoing.shared.entity.DeviceInfoWithLoadingInfoEntity;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Date: 6/22/13
 * Time: 8:30 PM
 *
 * @author ioanbsu
 */
@Singleton
public class DeviceListCell extends AbstractCell<DeviceInfoWithLoadingInfoEntity> {

    @Inject
    private Images images;
    private MyUiRenderer renderer = GWT.create(MyUiRenderer.class);

    public DeviceListCell() {
        super(BrowserEvents.CLICK, BrowserEvents.CHANGE);
    }

    @Override
    public void render(Context context, DeviceInfoWithLoadingInfoEntity value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        ImageResourceRenderer imageRenderer = new ImageResourceRenderer();
        SafeHtml imageSafeHtml = SafeHtmlUtils.EMPTY_SAFE_HTML;
        if (value.getLoadingState() == DeviceInfoWithLoadingInfoEntity.LoadingState.LOADING) {
            imageSafeHtml = imageRenderer.render(images.loadingSmallIcon());
        } else if (value.getLoadingState() == DeviceInfoWithLoadingInfoEntity.LoadingState.LOADED) {
            imageSafeHtml = imageRenderer.render(images.checkOk());
        }
        renderer.render(sb, value.getiUserDeviceModel().getHumanReadableName(), imageSafeHtml);
    }


    interface MyUiRenderer extends UiRenderer {
        void render(SafeHtmlBuilder sb, String deviceName, SafeHtml loadingIcon);
    }


}