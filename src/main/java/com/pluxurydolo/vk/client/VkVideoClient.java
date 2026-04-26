package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostVideoRequest;
import com.pluxurydolo.vk.exception.VkVideoUploadException;
import com.pluxurydolo.vk.step.video.VkVideoSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class VkVideoClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkVideoClient.class);

    private final VkVideoSender vkVideoSender;

    public VkVideoClient(VkVideoSender vkVideoSender) {
        this.vkVideoSender = vkVideoSender;
    }

    public Mono<String> sendVideoToGroup(PostVideoRequest request) {
        return vkVideoSender.send(request)
            .doOnSuccess(_ -> LOGGER.info("dyhn [vk-starter] Видео успешно опубликовано"))
            .onErrorResume(throwable -> {
                LOGGER.info("eupk [vk-starter] Произошла ошибка при публикации видео");
                return Mono.error(new VkVideoUploadException(throwable));
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
