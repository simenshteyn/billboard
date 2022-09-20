package com.robot.billboard.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTToken {
    @JsonProperty("email")
    private String userEmail;
    @JsonProperty("access_token")
    private String accessToken;
}
