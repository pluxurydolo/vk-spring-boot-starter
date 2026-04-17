package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.properties.Delay;
import com.pluxurydolo.vk.step.image.PhotoUploader;
import com.pluxurydolo.vk.step.image.WallPhotoSaver;
import com.pluxurydolo.vk.step.image.WallPoster;
import com.pluxurydolo.vk.step.image.WallUploadServerRetriever;
import com.pluxurydolo.vk.util.VkDelay;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkImageUploadStepConfiguration {

    @Bean
    public WallUploadServerRetriever wallUploadServerRetriever(
        VkApiClient vkApiClient,
        VkDelay vkDelay
    ) {
        return new WallUploadServerRetriever(vkApiClient, vkDelay);
    }

    @Bean
    public PhotoUploader photoUploader(VkApiClient vkApiClient, VkDelay vkDelay) {
        return new PhotoUploader(vkApiClient, vkDelay);
    }

    @Bean
    public WallPhotoSaver wallPhotoSaver(VkApiClient vkApiClient, VkDelay vkDelay) {
        return new WallPhotoSaver(vkApiClient, vkDelay);
    }

    @Bean
    public WallPoster wallPoster(VkApiClient vkApiClient, VkDelay vkDelay) {
        return new WallPoster(vkApiClient, vkDelay);
    }

    @Bean
    public VkDelay delayConfiguration(Delay delay) {
        return new VkDelay(delay);
    }
}
