package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.client.exception.UserNotLoggedInException;
import com.artigile.howismyphonedoing.client.rpc.AuthRpcService;
import com.artigile.howismyphonedoing.server.service.SecurityAspect;
import com.artigile.howismyphonedoing.shared.entity.GooglePlusAuthenticatedUser;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.gson.Gson;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author IoaN, 5/26/13 10:44 AM
 */
@Service
public class AuthRpcServiceImpl implements AuthRpcService {
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
    private static final String CLIENT_ID = "REPLACE";
    private static final String CLIENT_SECRET = "REPLACE";
    /**
     * Optionally replace this with your application's name.
     */
    private static final String APPLICATION_NAME = "How Is My Phone Doing";

    @Override
    public String userIsInSession() throws UserNotLoggedInException {
            return "ersponse!";
    }

    @Override
    public String validateGooglePlusCallback(GooglePlusAuthenticatedUser googlePlusAuthenticatedUser) {
        HttpServletRequest request = ServletUtils.getRequest();
        HttpServletResponse response = ServletUtils.getResponse();

        // Ensure that this is no request forgery going on, and that the user
        // sending us this connect request is the user that was supposed to.
       /* if (!request.getParameter("state").equals(request.getSession().getAttribute("state"))) {
            response.setStatus(401);
            return GSON.toJson("Invalid state parameter.");
        }*/
        try {
            // Upgrade the authorization code into an access and refresh token.
            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JSON_FACTORY,
                            CLIENT_ID, CLIENT_SECRET, googlePlusAuthenticatedUser.getCode(), "postmessage").execute();
            // Create a credential representation of the token data.
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory(JSON_FACTORY)
                    .setTransport(TRANSPORT)
                    .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
                    .setFromTokenResponse(tokenResponse);

            // Check that the token is valid.
            Oauth2 oauth2 = new Oauth2.Builder(
                    TRANSPORT, JSON_FACTORY, credential).build();
            Tokeninfo tokenInfo = oauth2.tokeninfo()
                    .setAccessToken(credential.getAccessToken()).execute();
            // If there was an error in the token info, abort.
            if (tokenInfo.containsKey("error")) {
                response.setStatus(401);
                return GSON.toJson(tokenInfo.get("error").toString());
            }
            // Make sure the token we got is for the intended user.
            /*if (!tokenInfo.getUserId().equals(clientId)) {
                response.setStatus(401);
                return GSON.toJson("Token's user ID doesn't match given user ID.");
            }*/
            // Make sure the token we got is for our app.
            if (!tokenInfo.getIssuedTo().equals(CLIENT_ID)) {
                response.setStatus(401);
                return GSON.toJson("Token's client ID does not match app's.");
            }
            // Store the token in the session for later use.
            request.getSession().setAttribute("token", tokenResponse.toString());
            request.getSession().setAttribute(SecurityAspect.SESSION_USER_ATTR_NAME, googlePlusAuthenticatedUser);

            Plus service = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            // Get a list of people that this user has shared with this app.
            PeopleFeed people = service.people().list("me", "visible").execute();
            String str = GSON.toJson(people);
            return GSON.toJson("Successfully connected user.");
        } catch (TokenResponseException e) {
            response.setStatus(500);
            return GSON.toJson("Failed to upgrade the authorization code.");
        } catch (IOException e) {
            response.setStatus(500);
            return GSON.toJson("Failed to read token data from Google. " +
                    e.getMessage());
        }

    }

    @Override
    public void logout() {
        ServletUtils.getRequest().getSession().invalidate();
    }
}
