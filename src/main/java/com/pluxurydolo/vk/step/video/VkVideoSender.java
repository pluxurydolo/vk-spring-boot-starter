package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.dto.PostVideoRequest;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import reactor.core.publisher.Mono;

import java.io.File;

public class VkVideoSender {
    private final VkVideoSaver vkVideoSaver;
    private final VkVideoUploader vkVideoUploader;
    private final VkVideoPoster vkVideoPoster;
    private final FileUtils fileUtils;

    public VkVideoSender(
        VkVideoSaver vkVideoSaver,
        VkVideoUploader vkVideoUploader,
        VkVideoPoster vkVideoPoster,
        FileUtils fileUtils
    ) {
        this.vkVideoSaver = vkVideoSaver;
        this.vkVideoUploader = vkVideoUploader;
        this.vkVideoPoster = vkVideoPoster;
        this.fileUtils = fileUtils;
    }

    public Mono<String> send(PostVideoRequest postVideoRequest) {
        byte[] video = postVideoRequest.video();
        String caption = postVideoRequest.caption();
        UserActor userActor = postVideoRequest.userActor();
        GroupActor groupActor = postVideoRequest.groupActor();

        Mono<SaveResponse> saveResponse = vkVideoSaver.save(userActor);
        Mono<File> file = fileUtils.createTempFile("file", ".mp4", video);

        return Mono.zip(saveResponse, file)
            .flatMap(zip -> vkVideoUploader.upload(zip.getT1(), zip.getT2()))
            .flatMap(response -> vkVideoPoster.post(response, userActor, groupActor, caption))
            .thenReturn(caption);
    }
}
