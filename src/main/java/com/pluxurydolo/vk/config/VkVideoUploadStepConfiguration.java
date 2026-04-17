package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.step.video.VideoPoster;
import com.pluxurydolo.vk.step.video.VideoSaver;
import com.pluxurydolo.vk.step.video.VideoUploader;
import com.pluxurydolo.vk.util.VkDelay;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkVideoUploadStepConfiguration {

    @Bean
    public VideoUploader videoUploader(VkApiClient vkApiClient, VkDelay vkDelay) {
        return new VideoUploader(vkApiClient, vkDelay);
    }

    @Bean
    public VideoSaver videoSaver(VkApiClient vkApiClient, VkDelay vkDelay) {
        return new VideoSaver(vkApiClient, vkDelay);
    }

    @Bean
    public VideoPoster videoPoster(VkApiClient vkApiClient, VkDelay vkDelay) {
        return new VideoPoster(vkApiClient, vkDelay);
    }
}
