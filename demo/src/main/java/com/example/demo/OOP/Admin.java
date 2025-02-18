package com.example.demo.OOP;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "Admin")
@Getter
@Setter
public class Admin {

    @Id
    @Column(name = "AdminID")
    private String adminID;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "Email", nullable = false, unique = true, length = 255)
    @Email(message = "Email không hợp lệ")
    private String email;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Teachers> teachers;



    @Column(name = "PhoneNumber", nullable = false, unique = true, length = 20)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
    private String PhoneNumber;
    public Admin(String adminID, String password, String firstName, String lastName, String email, String phoneNumber) {
        this.adminID = adminID;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.PhoneNumber = phoneNumber;
    }

    // Constructor không tham số (cần thiết cho JPA)
    public Admin() {
    }
}
