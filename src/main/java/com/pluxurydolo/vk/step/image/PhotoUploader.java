package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.util.VkDelay;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.queries.upload.UploadPhotoQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.time.Duration;

public class PhotoUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoUploader.class);

    private final VkApiClient vkApiClient;
    private final VkDelay vkDelay;

    public PhotoUploader(VkApiClient vkApiClient, VkDelay vkDelay) {
        this.vkApiClient = vkApiClient;
        this.vkDelay = vkDelay;
    }

    public Mono<PhotoUploadResponse> upload(GetWallUploadServerResponse getWallUploadServerResponse, File photo) {
        Duration delay = vkDelay.delay();

        String serverUrl = getWallUploadServerResponse.getUploadUrl().toString();

        UploadPhotoQuery query = vkApiClient.upload()
            .photo(serverUrl, photo);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("dkix [vk-starter] Картинка успешно загружена"));
    }
}
