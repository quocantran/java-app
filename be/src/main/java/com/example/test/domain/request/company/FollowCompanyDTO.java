package com.example.test.domain.request.company;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FollowCompanyDTO {
    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

}
