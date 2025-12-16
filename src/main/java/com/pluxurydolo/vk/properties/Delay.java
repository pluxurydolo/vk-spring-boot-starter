package com.pluxurydolo.vk.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "vk.delay")
public record Delay(long value, TimeUnit timeUnit) {
}
