package com.pluxurydolo.vk.dto;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;

public record PostImageRequest(
    byte[] image,
    String caption,
    UserActor userActor,
    GroupActor groupActor
) {
}
