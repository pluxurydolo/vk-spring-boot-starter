package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.exception.image.VkImageWallPostException;
import com.pluxurydolo.vk.properties.VkApiProperties;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.responses.SaveWallPhotoResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class VkImageWallPoster {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkImageWallPoster.class);

    private final VkApiClient vkApiClient;
    private final VkApiProperties vkApiProperties;

    public VkImageWallPoster(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        this.vkApiClient = vkApiClient;
        this.vkApiProperties = vkApiProperties;
    }

    public Mono<PostResponse> post(SaveWallPhotoResponse response, UserActor userActor, GroupActor groupActor, String text) {
        Duration delay = vkApiProperties.delay();

        Long ownerId = response.getOwnerId();
        Integer photoId = response.getId();
        String attachment = String.format("photo%s_%s", ownerId, photoId);
        Long groupId = groupActor.getGroupId();

        WallPostQuery query = vkApiClient.wall()
            .post(userActor)
            .ownerId(groupId)
            .message(text)
            .attachments(attachment)
            .fromGroup(true);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("ttgd [vk-starter] Картинка успешно опубликована в группу {} с текстом {}", groupId, text))
            .onErrorResume(throwable -> {
                LOGGER.error("izgl [vk-starter] Произошла ошибка при публикации картинки в группу {} с текстом {}", groupId, text);
                return Mono.error(new VkImageWallPostException(throwable));
            });
    }
}
