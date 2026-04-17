package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.util.VkDelay;
import com.vk.api.sdk.actions.Wall;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.SaveWallPhotoResponse;
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
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class WallPosterTests {

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private VkDelay vkDelay;

    @Mock
    private SaveWallPhotoResponse saveWallPhotoResponse;

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
    private WallPoster wallPoster;

    @Test
    void testPost() throws ClientException, ApiException {
        when(vkDelay.delay())
            .thenReturn(ZERO);
        when(saveWallPhotoResponse.getOwnerId())
            .thenReturn(1L);
        when(saveWallPhotoResponse.getId())
            .thenReturn(1);
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

        Mono<PostResponse> result = wallPoster.post(saveWallPhotoResponse, userActor, groupActor, "text");

        create(result)
            .expectNext(postResponse)
            .verifyComplete();
    }
}
