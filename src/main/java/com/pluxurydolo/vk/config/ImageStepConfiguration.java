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
public class ImageStepConfiguration {

    @Bean
    public WallUploadServerRetriever wallUploadServerRetriever(
        VkApiClient vkApiClient,
        DelayConfiguration delayConfiguration
    ) {
        return new WallUploadServerRetriever(vkApiClient, delayConfiguration);
    }

    @Bean
    public PhotoUploader photoUploader(VkApiClient vkApiClient, DelayConfiguration delayConfiguration) {
        return new PhotoUploader(vkApiClient, delayConfiguration);
    }

    @Bean
    public WallPhotoSaver wallPhotoSaver(VkApiClient vkApiClient, DelayConfiguration delayConfiguration) {
        return new WallPhotoSaver(vkApiClient, delayConfiguration);
    }

    @Bean
    public WallPoster wallPoster(VkApiClient vkApiClient, DelayConfiguration delayConfiguration) {
        return new WallPoster(vkApiClient, delayConfiguration);
    }

    @Bean
    public DelayConfiguration delayConfiguration(Delay delay) {
        return new DelayConfiguration(delay);
    }
}
