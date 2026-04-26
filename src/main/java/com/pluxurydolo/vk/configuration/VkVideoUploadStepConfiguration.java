package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.pluxurydolo.vk.step.video.VkVideoPoster;
import com.pluxurydolo.vk.step.video.VkVideoSaver;
import com.pluxurydolo.vk.step.video.VkVideoSender;
import com.pluxurydolo.vk.step.video.VkVideoUploader;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkVideoUploadStepConfiguration {

    @Bean
    public VkVideoSender vkVideoSender(
        VkVideoSaver vkVideoSaver,
        VkVideoUploader vkVideoUploader,
        VkVideoPoster vkVideoPoster,
        FileUtils fileUtils
    ) {
        return new VkVideoSender(vkVideoSaver, vkVideoUploader, vkVideoPoster, fileUtils);
    }

    @Bean
    public VkVideoUploader vkVideoUploader(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkVideoUploader(vkApiClient, vkApiProperties);
    }

    @Bean
    public VkVideoSaver vkVideoSaver(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkVideoSaver(vkApiClient, vkApiProperties);
    }

    @Bean
    public VkVideoPoster vkVideoPoster(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        return new VkVideoPoster(vkApiClient, vkApiProperties);
    }
}
