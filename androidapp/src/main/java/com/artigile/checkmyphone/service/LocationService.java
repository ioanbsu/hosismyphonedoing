package com.artigile.checkmyphone.service;

import com.google.android.gms.location.LocationListener;
import com.google.inject.ImplementedBy;

/**
 * @author IoaN, 6/6/13 10:20 PM
 */
@ImplementedBy(LocationServiceImpl.class)
public interface LocationService {
    void getLocation(LocationListener locationListener);
}
