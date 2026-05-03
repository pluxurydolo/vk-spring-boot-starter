package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.pluxurydolo.vk.step.image.VkImagePublisher;
import com.pluxurydolo.vk.step.image.VkImageUploadServerRetriever;
import com.pluxurydolo.vk.step.image.VkImageUploader;
import com.pluxurydolo.vk.step.image.VkImageWallPoster;
import com.pluxurydolo.vk.step.image.VkImageWallSaver;
import com.pluxurydolo.vk.io.FileUtils;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkImageUploadStepConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public VkImagePublisher vkImagePublisher(
        VkImageUploadServerRetriever vkImageUploadServerRetriever,
        VkImageUploader vkImageUploader,
        VkImageWallSaver vkImageWallSaver,
        VkImageWallPoster vkImageWallPoster
    ) {
        return new VkImagePublisher(vkImageUploadServerRetriever, vkImageUploader, vkImageWallSaver, vkImageWallPoster);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkImageUploadServerRetriever vkImageUploadServerRetriever(
        VkApiClient vkApiClient,
        VkApiProperties vkApiProperties
    ) {
        return new VkImageUploadServerRetriever(vkApiClient, vkApiProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkImageUploader vkImageUploader(VkApiClient vkApiClient, VkApiProperties vkApiProperties, FileUtils fileUtils) {
        return new VkImageUploader(vkApiClient, vkApiProperties, fileUtils);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkImageWallSaver vkImageWallSaver(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkImageWallSaver(vkApiClient, vkApiProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkImageWallPoster vkImageWallPoster(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkImageWallPoster(vkApiClient, vkApiProperties);
    }
}
