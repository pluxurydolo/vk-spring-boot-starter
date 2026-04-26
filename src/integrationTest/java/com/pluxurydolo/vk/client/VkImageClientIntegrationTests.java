package com.pluxurydolo.vk.client;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.pluxurydolo.vk.base.AbstractIntegrationTests;
import com.pluxurydolo.vk.dto.PostImageRequest;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.slf4j.LoggerFactory.getLogger;

class VkImageClientIntegrationTests extends AbstractIntegrationTests {
    private static final AppenderAttachable<ILoggingEvent> LOGGER =
        (Logger) getLogger(VkImageClient.class);

    @Autowired
    private VkImageClient vkImageClient;

    @Test
    void testSendImageToGroup() {
        List<ILoggingEvent> logs = listAppender().list;

        vkImageClient.sendImageToGroup(postImageRequest())
            .subscribe();

        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                assertThat(logs)
                    .hasSize(1);

                assertThat(logs.getFirst().getFormattedMessage())
                    .isEqualTo("cepb [vk-starter] Изображение успешно опубликовано");
            });
    }

    private static ListAppender<ILoggingEvent> listAppender() {
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        LOGGER.addAppender(listAppender);
        return listAppender;
    }

    private static PostImageRequest postImageRequest() {
        byte[] bytes = {};
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);

        when(userActor.getId())
            .thenReturn(1L);
        when(groupActor.getGroupId())
            .thenReturn(1L);

        return new PostImageRequest(bytes, "caption", userActor, groupActor);
    }
}
