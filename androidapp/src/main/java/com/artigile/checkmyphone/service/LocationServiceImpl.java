package com.artigile.checkmyphone.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author IoaN, 6/6/13 10:22 PM
 */
@Singleton
public class LocationServiceImpl implements LocationService {
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    @Inject
    private Context context;
    private Location bestLocation;
    private Timer locationDetectTimeoutTimer = new Timer();
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void getLocation(final LocationReadyListener locationReadyListener) {
        // Acquire a reference to the system Location Manager
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        } else {
            //stopping location timers and manager before starting new location improvements.
            stopImprovingLocation();
        }
        // Define a listener that responds to location updates
        if (locationListener == null) {
            locationListener = getLocationListener(locationReadyListener);
        }
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationDetectTimeoutTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                locationManager.removeUpdates(locationListener);
                locationDetectTimeoutTimer.cancel();
            }

        }, TWO_MINUTES);
    }

    private LocationListener getLocationListener(final LocationReadyListener locationReadyListener) {
        return new LocationListener() {
            public void onLocationChanged(Location newLocation) {
                // Called when a new location is found by the network location provider.
                if (isBetterLocation(newLocation, bestLocation)) {
                    bestLocation = newLocation;
                }
                locationReadyListener.onLocationReady(bestLocation);
                locationManager.removeUpdates(this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                if (bestLocation.getAccuracy() < 20) {
                    stopImprovingLocation();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    private void stopImprovingLocation() {
        locationManager.removeUpdates(locationListener);
        locationDetectTimeoutTimer.cancel();
        locationDetectTimeoutTimer = new Timer();
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
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
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

    public static interface LocationReadyListener {
        void onLocationReady(Location location);
    }
}
