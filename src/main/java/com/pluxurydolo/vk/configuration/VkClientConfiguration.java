package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.client.VkImageClient;
import com.pluxurydolo.vk.client.VkVideoClient;
import com.pluxurydolo.vk.step.image.VkImageSender;
import com.pluxurydolo.vk.step.video.VkVideoSender;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.vk.api.sdk.httpclient.HttpTransportClient.getInstance;

@Configuration
public class VkClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public VkImageClient vkImageClient(VkImageSender vkImageSender) {
        return new VkImageClient(vkImageSender);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkVideoClient vkVideoClient(VkVideoSender vkVideoSender) {
        return new VkVideoClient(vkVideoSender);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = getInstance();
        return new VkApiClient(transportClient);
    }
}
