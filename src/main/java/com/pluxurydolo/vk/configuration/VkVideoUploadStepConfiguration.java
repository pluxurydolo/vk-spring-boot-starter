package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.pluxurydolo.vk.step.video.VkVideoPublisher;
import com.pluxurydolo.vk.step.video.VkVideoSaver;
import com.pluxurydolo.vk.step.video.VkVideoUploader;
import com.pluxurydolo.vk.step.video.VkVideoWallPoster;
import com.pluxurydolo.vk.io.FileUtils;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkVideoUploadStepConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public VkVideoPublisher vkVideoPublisher(
        VkVideoSaver vkVideoSaver,
        VkVideoUploader vkVideoUploader,
        VkVideoWallPoster vkVideoWallPoster
    ) {
        return new VkVideoPublisher(vkVideoSaver, vkVideoUploader, vkVideoWallPoster);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkVideoUploader vkVideoUploader(VkApiClient vkApiClient, VkApiProperties vkApiProperties, FileUtils fileUtils) {
        return new VkVideoUploader(vkApiClient, vkApiProperties, fileUtils);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkVideoSaver vkVideoSaver(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkVideoSaver(vkApiClient, vkApiProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkVideoWallPoster vkVideoWallPoster(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkVideoWallPoster(vkApiClient, vkApiProperties);
    }
}
