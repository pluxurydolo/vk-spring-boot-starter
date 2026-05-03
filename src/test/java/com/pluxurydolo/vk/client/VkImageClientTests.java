package com.pluxurydolo.vk.client;

import com.pluxurydolo.vk.dto.PostImageRequest;
import com.pluxurydolo.vk.exception.image.VkImagePublishException;
import com.pluxurydolo.vk.step.image.VkImagePublisher;
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
class VkImageClientTests {

    @Mock
    private VkImagePublisher vkImagePublisher;

    @InjectMocks
    private VkImageClient vkImageClient;

    @Test
    void testSendImageToGroup() {
        when(vkImagePublisher.publish(any()))
            .thenReturn(Mono.just(""));

        Mono<String> result = vkImageClient.sendImageToGroup(postImageRequest());

        create(result)
            .expectNext("")
            .verifyComplete();
    }

    @Test
    void testSendImageToGroupWhenExceptionOccurred() {
        when(vkImagePublisher.publish(any()))
            .thenReturn(Mono.error(new RuntimeException()));

        Mono<String> result = vkImageClient.sendImageToGroup(postImageRequest());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(VkImagePublishException.class));
    }

    private static PostImageRequest postImageRequest() {
        byte[] image = {};
        String caption = "caption";
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);
        return new PostImageRequest(image, caption, userActor, groupActor);
    }
}

