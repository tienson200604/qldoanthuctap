package com.web.config;


import com.cloudinary.Cloudinary;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@SpringBootApplication
public class CloudConfig {

    @Bean
    public Cloudinary cloudinaryConfigs() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "dpujyomap");
        config.put("api_key", "467573562232771");
        config.put("api_secret", "SQx-Ydfr7iRfOAbKXWyJuc2-dSs");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

}
