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

import com.google.android.gms.location.LocationListener;
import com.google.inject.ImplementedBy;

/**
 * @author IoaN, 6/6/13 10:20 PM
 */
@ImplementedBy(LocationServiceImpl.class)
public interface LocationService {
    void getLocation(LocationListener locationListener);
}
