package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.config.DelayConfiguration;
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
    private final DelayConfiguration delayConfiguration;

    public VideoSaver(VkApiClient vkApiClient, DelayConfiguration delayConfiguration) {
        this.vkApiClient = vkApiClient;
        this.delayConfiguration = delayConfiguration;
    }

    public Mono<SaveResponse> save(UserActor userActor) {
        Duration delay = delayConfiguration.delay();

        VideoSaveQuery query = vkApiClient.video()
            .save(userActor);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("sfli Ссылка для сохранения видео успешно получена"));
    }
}
