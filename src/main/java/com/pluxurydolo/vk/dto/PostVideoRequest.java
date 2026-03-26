package com.pluxurydolo.vk.dto;

import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;

public record PostVideoRequest(
    byte[] video,
    String caption,
    UserActor userActor,
    GroupActor groupActor
) {
}
