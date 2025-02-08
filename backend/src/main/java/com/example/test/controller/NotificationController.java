package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Notification;
import com.example.test.domain.request.notification.CreateNotificationDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.service.NotificationService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @GetMapping("")
    public ResponsePaginationDTO getNotifications(@Filter Specification<Notification> spec, Pageable pageable) {
        return this.notificationService.getNotifications(spec, pageable);
    }
    
    
}
