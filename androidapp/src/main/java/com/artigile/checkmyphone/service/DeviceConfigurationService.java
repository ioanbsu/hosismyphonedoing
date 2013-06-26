package com.artigile.checkmyphone.service;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import com.artigile.howismyphonedoing.api.model.RingerMode;

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

    public void updateRingerMode(RingerMode ringerMode) {
        if (ringerMode == null) {
            return;
        }
        try {
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (ringerMode == RingerMode.RINGER_MODE_SILENT) {
                audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if (ringerMode == RingerMode.RINGER_MODE_NORMAL) {
                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else if (ringerMode == RingerMode.RINGER_MODE_VIBRATE) {
                audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
        } catch (Exception e) {
            Log.w("DeviceConfigurationService","Failed to set ringer mode:" +ringerMode);
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
