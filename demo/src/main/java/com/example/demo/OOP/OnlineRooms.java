package com.example.demo.OOP;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "OnlineRooms")
@Getter
@Setter
@NoArgsConstructor
public class OnlineRooms {

    @Id
    @Column(name = "RoomID")
    private String roomId;  // Không sử dụng @GeneratedValue vì ID có thể được đặt thủ công

    @Column(name = "RoomName", nullable = false, length = 255)
    private String roomName;

    @Column(name = "Link", nullable = true, length = 500)
    private String link;

    @Column(name = "Status", nullable = true)
    private Boolean status; // true: hoạt động, false: không hoạt động

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = true, foreignKey = @ForeignKey(name = "FK_OnlineRooms_Employee"))
    private Employees employee;  // Liên kết với Employee

    @Column(name = "StartTime", nullable = true)
    private LocalDateTime startTime;

    @Column(name = "EndTime", nullable = true)
    private LocalDateTime endTime;

    // Constructor có tham số
    public OnlineRooms(String roomId, String roomName, String link, Boolean status, Employees employee, LocalDateTime startTime, LocalDateTime endTime) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.link = link;
        this.status = status;
        this.employee = employee;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
