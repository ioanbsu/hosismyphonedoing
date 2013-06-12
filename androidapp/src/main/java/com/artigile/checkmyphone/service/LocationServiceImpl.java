/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

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
    public static final int MAX_RECONNECT_ATTEMPS = 5;
    private static final int THREE_MINUTES = 1000 * 60 * 3;
    private static final int TWENTY_SECONDS_INTERVAL = 1000 * 10;
    private static final int FIVE_SECONDS = 1000 * 5;
    @Inject
    private Context context;
    private LocationClientCallbacksListener locationClientCallbacksListener = new LocationClientCallbacksListener();
    private LocationClient locationClient;
    private LocationListener locationListener;
    private LocationListener internalLocationListener;
    private boolean locationRequestsAreInProgress = false;
    private int reconnectAttempt = 0;
    private Location currentBestLocation;

    @Override
    public void getLocation(LocationListener locationListener) {
        this.locationListener = locationListener;
        if (!locationRequestsAreInProgress) {
            reconnectAttempt = 0;
            locationRequestsAreInProgress = true;
            init();
            connectToLocationClient();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    stopRequestingLocationUpdates();
                }
            }, THREE_MINUTES);
        }
    }

    private void init() {
        if (internalLocationListener == null) {
            internalLocationListener = getNewInternalLocationListener();
        }
        if (locationClient == null) {
            locationClient = new LocationClient(context, locationClientCallbacksListener, locationClientCallbacksListener);
        }
    }

    private void connectToLocationClient() {
        Log.v(TAG, "Location Client connect");
        if (!(locationClient.isConnected() || locationClient.isConnecting())) {
            locationClient.connect();
        } else {
            try {
                requestLocationUpdates();
            } catch (IllegalStateException e) {
                Log.v(TAG, "Failed to get location");
            }
        }
    }

    private void requestLocationUpdates() {
        // Request for location updates
        LocationRequest request = LocationRequest.create()
                .setInterval(TWENTY_SECONDS_INTERVAL)
                .setExpirationDuration(THREE_MINUTES)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationClient.requestLocationUpdates(request, internalLocationListener);
    }

    private LocationListener getNewInternalLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (currentBestLocation == null) {
                    currentBestLocation = location;
                }
                if (isBetterLocation(location, currentBestLocation)) {
                    currentBestLocation = location;
                    locationListener.onLocationChanged(location);
                }
                if (currentBestLocation.getAccuracy() < 30) {
                    stopRequestingLocationUpdates();
                }
            }
        };
    }

    private void stopRequestingLocationUpdates() {
        Log.v(TAG, "Accuracy is very precise");
        locationClient.disconnect();
        locationRequestsAreInProgress = false;
    }

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > FIVE_SECONDS;
        boolean isSignificantlyOlder = timeDelta < -FIVE_SECONDS;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
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
                Log.e(TAG, "Location can not be achieved. trying to reconnect");
                if (reconnectAttempt < MAX_RECONNECT_ATTEMPS) {
                    connectToLocationClient();
                }
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
