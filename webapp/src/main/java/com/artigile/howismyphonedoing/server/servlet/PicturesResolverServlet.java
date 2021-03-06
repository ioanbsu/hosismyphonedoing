package com.artigile.howismyphonedoing.server.servlet;

import com.artigile.howismyphonedoing.api.CompressorUtil;
import com.artigile.howismyphonedoing.server.entity.PicturesFromDevice;
import com.artigile.howismyphonedoing.server.entity.UserDevice;
import com.artigile.howismyphonedoing.server.service.PicturesService;
import com.artigile.howismyphonedoing.server.service.SecurityAspect;
import com.artigile.howismyphonedoing.server.service.UserService;
import com.artigile.howismyphonedoing.shared.WebAppAndClientConstants;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 7/16/13
 * Time: 5:31 PM
 */
@Service
public class PicturesResolverServlet extends AbstractServlet {

    public static final Logger logger = Logger.getLogger(PicturesResolverServlet.class.getName());
    private static final String JPEG_EXTENSION = "jpeg";
    @Autowired
    private PicturesService picturesService;
    @Autowired
    private UserService userService;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pictureUuid = request.getParameter(WebAppAndClientConstants.PICTURE_UUID);
        Boolean resizeToIcon = Boolean.valueOf(request.getParameter(WebAppAndClientConstants.PICTURE_IS_ICON));
        PicturesFromDevice picturesFromDevice = picturesService.getPicture(pictureUuid);
        String loggedInUserEmail = request.getSession().getAttribute(SecurityAspect.USER_IN_SESSION_EMAIL) + "";
        UserDevice userDevice = userService.findUserDeviceByUuid(picturesFromDevice.getDeviceUuid());
        if (!"ioanbsu1@gmail.com".equals(loggedInUserEmail) && !userDevice.getUserEmail().equals(loggedInUserEmail)) {
            logger.warning("The user with email " + loggedInUserEmail + " tried to get access to pictures of user with email:  " + userDevice.getUserEmail());
            response.getWriter().append("You are not authorized to view this picture. Please check that you're logged in to [How Is My Phone Doing?] Web App and you have permissions to view this picture.");
        } else {
            writeImageInResponse(picturesFromDevice.getPictureData(), response, resizeToIcon);
        }
        return null;
    }

    public void writeImageInResponse(byte[] byteData, HttpServletResponse resp, boolean resizeToIcon) throws IOException {
        resp.setContentType("image/" + JPEG_EXTENSION);
        OutputStream responseOS = resp.getOutputStream();
        byte[] uncompressedBytes = CompressorUtil.extractBytes(byteData);
        logger.info("Get picture response. Data compression: " + byteData.length + "->" + uncompressedBytes.length);
        if (resizeToIcon) {
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            Image oldImage = ImagesServiceFactory.makeImage(uncompressedBytes);
            Transform resize = ImagesServiceFactory.makeResize(40, 60);
            Image newImage = imagesService.applyTransform(resize, oldImage);
            uncompressedBytes = newImage.getImageData();
            logger.info("Picture icon requested, new picture size =  " + "->" + uncompressedBytes.length);
        }
        resp.setContentLength(uncompressedBytes.length);
        responseOS.write(uncompressedBytes);
    }
}
