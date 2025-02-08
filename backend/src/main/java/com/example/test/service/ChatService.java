package com.example.test.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Chat;
import com.example.test.domain.Company;
import com.example.test.domain.User;
import com.example.test.domain.request.chat.CreateChatDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationCompanyDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.chat.ResponseChatDTO;
import com.example.test.repository.ChatRepository;
import com.example.test.repository.UserRepository;
import com.google.gson.reflect.TypeToken;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public ChatService(ChatRepository chatRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseChatDTO createChat(CreateChatDTO createChatDTO) throws BadRequestException {
        if (createChatDTO.getContent() == null && createChatDTO.getFileUrl() == null) {
            throw new BadRequestException("Content or fileUrl is required");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        Chat chat = new Chat();
        chat.setContent(createChatDTO.getContent());
        chat.setFileUrl(createChatDTO.getFileUrl());
        chat.setUser(user);

        ResponseChatDTO responseChatDTO = modelMapper.map(chat, ResponseChatDTO.class);
        responseChatDTO.setCreatedAt(Instant.now());
        responseChatDTO.setUpdatedAt(Instant.now());

        chatRepository.save(chat);

        return responseChatDTO;
    }

    public ResponsePaginationDTO getChats(Specification<Chat> spec, Pageable pageable, Boolean lastPage) {

        Page<Chat> chat;
        if (lastPage) {
            // Sắp xếp theo thời gian tạo (createdAt) giảm dần
            Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
            Pageable sortedPageable = PageRequest.of(0, pageable != null ? pageable.getPageSize() : 50, sort);

            // Lấy trang đầu tiên để tính tổng số trang
            Page<Chat> firstPage = this.chatRepository.findAll(spec, sortedPageable);
            int totalPages = firstPage.getTotalPages();

            // Tạo Pageable với trang cuối cùng
            Pageable lastPageable = PageRequest.of(totalPages - 1, pageable != null ? pageable.getPageSize() : 50,
                    sort);
            chat = this.chatRepository.findAll(spec, lastPageable);
        } else {
            chat = this.chatRepository.findAll(spec, pageable);
        }

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(chat.getNumber() + 1);
        meta.setPageSize(chat.getSize());

        meta.setPages(chat.getTotalPages());
        meta.setTotal(chat.getTotalElements());

        List<ResponseChatDTO> chats = chat.getContent().stream().map(chatEntity -> {
            ResponseChatDTO chatDTO = modelMapper.map(chatEntity, ResponseChatDTO.class);
            return chatDTO;
        }).collect(Collectors.toList());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(chats);

        return resultPaginationDTO;
    }

    public ResponseChatDTO delete(Long id) throws BadRequestException {
        Chat chat = chatRepository.findById(id).orElseThrow(() -> new BadRequestException("message not found"));

        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        if (!chat.getUser().equals(user)) {
            throw new BadRequestException("You are not allowed to delete this chat");
        }

        chatRepository.delete(chat);

        return modelMapper.map(chat, ResponseChatDTO.class);
    }
}
