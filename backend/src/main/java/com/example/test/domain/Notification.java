package com.example.test.domain;

import java.time.Instant;
import java.util.Map;

import com.example.test.config.JpaConverterJson;
import com.example.test.utils.constant.NotificationOptionEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private NotificationOptionEnum type;

    @Column(name = "content")
    private String content;

    @Column(name = "receiver_id")
    private String receiverId;

    @Column(name = "options")
    @Convert(converter = JpaConverterJson.class)
    private Map<Object, Object> options;

    private Boolean active;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_at")
    private Instant createdAt;


    @PrePersist
    public void prePersist() {
        this.active = true;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
