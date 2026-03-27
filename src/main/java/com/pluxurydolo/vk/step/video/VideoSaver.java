package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.config.VkDelayConfiguration;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.queries.video.VideoSaveQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class VideoSaver {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoSaver.class);

    private final VkApiClient vkApiClient;
    private final VkDelayConfiguration vkDelayConfiguration;

    public VideoSaver(VkApiClient vkApiClient, VkDelayConfiguration vkDelayConfiguration) {
        this.vkApiClient = vkApiClient;
        this.vkDelayConfiguration = vkDelayConfiguration;
    }

    public Mono<SaveResponse> save(UserActor userActor) {
        Duration delay = vkDelayConfiguration.delay();

        VideoSaveQuery query = vkApiClient.video()
            .save(userActor);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("sfli Ссылка для сохранения видео успешно получена"));
    }
}
