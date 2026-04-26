package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.dto.PostImageRequest;
import com.pluxurydolo.vk.util.FileUtils;
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

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkImageSenderTests {

    @Mock
    private VkWallUploadServerRetriever vkWallUploadServerRetriever;

    @Mock
    private VkPhotoUploader vkPhotoUploader;

    @Mock
    private VkWallPhotoSaver vkWallPhotoSaver;

    @Mock
    private VkWallPoster vkWallPoster;

    @Mock
    private FileUtils fileUtils;

    @Mock
    private GetWallUploadServerResponse getWallUploadServerResponse;

    @Mock
    private File file;

    @Mock
    private PhotoUploadResponse photoUploadResponse;

    @Mock
    private SaveWallPhotoResponse saveWallPhotoResponse;

    @Mock
    private PostResponse postResponse;

    @InjectMocks
    private VkImageSender vkImageSender;

    @Test
    void testSendToGroup() {
        when(vkWallUploadServerRetriever.retrieve(any(), any()))
            .thenReturn(Mono.just(getWallUploadServerResponse));
        when(fileUtils.createTempFile(anyString(), anyString(), any()))
            .thenReturn(Mono.just(file));
        when(vkPhotoUploader.upload(any(), any()))
            .thenReturn(Mono.just(photoUploadResponse));
        when(vkWallPhotoSaver.save(any(), any(), any()))
            .thenReturn(Mono.just(saveWallPhotoResponse));
        when(vkWallPoster.post(any(), any(), any(), anyString()))
            .thenReturn(Mono.just(postResponse));

        Mono<String> result = vkImageSender.sendToGroup(postImageRequest());

        create(result)
            .expectNext("caption")
            .verifyComplete();
    }

    @Test
    void testSendToGroupWhenExceptionOccurred() {
        when(vkWallUploadServerRetriever.retrieve(any(), any()))
            .thenReturn(Mono.just(getWallUploadServerResponse));
        when(fileUtils.createTempFile(anyString(), anyString(), any()))
            .thenReturn(Mono.just(file));
        when(vkPhotoUploader.upload(any(), any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = vkImageSender.sendToGroup(postImageRequest());

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }

    private static PostImageRequest postImageRequest() {
        byte[] bytes = {};
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);

        return new PostImageRequest(bytes, "caption", userActor, groupActor);
    }
}
