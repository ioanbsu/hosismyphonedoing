package com.artigile.howismyphonedoing.server.servlet;

import com.artigile.howismyphonedoing.api.CompressorUtil;
import com.artigile.howismyphonedoing.server.entity.PicturesFromDevice;
import com.artigile.howismyphonedoing.server.service.PicturesService;
import com.artigile.howismyphonedoing.shared.WebAppAndClientConstants;
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

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pictureUuid = request.getParameter(WebAppAndClientConstants.PICTURE_UUID);
        PicturesFromDevice picturesFromDevice = picturesService.getPicture(pictureUuid);
        writeImageInResponse(picturesFromDevice.getPictureData(), response);
        return null;
    }

    public void writeImageInResponse(byte[] byteData, HttpServletResponse resp) throws IOException {
        resp.setContentType("image/" + JPEG_EXTENSION);
        OutputStream responseOS = resp.getOutputStream();
        byte[] uncompressedBytes = CompressorUtil.extractBytes(byteData);
        logger.info("Get picture response. Data compression: " + byteData.length + "->" + uncompressedBytes.length);
        resp.setContentLength(uncompressedBytes.length);
        responseOS.write(uncompressedBytes);
    }
}
