package com.vehicle.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {



    public WebConfig() {
        System.out.println("WebConfig Loaded Successfully");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPath = System.getProperty("user.dir") + "/uploads/";
        Path path = Paths.get(uploadPath);
        String absolutePath = path.toUri().toString();

        System.out.println("Mapping Upload Folder To: " + absolutePath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(absolutePath);
    }
}
