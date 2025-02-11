package com.example.demo.OOP;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Rooms")
@Getter
@Setter
@NoArgsConstructor
public class Rooms {

    @Id
    @Column(name = "RoomID")
    private Long roomId;  // Không dùng @GeneratedValue vì bạn tự quản lý ID

    @Column(name = "RoomName", nullable = false, length = 255)
    private String roomName;

    @ManyToOne
    @JoinColumn(name = "RoomTypeID", nullable = true)
    private RoomTypes roomTypeID;  // Liên kết với RoomType (ManyToOne)

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employees EmployeeID;  // Liên kết với Employee (ManyToOne)

    public Rooms(Long roomId, String roomName, RoomTypes roomTypeID, Employees employeeID) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomTypeID = roomTypeID;
        this.EmployeeID = employeeID;
    }
}
