package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.client.VkClient;
import com.pluxurydolo.vk.properties.Delay;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static com.vk.api.sdk.httpclient.HttpTransportClient.getInstance;

@AutoConfiguration
@EnableConfigurationProperties(Delay.class)
@ConditionalOnProperty(
    prefix = "vk.delay",
    name = {"value", "time-unit"}
)
public class VkAutoConfiguration {

    @Bean
    public VkClient getVkClient(VkApiClient vkApiClient, DelayConfig delayConfig) {
        return new VkClient(vkApiClient, delayConfig);
    }

    @Bean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = getInstance();
        return new VkApiClient(transportClient);
    }

    @Bean
    public DelayConfig delayConfig(Delay delay) {
        return new DelayConfig(delay);
    }
}
