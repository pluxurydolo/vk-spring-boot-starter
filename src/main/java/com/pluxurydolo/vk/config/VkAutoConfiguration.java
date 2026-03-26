package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.client.VkPhotoClient;
import com.pluxurydolo.vk.client.VkVideoClient;
import com.pluxurydolo.vk.properties.Delay;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static com.vk.api.sdk.httpclient.HttpTransportClient.getInstance;

@AutoConfiguration
@ConditionalOnProperty(prefix = "vk", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(Delay.class)
public class VkAutoConfiguration {

    @Bean
    public VkPhotoClient vkPhotoClient(VkApiClient vkApiClient, FileUtils fileUtils, DelayConfiguration delayConfiguration) {
        return new VkPhotoClient(vkApiClient, fileUtils, delayConfiguration);
    }

    @Bean
    public VkVideoClient vkVideoClient(VkApiClient vkApiClient, FileUtils fileUtils, DelayConfiguration delayConfiguration) {
        return new VkVideoClient(vkApiClient, fileUtils, delayConfiguration);
    }

    @Bean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = getInstance();
        return new VkApiClient(transportClient);
    }

    @Bean
    public DelayConfiguration delayConfig(Delay delay) {
        return new DelayConfiguration(delay);
    }

    @Bean
    public FileUtils fileUtils() {
        return new FileUtils();
    }
}
