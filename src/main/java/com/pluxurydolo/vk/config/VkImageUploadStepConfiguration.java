package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.properties.Delay;
import com.pluxurydolo.vk.step.image.PhotoUploader;
import com.pluxurydolo.vk.step.image.WallPhotoSaver;
import com.pluxurydolo.vk.step.image.WallPoster;
import com.pluxurydolo.vk.step.image.WallUploadServerRetriever;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkImageUploadStepConfiguration {

    @Bean
    public WallUploadServerRetriever wallUploadServerRetriever(
        VkApiClient vkApiClient,
        VkDelayConfiguration vkDelayConfiguration
    ) {
        return new WallUploadServerRetriever(vkApiClient, vkDelayConfiguration);
    }

    @Bean
    public PhotoUploader photoUploader(VkApiClient vkApiClient, VkDelayConfiguration vkDelayConfiguration) {
        return new PhotoUploader(vkApiClient, vkDelayConfiguration);
    }

    @Bean
    public WallPhotoSaver wallPhotoSaver(VkApiClient vkApiClient, VkDelayConfiguration vkDelayConfiguration) {
        return new WallPhotoSaver(vkApiClient, vkDelayConfiguration);
    }

    @Bean
    public WallPoster wallPoster(VkApiClient vkApiClient, VkDelayConfiguration vkDelayConfiguration) {
        return new WallPoster(vkApiClient, vkDelayConfiguration);
    }

    @Bean
    public VkDelayConfiguration delayConfiguration(Delay delay) {
        return new VkDelayConfiguration(delay);
    }
}
