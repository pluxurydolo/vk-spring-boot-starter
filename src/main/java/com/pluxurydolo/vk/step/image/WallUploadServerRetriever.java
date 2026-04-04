package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.config.VkDelayConfiguration;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import com.vk.api.sdk.queries.photos.PhotosGetWallUploadServerQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class WallUploadServerRetriever {
    private static final Logger LOGGER = LoggerFactory.getLogger(WallUploadServerRetriever.class);

    private final VkApiClient vkApiClient;
    private final VkDelayConfiguration vkDelayConfiguration;

    public WallUploadServerRetriever(VkApiClient vkApiClient, VkDelayConfiguration vkDelayConfiguration) {
        this.vkApiClient = vkApiClient;
        this.vkDelayConfiguration = vkDelayConfiguration;
    }

    public Mono<GetWallUploadServerResponse> retrieve(UserActor userActor, GroupActor groupActor) {
        Duration delay = vkDelayConfiguration.delay();

        long groupId = -groupActor.getGroupId();

        PhotosGetWallUploadServerQuery query = vkApiClient.photos()
            .getWallUploadServer(userActor)
            .groupId(groupId);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("lexf [vk-starter] Успешно получен сервер для загрузки картинки"));
    }
}
