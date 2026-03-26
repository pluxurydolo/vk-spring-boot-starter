package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.config.DelayConfiguration;
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
    private final DelayConfiguration delayConfiguration;

    public PhotoUploader(VkApiClient vkApiClient, DelayConfiguration delayConfiguration) {
        this.vkApiClient = vkApiClient;
        this.delayConfiguration = delayConfiguration;
    }

    public Mono<PhotoUploadResponse> upload(GetWallUploadServerResponse getWallUploadServerResponse, File photo) {
        Duration delay = delayConfiguration.delay();

        String serverUrl = getWallUploadServerResponse.getUploadUrl().toString();

        UploadPhotoQuery query = vkApiClient.upload()
            .photo(serverUrl, photo);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("dkix Картинка успешно загружена"));
    }
}
