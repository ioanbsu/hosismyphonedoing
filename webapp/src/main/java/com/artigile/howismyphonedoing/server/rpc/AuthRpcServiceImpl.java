package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.client.exception.UserNotLoggedInException;
import com.artigile.howismyphonedoing.client.rpc.AuthRpcService;
import com.artigile.howismyphonedoing.server.service.SecurityAspect;
import com.artigile.howismyphonedoing.server.service.cloudutil.KeysResolver;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import com.artigile.howismyphonedoing.shared.entity.StateAndChanelEntity;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gson.Gson;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.artigile.howismyphonedoing.server.service.SecurityAspect.SESSION_STATE_KEY;
import static com.artigile.howismyphonedoing.server.service.SecurityAspect.SESSION_USER_ATTR_NAME;

/**
 * @author IoaN, 5/26/13 10:44 AM
 */
@Service
public class AuthRpcServiceImpl extends AbstractRpcService implements AuthRpcService {

    /**
     * Default HTTP transport to use to make HTTP requests.
     */
    private static final HttpTransport TRANSPORT = new NetHttpTransport();
    /**
     * Default JSON factory to use to deserialize JSON.
     */
    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();
    /**
     * Gson object to serialize JSON responses to requests to this servlet.
     */
    private static final Gson GSON = new Gson();
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    private KeysResolver keysResolver;

    @Override
    public StateAndChanelEntity getLoggedInUser() throws UserNotLoggedInException {
        StateAndChanelEntity stateAndChanelEntity = new StateAndChanelEntity();
        stateAndChanelEntity.setEmail(getUserEmailFromSession());
        stateAndChanelEntity.setChanelToken(initServerGaeChanel(stateAndChanelEntity.getEmail()));
        return stateAndChanelEntity;
    }

    @Override
    public StateAndChanelEntity validateGooglePlusCallback(GooglePlusAuthenticatedUser googlePlusAuthenticatedUser) throws Exception {
        logger.info("Got request to validate user");
        HttpServletRequest request = ServletUtils.getRequest();
        HttpServletResponse response = ServletUtils.getResponse();

        // Ensure that this is no request forgery going on, and that the user
        // sending us this connect request is the user that was supposed to.
        if (!googlePlusAuthenticatedUser.getState().equals(getSession().getAttribute(SESSION_STATE_KEY))) {
            response.setStatus(401);
            throw new Exception("Invalid state parameter.");
        }
        try {
            logger.info("1 --- " + keysResolver.getClientId() + " " + keysResolver.getClientSecret());
            // Upgrade the authorization code into an access and refresh token.
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JSON_FACTORY,
                    keysResolver.getClientId(), keysResolver.getClientSecret(), googlePlusAuthenticatedUser.getCode(),
                    "postmessage").execute();
            // Create a credential representation of the token data.
            logger.info("2");
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory(JSON_FACTORY)
                    .setTransport(TRANSPORT)
                    .setClientSecrets(keysResolver.getClientId(), keysResolver.getClientSecret()).build()
                    .setFromTokenResponse(tokenResponse);

            // Check that the token is valid.
            logger.info("3");
            Oauth2 oauth2 = new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential).build();
            Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(credential.getAccessToken()).execute();
            // If there was an error in the token info, abort.
            logger.info("4");
            if (tokenInfo.containsKey("error")) {
                response.setStatus(401);
                throw new Exception(tokenInfo.get("error").toString());
            }
            // Make sure the token we got is for the intended user.
            /*if (!tokenInfo.getUserId().equals(clientId)) {
                response.setStatus(401);
                return GSON.toJson("Token's user ID doesn't match given user ID.");
            }*/
            // Make sure the token we got is for our app.
            logger.info("5");
            if (!tokenInfo.getIssuedTo().equals(keysResolver.getClientId())) {
                response.setStatus(401);
                throw new Exception("Token's client ID does not match app's.");
            }
            // Store the token in the session for later use.
            logger.info("User had been validated. Saving into session");
            request.getSession().setAttribute("token", tokenResponse.toString());
            request.getSession().setAttribute(SESSION_USER_ATTR_NAME, googlePlusAuthenticatedUser);
            request.getSession().setAttribute(SecurityAspect.USER_IN_SESSION_EMAIL, tokenInfo.getEmail());
            logger.info("User had been saved into session");
            return getLoggedInUser();
        } catch (TokenResponseException e) {
            response.setStatus(500);
            e.printStackTrace();
            throw new Exception("Failed to upgrade the authorization code.");
        } catch (IOException e) {
            response.setStatus(500);
            logger.log(Level.WARNING, "Failed to read token data from Google. ", e);
            throw new Exception("Failed to read token data from Google. " + e.getMessage() + "[[ " + e.getCause());
        }

    }

    private String initServerGaeChanel(String email) {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        return channelService.createChannel(email);
    }

    @Override
    public void logout() {
        String stateKey = (String) getSession().getAttribute(SESSION_STATE_KEY);
        ServletUtils.getRequest().getSession().invalidate();
        getSession().setAttribute(SESSION_STATE_KEY, stateKey);
    }


}
