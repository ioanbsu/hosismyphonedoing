/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.checkmyphone;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import com.artigile.checkmyphone.service.ActivityAndBroadcastUtils;
import com.artigile.howismyphonedoing.api.model.MessageToDeviceModel;
import com.artigile.howismyphonedoing.api.model.MessageToDeviceType;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * User: ioanbsu
 * Date: 6/6/13
 * Time: 7:14 PM
 */
@Singleton
public class MessageToDeviceService implements TextToSpeech.OnInitListener {
    private static final String TAG = "TTS";
    private static boolean messageSaid = false;
    @Inject
    private Context context;
    private static final List<String> messageList = new ArrayList<>();

    private TextToSpeech tts;
    private Locale locale;
    private String lastMessage;
    private UtteranceProgressListener utteranceProgressListener = getUtteranceProgressListener();
    private int MAX_WAIT_ATTEMPTS = 15;

    public void onMessageToDeviceReceived(Locale locale, MessageToDeviceModel messageToDeviceModel) {
        if (messageToDeviceModel.getMessageToDeviceType() == MessageToDeviceType.SAY_IT_OUT_LOUD) {
            say(locale, messageToDeviceModel.getMessage());
        } else if (messageToDeviceModel.getMessageToDeviceType() == MessageToDeviceType.DISPLAY_ALERT) {
            messageList.add(messageToDeviceModel.getMessage());
            ActivityAndBroadcastUtils.startDialogActivity(context);
        } else { // by default saying the message out loud
            say(locale, messageToDeviceModel.getMessage());
        }
    }

    private String say(final Locale locale, final String message) {
        messageSaid = false;
        if (Strings.isNullOrEmpty(message)) {
            return "nothing to say - message empty";
        }
        this.locale = locale;
        lastMessage = message;
        tts = new TextToSpeech(context, this);


        int waitAttempt = 0;
        try {
            while (!isMessageSaid()) {
                Thread.sleep(500);
                waitAttempt++;
                if (waitAttempt > MAX_WAIT_ATTEMPTS) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "failed to send a message";
        }
        return "success";
    }

    public boolean isMessageSaid() {
        return messageSaid;
    }

    private void doSay(Locale locale, String message) {
        tts.setLanguage(locale);
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, message);
        tts.speak(message, TextToSpeech.QUEUE_ADD, map);
    }

    @Override
    public void onInit(int status) {
        tts.setOnUtteranceProgressListener(utteranceProgressListener);
        if (status == TextToSpeech.SUCCESS) {
            doSay(locale, lastMessage);
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public static List<String> getMessageList() {
        return messageList;
    }

    private UtteranceProgressListener getUtteranceProgressListener() {
        return new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                messageSaid = true;
            }

            @Override
            public void onDone(String utteranceId) {
                messageSaid = true;
            }

            @Override
            public void onError(String utteranceId) {
                messageSaid = true;
            }
        };
    }
}
