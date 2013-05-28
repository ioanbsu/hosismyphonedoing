package com.artigile.howismyphonedoing.client.mainpage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.mvp4g.client.view.ReverseViewInterface;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author IoaN, 5/26/13 9:10 AM
 */
@Singleton
public class MainPageView extends Composite implements ReverseViewInterface<MainPagePresenter> {

    public static interface Binder extends UiBinder<DockLayoutPanel, MainPageView> {
    }


    private MainPagePresenter mainPagePresenter;
    @UiField
    Button logoutButton;

    @Inject
    public MainPageView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public MainPagePresenter getPresenter() {
        return mainPagePresenter;
    }

    @Override
    public void setPresenter(MainPagePresenter mainPagePresenter) {
        this.mainPagePresenter = mainPagePresenter;
    }

    @UiHandler("logoutButton")
    void logOutButtonClickHandler(ClickEvent clickEvent){
        mainPagePresenter.logout();
    }
}
