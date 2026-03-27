package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.properties.Delay;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(prefix = "vk", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(Delay.class)
@Import({
    VkClientConfiguration.class,
    VkImageUploadStepConfiguration.class,
    VkVideoUploadStepConfiguration.class
})
public class VkAutoConfiguration {
}
