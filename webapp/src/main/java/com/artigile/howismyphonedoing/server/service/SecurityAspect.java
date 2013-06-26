/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.client.exception.UserNotLoggedInException;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * @author IoaN, 5/27/13 8:21 PM
 */
@Aspect
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SecurityAspect implements Serializable {
    public static final String SESSION_USER_ATTR_NAME = "googlePlusLoggedInUser";
    public static final String USER_IN_SESSION_EMAIL = "userInSessionEmail";


    @Before("execution (* com.artigile.howismyphonedoing.server*..rpc..*(..)) " +
            "&& !execution(* com.artigile.howismyphonedoing.server.rpc.AuthRpcServiceImpl.validateGooglePlusCallback(..)) " +
            "&& !execution(* com.artigile.howismyphonedoing.server.rpc.AuthRpcServiceImpl.logout(..))" +
            "&& !execution(* com.artigile.howismyphonedoing.server.rpc.AuthRpcServiceImpl.refreshStateToken(..))")
    public void checkRemoteConnection(JoinPoint joinPoint) throws UserNotLoggedInException {
        HttpSession session = ServletUtils.getRequest().getSession();
        if (session.getAttribute(SESSION_USER_ATTR_NAME) == null || !(session.getAttribute(SESSION_USER_ATTR_NAME) instanceof GooglePlusAuthenticatedUser)) {
            throw new UserNotLoggedInException();
        }
    }


}
