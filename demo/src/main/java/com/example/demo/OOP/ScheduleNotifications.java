package com.example.demo.OOP;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ScheduleNotifications")
@Getter
@Setter
public class ScheduleNotifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ScheduleNotificationID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Employees sender;

    @Column(name = "Member", nullable = false, length = 255)
    private String member;

    @Column(name = "RoomID", nullable = false, length = 50)
    private String roomId;  // Không ràng buộc FK để có thể chứa cả OnlineRooms và Rooms

    @Column(name = "Message", nullable = false, columnDefinition = "TEXT")
    private String message;
}


