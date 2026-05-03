package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.properties.VkApiProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(VkApiProperties.class)
@Import({
    VkClientConfiguration.class,
    VkImageUploadStepConfiguration.class,
    VkVideoUploadStepConfiguration.class,
    VkIOConfiguration.class
})
public class VkAutoConfiguration {
}
