package com.example.test.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Company;
import com.example.test.domain.Notification;
import com.example.test.domain.Permission;
import com.example.test.domain.User;
import com.example.test.domain.request.notification.CreateNotificationDTO;
import com.example.test.domain.request.notification.GetNotificationDTO;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.repository.CompanyRepository;
import com.example.test.repository.NotificationRepository;
import com.example.test.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public NotificationService(NotificationRepository notificationRepository, CompanyRepository companyRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
    }

    public ResponsePaginationDTO getNotifications(Specification<Notification> spec, Pageable pageable) {
        String emailUser = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(emailUser);

        Specification<Notification> receiverSpec = (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("receiverId"), user.getId());


        Specification<Notification> combinedSpec = spec.and(receiverSpec);

        Page<Notification> notifications = this.notificationRepository.findAll(combinedSpec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();
        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setCurrent(notifications.getNumber() + 1);
        meta.setPageSize(notifications.getSize());
        meta.setPages(notifications.getTotalPages());
        meta.setTotal(notifications.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        Page<GetNotificationDTO> getNotificationDTOs = notifications.map(notification -> modelMapper.map(notification, GetNotificationDTO.class));
        resultPaginationDTO.setResult(getNotificationDTOs.getContent());

        return resultPaginationDTO;

    }

    @Async
    @Transactional
    public void create(CreateNotificationDTO createNotificationDTO){
        switch (createNotificationDTO.getType()) {
            case CREATE_JOB:
                Company company = companyRepository.findById(Long.parseLong(createNotificationDTO.getSenderId()));
                
                List<User> users = company.getUsersFollowed();

                for (User user : users) {
                    Notification notification = new Notification();
                    notification.setSenderId(company.getId() + "");
                    notification.setReceiverId(user.getId() + "");
                    notification.setContent(createNotificationDTO.getContent());
                    notification.setType(createNotificationDTO.getType());
                    notification.setOptions(createNotificationDTO.getOptions() != null ? createNotificationDTO.getOptions() : null);
                    notificationRepository.save(notification);
                }
                
                break;
        
            default:
                break;
                
        }

    }
}
