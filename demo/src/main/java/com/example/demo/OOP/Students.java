package com.example.demo.OOP;

import jakarta.persistence.*;

@Entity
@Table(name = "Students")
public class Students {

    @Id
    @Column(name = "StudentID")
    private String studentID;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "PhoneNumber", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "MIS_ID", length = 50)
    private String misId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", nullable = true)
    private Employees employee;  // Assuming you have an Employee entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AdminID", nullable = false)
    private Admin admin;  // Assuming you have an Admin entity

    // Constructors, Getters, Setters
    public Students() {}

    public Students(String password, String firstName, String lastName, String email, String phoneNumber, String misId, Employees employee, Admin admin) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.misId = misId;
        this.employee = employee;
        this.admin = admin;
    }
    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Getters and setters
    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMisId() {
        return misId;
    }

    public void setMisId(String misId) {
        this.misId = misId;
    }

    public Employees getEmployee() {
        return employee;
    }

    public void setEmployee(Employees employee) {
        this.employee = employee;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}

