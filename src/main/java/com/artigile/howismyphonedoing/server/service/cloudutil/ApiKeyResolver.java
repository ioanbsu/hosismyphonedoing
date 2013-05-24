package com.artigile.howismyphonedoing.server.service.cloudutil;

import com.google.appengine.repackaged.com.google.common.io.Files;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

/**
 * User: ioanbsu
 * Date: 5/23/13
 * Time: 6:38 PM
 */
@Service
public class ApiKeyResolver {

    private static final String PATH = "/api.key";
    protected final Logger logger = Logger.getLogger(getClass().getName());
    public String key;

    @PostConstruct
    public void init() throws IOException {
        try {
             key = Files.toString(new DefaultResourceLoader().getResource(PATH).getFile(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException("Could not read file " + PATH, e);
        }
    }

    public String getKey() {
        return key;
    }
}
