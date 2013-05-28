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

/**
 * @author IoaN, 5/27/13 8:21 PM
 */
@Aspect
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SecurityAspect {
    public static final String SESSION_USER_ATTR_NAME = "googlePlusLoggedInUser";

    @Before("execution (* com.artigile.howismyphonedoing*..rpc..*(..)) && !execution(* com.artigile.howismyphonedoing.server.rpc.AuthRpcServiceImpl.validateGooglePlusCallback(..))")
    public void checkRemoteConnection(JoinPoint joinPoint) throws UserNotLoggedInException {
        HttpSession session = ServletUtils.getRequest().getSession();
        if (session.getAttribute(SESSION_USER_ATTR_NAME) == null || !(session.getAttribute(SESSION_USER_ATTR_NAME) instanceof GooglePlusAuthenticatedUser)) {
            throw new UserNotLoggedInException();
        }
    }

}
