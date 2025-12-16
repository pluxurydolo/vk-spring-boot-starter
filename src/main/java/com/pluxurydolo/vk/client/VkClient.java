package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.config.DelayConfig;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveWallPhotoResponse;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.objects.video.responses.UploadResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.photos.PhotosGetWallUploadServerQuery;
import com.vk.api.sdk.queries.photos.PhotosSaveWallPhotoQuery;
import com.vk.api.sdk.queries.upload.UploadPhotoQuery;
import com.vk.api.sdk.queries.upload.UploadVideoQuery;
import com.vk.api.sdk.queries.video.VideoSaveQuery;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.util.List;

public class VkClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(VkClient.class);

    private final VkApiClient vkApiClient;
    private final DelayConfig delayConfig;

    public VkClient(VkApiClient vkApiClient, DelayConfig delayConfig) {
        this.vkApiClient = vkApiClient;
        this.delayConfig = delayConfig;
    }

    public Mono<GetWallUploadServerResponse> getWallUploadServer(UserActor userActor, GroupActor groupActor) {
        long groupId = -groupActor.getGroupId();

        PhotosGetWallUploadServerQuery query = vkApiClient.photos()
            .getWallUploadServer(userActor)
            .groupId(groupId);

        return Mono.fromCallable(query::execute)
            .delayElement(delayConfig.delay(), Schedulers.boundedElastic())
            .doOnSuccess(it -> LOGGER.info("lexf Успешно получен сервер для загрузки картинки"));
    }

    public Mono<PhotoUploadResponse> uploadPhoto(GetWallUploadServerResponse getWallUploadServerResponse, File photo) {
        String serverUrl = getWallUploadServerResponse.getUploadUrl().toString();

        UploadPhotoQuery query = vkApiClient.upload()
            .photo(serverUrl, photo);

        return Mono.fromCallable(query::execute)
            .delayElement(delayConfig.delay(), Schedulers.boundedElastic())
            .doOnSuccess(it -> LOGGER.info("dkix Картинка успешно загружена"));
    }

    public Mono<SaveWallPhotoResponse> saveWallPhoto(
        PhotoUploadResponse photoUploadResponse,
        UserActor userActor,
        GroupActor groupActor
    ) {
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
            .delayElement(delayConfig.delay(), Schedulers.boundedElastic())
            .map(List::getFirst)
            .doOnSuccess(it -> LOGGER.info("nfcv Картинка успешно сохранена в альбом wall группы {}", groupId));
    }

    public Mono<PostResponse> postPic(
        SaveWallPhotoResponse saveWallPhotoResponse,
        UserActor userActor,
        GroupActor groupActor,
        String text
    ) {
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
            .delayElement(delayConfig.delay(), Schedulers.boundedElastic())
            .doOnSuccess(it -> LOGGER.info("ttgd Картинка успешно выложена в группу {} с текстом {}", groupId, text));
    }

    public Mono<SaveResponse> saveVideo(UserActor userActor) {
        VideoSaveQuery query = vkApiClient.video()
            .save(userActor);

        return Mono.fromCallable(query::execute)
            .delayElement(delayConfig.delay(), Schedulers.boundedElastic())
            .doOnSuccess(it -> LOGGER.info("sfli Ссылка для сохранения видео успешно получена"));
    }

    public Mono<UploadResponse> uploadVideo(SaveResponse saveResponse, File file) {
        String uploadUrl = saveResponse.getUploadUrl().toString();

        UploadVideoQuery query = vkApiClient.upload()
            .video(uploadUrl, file);

        return Mono.fromCallable(query::execute)
            .delayElement(delayConfig.delay(), Schedulers.boundedElastic())
            .doOnSuccess(it -> LOGGER.info("hxbp Видео успешно загружено на {}", uploadUrl));
    }

    public Mono<PostResponse> postVideo(
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
            .delayElement(delayConfig.delay(), Schedulers.boundedElastic())
            .doOnSuccess(it -> LOGGER.info("qicr Видео успешно выложено в группу {} с текстом {}", groupId, text));
    }
}
