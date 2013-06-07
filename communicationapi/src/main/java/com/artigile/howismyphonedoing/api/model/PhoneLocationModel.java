package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * @author IoaN, 6/7/13 7:19 AM
 */
public class PhoneLocationModel implements Serializable {
    private String provider;
    private long time = 0;
    private long elapsedRealtimeNanos = 0;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private boolean hasAltitude = false;
    private double altitude = 0.0f;
    private boolean hasSpeed = false;
    private float speed = 0.0f;
    private boolean hasBearing = false;
    private float bearing = 0.0f;
    private boolean hasAccuracy = false;
    private float accuracy = 0.0f;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getElapsedRealtimeNanos() {
        return elapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(long elapsedRealtimeNanos) {
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isHasAltitude() {
        return hasAltitude;
    }

    public void setHasAltitude(boolean hasAltitude) {
        this.hasAltitude = hasAltitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public boolean isHasSpeed() {
        return hasSpeed;
    }

    public void setHasSpeed(boolean hasSpeed) {
        this.hasSpeed = hasSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isHasBearing() {
        return hasBearing;
    }

    public void setHasBearing(boolean hasBearing) {
        this.hasBearing = hasBearing;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public boolean isHasAccuracy() {
        return hasAccuracy;
    }

    public void setHasAccuracy(boolean hasAccuracy) {
        this.hasAccuracy = hasAccuracy;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
