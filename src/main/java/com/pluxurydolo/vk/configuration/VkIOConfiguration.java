package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkIOConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileUtils fileUtils() {
        return new FileUtils();
    }
}
