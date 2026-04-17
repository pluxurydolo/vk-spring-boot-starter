package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.properties.Delay;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(Delay.class)
@Import({
    VkClientConfiguration.class,
    VkImageUploadStepConfiguration.class,
    VkVideoUploadStepConfiguration.class,
    VkUtilConfiguration.class
})
public class VkAutoConfiguration {
}
