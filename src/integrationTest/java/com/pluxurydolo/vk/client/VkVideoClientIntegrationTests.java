package com.pluxurydolo.vk.client;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.core.spi.AppenderAttachable;
import com.pluxurydolo.vk.base.AbstractIntegrationTests;
import com.pluxurydolo.vk.dto.PostVideoRequest;
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

class VkVideoClientIntegrationTests extends AbstractIntegrationTests {
    private static final AppenderAttachable<ILoggingEvent> LOGGER =
        (Logger) getLogger(VkVideoClient.class);

    @Autowired
    private VkVideoClient vkVideoClient;

    @Test
    void testSendVideoToGroup() {
        List<ILoggingEvent> logs = listAppender().list;

        vkVideoClient.sendVideoToGroup(postVideoRequest())
            .subscribe();

        await().atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                assertThat(logs)
                    .hasSize(1);

                assertThat(logs.getFirst().getFormattedMessage())
                    .isEqualTo("dyhn [vk-starter] Видео успешно опубликовано");
            });
    }

    private static ListAppender<ILoggingEvent> listAppender() {
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        LOGGER.addAppender(listAppender);
        return listAppender;
    }

    private static PostVideoRequest postVideoRequest() {
        byte[] bytes = {};
        UserActor userActor = mock(UserActor.class);
        GroupActor groupActor = mock(GroupActor.class);

        when(userActor.getId())
            .thenReturn(1L);
        when(groupActor.getGroupId())
            .thenReturn(1L);

        return new PostVideoRequest(bytes, "caption", userActor, groupActor);
    }
}
