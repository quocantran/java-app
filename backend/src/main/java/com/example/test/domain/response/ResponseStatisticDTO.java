package com.example.test.domain.response;

public class ResponseStatisticDTO {
    private Long totalUsers, totalJobs, totalCompanies;

    // one week ago data
    private Long totalUsersPast, totalJobsPast, totalCompaniesPast;

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(Long totalJobs) {
        this.totalJobs = totalJobs;
    }

    public Long getTotalCompanies() {
        return totalCompanies;
    }

    public void setTotalCompanies(Long totalCompanies) {
        this.totalCompanies = totalCompanies;
    }

    public Long getTotalUsersPast() {
        return totalUsersPast;
    }

    public void setTotalUsersPast(Long totalUsersPast) {
        this.totalUsersPast = totalUsersPast;
    }

    public Long getTotalJobsPast() {
        return totalJobsPast;
    }

    public void setTotalJobsPast(Long totalJobsPast) {
        this.totalJobsPast = totalJobsPast;
    }

    public Long getTotalCompaniesPast() {
        return totalCompaniesPast;
    }

    public void setTotalCompaniesPast(Long totalCompaniesPast) {
        this.totalCompaniesPast = totalCompaniesPast;
    }

}
