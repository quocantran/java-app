package com.example.test.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Comment;
import com.example.test.domain.Company;
import com.example.test.domain.User;
import com.example.test.domain.request.comment.CommentCreateDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.comment.ResponseCommentDTO;
import com.example.test.repository.CommentRepository;
import com.example.test.repository.CompanyRepository;
import com.example.test.repository.UserRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseCommentDTO createComment(CommentCreateDTO dto) throws BadRequestException {
        Long companyId;
        Long parentId = null;

        try {
            companyId = Long.parseLong(dto.getCompanyId());
            if (dto.getParentId() != null) {
                parentId = Long.parseLong(dto.getParentId());
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid ID format");
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BadRequestException("Company not found"));

        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Comment parentComment = null;
        if (parentId != null) {
            parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new BadRequestException("Parent comment not found"));
            if (!parentComment.getCompany().equals(company)) {
                throw new BadRequestException("Parent comment does not belong to this company");
            }
        }

        int rightValue = parentComment != null ? parentComment.getRight() : 1;
        if (parentComment != null) {
            commentRepository.updateRightValues(company, rightValue);
            commentRepository.updateLeftValues(company, rightValue);
        } else {
            Integer maxRightValue = commentRepository.findMaxRightValue(company);
            rightValue = maxRightValue != null ? maxRightValue + 1 : 1;
        }

        Comment comment = new Comment();
        comment.setCompany(company);
        comment.setContent(dto.getContent());
        comment.setUser(user);
        comment.setParent(parentComment);
        comment.setLeft(rightValue);
        comment.setRight(rightValue + 1);
        comment = commentRepository.save(comment);

        return convertToResponseDTO(comment);
    }

    @Transactional
    public ResponseCommentDTO deleteComment(Long commentId) throws BadRequestException {
        if (commentId instanceof Long == false) {
            throw new BadRequestException("Id must be a number");
        }

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("Comment not found"));

        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!comment.getUser().equals(user)) {
            throw new BadRequestException("You are not allowed to delete this comment");
        }

        int leftValue = comment.getLeft();
        int rightValue = comment.getRight();
        int width = rightValue - leftValue + 1;

        deleteChildComments(comment);

        commentRepository.deleteCommentsInRange(comment.getCompany(), leftValue, rightValue);
        commentRepository.updateLeftValuesAfterDelete(comment.getCompany(), rightValue, width);
        commentRepository.updateRightValuesAfterDelete(comment.getCompany(), rightValue, width);
        return convertToResponseDTO(comment);
    }

    private void deleteChildComments(Comment parentComment) {
        List<Comment> childComments = commentRepository.findByParentId(parentComment.getId());
        for (Comment childComment : childComments) {
            deleteChildComments(childComment);
        }
        commentRepository.deleteAll(childComments);
    }

    public ResponsePaginationDTO getCommentsByCompany(Long companyId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc(sort)));
        Page<Comment> commentsPage = commentRepository.findByCompanyIdAndParentIsNull(companyId, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();
        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(commentsPage.getNumber() + 1);
        meta.setPageSize(commentsPage.getSize());
        meta.setPages(commentsPage.getTotalPages());
        meta.setTotal(commentsPage.getTotalElements());

        List<ResponseCommentDTO> comments = commentsPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(comments);

        return resultPaginationDTO;
    }

    public ResponsePaginationDTO getCommentsByParent(Long parentId, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc(sort)));
        Page<Comment> commentsPage = commentRepository.findByParentId(parentId, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();
        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(commentsPage.getNumber() + 1);
        meta.setPageSize(commentsPage.getSize());
        meta.setPages(commentsPage.getTotalPages());
        meta.setTotal(commentsPage.getTotalElements());

        List<ResponseCommentDTO> comments = commentsPage.getContent().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(comments);

        return resultPaginationDTO;
    }

    private ResponseCommentDTO convertToResponseDTO(Comment comment) {
        ResponseCommentDTO dto = new ResponseCommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUser(new ResponseCommentDTO.UserComment(comment.getUser().getId(), comment.getUser().getName()));
        dto.setCompany(
                new ResponseCommentDTO.CompanyComment(comment.getCompany().getId(), comment.getCompany().getName()));
        dto.setLeft(comment.getLeft());
        dto.setRight(comment.getRight());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setParent(comment.getParent() != null ? comment.getParent().getId() : null);
        return dto;
    }
}
