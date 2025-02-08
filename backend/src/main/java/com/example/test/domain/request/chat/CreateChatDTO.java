package com.example.test.domain.request.chat;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CreateChatDTO {
    private String content;
    private String fileUrl;

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
}
