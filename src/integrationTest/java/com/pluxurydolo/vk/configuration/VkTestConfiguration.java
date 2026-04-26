package com.pluxurydolo.vk.configuration;

import com.vk.api.sdk.actions.Photos;
import com.vk.api.sdk.actions.Upload;
import com.vk.api.sdk.actions.Video;
import com.vk.api.sdk.actions.Wall;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveWallPhotoResponse;
import com.vk.api.sdk.objects.video.responses.SaveResponse;
import com.vk.api.sdk.objects.video.responses.UploadResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import com.vk.api.sdk.queries.photos.PhotosGetWallUploadServerQuery;
import com.vk.api.sdk.queries.photos.PhotosSaveWallPhotoQuery;
import com.vk.api.sdk.queries.upload.UploadPhotoQuery;
import com.vk.api.sdk.queries.upload.UploadVideoQuery;
import com.vk.api.sdk.queries.video.VideoSaveQuery;
import com.vk.api.sdk.queries.wall.WallPostQuery;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.net.URI;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class VkTestConfiguration {

    @Bean
    public VkApiClient vkApiClient() throws ClientException, ApiException {
        VkApiClient vkApiClient = mock(VkApiClient.class);
        Photos photos = mock(Photos.class);
        PhotosGetWallUploadServerQuery photosGetWallUploadServerQuery = mock(PhotosGetWallUploadServerQuery.class);
        GetWallUploadServerResponse getWallUploadServerResponse = mock(GetWallUploadServerResponse.class);
        Upload upload = mock(Upload.class);
        UploadPhotoQuery uploadPhotoQuery = mock(UploadPhotoQuery.class);
        PhotoUploadResponse photoUploadResponse = mock(PhotoUploadResponse.class);
        PhotosSaveWallPhotoQuery photosSaveWallPhotoQuery = mock(PhotosSaveWallPhotoQuery.class);
        SaveWallPhotoResponse saveWallPhotoResponse = mock(SaveWallPhotoResponse.class);
        Wall wall = mock(Wall.class);
        WallPostQuery wallPostQuery = mock(WallPostQuery.class);
        PostResponse postResponse = mock(PostResponse.class);
        Video video = mock(Video.class);
        VideoSaveQuery videoSaveQuery = mock(VideoSaveQuery.class);
        SaveResponse saveResponse = mock(SaveResponse.class);
        UploadVideoQuery uploadVideoQuery = mock(UploadVideoQuery.class);
        UploadResponse uploadResponse = mock(UploadResponse.class);

        when(vkApiClient.photos())
            .thenReturn(photos);
        when(photos.getWallUploadServer(any()))
            .thenReturn(photosGetWallUploadServerQuery);
        when(photosGetWallUploadServerQuery.groupId(anyLong()))
            .thenReturn(photosGetWallUploadServerQuery);
        when(photosGetWallUploadServerQuery.execute())
            .thenReturn(getWallUploadServerResponse);
        when(getWallUploadServerResponse.getUploadUrl())
            .thenReturn(URI.create(""));
        when(vkApiClient.upload())
            .thenReturn(upload);
        when(upload.photo(anyString(), any(File.class)))
            .thenReturn(uploadPhotoQuery);
        when(uploadPhotoQuery.execute())
            .thenReturn(photoUploadResponse);
        when(photoUploadResponse.getServer())
            .thenReturn(1);
        when(photoUploadResponse.getHash())
            .thenReturn("hash");
        when(photoUploadResponse.getPhoto())
            .thenReturn("photo");
        when(photos.saveWallPhoto(any()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.server(anyInt()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.hash(anyString()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.photo(anyString()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.groupId(anyLong()))
            .thenReturn(photosSaveWallPhotoQuery);
        when(photosSaveWallPhotoQuery.execute())
            .thenReturn(List.of(saveWallPhotoResponse));
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
        when(vkApiClient.video())
            .thenReturn(video);
        when(video.save(any()))
            .thenReturn(videoSaveQuery);
        when(videoSaveQuery.execute())
            .thenReturn(saveResponse);
        when(saveResponse.getUploadUrl())
            .thenReturn(URI.create(""));
        when(upload.video(anyString(), any(File.class)))
            .thenReturn(uploadVideoQuery);
        when(uploadVideoQuery.execute())
            .thenReturn(uploadResponse);
        when(uploadResponse.getVideoId())
            .thenReturn(1);

        return vkApiClient;
    }
}
