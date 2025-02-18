package com.example.demo.OOP;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ClassroomDetails")
@Getter
@Setter
@NoArgsConstructor
public class ClassroomDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClassroomDetailsID")
    private Long classroomDetailsId;

    @Column(name = "RoomID", nullable = false, length = 36)
    private String roomId; // Lưu ID của Rooms hoặc OnlineRooms như một giá trị String bình thường

    @Column(name = "MemberID", nullable = false, length = 36)
    private String memberId; // Lưu ID của Teacher hoặc Student như một String bình thường

    // Constructor có tham số
    public ClassroomDetails(String roomId, String memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }
}
