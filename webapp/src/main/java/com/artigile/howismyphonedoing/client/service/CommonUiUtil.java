package com.artigile.howismyphonedoing.client.service;

import com.artigile.howismyphonedoing.shared.RpcConstants;
import com.artigile.howismyphonedoing.shared.WebAppAndClientConstants;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import org.springframework.stereotype.Service;

/**
 * Date: 7/31/13
 * Time: 10:18 PM
 *
 * @author ioanbsu
 */

@Service
public class CommonUiUtil {

    public String getPictureUrl(String pictureId, Boolean isIcon) {
        return new UrlBuilder()
                .setHost(Window.Location.getHost())
                .setPath(RpcConstants.SERVLET_MAIN_ENTRANCE + WebAppAndClientConstants.PICTURE_URL_PATH)
                .setParameter(WebAppAndClientConstants.PICTURE_UUID, pictureId)
                .setParameter(WebAppAndClientConstants.PICTURE_IS_ICON, isIcon + "")
                .buildString();
    }
}
