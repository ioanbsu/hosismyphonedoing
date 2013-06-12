package com.artigile.howismyphonedoing.client.mvp.mainpage;

import com.artigile.howismyphonedoing.client.mvp.mapview.MapBodyView;
import com.artigile.howismyphonedoing.client.mvp.toppanel.TopPanelView;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author IoaN, 5/26/13 9:10 AM
 */
@Singleton
public class MainPageView extends ResizeComposite implements ReverseViewInterface<MainPagePresenter> {

    @UiField
    DeckPanel mainViewPanel;
    @UiField(provided = true)
    TopPanelView topPanelView;
    @UiField(provided = true)
    MapBodyView mapBodyView;
    private MainPagePresenter mainPagePresenter;

    @Inject
    public MainPageView(Binder binder, TopPanelView topPanelView, MapBodyView mapBodyView) {
        this.topPanelView = topPanelView;
        this.mapBodyView = mapBodyView;
        initWidget(binder.createAndBindUi(this));
        mainViewPanel.showWidget(0);
        onResize();

    }

    @Override
    public MainPagePresenter getPresenter() {
        return mainPagePresenter;
    }

    @Override
    public void setPresenter(MainPagePresenter mainPagePresenter) {
        this.mainPagePresenter = mainPagePresenter;
    }

    @Override
    public void onResize() {
        mainViewPanel.setHeight((Window.getClientHeight() - 70) + "px");
    }

    public void showMap() {
//        mainViewPanel.showWidget(1);
    }

    public void onUserLoggedIn() {
        topPanelView.getElement().getStyle().setDisplay(Style.Display.BLOCK);
    }


    public static interface Binder extends UiBinder<DockLayoutPanel, MainPageView> {
    }
}
