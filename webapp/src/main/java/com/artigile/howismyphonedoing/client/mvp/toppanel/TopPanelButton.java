package com.artigile.howismyphonedoing.client.mvp.toppanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * User: ioanbsu
 * Date: 6/20/13
 * Time: 7:05 PM
 */
public class TopPanelButton extends Composite {


    @UiField
    Label buttonText;
    @UiField
    FocusPanel mainPanel;
    @UiField
    Image loadingIcon;

    public TopPanelButton() {
        Binder binder = GWT.create(Binder.class);
        initWidget(binder.createAndBindUi(this));
        hideLoading();
    }

    public void setText(String text) {
        buttonText.setText(text);
    }

    public HandlerRegistration addClickHandler(ClickHandler clickHandler) {
        return mainPanel.addClickHandler(clickHandler);
    }

    public void showLoading(String title) {
        loadingIcon.setTitle(title);
        loadingIcon.setVisible(true);
    }

    public void hideLoading() {
        loadingIcon.setVisible(false);
        loadingIcon.setTitle("");
    }

    public boolean isLoadingShowing(){
        return loadingIcon.isVisible();
    }

    public void showTooltip(String tooltipText){
        PopupPanel panel=new PopupPanel(true,false);
        panel.add(new Label(tooltipText));
        panel.setPopupPosition(mainPanel.getAbsoluteLeft(),mainPanel.getAbsoluteTop()+65);
        panel.show();
    }

    public static interface Binder extends UiBinder<FocusPanel, TopPanelButton> {
    }
}
