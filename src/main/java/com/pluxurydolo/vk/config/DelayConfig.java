package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.properties.Delay;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DelayConfig {
    private final Delay delay;

    public DelayConfig(Delay delay) {
        this.delay = delay;
    }

    public Duration delay() {
        long value = delay.value();
        ChronoUnit timeUnit = delay.timeUnit().toChronoUnit();
        return Duration.of(value, timeUnit);
    }
}
