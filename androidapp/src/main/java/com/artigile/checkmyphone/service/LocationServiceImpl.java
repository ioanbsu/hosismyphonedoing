package com.artigile.checkmyphone.service;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author IoaN, 6/6/13 10:22 PM
 */
@Singleton
public class LocationServiceImpl implements LocationService {

    public static final String TAG = "LocationServiceImpl";
    private static final int THREE_MINUTES = 1000 * 60 * 3;
    private static final int TWENTY_SECONDS_INTERVAL = 1000 * 10;
    @Inject
    private Context context;
    private LocationClientCallbacksListener locationClientCallbacksListener = new LocationClientCallbacksListener();
    private LocationClient locationClient;
    private LocationListener locationListener;
    private LocationListener internalLocationListener;
    private boolean doneRequestingBestAvailableLocation = true;

    @Override
    public void getLocation(LocationListener locationListener) {
        this.locationListener = locationListener;
        internalLocationListener = getNewInternalLocationListener();
        if (doneRequestingBestAvailableLocation) {
            if (locationClient == null) {
                init();
            } else {
                locationClient.disconnect();
            }
            requestLocationUpdates();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    stopRequestingLocationUpdates();
                }
            }, THREE_MINUTES);
        }

    }

    private void init() {
        if (locationClient == null) {
            locationClient = new LocationClient(context, locationClientCallbacksListener, locationClientCallbacksListener);
            connectToLocationClient();
        }
    }

    private void connectToLocationClient() {
        Log.v(TAG, "Location Client connect");
        if (!(locationClient.isConnected() || locationClient.isConnecting())) {
            locationClient.connect();
        }
    }

    private void requestLocationUpdates() {
        doneRequestingBestAvailableLocation = false;
        if (locationClient.isConnected() && locationClient.isConnectionCallbacksRegistered(locationClientCallbacksListener)) {
            // Request for location updates
            LocationRequest request = LocationRequest.create()
                    .setInterval(TWENTY_SECONDS_INTERVAL)
                    .setExpirationDuration(THREE_MINUTES)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationClient.requestLocationUpdates(request, internalLocationListener);
        } else {
            connectToLocationClient();
        }
    }

    private LocationListener getNewInternalLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < 30) {
                    stopRequestingLocationUpdates();
                }
                locationListener.onLocationChanged(location);
            }
        };
    }

    private void stopRequestingLocationUpdates() {
        Log.v(TAG, "Accuracy is very precise ");

        locationClient.disconnect();
        doneRequestingBestAvailableLocation = true;
    }

    private class LocationClientCallbacksListener implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

        @Override
        public void onConnected(Bundle connectionHint) {
            try {
                Log.v(TAG, "Location Client connected");
                // Display last location
                Location location = locationClient.getLastLocation();
                if (location != null) {
                    internalLocationListener.onLocationChanged(location);
                }
                requestLocationUpdates();
            } catch (IllegalStateException e) {
                Log.e(TAG, "Location can not be achieved.");
            }
        }

        @Override
        public void onDisconnected() {
            Log.v(TAG, "Disconnected");
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.v(TAG, "Connection Failed");
        }
    }


}
