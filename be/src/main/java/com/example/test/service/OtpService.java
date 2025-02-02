package com.example.test.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.User;
import com.example.test.domain.request.otp.OtpDTO;

@Service
public class OtpService {

    private final UserService userService;

    private final RedisTemplate<String, Object> redisTemplate;

    private final MailService emailService;

    @Value("${backend.url}")
    private String backendUrl;

    public OtpService(UserService userService, RedisTemplate<String, Object> redisTemplate, MailService emailService) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
    }

    public Boolean verifyOtp(String email, String otp) {
        OtpDTO cur = (OtpDTO) redisTemplate.opsForValue().get(email);

        if (cur == null) {
            return false;
        }

        if (!cur.getOtp().equals(otp)) {
            return false;
        }

        redisTemplate.delete(email);

        return true;
    }

    public OtpDTO createOtp(String email) throws BadRequestException {
        User usr = this.userService.getUserByEmail(email);

        if (usr == null) {
            throw new BadRequestException("User not found");
        }

        OtpDTO cur = (OtpDTO) redisTemplate.opsForValue().get(email);

        if (cur != null) {
            throw new BadRequestException("Otp already sent");
        }

        String otp = UUID.randomUUID().toString();
        OtpDTO otpData = new OtpDTO(email, otp);

        redisTemplate.opsForValue().set(email, otpData, Duration.ofMinutes(10));

        String linkVerify = backendUrl + "/api/v1/users/password/forgot-password?token=" + otp + "&email=" + email;
        emailService.sendOtpEmail(email, linkVerify);

        OtpDTO otpResponse = new OtpDTO(email, otp);

        return otpResponse;
    }
}
