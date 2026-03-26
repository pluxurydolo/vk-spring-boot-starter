package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.step.video.VideoPoster;
import com.pluxurydolo.vk.step.video.VideoSaver;
import com.pluxurydolo.vk.step.video.VideoUploader;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoStepConfiguration {

    @Bean
    public VideoUploader videoUploader(VkApiClient vkApiClient, DelayConfiguration delayConfiguration) {
        return new VideoUploader(vkApiClient, delayConfiguration);
    }

    @Bean
    public VideoSaver videoSaver(VkApiClient vkApiClient, DelayConfiguration delayConfiguration) {
        return new VideoSaver(vkApiClient, delayConfiguration);
    }

    @Bean
    public VideoPoster videoPoster(VkApiClient vkApiClient, DelayConfiguration delayConfiguration) {
        return new VideoPoster(vkApiClient, delayConfiguration);
    }
}
