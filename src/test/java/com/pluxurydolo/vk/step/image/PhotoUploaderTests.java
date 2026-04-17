package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.util.VkDelay;
import com.vk.api.sdk.actions.Upload;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.queries.upload.UploadPhotoQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.io.File;

import static java.net.URI.create;
import static java.time.Duration.ZERO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class PhotoUploaderTests {

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private VkDelay vkDelay;

    @Mock
    private GetWallUploadServerResponse getWallUploadServerResponse;

    @Mock
    private File file;

    @Mock
    private Upload upload;

    @Mock
    private UploadPhotoQuery uploadPhotoQuery;

    @Mock
    private PhotoUploadResponse photoUploadResponse;

    @InjectMocks
    private PhotoUploader photoUploader;

    @Test
    void testUpload() throws ClientException, ApiException {
        when(vkDelay.delay())
            .thenReturn(ZERO);
        when(getWallUploadServerResponse.getUploadUrl())
            .thenReturn(create("uri"));
        when(vkApiClient.upload())
            .thenReturn(upload);
        when(upload.photo(anyString(), any(File.class)))
            .thenReturn(uploadPhotoQuery);
        when(uploadPhotoQuery.execute())
            .thenReturn(photoUploadResponse);

        Mono<PhotoUploadResponse> result = photoUploader.upload(getWallUploadServerResponse, file);

        create(result)
            .expectNext(photoUploadResponse)
            .verifyComplete();
    }
}
