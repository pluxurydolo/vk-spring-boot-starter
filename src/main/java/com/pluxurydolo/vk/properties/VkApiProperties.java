package com.pluxurydolo.vk.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "vk.api")
public record VkApiProperties(Duration delay) {
}
