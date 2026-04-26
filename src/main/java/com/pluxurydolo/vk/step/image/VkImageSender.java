package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.dto.PostImageRequest;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;

public class VkImageSender {
    private final VkWallUploadServerRetriever vkWallUploadServerRetriever;
    private final VkPhotoUploader vkPhotoUploader;
    private final VkWallPhotoSaver vkWallPhotoSaver;
    private final VkWallPoster vkWallPoster;
    private final FileUtils fileUtils;

    public VkImageSender(
        VkWallUploadServerRetriever vkWallUploadServerRetriever,
        VkPhotoUploader vkPhotoUploader,
        VkWallPhotoSaver vkWallPhotoSaver,
        VkWallPoster vkWallPoster,
        FileUtils fileUtils
    ) {
        this.vkWallUploadServerRetriever = vkWallUploadServerRetriever;
        this.vkPhotoUploader = vkPhotoUploader;
        this.vkWallPhotoSaver = vkWallPhotoSaver;
        this.vkWallPoster = vkWallPoster;
        this.fileUtils = fileUtils;
    }

    public Mono<String> send(PostImageRequest postImageRequest) {
        byte[] image = postImageRequest.image();
        String caption = postImageRequest.caption();
        UserActor userActor = postImageRequest.userActor();
        GroupActor groupActor = postImageRequest.groupActor();

        Mono<GetWallUploadServerResponse> wallUploadServer = vkWallUploadServerRetriever.retrieve(userActor, groupActor);
        Mono<File> file = fileUtils.createTempFile("file", ".jpg", image);

        return Mono.zip(wallUploadServer, file)
            .flatMap(zip -> vkPhotoUploader.upload(zip.getT1(), zip.getT2()))
            .flatMap(response -> vkWallPhotoSaver.save(response, userActor, groupActor))
            .flatMap(response -> vkWallPoster.post(response, userActor, groupActor, caption))
            .thenReturn(caption);
    }
}
