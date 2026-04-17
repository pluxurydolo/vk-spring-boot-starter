package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.util.VkDelay;
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

public class WallPoster {
    private static final Logger LOGGER = LoggerFactory.getLogger(WallPoster.class);

    private final VkApiClient vkApiClient;
    private final VkDelay vkDelay;

    public WallPoster(VkApiClient vkApiClient, VkDelay vkDelay) {
        this.vkApiClient = vkApiClient;
        this.vkDelay = vkDelay;
    }

    public Mono<PostResponse> post(
        SaveWallPhotoResponse saveWallPhotoResponse,
        UserActor userActor,
        GroupActor groupActor,
        String text
    ) {
        Duration delay = vkDelay.delay();

        Long ownerId = saveWallPhotoResponse.getOwnerId();
        Integer photoId = saveWallPhotoResponse.getId();
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
            .doOnSuccess(_ -> LOGGER.info("ttgd [vk-starter] Картинка успешно выложена в группу {} с текстом {}", groupId, text));
    }
}
