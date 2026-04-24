package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.pluxurydolo.vk.step.video.VideoPoster;
import com.pluxurydolo.vk.step.video.VideoSaver;
import com.pluxurydolo.vk.step.video.VideoUploader;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkVideoUploadStepConfiguration {

    @Bean
    public VideoUploader videoUploader(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VideoUploader(vkApiClient, vkApiProperties);
    }

    @Bean
    public VideoSaver videoSaver(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VideoSaver(vkApiClient, vkApiProperties);
    }

    @Bean
    public VideoPoster videoPoster(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VideoPoster(vkApiClient, vkApiProperties);
    }
}
