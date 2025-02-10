package com.example.demo.OOP;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Teachers")
@Getter
@Setter
public class Teachers {

    @Id
    @Column(name = "TeacherID")
    private Long teacherID;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "Email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "PhoneNumber", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "MIS_ID", length = 100)
    private String misID;

    @ManyToOne
    @JoinColumn(name = "EmployeeID", nullable = true)
    private Employees employee; // Liên kết với bảng Employee (có thể là NULL)

    @ManyToOne
    @JoinColumn(name = "AdminID", nullable = false)
    private Admin admin; // Khóa ngoại liên kết đến bảng Admin (bắt buộc)

    // Constructor không tham số (cần thiết cho JPA)
    public Teachers() {
    }

    // Constructor có tham số
    public Teachers(String password, String firstName, String lastName, String email, String phoneNumber, String misID, Employees employee, Admin admin) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.misID = misID;
        this.employee = employee;
        this.admin = admin;
    }
}
