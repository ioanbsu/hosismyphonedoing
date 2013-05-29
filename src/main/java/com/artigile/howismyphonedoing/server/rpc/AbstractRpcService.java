package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.server.service.SecurityAspect;
import org.gwtwidgets.server.spring.ServletUtils;

import javax.servlet.http.HttpSession;

/**
 * @author IoaN, 5/28/13 9:51 PM
 */
public abstract class AbstractRpcService {

    public String getUserEmailFromSession(){
        return getSession().getAttribute(SecurityAspect.USER_IN_SESSION_EMAIL)+"";
    }

    public HttpSession getSession(){
        return ServletUtils.getRequest().getSession();
    }

}


