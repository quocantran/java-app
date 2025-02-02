package com.example.test.domain.response.chat;

import java.time.Instant;

public class ResponseChatDTO {
    private Long id;
    private String content;
    private String fileUrl;
    private Instant createdAt, updatedAt;

    public ResponseChatDTO() {

    }

    public static class UserChat {
        private Long id;
        private String name;

        public UserChat() {

        }

        public UserChat(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    private UserChat user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserChat getUser() {
        return user;
    }

    public void setUser(UserChat user) {
        this.user = user;
    }

}
