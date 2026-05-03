package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.dto.PostVideoRequest;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import reactor.core.publisher.Mono;

public class VkVideoPublisher {
    private final VkVideoSaver vkVideoSaver;
    private final VkVideoUploader vkVideoUploader;
    private final VkVideoWallPoster vkVideoWallPoster;

    public VkVideoPublisher(
        VkVideoSaver vkVideoSaver,
        VkVideoUploader vkVideoUploader,
        VkVideoWallPoster vkVideoWallPoster
    ) {
        this.vkVideoSaver = vkVideoSaver;
        this.vkVideoUploader = vkVideoUploader;
        this.vkVideoWallPoster = vkVideoWallPoster;
    }

    public Mono<String> send(PostVideoRequest request) {
        byte[] video = request.video();
        String caption = request.caption();
        UserActor userActor = request.userActor();
        GroupActor groupActor = request.groupActor();

        return vkVideoSaver.save(userActor)
            .flatMap(response -> vkVideoUploader.upload(response, video))
            .flatMap(response -> vkVideoWallPoster.post(response, userActor, groupActor, caption))
            .thenReturn(caption);
    }
}
