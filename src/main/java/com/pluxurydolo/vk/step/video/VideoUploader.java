package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.util.VkDelay;
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

public class VideoUploader {
    private static final Logger LOGGER = LoggerFactory.getLogger(VideoUploader.class);

    private final VkApiClient vkApiClient;
    private final VkDelay vkDelay;

    public VideoUploader(VkApiClient vkApiClient, VkDelay vkDelay) {
        this.vkApiClient = vkApiClient;
        this.vkDelay = vkDelay;
    }

    public Mono<UploadResponse> upload(SaveResponse saveResponse, File file) {
        Duration delay = vkDelay.delay();

        String uploadUrl = saveResponse.getUploadUrl().toString();

        UploadVideoQuery query = vkApiClient.upload()
            .video(uploadUrl, file);

        return Mono.fromCallable(query::execute)
            .delayElement(delay, Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("hxbp [vk-starter] Видео успешно загружено на {}", uploadUrl));
    }
}
