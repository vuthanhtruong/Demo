package com.example.demo.POST;
import com.example.demo.OOP.Admin;
import com.example.demo.OOP.Employees;
import com.example.demo.OOP.Teachers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@Transactional
public class GiaoVienPost {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/DangKyGiaoVien")
    public String dangKyGiaoVien(@RequestParam("EmployeeID") Long employeeID,
                                 @RequestParam("TeacherID") Long teacherID,
                                 @RequestParam("FirstName") String firstName,
                                 @RequestParam("LastName") String lastName,
                                 @RequestParam("Email") String email,
                                 @RequestParam("PhoneNumber") String phoneNumber,
                                 @RequestParam(value = "MisID", required = false) String misID,
                                 @RequestParam("Password") String password,
                                 @RequestParam("ConfirmPassword") String confirmPassword) {

        // Kiểm tra mật khẩu có khớp không
        if (!password.equals(confirmPassword)) {
            return "redirect:/DangKyGiaoVien?error=passwordsNotMatch";
        }

        Admin admin = entityManager.find(Admin.class, 1);
        Employees employee = entityManager.find(Employees.class, employeeID);

        // Kiểm tra nếu TeacherID đã tồn tại
        if (entityManager.find(Teachers.class, teacherID) != null) {
            return "redirect:/DangKyGiaoVien?error=teacherIDExists";
        }

        Teachers giaoVien = new Teachers();
        giaoVien.setEmployee(employee);
        giaoVien.setAdmin(admin);
        giaoVien.setTeacherID(teacherID);
        giaoVien.setFirstName(firstName);
        giaoVien.setLastName(lastName);
        giaoVien.setEmail(email);
        giaoVien.setPhoneNumber(phoneNumber);
        giaoVien.setMisID(misID);
        giaoVien.setPassword(password);
        entityManager.persist(giaoVien);

        return "redirect:/DangNhapGiaoVien";
    }
}

