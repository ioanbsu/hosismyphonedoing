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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import com.artigile.checkmyphone.service.*;
import com.artigile.checkmyphone.service.admin.DeviceAdminReceiverImpl;
import com.artigile.checkmyphone.util.GCMRegistrar;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.artigile.checkmyphone.service.ActivityAndBroadcastUtils.*;

/**
 * User: ioanbsu
 * Date: 5/20/13
 * Time: 3:47 PM
 */
@Singleton
public class MainActivity extends RoboActivity {
    private static final int ENABLE_ADMIN_SUPPORT_INTENT_CODE = 2348756;
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getExtras().getString(MESSAGE);
            int messageType = intent.getExtras().getInt(MESSAGE_TYPE);
            if (messageType == LOG_MESSAGE_TYPE) {
                mDisplay.append(message + "\n");
            } else if (messageType == REGISTRATION_STATUS_MESSAGE_TYPE) {
                displayRegisteredMessages(message);
            } else if (messageType == SHOW_LOGS_STATE_UPDATED) {
                checkLogsShouldBeDisplayed();
            }
        }
    };
    @InjectView(R.id.display)
    TextView mDisplay;
    @InjectView(R.id.deviceRegisteredLabel)
    TextView deviceRegisteredLabel;
    @InjectView(R.id.deviceNotRegisteredLabel)
    TextView deviceNotRegisteredLabel;
    @InjectView(R.id.infoLabel)
    TextView infoLabel;
    @InjectView(R.id.cleanLogsButton)
    Button cleanLogsButton;
    @InjectView(R.id.logsScrollView)
    ScrollView logsScrollView;
    @InjectView(R.id.enableAntiTheftButton)
    Button enableAntiTheftButton;
    AsyncTask<Void, Void, Void> mRegisterTask;
    @Inject
    private DeviceRegistrationServiceImpl deviceRegistrationService;
    @Inject
    private TextToSpeechService textToSpeechService;
    @Inject
    private LocationService locationService;
    @Inject
    private ActivityAndBroadcastUtils commonUtilities;
    @Inject
    private DeviceDetailsReader deviceDetailsReader;
    @Inject
    private SharedPreferences prefs;
    @Inject
    private AntiTheftService antiTheftService;
    @Inject
    private CameraService cameraService;
    private Dialog errorDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNotNull(commonUtilities.getServerUrl(), "SERVER_URL");
        checkNotNull(SENDER_ID, "SENDER_ID");
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        setContentView(R.layout.main);
        registerPhoneIfNecessary();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkIfUserHasGoogleAccount();
        checkLogsShouldBeDisplayed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAntiTheft();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ENABLE_ADMIN_SUPPORT_INTENT_CODE) {
            checkAntiTheft();
        }
    }

    public void unregisterDevice(View v) {
        GCMRegistrar.unregister(this);
    }

    public void registerDevice(View v) {
        GCMRegistrar.register(this, SENDER_ID);
    }

    public void cleanLogsButton(View v) {
        mDisplay.setText("");
    }

    public void enableAntiTheftButton(View v) {
        ComponentName mDeviceAdminSample = new ComponentName(this, DeviceAdminReceiverImpl.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.device_admin_explanation));
        startActivityForResult(intent, ENABLE_ADMIN_SUPPORT_INTENT_CODE);
    }

    public void testButton(View v) {
        try {
            antiTheftService.takePicture(null);
        } catch (DeviceHasNoCameraException e) {
            e.printStackTrace();
        }
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
                commonUtilities.displayMessage(this, getString(R.string.already_registered), ActivityAndBroadcastUtils.LOG_MESSAGE_TYPE);
                displayRegisteredMessages(getString(R.string.device_registered));
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

    private void displayRegisteredMessages(String message) {
        if (getString(R.string.device_registered).equals(message)) {
            deviceNotRegisteredLabel.setVisibility(View.GONE);
            deviceRegisteredLabel.setVisibility(View.VISIBLE);
            infoLabel.setVisibility(View.VISIBLE);
        } else if (getString(R.string.device_not_registered).equals(message)) {
            deviceNotRegisteredLabel.setVisibility(View.VISIBLE);
            deviceRegisteredLabel.setVisibility(View.GONE);
            infoLabel.setVisibility(View.GONE);
        }
    }

    private void checkIfUserHasGoogleAccount() {
        Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
        if (accounts.length == 0) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.your_device_has_no_linked_google_account_title);
            alertDialogBuilder.setMessage(R.string.your_device_has_no_linked_google_account_message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            MainActivity.this.finish();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    private void checkLogsShouldBeDisplayed() {
        boolean displayLogs = prefs.getBoolean(Constants.DISPLAY_LOGS_FLAG, false);
        if (displayLogs) {
            logsScrollView.setVisibility(View.VISIBLE);
            cleanLogsButton.setVisibility(View.VISIBLE);
        } else {
            logsScrollView.setVisibility(View.GONE);
            cleanLogsButton.setVisibility(View.GONE);
        }
    }


    private void checkAntiTheft() {
        if (antiTheftService.isAntiTheftEnabled()) {
            enableAntiTheftButton.setVisibility(View.GONE);
        } else {
            enableAntiTheftButton.setVisibility(View.VISIBLE);
        }
    }
}