package com.example.test.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.test.domain.User;
import com.example.test.domain.request.user.UpdatePasswordUserDTO;
import com.example.test.domain.request.user.UpdateUserDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.ResponseString;
import com.example.test.domain.response.user.ResponseUserDTO;
import com.example.test.service.OtpService;
import com.example.test.service.UserService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.test.core.error.BadRequestException;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final OtpService otpService;

    public UserController(UserService userService, OtpService otpService) {
        this.userService = userService;
        this.otpService = otpService;
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getAllUsers(@Filter Specification<User> spec, Pageable pageable) {

        ResponsePaginationDTO users = this.userService.getAllUsers(spec, pageable);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseUserDTO> createUser(@Valid @RequestBody User user)
            throws BadRequestException {

        ResponseUserDTO createdUser = this.userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable String id, @RequestBody UpdateUserDTO user)
            throws BadRequestException {

        ResponseUserDTO updatedUser = this.userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseString> deleteUser(@PathVariable String id) throws BadRequestException {

        this.userService.deleteUser(id);
        return new ResponseEntity<>(new ResponseString("Deleted"), HttpStatus.OK);

    }

    @GetMapping("/password/forgot-password")
    public String forgotPassword(@RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "email", required = false) String email)
            throws BadRequestException {
        this.otpService.verifyOtp(email, token);

        this.userService.resetPassword(email);
        return "forgot-password.template.html";
    }

    @PatchMapping("/password/update")
    public ResponseEntity<ResponseString> updateUserPassword(@Valid @RequestBody UpdatePasswordUserDTO entity)
            throws BadRequestException {
        this.userService.updateUserPassword(entity);
        return new ResponseEntity<>(new ResponseString("Change password success"), HttpStatus.OK);
    }

}
