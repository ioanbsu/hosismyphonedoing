/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

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


