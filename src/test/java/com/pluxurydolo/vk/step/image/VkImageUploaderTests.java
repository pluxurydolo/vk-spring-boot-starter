package com.pluxurydolo.vk.step.image;

import com.pluxurydolo.vk.exception.image.VkImageUploadException;
import com.pluxurydolo.vk.properties.VkApiProperties;
import com.pluxurydolo.vk.io.FileUtils;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static reactor.test.StepVerifier.create;

@ExtendWith(MockitoExtension.class)
class VkImageUploaderTests {

    @Mock
    private VkApiClient vkApiClient;

    @Mock
    private VkApiProperties vkApiProperties;

    @Mock
    private FileUtils fileUtils;

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
    private VkImageUploader vkImageUploader;

    @Test
    void testUpload() throws ClientException, ApiException {
        when(getWallUploadServerResponse.getUploadUrl())
            .thenReturn(create("uri"));
        when(fileUtils.createTempFile(anyString(), anyString(), any()))
            .thenReturn(Mono.just(file));
        when(fileUtils.deleteTempFile(any()))
            .thenReturn(Mono.just(true));
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(vkApiClient.upload())
            .thenReturn(upload);
        when(upload.photo(anyString(), any(File.class)))
            .thenReturn(uploadPhotoQuery);
        when(uploadPhotoQuery.execute())
            .thenReturn(photoUploadResponse);

        Mono<PhotoUploadResponse> result = vkImageUploader.upload(getWallUploadServerResponse, bytes());

        create(result)
            .expectNext(photoUploadResponse)
            .verifyComplete();
    }

    @Test
    void testUploadWhenExceptionOccurred() throws ClientException, ApiException {
        doThrow(RuntimeException.class)
            .when(uploadPhotoQuery).execute();
        when(getWallUploadServerResponse.getUploadUrl())
            .thenReturn(create("uri"));
        when(fileUtils.createTempFile(anyString(), anyString(), any()))
            .thenReturn(Mono.just(file));
        when(vkApiProperties.delay())
            .thenReturn(ZERO);
        when(vkApiClient.upload())
            .thenReturn(upload);
        when(upload.photo(anyString(), any(File.class)))
            .thenReturn(uploadPhotoQuery);

        Mono<PhotoUploadResponse> result = vkImageUploader.upload(getWallUploadServerResponse, bytes());

        create(result)
            .verifyErrorMatches(throwable -> throwable.getClass().equals(VkImageUploadException.class));
    }

    private static byte[] bytes() {
        return new byte[]{};
    }
}
