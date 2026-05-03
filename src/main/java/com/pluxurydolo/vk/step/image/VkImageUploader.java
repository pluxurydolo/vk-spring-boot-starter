package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.exception.image.VkImageUploadException;
import com.pluxurydolo.vk.properties.VkApiProperties;
import com.pluxurydolo.vk.io.FileUtils;
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

public class VkImageUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkImageUploader.class);

    private final VkApiClient vkApiClient;
    private final VkApiProperties vkApiProperties;
    private final FileUtils fileUtils;

    public VkImageUploader(VkApiClient vkApiClient, VkApiProperties vkApiProperties, FileUtils fileUtils) {
        this.vkApiClient = vkApiClient;
        this.vkApiProperties = vkApiProperties;
        this.fileUtils = fileUtils;
    }

    public Mono<PhotoUploadResponse> upload(GetWallUploadServerResponse response, byte[] image) {
        String uploadUrl = response.getUploadUrl().toString();

        return fileUtils.createTempFile("file", ".jpg", image)
            .flatMap(file -> executeUpload(uploadUrl, file))
            .doOnSuccess(_ -> LOGGER.info("dkix [vk-starter] Картинка успешно загружена на {}", uploadUrl))
            .onErrorResume(throwable -> {
                LOGGER.error("zaja [vk-starter] Произошла ошибка при загрузке картинки на {}", uploadUrl);
                return Mono.error(new VkImageUploadException(throwable));
            });
    }

    private Mono<PhotoUploadResponse> executeUpload(String uploadUrl, File file) {
        Duration delay = vkApiProperties.delay();

        UploadPhotoQuery query = vkApiClient.upload()
            .photo(uploadUrl, file);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .flatMap(response -> deleteTempImage(response, file));
    }

    private Mono<PhotoUploadResponse> deleteTempImage(PhotoUploadResponse response, File file) {
        return fileUtils.deleteTempFile(file)
            .thenReturn(response);
    }
}
