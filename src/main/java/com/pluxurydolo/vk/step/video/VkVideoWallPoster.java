package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.exception.video.VkVideoPostException;
import com.pluxurydolo.vk.properties.VkApiProperties;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.video.responses.UploadResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class VkVideoWallPoster {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkVideoWallPoster.class);

    private final VkApiClient vkApiClient;
    private final VkApiProperties vkApiProperties;

    public VkVideoWallPoster(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        this.vkApiClient = vkApiClient;
        this.vkApiProperties = vkApiProperties;
    }

    public Mono<PostResponse> post(UploadResponse response, UserActor userActor, GroupActor groupActor, String text) {
        Duration delay = vkApiProperties.delay();

        Integer videoId = response.getVideoId();
        Long userId = userActor.getId();
        Long groupId = groupActor.getGroupId();
        String attachment = String.format("video%s_%s", userId, videoId);

        WallPostQuery query = vkApiClient.wall()
            .post(userActor)
            .ownerId(groupId)
            .message(text)
            .attachments(attachment)
            .fromGroup(true);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("qicr [vk-starter] Видео успешно опубликовано в группу {} с текстом {}", groupId, text))
            .onErrorResume(throwable -> {
                LOGGER.error("iejp [vk-starter] Произошла ошибка при публикации видео в группу {} с текстом {}", groupId, text);
                return Mono.error(new VkVideoPostException(throwable));
            });
    }
}
