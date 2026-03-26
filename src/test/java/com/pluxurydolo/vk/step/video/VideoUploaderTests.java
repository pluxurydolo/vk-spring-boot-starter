package com.pluxurydolo.vk.step.video;

import com.pluxurydolo.vk.config.DelayConfiguration;
import com.vk.api.sdk.actions.Upload;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.objects.video.responses.UploadResponse;
import com.vk.api.sdk.queries.upload.UploadVideoQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URI;

import static java.net.URI.create;
import static java.time.Duration.ZERO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VideoUploaderTests {

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private DelayConfiguration delayConfiguration;

    @Mock
    private SaveResponse saveResponse;

    @Mock
    private File file;

    @Mock
    private Upload upload;

    @Mock
    private UploadVideoQuery uploadVideoQuery;

    @Mock
    private UploadResponse uploadResponse;

    @InjectMocks
    private VideoUploader videoUploader;

    @Test
    void testUpload() throws ClientException, ApiException {
        when(delayConfiguration.delay())
            .thenReturn(ZERO);
        when(saveResponse.getUploadUrl())
            .thenReturn(uri());
        when(vkApiClient.upload())
            .thenReturn(upload);
        when(upload.video(anyString(), any(File.class)))
            .thenReturn(uploadVideoQuery);
        when(uploadVideoQuery.execute())
            .thenReturn(uploadResponse);

        Mono<UploadResponse> result = videoUploader.upload(saveResponse, file);

        create(result)
            .expectNext(uploadResponse)
            .verifyComplete();
    }

    private static URI uri() {
        return create("uri");
    }
}
