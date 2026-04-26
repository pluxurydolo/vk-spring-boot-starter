package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.pluxurydolo.vk.step.image.VkImageSender;
import com.pluxurydolo.vk.step.image.VkPhotoUploader;
import com.pluxurydolo.vk.step.image.VkWallPhotoSaver;
import com.pluxurydolo.vk.step.image.VkWallPoster;
import com.pluxurydolo.vk.step.image.VkWallUploadServerRetriever;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkImageUploadStepConfiguration {

    @Bean
    public VkImageSender vkImageSender(
        VkWallUploadServerRetriever vkWallUploadServerRetriever,
        VkPhotoUploader vkPhotoUploader,
        VkWallPhotoSaver vkWallPhotoSaver,
        VkWallPoster vkWallPoster,
        FileUtils fileUtils
    ) {
        return new VkImageSender(
            vkWallUploadServerRetriever,
            vkPhotoUploader,
            vkWallPhotoSaver,
            vkWallPoster,
            fileUtils
        );
    }

    @Bean
    public VkWallUploadServerRetriever vkWallUploadServerRetriever(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkWallUploadServerRetriever(vkApiClient, vkApiProperties);
    }

    @Bean
    public VkPhotoUploader vkPhotoUploader(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkPhotoUploader(vkApiClient, vkApiProperties);
    }

    @Bean
    public VkWallPhotoSaver vkWallPhotoSaver(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkWallPhotoSaver(vkApiClient, vkApiProperties);
    }

    @Bean
    public VkWallPoster vkWallPoster(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkWallPoster(vkApiClient, vkApiProperties);
    }
}
