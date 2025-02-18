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
    private String employeeID;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "Email", nullable = false, unique = true, length = 255)
    @Email(message = "Email không hợp lệ")
    private String email;

    @Column(name = "PhoneNumber", nullable = false, unique = true, length = 20)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @ManyToOne
    @JoinColumn(name = "AdminID", nullable = false)
    private Admin adminID;

    // Constructor có đối số
    public Employees(String employeeID, String firstName, String lastName, String email, String phoneNumber, String password, Admin adminID) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.adminID = adminID;
    }

    // Constructor không tham số (cần thiết cho JPA)
    public Employees() {
    }
}
