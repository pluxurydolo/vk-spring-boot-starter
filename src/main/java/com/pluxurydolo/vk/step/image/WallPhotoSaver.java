package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveWallPhotoResponse;
import com.vk.api.sdk.queries.photos.PhotosSaveWallPhotoQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;

public class WallPhotoSaver {
    private static final Logger LOGGER = LoggerFactory.getLogger(WallPhotoSaver.class);

    private final VkApiClient vkApiClient;
    private final VkApiProperties vkApiProperties;

    public WallPhotoSaver(VkApiClient vkApiClient, VkApiProperties vkApiProperties) {
        this.vkApiClient = vkApiClient;
        this.vkApiProperties = vkApiProperties;
    }

    public Mono<SaveWallPhotoResponse> save(
        PhotoUploadResponse photoUploadResponse,
        UserActor userActor,
        GroupActor groupActor
    ) {
        Duration delay = vkApiProperties.delay();

        Integer server = photoUploadResponse.getServer();
        String hash = photoUploadResponse.getHash();
        String photo = photoUploadResponse.getPhoto();
        long groupId = -groupActor.getGroupId();

        PhotosSaveWallPhotoQuery query = vkApiClient.photos()
            .saveWallPhoto(userActor)
            .server(server)
            .hash(hash)
            .photo(photo)
            .groupId(groupId);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .map(List::getFirst)
            .doOnSuccess(_ -> LOGGER.info("nfcv [vk-starter] Картинка успешно сохранена в альбом wall группы {}", groupId));
    }
}
