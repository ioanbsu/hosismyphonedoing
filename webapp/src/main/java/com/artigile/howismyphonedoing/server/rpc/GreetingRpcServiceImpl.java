package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.api.CommonContants;
import com.artigile.howismyphonedoing.api.EventType;
import com.artigile.howismyphonedoing.client.rpc.GreetingRpcService;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.gcmserver.Message;
import com.artigile.howismyphonedoing.server.service.MessagingService;
import com.artigile.howismyphonedoing.server.dao.UserAndDeviceDao;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Service
public class GreetingRpcServiceImpl extends AbstractRpcService implements GreetingRpcService {

    @Autowired
    private UserAndDeviceDao userAndDeviceDao;
    @Autowired
    private MessagingService messagingService;

    public String sendSimpleTextMessage(String input) throws IllegalArgumentException {
   /*     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query.Filter filter=new Query.FilterPredicate("firstName", Query.FilterOperator.EQUAL, "Antonio");
        Query q=new Query("Test").setFilter(filter);
        List<Entity> entityList=datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());

        Entity testEntity = new Entity("Test", 33);
        testEntity.setProperty("firstName", "Antonio");
        testEntity.setProperty("lastName", "Salieri");

        Key key = testEntity.getPhoneApiKey();
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
        Set<UserDevice> userDevice = userAndDeviceDao.getDevices(getUserEmailFromSession());
        if (userDevice.isEmpty()) {
            return "message was not sent";
        }
        Message message = new Message.Builder().addData("mydata", input).build();
        try {
            messagingService.sendMessage(userDevice, message);
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

    @Override
    public String getPhoneInfo() {
        Message message = new Message.Builder().addData(CommonContants.MESSAGE_EVENT_TYPE, EventType.PHONE_INFO.toString()).build();

        Set<UserDevice> userDevice = userAndDeviceDao.getDevices(getUserEmailFromSession());
        for (UserDevice device : userDevice) {
            try {
                messagingService.sendMessage(userDevice, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "message sent";
        }
        return "message sent";
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
