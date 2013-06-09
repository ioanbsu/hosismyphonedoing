package com.artigile.checkmyphone;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.artigile.checkmyphone.service.CommonUtilities;
import com.artigile.checkmyphone.service.DeviceRegistrationServiceImpl;
import com.artigile.checkmyphone.service.LocationService;
import com.artigile.checkmyphone.util.GCMRegistrar;
import com.artigile.howismyphonedoing.api.CommonConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Locale;

import static com.artigile.checkmyphone.service.CommonUtilities.*;

/**
 * User: ioanbsu
 * Date: 5/20/13
 * Time: 3:47 PM
 */
@Singleton
public class MainActivity extends RoboActivity implements
        TextToSpeech.OnInitListener {

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            mDisplay.append(newMessage + "\n");
        }
    };
    @InjectView(value = R.id.display)
    TextView mDisplay;
    AsyncTask<Void, Void, Void> mRegisterTask;
    @Inject
    private DeviceRegistrationServiceImpl deviceRegistrationService;
    @Inject
    private TextToSpeechService textToSpeechService;
    @Inject
    private LocationService locationService;
    @Inject
    private CommonUtilities commonUtilities;
    private Dialog errorDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNotNull(commonUtilities.getServerUrl(CommonConstants.SERVER_URL_PARAM_NAME), "SERVER_URL");
        checkNotNull(SENDER_ID, "SENDER_ID");
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        setContentView(R.layout.main);
        registerPhoneIfNecessary();
        textToSpeechService.setTts(new TextToSpeech(this, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
             * Typically, an application registers automatically, so options
             * below are disabled. Uncomment them if you want to manually
             * register or unregister the device (you will also need to
             * uncomment the equivalent options on options_menu.xml).
             */
            /*
            case R.id.options_register:
                GCMRegistrar.register(this, SENDER_ID);
                return true;
            case R.id.options_unregister:
                GCMRegistrar.unregister(this);
                return true;
             */
            /*case R.id.options_clear:
                mDisplay.setText(null);
                return true;
            case R.id.options_exit:
                finish();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void unregisterDevice(View v) {
        GCMRegistrar.unregister(this);
    }

    public void registerDevice(View v) {
        GCMRegistrar.register(this, SENDER_ID);
    }

    public void testButton(View v) {
        Context context = getApplicationContext();
        CharSequence text = "Button clicked";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        mDisplay.setText("Request for location update sent");
        locationService.getLocation(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mDisplay.setText(location.getLatitude() + " " + location.getLongitude() + " " + location.getAccuracy());
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        unregisterReceiver(mHandleMessageReceiver);
        GCMRegistrar.onDestroy(this);
        super.onDestroy();
    }

    private void checkNotNull(Object reference, String name) {
        if (reference == null) {
            throw new NullPointerException(getString(R.string.error_config, name));
        }
    }

    private void registerPhoneIfNecessary() {
        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
            // Automatically registers application on startup.
            GCMRegistrar.register(this, SENDER_ID);
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                mDisplay.append(getString(R.string.already_registered) + "\n");
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        deviceRegistrationService.register(context, regId);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeechService.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void checkGooglePlayServiceAvailability(int requestCode) {
        // Query for the status of Google Play services on the device
        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (statusCode == ConnectionResult.SUCCESS) {
//			init();
        } else {
            if (GooglePlayServicesUtil.isUserRecoverableError(statusCode)) {
                errorDialog = GooglePlayServicesUtil.getErrorDialog(statusCode, this, requestCode);
                errorDialog.show();
            } else {
                // Handle unrecoverable error
            }
        }
    }


}