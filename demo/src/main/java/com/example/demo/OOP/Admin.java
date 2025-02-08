package com.example.demo.OOP;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Admin")
@Getter
@Setter
public class Admin {

    @Id
    @Column(name = "AdminID")
    private Long AdminID;

    @Column(name = "Password", nullable = false, length = 255)
    private String Password;

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
    public Admin(Long adminID, String password, String firstName, String lastName, String email, String phoneNumber) {
        this.AdminID = adminID;
        this.Password = password;
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }

    // Constructor không tham số (cần thiết cho JPA)
    public Admin() {
    }
}
