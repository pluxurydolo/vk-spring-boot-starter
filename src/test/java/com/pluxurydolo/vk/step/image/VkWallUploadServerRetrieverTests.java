package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.vk.api.sdk.actions.Photos;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import com.vk.api.sdk.queries.photos.PhotosGetWallUploadServerQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static java.time.Duration.ZERO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkWallUploadServerRetrieverTests {

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private VkApiProperties vkApiProperties;

    @Mock
    private UserActor userActor;

    @Mock
    private GroupActor groupActor;

    @Mock
    private Photos photos;

    @Mock
    private PhotosGetWallUploadServerQuery photosGetWallUploadServerQuery;

    @Mock
    private GetWallUploadServerResponse getWallUploadServerResponse;

    @InjectMocks
    private VkWallUploadServerRetriever vkWallUploadServerRetriever;

    @Test
    void testRetrieve() throws ClientException, ApiException {
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(groupActor.getGroupId())
            .thenReturn(1L);
        when(vkApiClient.photos())
            .thenReturn(photos);
        when(photos.getWallUploadServer(any()))
            .thenReturn(photosGetWallUploadServerQuery);
        when(photosGetWallUploadServerQuery.groupId(anyLong()))
            .thenReturn(photosGetWallUploadServerQuery);
        when(photosGetWallUploadServerQuery.execute())
            .thenReturn(getWallUploadServerResponse);

        Mono<GetWallUploadServerResponse> result = vkWallUploadServerRetriever.retrieve(userActor, groupActor);

        create(result)
            .expectNext(getWallUploadServerResponse)
            .verifyComplete();
    }

    @Test
    void testRetrieveWhenExceptionOccurred() throws ClientException, ApiException {
        doThrow(RuntimeException.class)
            .when(photosGetWallUploadServerQuery).execute();
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(groupActor.getGroupId())
            .thenReturn(1L);
        when(vkApiClient.photos())
            .thenReturn(photos);
        when(photos.getWallUploadServer(any()))
            .thenReturn(photosGetWallUploadServerQuery);
        when(photosGetWallUploadServerQuery.groupId(anyLong()))
            .thenReturn(photosGetWallUploadServerQuery);

        Mono<GetWallUploadServerResponse> result = vkWallUploadServerRetriever.retrieve(userActor, groupActor);

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }
}
