package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/Teachers/**")
                .addResourceLocations("classpath:/static/Teachers/");

        registry.addResourceHandler("/Employees/**")
                .addResourceLocations("classpath:/static/Employees/");

        registry.addResourceHandler("/TrangChu/**")
                .addResourceLocations("classpath:/static/TrangChu/");

        registry.addResourceHandler("/Students/**")
                .addResourceLocations("classpath:/static/Students/");

        registry.addResourceHandler("/Admin/**")
                .addResourceLocations("classpath:/static/Admin/");
    }
}
