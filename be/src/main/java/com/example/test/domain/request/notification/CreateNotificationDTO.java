package com.example.test.domain.request.notification;

import java.util.Map;

import com.example.test.utils.constant.NotificationOptionEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateNotificationDTO {
    private String senderId;

    private NotificationOptionEnum type;

    private String content;

    private String receiverId;

    private Map<Object, Object> options;
}
