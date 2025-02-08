package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.service.MailService;
import com.example.test.service.SubscriberService;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/mails")
public class MailController {

    private final MailService mailService;
    private final SubscriberService subscriberService;

    public MailController(MailService mailService, SubscriberService subscriberService) {
        this.mailService = mailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("")
    @Transactional
    @Scheduled(cron = "0 0 0 * * MON")
    public ResponseEntity<String> sendMail() {
        this.subscriberService.sendEmailToSubscribers();
        return ResponseEntity.ok("Mail sent");
    }

}
