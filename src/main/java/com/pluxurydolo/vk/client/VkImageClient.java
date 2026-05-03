package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostImageRequest;
import com.pluxurydolo.vk.exception.image.VkImagePublishException;
import com.pluxurydolo.vk.step.image.VkImagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class VkImageClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkImageClient.class);

    private final VkImagePublisher vkImagePublisher;

    public VkImageClient(VkImagePublisher vkImagePublisher) {
        this.vkImagePublisher = vkImagePublisher;
    }

    public Mono<String> sendImageToGroup(PostImageRequest request) {
        return vkImagePublisher.publish(request)
            .doOnSuccess(_ -> LOGGER.info("cepb [vk-starter] Изображение успешно опубликовано"))
            .onErrorResume(throwable -> {
                LOGGER.info("abka [vk-starter] Произошла ошибка при публикации изображения");
                return Mono.error(new VkImagePublishException(throwable));
            })
            .subscribeOn(Schedulers.boundedElastic());
    }
}
