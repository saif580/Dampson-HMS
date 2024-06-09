package com.hms.medicalrecordmicroservice.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "db3kjpvkf",
                "api_key", "329387723369223",
                "api_secret", "uN1XkV58fQjiyervyi8g9uVcaTY"
        ));
    }
}
