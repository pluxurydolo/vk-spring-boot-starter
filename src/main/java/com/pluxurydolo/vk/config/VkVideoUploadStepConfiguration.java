package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.step.video.VideoPoster;
import com.pluxurydolo.vk.step.video.VideoSaver;
import com.pluxurydolo.vk.step.video.VideoUploader;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkVideoUploadStepConfiguration {

    @Bean
    public VideoUploader videoUploader(VkApiClient vkApiClient, VkDelayConfiguration vkDelayConfiguration) {
        return new VideoUploader(vkApiClient, vkDelayConfiguration);
    }

    @Bean
    public VideoSaver videoSaver(VkApiClient vkApiClient, VkDelayConfiguration vkDelayConfiguration) {
        return new VideoSaver(vkApiClient, vkDelayConfiguration);
    }

    @Bean
    public VideoPoster videoPoster(VkApiClient vkApiClient, VkDelayConfiguration vkDelayConfiguration) {
        return new VideoPoster(vkApiClient, vkDelayConfiguration);
    }
}
