package com.example.test.domain.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdatePasswordUserDTO {

    @NotBlank(message = "password is required")
    private String password;
    @NotBlank(message = "newPassword is required")
    private String newPassword;
    @NotBlank(message = "repeatedPassword is required")
    private String repeatedPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

}
