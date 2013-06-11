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
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author IoaN, 5/27/13 8:21 PM
 */
@Aspect
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SecurityAspect implements Serializable{
    public static final String SESSION_USER_ATTR_NAME = "googlePlusLoggedInUser";
    public static final String USER_IN_SESSION_EMAIL = "userInSessionEmail";
    public static final String SESSION_STATE_KEY = "sessionStateKey";

    @Before("execution (* com.artigile.howismyphonedoing.server*..rpc..*(..)) " +
            "&& !execution(* com.artigile.howismyphonedoing.server.rpc.AuthRpcServiceImpl.validateGooglePlusCallback(..))")
    public void checkRemoteConnection(JoinPoint joinPoint) throws UserNotLoggedInException {
        HttpSession session = ServletUtils.getRequest().getSession();
        if (session.getAttribute(SESSION_USER_ATTR_NAME) == null || !(session.getAttribute(SESSION_USER_ATTR_NAME) instanceof GooglePlusAuthenticatedUser)) {
            throw new UserNotLoggedInException(createStateKey());
        }
    }



    private String createStateKey() {
        HttpSession session = ServletUtils.getRequest().getSession();

        String state = (String) session.getAttribute(SESSION_STATE_KEY);
        if (state == null) {
            // Create a state token to prevent request forgery.
            // Store it in the session for later validation.
            state = new BigInteger(130, new SecureRandom()).toString(32);
            session.setAttribute(SESSION_STATE_KEY, state);
        }
        return state;
    }

}
