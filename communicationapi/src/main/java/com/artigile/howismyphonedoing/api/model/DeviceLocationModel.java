package com.artigile.howismyphonedoing.api.model;

/**
 * @author IoaN, 6/7/13 7:19 AM
 */
public class DeviceLocationModel implements IDeviceLocationModel {
    private String deviceId;
    private String deviceName;
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

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public long getElapsedRealtimeNanos() {
        return elapsedRealtimeNanos;
    }

    @Override
    public void setElapsedRealtimeNanos(long elapsedRealtimeNanos) {
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean isHasAltitude() {
        return hasAltitude;
    }

    @Override
    public void setHasAltitude(boolean hasAltitude) {
        this.hasAltitude = hasAltitude;
    }

    @Override
    public double getAltitude() {
        return altitude;
    }

    @Override
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public boolean isHasSpeed() {
        return hasSpeed;
    }

    @Override
    public void setHasSpeed(boolean hasSpeed) {
        this.hasSpeed = hasSpeed;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public boolean isHasBearing() {
        return hasBearing;
    }

    @Override
    public void setHasBearing(boolean hasBearing) {
        this.hasBearing = hasBearing;
    }

    @Override
    public float getBearing() {
        return bearing;
    }

    @Override
    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    @Override
    public boolean isHasAccuracy() {
        return hasAccuracy;
    }

    @Override
    public void setHasAccuracy(boolean hasAccuracy) {
        this.hasAccuracy = hasAccuracy;
    }

    @Override
    public float getAccuracy() {
        return accuracy;
    }

    @Override
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
