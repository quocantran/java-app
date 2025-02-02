package com.example.test.domain.request.comment;

import jakarta.validation.constraints.NotBlank;

public class CommentCreateDTO {
    @NotBlank(message = "Company ID is required")
    private String companyId;

    private String parentId;

    @NotBlank(message = "Content is required")
    private String content;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
