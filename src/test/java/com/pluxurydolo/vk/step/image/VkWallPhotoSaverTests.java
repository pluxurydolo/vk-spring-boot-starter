package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.vk.api.sdk.actions.Photos;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveWallPhotoResponse;
import com.vk.api.sdk.queries.photos.PhotosSaveWallPhotoQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.time.Duration.ZERO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkWallPhotoSaverTests {

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private VkApiProperties vkApiProperties;

    @Mock
    private PhotoUploadResponse photoUploadResponse;

    @Mock
    private UserActor userActor;

    @Mock
    private GroupActor groupActor;

    @Mock
    private Photos photos;

    @Mock
    private PhotosSaveWallPhotoQuery photosSaveWallPhotoQuery;

    @Mock
    private SaveWallPhotoResponse saveWallPhotoResponse;

    @InjectMocks
    private VkWallPhotoSaver vkWallPhotoSaver;

    @Test
    void testSave() throws ClientException, ApiException {
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(photoUploadResponse.getServer())
            .thenReturn(1);
        when(photoUploadResponse.getHash())
            .thenReturn("hash");
        when(photoUploadResponse.getPhoto())
            .thenReturn("photo");
        when(groupActor.getGroupId())
            .thenReturn(1L);
        when(vkApiClient.photos())
            .thenReturn(photos);
        when(photos.saveWallPhoto(any()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.server(anyInt()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.hash(anyString()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.photo(anyString()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.groupId(anyLong()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.execute())
            .thenReturn(List.of(saveWallPhotoResponse));

        Mono<SaveWallPhotoResponse> result = vkWallPhotoSaver.save(photoUploadResponse, userActor, groupActor);

        create(result)
            .expectNext(saveWallPhotoResponse)
            .verifyComplete();
    }

    @Test
    void testSaveWhenExceptionOccurred() throws ClientException, ApiException {
        doThrow(RuntimeException.class)
            .when(photosSaveWallPhotoQuery).execute();
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(photoUploadResponse.getServer())
            .thenReturn(1);
        when(photoUploadResponse.getHash())
            .thenReturn("hash");
        when(photoUploadResponse.getPhoto())
            .thenReturn("photo");
        when(groupActor.getGroupId())
            .thenReturn(1L);
        when(vkApiClient.photos())
            .thenReturn(photos);
        when(photos.saveWallPhoto(any()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.server(anyInt()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.hash(anyString()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.photo(anyString()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.groupId(anyLong()))
            .thenReturn(photosSaveWallPhotoQuery);

        Mono<SaveWallPhotoResponse> result = vkWallPhotoSaver.save(photoUploadResponse, userActor, groupActor);

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }
}
