package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.dto.PostImageRequest;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveWallPhotoResponse;
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
class VkImagePublisherTests {

    @Mock
    private VkImageUploadServerRetriever vkImageUploadServerRetriever;

    @Mock
    private VkImageUploader vkImageUploader;

    @Mock
    private VkImageWallSaver vkImageWallSaver;

    @Mock
    private VkImageWallPoster vkImageWallPoster;

    @Mock
    private GetWallUploadServerResponse getWallUploadServerResponse;

    @Mock
    private PhotoUploadResponse photoUploadResponse;

    @Mock
    private SaveWallPhotoResponse saveWallPhotoResponse;

    @Mock
    private PostResponse postResponse;

    @InjectMocks
    private VkImagePublisher vkImagePublisher;

    @Test
    void testPublish() {
        when(vkImageUploadServerRetriever.retrieve(any(), any()))
            .thenReturn(Mono.just(getWallUploadServerResponse));
        when(vkImageUploader.upload(any(), any()))
            .thenReturn(Mono.just(photoUploadResponse));
        when(vkImageWallSaver.save(any(), any(), any()))
            .thenReturn(Mono.just(saveWallPhotoResponse));
        when(vkImageWallPoster.post(any(), any(), any(), anyString()))
            .thenReturn(Mono.just(postResponse));

        Mono<String> result = vkImagePublisher.publish(postImageRequest());

        create(result)
            .expectNext("caption")
            .verifyComplete();
    }

    @Test
    void testPublishWhenExceptionOccurred() {
        when(vkImageUploadServerRetriever.retrieve(any(), any()))
            .thenReturn(Mono.just(getWallUploadServerResponse));
        when(vkImageUploader.upload(any(), any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = vkImagePublisher.publish(postImageRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(RuntimeException.class));
    }

    private static PostImageRequest postImageRequest() {
        byte[] bytes = {};
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);
        return new PostImageRequest(bytes, "caption", userActor, groupActor);
    }
}
