package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.dto.PostVideoRequest;
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
class VkVideoSenderTests {

    @Mock
    private VkVideoSaver vkVideoSaver;

    @Mock
    private VkVideoUploader vkVideoUploader;

    @Mock
    private VkVideoPoster vkVideoPoster;

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
    private VkVideoSender vkVideoSender;

    @Test
    void testSend() {
        when(vkVideoSaver.save(any()))
            .thenReturn(Mono.just(saveResponse));
        when(fileUtils.createTempFile(anyString(), anyString(), any()))
            .thenReturn(Mono.just(file));
        when(vkVideoUploader.upload(any(), any()))
            .thenReturn(Mono.just(uploadResponse));
        when(vkVideoPoster.post(any(), any(), any(), anyString()))
            .thenReturn(Mono.just(postResponse));

        Mono<String> result = vkVideoSender.send(postVideoRequest());

        create(result)
            .expectNext("caption")
            .verifyComplete();
    }

    @Test
    void testSendWhenExceptionOccurred() {
        when(vkVideoSaver.save(any()))
            .thenReturn(Mono.just(saveResponse));
        when(fileUtils.createTempFile(anyString(), anyString(), any()))
            .thenReturn(Mono.just(file));
        when(vkVideoUploader.upload(any(), any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = vkVideoSender.send(postVideoRequest());

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    private static PostVideoRequest postVideoRequest() {
        byte[] bytes = {};
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);
        return new PostVideoRequest(bytes, "caption", userActor, groupActor);
    }
}
