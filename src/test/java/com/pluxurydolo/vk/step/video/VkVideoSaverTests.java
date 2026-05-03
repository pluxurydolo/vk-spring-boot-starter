package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.exception.video.VkVideoSaveException;
import com.pluxurydolo.vk.properties.VkApiProperties;
import com.vk.api.sdk.actions.Video;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.queries.video.VideoSaveQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static java.time.Duration.ZERO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkVideoSaverTests {

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private VkApiProperties vkApiProperties;

    @Mock
    private UserActor userActor;

    @Mock
    private Video video;

    @Mock
    private VideoSaveQuery videoSaveQuery;

    @Mock
    private SaveResponse saveResponse;

    @InjectMocks
    private VkVideoSaver vkVideoSaver;

    @Test
    void testSave() throws ClientException, ApiException {
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(vkApiClient.video())
            .thenReturn(video);
        when(video.save(any()))
            .thenReturn(videoSaveQuery);
        when(videoSaveQuery.execute())
            .thenReturn(saveResponse);

        Mono<SaveResponse> result = vkVideoSaver.save(userActor);

        create(result)
            .expectNext(saveResponse)
            .verifyComplete();
    }

    @Test
    void testSaveWhenExceptionOccurred() throws ClientException, ApiException {
        doThrow(RuntimeException.class)
            .when(videoSaveQuery).execute();
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(vkApiClient.video())
            .thenReturn(video);
        when(video.save(any()))
            .thenReturn(videoSaveQuery);

        Mono<SaveResponse> result = vkVideoSaver.save(userActor);

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(VkVideoSaveException.class));
    }
}
