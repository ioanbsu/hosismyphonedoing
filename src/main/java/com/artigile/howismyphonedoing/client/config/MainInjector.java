package com.artigile.howismyphonedoing.client.config;

import com.artigile.howismyphonedoing.client.CometListenerImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author IoaN, 5/23/13 10:05 PM
 */
@GinModules(InjectorModule.class)
public interface MainInjector extends Ginjector {
    public static MainInjector MAIN_INJECTOR = GWT.create(MainInjector.class);

    public CometListenerImpl comet();

}

