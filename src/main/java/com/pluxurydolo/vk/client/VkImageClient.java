package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostImageRequest;
import com.pluxurydolo.vk.step.image.PhotoUploader;
import com.pluxurydolo.vk.step.image.WallPhotoSaver;
import com.pluxurydolo.vk.step.image.WallPoster;
import com.pluxurydolo.vk.step.image.WallUploadServerRetriever;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;

public class VkImageClient {
    private final WallUploadServerRetriever wallUploadServerRetriever;
    private final PhotoUploader photoUploader;
    private final WallPhotoSaver wallPhotoSaver;
    private final WallPoster wallPoster;
    private final FileUtils fileUtils;

    public VkImageClient(
        WallUploadServerRetriever wallUploadServerRetriever,
        PhotoUploader photoUploader,
        WallPhotoSaver wallPhotoSaver,
        WallPoster wallPoster,
        FileUtils fileUtils
    ) {
        this.wallUploadServerRetriever = wallUploadServerRetriever;
        this.photoUploader = photoUploader;
        this.wallPhotoSaver = wallPhotoSaver;
        this.wallPoster = wallPoster;
        this.fileUtils = fileUtils;
    }

    public Mono<String> postImageToGroup(PostImageRequest postImageRequest) {
        byte[] image = postImageRequest.image();
        String caption = postImageRequest.caption();
        UserActor userActor = postImageRequest.userActor();
        GroupActor groupActor = postImageRequest.groupActor();

        Mono<GetWallUploadServerResponse> wallUploadServer = wallUploadServerRetriever.retrieve(userActor, groupActor);
        Mono<File> file = fileUtils.createTempFile("file", ".jpg", image);

        return Mono.zip(wallUploadServer, file)
            .flatMap(zip -> photoUploader.upload(zip.getT1(), zip.getT2()))
            .flatMap(response -> wallPhotoSaver.save(response, userActor, groupActor))
            .flatMap(response -> wallPoster.post(response, userActor, groupActor, caption))
            .thenReturn(caption);
    }
}
