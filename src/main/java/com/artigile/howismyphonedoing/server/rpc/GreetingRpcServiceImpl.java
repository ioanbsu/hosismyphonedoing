package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.client.rpc.GreetingRpcService;
import com.artigile.howismyphonedoing.server.gcmserver.Message;
import com.artigile.howismyphonedoing.server.service.MessagingService;
import com.artigile.howismyphonedoing.server.service.cloudutil.PhoneDatastore;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Service
public class GreetingRpcServiceImpl implements GreetingRpcService {

    @Autowired
    private MessagingService messagingService;

    public String greetServer(String input) throws IllegalArgumentException {
   /*     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filter=new Query.FilterPredicate("firstName", Query.FilterOperator.EQUAL, "Antonio");
        Query q=new Query("Test").setFilter(filter);
        List<Entity> entityList=datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        Entity testEntity = new Entity("Test", 33);
        testEntity.setProperty("firstName", "Antonio");
        testEntity.setProperty("lastName", "Salieri");

        Key key = testEntity.getKey();
        Date hireDate = new Date();
        testEntity.setProperty("hireDate", hireDate);
        datastore.beginTransaction();
        datastore.put(testEntity);
        datastore.getCurrentTransaction().commit();

        try {
            Entity fromDb = datastore.get(key);
            System.out.println(fromDb);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        // Verify that the input is valid.
        if (!FieldVerifier.isValidName(input)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long");
        }

*/

        Message message = new Message.Builder().addData("mydata", input).build();
        try {
            messagingService.sendMessage(PhoneDatastore.getDevices(), message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String serverInfo = ServletUtils.getRequest().getSession().getServletContext().getServerInfo();
        String userAgent = "not implemented yet.";//getThreadLocalRequest().getHeader("User-Agent");

        // Escape data from the client to avoid cross-site script vulnerabilities.
        input = escapeHtml(input);
        userAgent = escapeHtml(userAgent);

        return "Hello, " + input + "!<br><br>I am running " + serverInfo
                + ".<br><br>It looks like you are using:<br>" + userAgent;
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     *
     * @param html the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
                ">", "&gt;");
    }
}
