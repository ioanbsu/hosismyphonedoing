package com.artigile.howismyphonedoing.server.servlet;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author IoaN, 5/23/13 11:03 PM
 */
@Service
public class TestCometServlet extends AbstractServlet {
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
       AbstractServlet.cometSession.enqueue("Hello World!!!!");
        AbstractServlet.cometSession.enqueued();
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
