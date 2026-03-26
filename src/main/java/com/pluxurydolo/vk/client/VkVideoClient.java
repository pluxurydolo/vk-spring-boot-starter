package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostVideoRequest;
import com.pluxurydolo.vk.step.video.VideoPoster;
import com.pluxurydolo.vk.step.video.VideoSaver;
import com.pluxurydolo.vk.step.video.VideoUploader;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import reactor.core.publisher.Mono;

import java.io.File;

public class VkVideoClient {
    private final VideoSaver videoSaver;
    private final VideoUploader videoUploader;
    private final VideoPoster videoPoster;
    private final FileUtils fileUtils;

    public VkVideoClient(VideoSaver videoSaver, VideoUploader videoUploader, VideoPoster videoPoster, FileUtils fileUtils) {
        this.videoSaver = videoSaver;
        this.videoUploader = videoUploader;
        this.videoPoster = videoPoster;
        this.fileUtils = fileUtils;
    }

    public Mono<String> postVideoToGroup(PostVideoRequest postVideoRequest) {
        byte[] video = postVideoRequest.video();
        String caption = postVideoRequest.caption();
        UserActor userActor = postVideoRequest.userActor();
        GroupActor groupActor = postVideoRequest.groupActor();

        Mono<SaveResponse> saveResponse = videoSaver.save(userActor);
        Mono<File> file = fileUtils.createTempFile("file", ".mp4", video);

        return Mono.zip(saveResponse, file)
            .flatMap(zip -> videoUploader.upload(zip.getT1(), zip.getT2()))
            .flatMap(response -> videoPoster.post(response, userActor, groupActor, caption))
            .thenReturn(caption);
    }
}
