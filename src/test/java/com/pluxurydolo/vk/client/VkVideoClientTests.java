package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostVideoRequest;
import com.pluxurydolo.vk.step.video.VideoPoster;
import com.pluxurydolo.vk.step.video.VideoSaver;
import com.pluxurydolo.vk.step.video.VideoUploader;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.objects.video.responses.UploadResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkVideoClientTests {

    @Mock
    private VideoSaver videoSaver;

    @Mock
    private VideoUploader videoUploader;

    @Mock
    private VideoPoster videoPoster;

    @Mock
    private FileUtils fileUtils;

    @Mock
    private SaveResponse saveResponse;

    @Mock
    private File file;

    @Mock
    private UploadResponse uploadResponse;

    @Mock
    private PostResponse postResponse;

    @InjectMocks
    private VkVideoClient vkVideoClient;

    @Test
    void testPostVideoToGroup() {
        when(videoSaver.save(any()))
            .thenReturn(Mono.just(saveResponse));
        when(fileUtils.createTempFile(anyString(), anyString(), any()))
            .thenReturn(Mono.just(file));
        when(videoUploader.upload(any(), any()))
            .thenReturn(Mono.just(uploadResponse));
        when(videoPoster.post(any(), any(), any(), anyString()))
            .thenReturn(Mono.just(postResponse));

        Mono<String> result = vkVideoClient.postVideoToGroup(postVideoRequest());

        create(result)
            .expectNext("caption")
            .verifyComplete();
    }

    private static PostVideoRequest postVideoRequest() {
        byte[] video = {};
        String caption = "caption";
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);
        return new PostVideoRequest(video, caption, userActor, groupActor);
    }
}
