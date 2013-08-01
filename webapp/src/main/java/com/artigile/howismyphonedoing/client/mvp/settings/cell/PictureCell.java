package com.artigile.howismyphonedoing.client.mvp.settings.cell;

import com.artigile.howismyphonedoing.client.resources.Images;
import com.artigile.howismyphonedoing.shared.entity.PictureCellEntity;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiRenderer;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 7/31/13
 * Time: 5:30 PM
 */
@Singleton
public class PictureCell extends AbstractCell<PictureCellEntity> {

    @Inject
    private Images images;
    private MyUiRenderer renderer = GWT.create(MyUiRenderer.class);

    public PictureCell() {
        super(BrowserEvents.CLICK, BrowserEvents.CHANGE);
    }

    @Override
    public void render(Cell.Context context, PictureCellEntity value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        SafeHtml imageSafeHtml = SafeHtmlUtils.EMPTY_SAFE_HTML;
        renderer.render(sb, value.getPictureName(), imageSafeHtml);
    }


    interface MyUiRenderer extends UiRenderer {
        void render(SafeHtmlBuilder sb, String pictureName, SafeHtml pictureIcon);
    }

}
