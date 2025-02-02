package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.Response;
import com.example.test.core.error.BadRequestException;
import com.example.test.domain.request.otp.OtpDTO;
import com.example.test.service.OtpService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/otps")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("")
    public ResponseEntity<OtpDTO> createOtp(@RequestParam("email") String email) throws BadRequestException {
        OtpDTO otp = this.otpService.createOtp(email);
        return new ResponseEntity<>(otp, HttpStatus.CREATED);
    }
}
