package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostVideoRequest;
import com.pluxurydolo.vk.exception.video.VkVideoPublishException;
import com.pluxurydolo.vk.step.video.VkVideoPublisher;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkVideoClientTests {

    @Mock
    private VkVideoPublisher vkVideoPublisher;

    @InjectMocks
    private VkVideoClient vkVideoClient;

    @Test
    void testSendVideoToGroup() {
        when(vkVideoPublisher.send(any()))
            .thenReturn(Mono.just(""));

        Mono<String> result = vkVideoClient.sendVideoToGroup(postVideoRequest());

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testSendVideoToGroupWhenExceptionOccurred() {
        when(vkVideoPublisher.send(any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = vkVideoClient.sendVideoToGroup(postVideoRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(VkVideoPublishException.class));
    }

    private static PostVideoRequest postVideoRequest() {
        byte[] video = {};
        String caption = "caption";
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);
        return new PostVideoRequest(video, caption, userActor, groupActor);
    }
}
