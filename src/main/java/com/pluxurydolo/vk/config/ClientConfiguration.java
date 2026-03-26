package com.pluxurydolo.vk.config;

import com.pluxurydolo.vk.client.VkImageClient;
import com.pluxurydolo.vk.client.VkVideoClient;
import com.pluxurydolo.vk.step.image.PhotoUploader;
import com.pluxurydolo.vk.step.image.WallPhotoSaver;
import com.pluxurydolo.vk.step.image.WallPoster;
import com.pluxurydolo.vk.step.image.WallUploadServerRetriever;
import com.pluxurydolo.vk.step.video.VideoPoster;
import com.pluxurydolo.vk.step.video.VideoSaver;
import com.pluxurydolo.vk.step.video.VideoUploader;
import com.pluxurydolo.vk.util.FileUtils;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.vk.api.sdk.httpclient.HttpTransportClient.getInstance;

@Configuration
public class ClientConfiguration {

    @Bean
    public VkImageClient vkPhotoClient(
        WallUploadServerRetriever wallUploadServerRetriever,
        PhotoUploader photoUploader,
        WallPhotoSaver wallPhotoSaver,
        WallPoster wallPoster,
        FileUtils fileUtils
    ) {
        return new VkImageClient(wallUploadServerRetriever, photoUploader, wallPhotoSaver, wallPoster, fileUtils);
    }

    @Bean
    public VkVideoClient vkVideoClient(
        VideoSaver videoSaver,
        VideoUploader videoUploader,
        VideoPoster videoPoster,
        FileUtils fileUtils
    ) {
        return new VkVideoClient(videoSaver, videoUploader, videoPoster, fileUtils);
    }

    @Bean
    public VkApiClient vkApiClient() {
        TransportClient transportClient = getInstance();
        return new VkApiClient(transportClient);
    }

    @Bean
    public FileUtils fileUtils() {
        return new FileUtils();
    }
}
