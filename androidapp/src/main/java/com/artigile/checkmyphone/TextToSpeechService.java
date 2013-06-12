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

import android.speech.tts.TextToSpeech;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * User: ioanbsu
 * Date: 6/6/13
 * Time: 7:14 PM
 */
@Singleton
public class TextToSpeechService {

    private TextToSpeech tts;

    public void setTts(TextToSpeech tts) {
        this.tts = tts;
    }

    public void talk(String messageStr) {
        tts.speak(messageStr, TextToSpeech.QUEUE_ADD, null);
    }

    public int setLanguage(Locale locale) {
        return tts.setLanguage(locale);
    }
}
