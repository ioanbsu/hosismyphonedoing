package com.artigile.howismyphonedoing.server.service;

import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.gcmserver.*;
import com.artigile.howismyphonedoing.server.service.cloudutil.KeysResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 5/24/13
 * Time: 9:22 AM
 */
@Service
public class MessagingServiceImpl implements MessagingService {
    private static final Executor threadPool = Executors.newFixedThreadPool(5);
    private static final int MULTICAST_SIZE = 1000;
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private Sender sender;
    @Autowired
    private KeysResolver apiKeyResolver;
    @Autowired
    private UserAndDeviceService userAndDeviceService;

    @PostConstruct
    private void init() {
        sender = new Sender(apiKeyResolver.getPhoneApiKey());
    }

    @Override
    public void sendMessage(Set<UserDevice> devices, Message message) throws IOException {
        String status;
        if (devices.isEmpty()) {
            status = "Message ignored as there is no device registered!";
        } else {
            // NOTE: check below is for demonstration purposes; a real application
            // could always send a multicast, even for just one recipient
            if (devices.size() == 1) {
                // send a single message using plain post
                String registrationId = devices.iterator().next().getRegisteredId();
                Result result = sender.send(message, registrationId, 5);
                status = "Sent message to one device: " + result;
            } else {
                // send a multicast message using JSON
                // must split in chunks of 1000 devices (GCM limit)
                int total = devices.size();
                List<String> partialDevices = new ArrayList<String>(total);
                int counter = 0;
                int tasks = 0;
                for (UserDevice device : devices) {
                    counter++;
                    partialDevices.add(device.getRegisteredId());
                    int partialSize = partialDevices.size();
                    if (partialSize == MULTICAST_SIZE || counter == total) {
                        asyncSend(partialDevices, message);
                        partialDevices.clear();
                        tasks++;
                    }
                }
                status = "Asynchronously sending " + tasks + " multicast messages to " +
                        total + " devices";
            }
        }

    }

    private void asyncSend(List<String> partialDevices, final Message message) {
        // make a copy
        final List<String> devices = new ArrayList<String>(partialDevices);
        threadPool.execute(new Runnable() {

            public void run() {
                MulticastResult multicastResult;
                try {
                    multicastResult = sender.send(message, devices, 5);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error posting messages", e);
                    return;
                }
                List<Result> results = multicastResult.getResults();
                // analyze the results
                for (int i = 0; i < devices.size(); i++) {
                    String regId = devices.get(i);
                    Result result = results.get(i);
                    String messageId = result.getMessageId();
                    if (messageId != null) {
                        logger.fine("Succesfully sent message to device: " + regId +
                                "; messageId = " + messageId);
                        String canonicalRegId = result.getCanonicalRegistrationId();
                        if (canonicalRegId != null) {
                            // same device has more than on registration id: update it
                            logger.info("canonicalRegId " + canonicalRegId);
                            userAndDeviceService.updateRegistration(regId, canonicalRegId);
                        }
                    } else {
                        String error = result.getErrorCodeName();
                        if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                            // application has been removed from device - unregister it
                            logger.info("Unregistered device: " + regId);
                            userAndDeviceService.unregister(regId);
                        } else {
                            logger.severe("Error sending message to " + regId + ": " + error);
                        }
                    }
                }
            }
        });
    }


}
