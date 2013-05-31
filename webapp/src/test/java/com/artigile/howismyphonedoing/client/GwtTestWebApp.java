package com.artigile.howismyphonedoing.client;

import com.artigile.howismyphonedoing.client.rpc.GreetingRpcService;
import com.artigile.howismyphonedoing.client.rpc.GreetingRpcServiceAsync;
import com.artigile.howismyphonedoing.shared.FieldVerifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * GWT JUnit <b>integration</b> tests must extend GWTTestCase.
 * Using <code>"GwtTest*"</code> naming pattern exclude them from running with
 * surefire during the test phase.
 * 
 * If you run the tests using the Maven command line, you will have to 
 * navigate with your browser to a specific url given by Maven. 
 * See http://mojo.codehaus.org/gwt-maven-plugin/user-guide/testing.html 
 * for details.
 */
public class GwtTestWebApp extends GWTTestCase {

  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "com.artigile.howismyphonedoing.WebAppJUnit";
  }

  /**
   * Tests the FieldVerifier.
   */
  public void testFieldVerifier() {
    assertFalse(FieldVerifier.isValidName(null));
    assertFalse(FieldVerifier.isValidName(""));
    assertFalse(FieldVerifier.isValidName("a"));
    assertFalse(FieldVerifier.isValidName("ab"));
    assertFalse(FieldVerifier.isValidName("abc"));
    assertTrue(FieldVerifier.isValidName("abcd"));
  }

  /**
   * This test will send a request to the server using the sendSimpleTextMessage method in
   * GreetingRpcService and verify the response.
   */
  public void testGreetingService() {
    // Create the service that we will test.
      GreetingRpcServiceAsync greetingService = GWT.create(GreetingRpcService.class);
    ServiceDefTarget target = (ServiceDefTarget) greetingService;
    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "WebApp/greet");

    // Since RPC calls are asynchronous, we will need to wait for a response
    // after this test method returns. This line tells the test runner to wait
    // up to 10 seconds before timing out.
    delayTestFinish(10000);

    // Send a request to the server.
    greetingService.sendSimpleTextMessage("GWT User", new AsyncCallback<String>() {
        public void onFailure(Throwable caught) {
            // The request resulted in an unexpected error.
            fail("Request failure: " + caught.getMessage());
        }

        public void onSuccess(String result) {
            // Verify that the response is correct.
            assertTrue(result.startsWith("Hello, GWT User!"));

            // Now that we have received a response, we need to tell the test runner
            // that the test is complete. You must call finishTest() after an
            // asynchronous test finishes successfully, or the test will time out.
            finishTest();
        }
    });
  }


}