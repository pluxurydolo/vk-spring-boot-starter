package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.queries.video.VideoSaveQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class VkVideoSaver {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkVideoSaver.class);

    private final VkApiClient vkApiClient;
    private final VkApiProperties vkApiProperties;

    public VkVideoSaver(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        this.vkApiClient = vkApiClient;
        this.vkApiProperties = vkApiProperties;
    }

    public Mono<SaveResponse> save(UserActor userActor) {
        Duration delay = vkApiProperties.delay();

        VideoSaveQuery query = vkApiClient.video()
            .save(userActor);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("sfli [vk-starter] Ссылка для сохранения видео успешно получена"));
    }
}
