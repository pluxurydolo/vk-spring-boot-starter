package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostImageRequest;
import com.pluxurydolo.vk.step.image.PhotoUploader;
import com.pluxurydolo.vk.step.image.WallPhotoSaver;
import com.pluxurydolo.vk.step.image.WallPoster;
import com.pluxurydolo.vk.step.image.WallUploadServerRetriever;
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
class VkImageClientTests {

    @Mock
    private WallUploadServerRetriever wallUploadServerRetriever;

    @Mock
    private PhotoUploader photoUploader;

    @Mock
    private WallPhotoSaver wallPhotoSaver;

    @Mock
    private WallPoster wallPoster;

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
    private VkImageClient vkImageClient;

    @Test
    void testPostImageToGroup() {
        when(wallUploadServerRetriever.retrieve(any(), any()))
            .thenReturn(Mono.just(getWallUploadServerResponse));
        when(fileUtils.createTempFile(anyString(), anyString(), any()))
            .thenReturn(Mono.just(file));
        when(photoUploader.upload(any(), any()))
            .thenReturn(Mono.just(photoUploadResponse));
        when(wallPhotoSaver.save(any(), any(), any()))
            .thenReturn(Mono.just(saveWallPhotoResponse));
        when(wallPoster.post(any(), any(), any(), anyString()))
            .thenReturn(Mono.just(postResponse));

        Mono<String> result = vkImageClient.postImageToGroup(postImageRequest());

        create(result)
            .expectNext("caption")
            .verifyComplete();
    }

    private static PostImageRequest postImageRequest() {
        byte[] image = {};
        String caption = "caption";
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);
        return new PostImageRequest(image, caption, userActor, groupActor);
    }
}

