package com.artigile.howismyphonedoing.server.servlet;

import net.zschech.gwt.comet.server.CometSession;
import org.springframework.web.servlet.mvc.Controller;

/**
 * User: ioanbsu
 * Date: 5/23/13
 * Time: 6:15 PM
 */
public abstract class AbstractServlet implements Controller{
    static final String ATTRIBUTE_STATUS = "status";

    public static CometSession cometSession;

}
