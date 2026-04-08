package com.pluxurydolo.vk.util;

import com.pluxurydolo.vk.base.AbstractIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.test.StepVerifier.create;

class FileUtilsTests extends AbstractIntegrationTests {

    @Autowired
    private FileUtils fileUtils;

    @Test
    void testCreateTempFile() {
        Mono<File> result = fileUtils.createTempFile("blah", ".txt", new byte[]{1, 2, 3});

        create(result)
            .expectNextMatches(file -> {
                Path filePath = file.toPath();
                long fileLength = file.length();

                assertThat(filePath)
                    .exists();
                assertThat(fileLength)
                    .isEqualTo(3L);

                return true;
            })
            .verifyComplete();
    }
}
