package com.example.test.domain.response.company;

import java.util.List;

public class ResponseCompanyUserDTO {
    private Long id;
    private String name, logo, address, description;

    public static class UserFollowed {
        private Long id;
        private String name;

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

    private List<UserFollowed> usersFollowed;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserFollowed> getUsersFollowed() {
        return usersFollowed;
    }

    public void setUsersFollowed(List<UserFollowed> usersFollowed) {
        this.usersFollowed = usersFollowed;
    }

}
