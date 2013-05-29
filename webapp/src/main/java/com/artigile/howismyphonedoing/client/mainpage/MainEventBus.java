package com.artigile.howismyphonedoing.client.mainpage;

import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

/**
 * @author IoaN, 5/26/13 9:07 AM
 */
@Events(startPresenter = MainPagePresenter.class)
public interface MainEventBus extends EventBus{


    @Start
    @Event(handlers = MainPagePresenter.class)
    void initApp();

}
