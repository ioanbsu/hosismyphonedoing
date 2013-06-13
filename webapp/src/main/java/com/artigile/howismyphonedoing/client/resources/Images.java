/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.howismyphonedoing.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * User: ioanbsu
 * Date: 6/12/13
 * Time: 9:27 AM
 */
public interface Images extends ClientBundle {

    @Source("com/artigile/howismyphonedoing/public/images/loadingIcon.gif")
    ImageResource loadingIcon();

    @Source("com/artigile/howismyphonedoing/public/images/smallLoading.gif")
    ImageResource loadingSmallIcon();

    @Source("com/artigile/howismyphonedoing/public/images/checkOk.png")
    ImageResource checkOk();

    @Source("com/artigile/howismyphonedoing/public/images/exclamation.png")
    ImageResource exclamation();

    @Source("com/artigile/howismyphonedoing/public/images/information.png")
    ImageResource infomation();

    @Source("com/artigile/howismyphonedoing/public/images/questionIcon.png")
    ImageResource question();

    @Source("com/artigile/howismyphonedoing/public/images/refresh.png")
    ImageResource refresh();


}
