package com.example.demo.POST;

import com.example.demo.OOP.Admin;
import com.example.demo.OOP.Employees;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
@Transactional
public class NhanVienPost {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/DangKyNhanVien")
    public String DangKyNhanVien(@RequestParam Long EmployeeID, @RequestParam String FirstName,
                                 @RequestParam String LastName, @RequestParam String Email,@RequestParam String PhoneNumber,
                                 @RequestParam String Password) {
        Admin admin = entityManager.find(Admin.class, 1);

        Employees employees = new Employees();
        employees.setEmployeeID(EmployeeID);
        employees.setFirstName(FirstName);
        employees.setLastName(LastName);
        employees.setEmail(Email);
        employees.setPassword(Password);
        employees.setPhoneNumber(PhoneNumber);
        employees.setAdminID(admin);

        entityManager.persist(employees);

        return "redirect:/DangNhapNhanVien"; // Chuyển hướng về trang đăng nhập
    }

}
