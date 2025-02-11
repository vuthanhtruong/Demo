package com.example.demo.OOP;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RoomTypes")
@Getter
@Setter
@NoArgsConstructor
public class RoomTypes {

    @Id
    @Column(name = "RoomTypeID")
    private Long roomTypeId;  // Không dùng @GeneratedValue vì bạn tự quản lý ID

    @Column(name = "RoomTypeName", nullable = false, length = 255)
    private String roomTypeName;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employees EmployeeID;  // Nhân viên tạo loại phòng

    public RoomTypes(Long roomTypeId, String roomTypeName, Employees employee) {
        this.roomTypeId = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.EmployeeID = employee;
    }
}
