package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Chat;
import com.example.test.domain.request.chat.CreateChatDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.chat.ResponseChatDTO;
import com.example.test.service.ChatService;
import com.turkraft.springfilter.boot.Filter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseChatDTO> create(@RequestBody CreateChatDTO entity) throws BadRequestException {

        return new ResponseEntity<>(this.chatService.createChat(entity), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getChats(
            @RequestParam(value = "lastPage", required = false, defaultValue = "false") boolean lastPage,
            @Filter Specification<Chat> spec, Pageable pageable) {
        return new ResponseEntity<>(this.chatService.getChats(spec, pageable, lastPage), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseChatDTO> delete(@PathVariable Long id) throws BadRequestException {
        return new ResponseEntity<>(this.chatService.delete(id), HttpStatus.OK);
    }

}
