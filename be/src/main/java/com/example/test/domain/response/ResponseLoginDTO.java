package com.example.test.domain.response;

import java.security.Permission;

import com.example.test.domain.response.user.ResponseUserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseLoginDTO {
    @JsonProperty("access_token")
    private String accessToken;
    private ResponseUserDTO user;

    public ResponseLoginDTO() {
    }

    public ResponseLoginDTO(String accessToken, ResponseUserDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ResponseUserDTO getUser() {
        return user;
    }

    public void setUser(ResponseUserDTO user) {
        this.user = user;
    }

    public static class GetAccount {
        private ResponseUserDTO user;

        public ResponseUserDTO getUser() {
            return user;
        }

        public void setUser(ResponseUserDTO user) {
            this.user = user;
        }

        public GetAccount(ResponseUserDTO user) {
            this.user = user;
        }

        public GetAccount() {

        }

    }
}
