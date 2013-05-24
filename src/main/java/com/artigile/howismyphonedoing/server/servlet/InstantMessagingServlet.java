package com.artigile.howismyphonedoing.server.servlet;

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometServletResponse;
import net.zschech.gwt.comet.server.CometSession;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author IoaN, 5/23/13 10:10 PM
 */
public class InstantMessagingServlet extends CometServlet {

    @Override
    protected void doComet(CometServletResponse cometResponse) throws ServletException, IOException {
        CometSession cometSession = cometResponse.getSession(false);
        if (cometSession == null) {
            // The comet session has not been created yet so create it.
            AbstractServlet.cometSession = cometResponse.getSession();

        }
    }

}
