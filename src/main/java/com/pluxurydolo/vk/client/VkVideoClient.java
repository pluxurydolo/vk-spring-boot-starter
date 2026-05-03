package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostVideoRequest;
import com.pluxurydolo.vk.exception.video.VkVideoPublishException;
import com.pluxurydolo.vk.step.video.VkVideoPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class VkVideoClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkVideoClient.class);

    private final VkVideoPublisher vkVideoPublisher;

    public VkVideoClient(VkVideoPublisher vkVideoPublisher) {
        this.vkVideoPublisher = vkVideoPublisher;
    }

    public Mono<String> sendVideoToGroup(PostVideoRequest request) {
        return vkVideoPublisher.send(request)
            .doOnSuccess(_ -> LOGGER.info("dyhn [vk-starter] Видео успешно опубликовано"))
            .onErrorResume(throwable -> {
                LOGGER.info("eupk [vk-starter] Произошла ошибка при публикации видео");
                return Mono.error(new VkVideoPublishException(throwable));
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
