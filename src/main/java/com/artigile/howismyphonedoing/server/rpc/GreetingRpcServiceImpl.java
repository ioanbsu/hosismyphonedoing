package com.artigile.howismyphonedoing.server.rpc;

import com.artigile.howismyphonedoing.client.rpc.GreetingRpcService;
import com.artigile.howismyphonedoing.server.entity.TestEntity;
import com.artigile.howismyphonedoing.server.service.PersistenceFactory;
import com.artigile.howismyphonedoing.shared.FieldVerifier;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.PersistenceManager;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
@Service
public class GreetingRpcServiceImpl implements GreetingRpcService {

    @Autowired
    private PersistenceFactory persistenceFactory;

    public String greetServer(String input) throws IllegalArgumentException {
        PersistenceManager persistenceManager = persistenceFactory.get().getPersistenceManager();
        TestEntity testEntity = new TestEntity();
        testEntity.setId(23L);
        testEntity.setValue("adsfsdfg");
        persistenceManager.makePersistent(testEntity);
        persistenceManager.close();

        persistenceManager = persistenceFactory.get().getPersistenceManager();
        TestEntity fromDb = (TestEntity) persistenceManager.getObjectById(23);
        System.out.println(fromDb.getValue());
        // Verify that the input is valid.
        if (!FieldVerifier.isValidName(input)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long");
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
