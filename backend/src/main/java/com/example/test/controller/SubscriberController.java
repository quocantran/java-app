package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Subscriber;
import com.example.test.domain.User;
import com.example.test.domain.request.subscriber.CreateSubscriberDTO;
import com.example.test.service.SubscriberService;
import com.example.test.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {
    private final SubscriberService subscriberService;
    private final UserService userService;

    public SubscriberController(SubscriberService subscriberService, UserService userService) {
        this.subscriberService = subscriberService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<CreateSubscriberDTO> create(@Valid @RequestBody CreateSubscriberDTO entity)
            throws BadRequestException {

        User user = userService.getUserByEmail(entity.getEmail());
        if (user == null) {
            throw new BadRequestException("User not found");
        }

        if (user.getName().equals(entity.getName()) == false) {
            throw new BadRequestException("User not found");
        }

        CreateSubscriberDTO subscriber = subscriberService.create(entity);

        return new ResponseEntity<>(subscriber, HttpStatus.CREATED);
    }

    @GetMapping("/by-email")
    public ResponseEntity<Subscriber> getSubscriberByEmail(@RequestParam("email") String email)
            throws BadRequestException {
        return new ResponseEntity<>(this.subscriberService.findByEmail(email), HttpStatus.OK);
    }

}
