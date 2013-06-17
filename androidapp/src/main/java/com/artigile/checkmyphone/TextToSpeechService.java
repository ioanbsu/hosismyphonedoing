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
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Locale;

/**
 * User: ioanbsu
 * Date: 6/6/13
 * Time: 7:14 PM
 */
@Singleton
public class TextToSpeechService implements TextToSpeech.OnInitListener {
    private static final String TAG = "TTS";
    private static boolean messageSaid = false;
    @Inject
    private Context context;
    private TextToSpeech tts;
    private Locale locale;
    private String lastMessage;
    private UtteranceProgressListener utteranceProgressListener = getUtteranceProgressListener();

    public void say(final Locale locale, final String message) {
        messageSaid = false;
        if (Strings.isNullOrEmpty(message)) {
            return;
        }
        this.locale = locale;
        lastMessage = message;
        tts = new TextToSpeech(context, this);
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
