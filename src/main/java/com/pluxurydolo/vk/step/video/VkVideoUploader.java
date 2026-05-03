package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.exception.video.VkVideoUploadException;
import com.pluxurydolo.vk.properties.VkApiProperties;
import com.pluxurydolo.vk.io.FileUtils;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.objects.video.responses.UploadResponse;
import com.vk.api.sdk.queries.upload.UploadVideoQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.time.Duration;

public class VkVideoUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkVideoUploader.class);

    private final VkApiClient vkApiClient;
    private final VkApiProperties vkApiProperties;
    private final FileUtils fileUtils;

    public VkVideoUploader(VkApiClient vkApiClient, VkApiProperties vkApiProperties, FileUtils fileUtils) {
        this.vkApiClient = vkApiClient;
        this.vkApiProperties = vkApiProperties;
        this.fileUtils = fileUtils;
    }

    public Mono<UploadResponse> upload(SaveResponse response, byte[] video) {
        String uploadUrl = response.getUploadUrl().toString();

        return fileUtils.createTempFile("file", ".mp4", video)
            .flatMap(file -> executeUpload(uploadUrl, file))
            .doOnSuccess(_ -> LOGGER.info("hxbp [vk-starter] Видео успешно загружено на {}", uploadUrl))
            .onErrorResume(throwable -> {
                LOGGER.error("gezv [vk-starter] Произошла ошибка при загрузке видео на {}", uploadUrl);
                return Mono.error(new VkVideoUploadException(throwable));
            });
    }

    private Mono<UploadResponse> executeUpload(String uploadUrl, File file) {
        Duration delay = vkApiProperties.delay();

        UploadVideoQuery query = vkApiClient.upload()
            .video(uploadUrl, file);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .flatMap(response -> deleteTempVideo(response, file));
    }

    private Mono<UploadResponse> deleteTempVideo(UploadResponse response, File file) {
        return fileUtils.deleteTempFile(file)
            .thenReturn(response);
    }
}
