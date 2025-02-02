package com.example.test.domain.request.otp;

public class OtpDTO {
    private String email, otp;

    public OtpDTO(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public OtpDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
