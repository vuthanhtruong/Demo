package com.example.demo.OOP;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Employees")
@Getter
@Setter
public class Employees {

    @Id
    @Column(name = "EmployeeID")
    private Long EmployeeID;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String FirstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String LastName;

    @Column(name = "Email", nullable = false, unique = true, length = 255)
    @Email(message = "Email không hợp lệ")
    private String Email;

    @Column(name = "PhoneNumber", nullable = false, unique = true, length = 20)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
    private String PhoneNumber;

    @Column(name = "Password", nullable = false, length = 255)
    private String Password;

    @ManyToOne
    @JoinColumn(name = "AdminID", nullable = false)
    private Admin AdminID;

    // Constructor có đối số
    public Employees(Long employeeID, String firstName, String lastName, String email, String phoneNumber, String password, Admin adminID) {
        this.EmployeeID = employeeID;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
        this.Password = password;
        this.AdminID = adminID;
    }

    // Constructor không tham số (cần thiết cho JPA)
    public Employees() {
    }
}
