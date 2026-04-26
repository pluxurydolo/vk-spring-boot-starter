package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.properties.VkApiProperties;
import com.vk.api.sdk.actions.Wall;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.video.responses.UploadResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static java.time.Duration.ZERO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkVideoPosterTests {

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private VkApiProperties vkApiProperties;

    @Mock
    private UploadResponse uploadResponse;

    @Mock
    private UserActor userActor;

    @Mock
    private GroupActor groupActor;

    @Mock
    private Wall wall;

    @Mock
    private WallPostQuery wallPostQuery;

    @Mock
    private PostResponse postResponse;

    @InjectMocks
    private VkVideoPoster vkVideoPoster;

    @Test
    void testPost() throws ClientException, ApiException {
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(uploadResponse.getVideoId())
            .thenReturn(1);
        when(userActor.getId())
            .thenReturn(1L);
        when(groupActor.getGroupId())
            .thenReturn(1L);
        when(vkApiClient.wall())
            .thenReturn(wall);
        when(wall.post(any()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.ownerId(anyLong()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.message(anyString()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.attachments(anyString()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.fromGroup(anyBoolean()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.execute())
            .thenReturn(postResponse);

        Mono<PostResponse> result = vkVideoPoster.post(uploadResponse, userActor, groupActor, "text");

        create(result)
            .expectNext(postResponse)
            .verifyComplete();
    }

    @Test
    void testPostWhenExceptionOccurred() throws ClientException, ApiException {
        doThrow(RuntimeException.class)
            .when(wallPostQuery).execute();
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(uploadResponse.getVideoId())
            .thenReturn(1);
        when(userActor.getId())
            .thenReturn(1L);
        when(groupActor.getGroupId())
            .thenReturn(1L);
        when(vkApiClient.wall())
            .thenReturn(wall);
        when(wall.post(any()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.ownerId(anyLong()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.message(anyString()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.attachments(anyString()))
            .thenReturn(wallPostQuery);
        when(wallPostQuery.fromGroup(anyBoolean()))
            .thenReturn(wallPostQuery);

        Mono<PostResponse> result = vkVideoPoster.post(uploadResponse, userActor, groupActor, "text");

        create(result)
            .expectError(RuntimeException.class)
            .verify();
    }
}
