package com.example.demo.POST;

import com.example.demo.OOP.Admin;
import com.example.demo.OOP.Employees;
import com.example.demo.OOP.Students;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@Transactional
public class StudentPost {
    @PersistenceContext
    private EntityManager entityManager;
    @PostMapping("/DangKyHocSinh")
    public String DangKyHocSinh(@RequestParam("EmployeeID") Long employeeID,
                                @RequestParam("StudentID") Long studentID,
                                @RequestParam("FirstName") String firstName,
                                @RequestParam("LastName") String lastName,
                                @RequestParam("Email") String email,
                                @RequestParam("PhoneNumber") String phoneNumber,
                                @RequestParam(value = "MisID", required = false) String misID,
                                @RequestParam("Password") String password,
                                @RequestParam("ConfirmPassword") String ConfirmPassword) {
        Admin admin = entityManager.find(Admin.class, 1);
        Employees employee = entityManager.find(Employees.class, employeeID);

        if (employee == null) {
            return "redirect:/DangKyHocSinh?error=invalid_employee";
        }

        // Kiểm tra xem student có tồn tại không
        Students existingStudent = entityManager.find(Students.class, studentID);
        if (existingStudent != null) {
            return "redirect:/DangKyHocSinh?error=student_exists";
        }

        Students student = new Students();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setPassword(password);
        student.setStudentID(studentID);
        student.setAdmin(admin);
        student.setEmployee(employee);
        student.setMisId(misID);

        entityManager.merge(student);

        return "redirect:/DangNhapHocSinh";
    }
    @PostMapping("/DangNhapHocSinh")
    public String DangNhapHocSinh(@RequestParam("studentID") long studentID,
                                  @RequestParam("password") String password,
                                  ModelMap model,
                                  HttpSession session) {
        try {
            Students student = entityManager.find(Students.class, studentID);

            if (student != null && student.getPassword().equals(password)) {
                session.setAttribute("StudentID", student.getStudentID());
                return "redirect:/TrangChuHocSinh";
            } else {
                model.addAttribute("error", "Mã học sinh hoặc mật khẩu không đúng!");
                return "redirect:/DangNhapHocSinh";
            }
        } catch (NoResultException e) {
            model.addAttribute("error", "Mã học sinh không tồn tại!");
            return "redirect:/DangNhapHocSinh";
        }
    }


}
