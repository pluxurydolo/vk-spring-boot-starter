package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.util.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkUtilConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileUtils fileUtils() {
        return new FileUtils();
    }
}
