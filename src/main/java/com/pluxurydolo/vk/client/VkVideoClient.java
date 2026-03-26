package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.config.DelayConfiguration;
import com.pluxurydolo.vk.dto.PostVideoRequest;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.objects.video.responses.UploadResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.upload.UploadVideoQuery;
import com.vk.api.sdk.queries.video.VideoSaveQuery;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;

public class VkVideoClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkVideoClient.class);

    private final VkApiClient vkApiClient;
    private final FileUtils fileUtils;
    private final DelayConfiguration delayConfiguration;

    public VkVideoClient(VkApiClient vkApiClient, FileUtils fileUtils, DelayConfiguration delayConfiguration) {
        this.vkApiClient = vkApiClient;
        this.fileUtils = fileUtils;
        this.delayConfiguration = delayConfiguration;
    }

    public Mono<String> postVideoToGroup(PostVideoRequest postVideoRequest) {
        byte[] video = postVideoRequest.video();
        String caption = postVideoRequest.caption();
        UserActor userActor = postVideoRequest.userActor();
        GroupActor groupActor = postVideoRequest.groupActor();

        Mono<SaveResponse> saveResponse = saveVideo(userActor);
        Mono<File> file = fileUtils.createTempFile("file", ".mp4", video);

        return Mono.zip(saveResponse, file)
            .flatMap(zip -> uploadVideo(zip.getT1(), zip.getT2()))
            .flatMap(response -> postVideo(response, userActor, groupActor, caption))
            .thenReturn(caption);
    }

    private Mono<PostResponse> postVideo(
        UploadResponse uploadResponse,
        UserActor userActor,
        GroupActor groupActor,
        String text
    ) {
        Long groupId = groupActor.getGroupId();
        Long userId = userActor.getId();
        Integer videoId = uploadResponse.getVideoId();
        String attachment = String.format("video%s_%s", userId, videoId);

        WallPostQuery query = vkApiClient.wall()
            .post(userActor)
            .ownerId(groupId)
            .message(text)
            .attachments(attachment)
            .fromGroup(true);

        return Mono.fromCallable(query::execute)
            .delayElement(delayConfiguration.delay(), Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("qicr Видео успешно выложено в группу {} с текстом {}", groupId, text));
    }

    private Mono<UploadResponse> uploadVideo(SaveResponse saveResponse, File file) {
        String uploadUrl = saveResponse.getUploadUrl().toString();

        UploadVideoQuery query = vkApiClient.upload()
            .video(uploadUrl, file);

        return Mono.fromCallable(query::execute)
            .delayElement(delayConfiguration.delay(), Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("hxbp Видео успешно загружено на {}", uploadUrl));
    }

    private Mono<SaveResponse> saveVideo(UserActor userActor) {
        VideoSaveQuery query = vkApiClient.video()
            .save(userActor);

        return Mono.fromCallable(query::execute)
            .delayElement(delayConfiguration.delay(), Schedulers.boundedElastic())
            .doOnSuccess(_ -> LOGGER.info("sfli Ссылка для сохранения видео успешно получена"));
    }
}
