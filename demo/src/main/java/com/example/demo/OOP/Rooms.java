package com.example.demo.OOP;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Rooms")
@Getter
@Setter
@NoArgsConstructor
public class Rooms {

    @Id
    @Column(name = "RoomID")
    private String roomId;  // Không sử dụng @GeneratedValue để ID có thể được đặt thủ công

    @Column(name = "RoomName", nullable = false, length = 255)
    private String roomName;

    @Column(name = "Address", nullable = false, length = 500)
    private String address;

    @Column(name = "Status", nullable = false)
    private Boolean status; // true: hoạt động, false: không hoạt động

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = false, foreignKey = @ForeignKey(name = "FK_Rooms_Employee"))
    private Employees employee;  // Liên kết với Employee

    @Column(name = "StartTime", nullable = true)
    private LocalDateTime startTime;

    @Column(name = "EndTime", nullable = true)
    private LocalDateTime endTime;

    // Constructor có tham số
    public Rooms(String roomId, String roomName, String address, Boolean status, Employees employee, LocalDateTime startTime, LocalDateTime endTime) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.address = address;
        this.status = status;
        this.employee = employee;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
