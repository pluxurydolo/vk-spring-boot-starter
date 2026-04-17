package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.properties.Delay;
import com.pluxurydolo.vk.util.FileUtils;
import com.pluxurydolo.vk.util.VkDelay;
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

    @Bean
    @ConditionalOnMissingBean
    public VkDelay vkDelay(Delay delay) {
        return new VkDelay(delay);
    }
}
