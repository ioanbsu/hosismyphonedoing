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
