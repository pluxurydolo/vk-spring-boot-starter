package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.dto.PostVideoRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkVideoPublisherTests {

    @Mock
    private VkVideoSaver vkVideoSaver;

    @Mock
    private VkVideoUploader vkVideoUploader;

    @Mock
    private VkVideoWallPoster vkVideoWallPoster;

    @Mock
    private SaveResponse saveResponse;

    @Mock
    private UploadResponse uploadResponse;

    @Mock
    private PostResponse postResponse;

    @InjectMocks
    private VkVideoPublisher vkVideoPublisher;

    @Test
    void testSend() {
        when(vkVideoSaver.save(any()))
            .thenReturn(Mono.just(saveResponse));
        when(vkVideoUploader.upload(any(), any()))
            .thenReturn(Mono.just(uploadResponse));
        when(vkVideoWallPoster.post(any(), any(), any(), anyString()))
            .thenReturn(Mono.just(postResponse));

        Mono<String> result = vkVideoPublisher.send(postVideoRequest());

        create(result)
            .expectNext("caption")
            .verifyComplete();
    }

    @Test
    void testSendWhenExceptionOccurred() {
        when(vkVideoSaver.save(any()))
            .thenReturn(Mono.just(saveResponse));
        when(vkVideoUploader.upload(any(), any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = vkVideoPublisher.send(postVideoRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(RuntimeException.class));
    }

    private static PostVideoRequest postVideoRequest() {
        byte[] bytes = {};
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);
        return new PostVideoRequest(bytes, "caption", userActor, groupActor);
    }
}
