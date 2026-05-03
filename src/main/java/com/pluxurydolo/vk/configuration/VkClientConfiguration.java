package com.pluxurydolo.vk.configuration;

import com.pluxurydolo.vk.client.VkImageClient;
import com.pluxurydolo.vk.client.VkVideoClient;
import com.pluxurydolo.vk.step.image.VkImagePublisher;
import com.pluxurydolo.vk.step.video.VkVideoPublisher;
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
    public VkImageClient vkImageClient(VkImagePublisher vkImagePublisher) {
        return new VkImageClient(vkImagePublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkVideoClient vkVideoClient(VkVideoPublisher vkVideoPublisher) {
        return new VkVideoClient(vkVideoPublisher);
    }

    @Bean
    @ConditionalOnMissingBean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = getInstance();
        return new VkApiClient(transportClient);
    }
}
