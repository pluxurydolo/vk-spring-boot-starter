package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.properties.VkApiProperties;
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
    public WallUploadServerRetriever wallUploadServerRetriever(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new WallUploadServerRetriever(vkApiClient, vkApiProperties);
    }

    @Bean
    public PhotoUploader photoUploader(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new PhotoUploader(vkApiClient, vkApiProperties);
    }

    @Bean
    public WallPhotoSaver wallPhotoSaver(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new WallPhotoSaver(vkApiClient, vkApiProperties);
    }

    @Bean
    public WallPoster wallPoster(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new WallPoster(vkApiClient, vkApiProperties);
    }
}
