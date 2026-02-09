package com.example.hmrback.model.response;

import com.example.hmrback.model.User;
import com.example.hmrback.model.UserBasicInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
        String token,
        User user,
        @JsonProperty("need_registration")
        boolean needRegistration,
        @JsonProperty("user_basic_info")
        UserBasicInfo userBasicInfo
) {}

