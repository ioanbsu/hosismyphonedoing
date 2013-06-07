package com.artigile.howismyphonedoing.api.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 6/7/13
 * Time: 2:51 PM
 */
public interface IDeviceLocationModel extends Serializable {
    String getProvider();

    void setProvider(String provider);

    long getTime();

    void setTime(long time);

    long getElapsedRealtimeNanos();

    void setElapsedRealtimeNanos(long elapsedRealtimeNanos);

    double getLatitude();

    void setLatitude(double latitude);

    double getLongitude();

    void setLongitude(double longitude);

    boolean isHasAltitude();

    void setHasAltitude(boolean hasAltitude);

    double getAltitude();

    void setAltitude(double altitude);

    boolean isHasSpeed();

    void setHasSpeed(boolean hasSpeed);

    float getSpeed();

    void setSpeed(float speed);

    boolean isHasBearing();

    void setHasBearing(boolean hasBearing);

    float getBearing();

    void setBearing(float bearing);

    boolean isHasAccuracy();

    void setHasAccuracy(boolean hasAccuracy);

    float getAccuracy();

    void setAccuracy(float accuracy);
}
