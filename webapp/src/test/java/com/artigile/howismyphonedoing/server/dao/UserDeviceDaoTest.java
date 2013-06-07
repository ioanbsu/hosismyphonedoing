package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.UnitTestsConfiguration;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.google.appengine.api.datastore.*;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Iterator;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author IoaN, 6/2/13 7:38 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestsConfiguration.class}, loader = AnnotationConfigWebContextLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
@WebAppConfiguration
public class UserDeviceDaoTest extends AbstractJUnit4SpringContextTests {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
    @Autowired
    private UserAndDeviceDao userAndDeviceDao;

    @Before
    public void setUp() {
        helper.setUp();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Query mydeleteq = new Query();
        PreparedQuery pq = datastore.prepare(mydeleteq);
        for (Entity result : pq.asIterable()) {
            try {
                datastore.delete(result.getKey());
            } catch (IllegalArgumentException e) {
                //entity was not deleted
            }
        }
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void test() {
        UserDevice userDevice1 = new UserDevice();
        userDevice1.setUuid("asdasdas1");
        userDevice1.setUserEmail("ioan@gmail.com");

        UserDevice userDevice2 = new UserDevice();
        userDevice2.setUuid("asdasdas2");
        userDevice2.setUserEmail("ioan@gmail.com");

        UserDevice userDevice3 = new UserDevice();
        userDevice3.setUuid("asdasdas3");
        userDevice3.setUserEmail("ioan1@gmail.com");
        userAndDeviceDao.register(userDevice1);
        userAndDeviceDao.register(userDevice2);
        userAndDeviceDao.register(userDevice3);

        Set<UserDevice> userDevicesList = userAndDeviceDao.getDevices("ioan@gmail.com");
        assertNotNull(userDevicesList);
        assertEquals(2, userDevicesList.size());
        Iterator<UserDevice> usersDevicesIterator = userDevicesList.iterator();

        UserDevice foundUser2 = usersDevicesIterator.next();
        UserDevice foundUser1 = usersDevicesIterator.next();
        assertEquals("ioan@gmail.com", foundUser1.getUserEmail());
        assertEquals("asdasdas1", foundUser1.getUuid());
        assertEquals("ioan@gmail.com", foundUser1.getUserEmail());
        assertEquals("asdasdas2", foundUser2.getUuid());

    }
}
