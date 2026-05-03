package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.dto.PostImageRequest;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import reactor.core.publisher.Mono;

public class VkImagePublisher {
    private final VkImageUploadServerRetriever vkImageUploadServerRetriever;
    private final VkImageUploader vkImageUploader;
    private final VkImageWallSaver vkImageWallSaver;
    private final VkImageWallPoster vkImageWallPoster;

    public VkImagePublisher(
        VkImageUploadServerRetriever vkImageUploadServerRetriever,
        VkImageUploader vkImageUploader,
        VkImageWallSaver vkImageWallSaver,
        VkImageWallPoster vkImageWallPoster
    ) {
        this.vkImageUploadServerRetriever = vkImageUploadServerRetriever;
        this.vkImageUploader = vkImageUploader;
        this.vkImageWallSaver = vkImageWallSaver;
        this.vkImageWallPoster = vkImageWallPoster;
    }

    public Mono<String> publish(PostImageRequest request) {
        byte[] image = request.image();
        String caption = request.caption();
        UserActor userActor = request.userActor();
        GroupActor groupActor = request.groupActor();

        return vkImageUploadServerRetriever.retrieve(userActor, groupActor)
            .flatMap(response -> vkImageUploader.upload(response, image))
            .flatMap(response -> vkImageWallSaver.save(response, userActor, groupActor))
            .flatMap(response -> vkImageWallPoster.post(response, userActor, groupActor, caption))
            .thenReturn(caption);
    }
}
