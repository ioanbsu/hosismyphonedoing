package com.artigile.checkmyphone.service;

import android.content.Context;
import android.media.AudioManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 6/25/13
 * Time: 5:28 PM
 */
@Singleton
public class DeviceConfigurationService {

    @Inject
    private Context context;

    public void enableSilentMode(boolean silentModeEnabled) {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (silentModeEnabled) {
            audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    public void setAlarm() {
        /*AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, YourBroadcastReceiver.class);
        intent.setAction(define action here);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
        alarmManager.set(AlarmManager.RTC, time.getTimeInMillis(),pendingIntent );*/
    }
}
