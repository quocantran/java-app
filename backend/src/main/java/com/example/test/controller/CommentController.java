package com.example.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.request.comment.CommentCreateDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.comment.ResponseCommentDTO;
import com.example.test.service.CommentService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("")
    public ResponseEntity<ResponseCommentDTO> createComment(@RequestBody @Valid CommentCreateDTO dto)
            throws BadRequestException {
        ResponseCommentDTO responseDTO = commentService.createComment(dto);
        return ResponseEntity.status(201).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseCommentDTO> deleteComment(@PathVariable Long id) throws BadRequestException {

        return ResponseEntity.ok(commentService.deleteComment(id));
    }

    @GetMapping("/by-company/{companyId}")
    public ResponseEntity<ResponsePaginationDTO> getCommentsByCompany(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort) {
        ResponsePaginationDTO comments = commentService.getCommentsByCompany(companyId, page, size, sort);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/by-parent/{parentId}")
    public ResponseEntity<ResponsePaginationDTO> getCommentsByParent(
            @PathVariable Long parentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort) {
        ResponsePaginationDTO comments = commentService.getCommentsByParent(parentId, page, size, sort);
        return ResponseEntity.ok(comments);
    }

}
