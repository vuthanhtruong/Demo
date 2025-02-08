package com.example.demo.POST;

import com.example.demo.OOP.Admin;
import com.example.demo.OOP.Employees;
import com.example.demo.OOP.Students;
import com.example.demo.OOP.Teachers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@Transactional
public class AdminPost {
    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/DangNhapAdmin")
    public String DangNhapAdmin(@RequestParam("AdminID") long AdminID,
                                @RequestParam("PasswordAdmin") String PasswordAdmin,
                                HttpSession session) {
        Admin admin = entityManager.find(Admin.class, AdminID);

        if (admin == null) {
            return "redirect:/DangNhapAdmin";
        }

        if (!PasswordAdmin.equals(admin.getPassword())) {
            return "redirect:/DangNhapAdmin";
        }

        // Lưu AdminID vào session
        session.setAttribute("AdminID", AdminID);
        return "redirect:/TrangChuAdmin";
    }
    @PostMapping("/ThemGiaoVien")
    public String ThemGiaoVien(@RequestParam("EmployeeID") Long employeeID,
                               @RequestParam("TeacherID") Long teacherID,
                               @RequestParam("FirstName") String firstName,
                               @RequestParam("LastName") String lastName,
                               @RequestParam("Email") String email,
                               @RequestParam("PhoneNumber") String phoneNumber,
                               @RequestParam(value = "MisID", required = false) String misID,
                               @RequestParam("Password") String password, HttpSession session) {


        Admin admin = entityManager.find(Admin.class, session.getAttribute("AdminID"));
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
        return "redirect:/DanhSachGiaoVien";
    }
    @PostMapping("/ThemHocSinh")
    public String ThemHocSinh(@RequestParam("EmployeeID") Long employeeID,
                                @RequestParam("StudentID") Long studentID,
                                @RequestParam("FirstName") String firstName,
                                @RequestParam("LastName") String lastName,
                                @RequestParam("Email") String email,
                                @RequestParam("PhoneNumber") String phoneNumber,
                                @RequestParam(value = "MisID", required = false) String misID,
                                @RequestParam("Password") String password, HttpSession session) {
        Admin admin = entityManager.find(Admin.class, session.getAttribute("AdminID"));
        Employees employee = entityManager.find(Employees.class, employeeID);


        Students existingStudent = entityManager.find(Students.class, studentID);


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

        return "redirect:/DanhSachHocSinh";
    }
    @PostMapping("/ThemNhanVien")
    public String ThemNhanVien(@RequestParam Long EmployeeID, @RequestParam String FirstName,
                                 @RequestParam String LastName, @RequestParam String Email,@RequestParam String PhoneNumber,
                                 @RequestParam String Password, HttpSession session) {
        Admin admin = entityManager.find(Admin.class, session.getAttribute("AdminID"));

        Employees employees = new Employees();
        employees.setEmployeeID(EmployeeID);
        employees.setFirstName(FirstName);
        employees.setLastName(LastName);
        employees.setEmail(Email);
        employees.setPassword(Password);
        employees.setPhoneNumber(PhoneNumber);
        employees.setAdminID(admin);

        entityManager.persist(employees);

        return "redirect:/DanhSachNhanVien";
    }
    @PostMapping("/SuaHocSinh/{id}")
    public String SuaHocSinh(@PathVariable("id") Long id,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("email") String email,
                             @RequestParam("phoneNumber") String phoneNumber,
                             @RequestParam("misId") String misId,
                             @RequestParam(value = "employeeID", required = false) Long employeeID) {

        // Tìm học sinh theo ID
        Students student = entityManager.find(Students.class, id);
        if (student == null) {
            throw new EntityNotFoundException("Không tìm thấy học sinh với ID: " + id);
        }

        // Cập nhật thông tin học sinh
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setMisId(misId);

        // Cập nhật nhân viên phụ trách nếu có
        if (employeeID != null) {
            Employees employee = entityManager.find(Employees.class, employeeID);
            student.setEmployee(employee);
        } else {
            student.setEmployee(null); // Cho phép bỏ chọn nhân viên
        }

        // Lưu thay đổi
        entityManager.merge(student);

        return "redirect:/DanhSachHocSinh";
    }
    @PostMapping("/SuaGiaoVien/{id}")
    public String SuaGiaoVien(@PathVariable("id") Long id,
                              @RequestParam("firstName") String firstName,
                              @RequestParam("lastName") String lastName,
                              @RequestParam("email") String email,
                              @RequestParam("phoneNumber") String phoneNumber,
                              @RequestParam(value = "misID", required = false) String misID,
                              @RequestParam(value = "employeeID", required = false) Long employeeID) {
        // Tìm giáo viên theo ID
        Teachers teacher = entityManager.find(Teachers.class, id);
        if (teacher == null) {
            return "redirect:/DanhSachGiaoVien"; // Nếu không tìm thấy thì quay về danh sách
        }

        // Cập nhật thông tin giáo viên
        teacher.setFirstName(firstName);
        teacher.setLastName(lastName);
        teacher.setEmail(email);
        teacher.setPhoneNumber(phoneNumber);

        if (misID != null) {
            teacher.setMisID(misID);
        }

        // Nếu có chọn nhân viên phụ trách, cập nhật thông tin nhân viên
        if (employeeID != null) {
            Employees employee = entityManager.find(Employees.class, employeeID);
            teacher.setEmployee(employee);
        } else {
            teacher.setEmployee(null);
        }

        // Lưu thay đổi vào database
        entityManager.merge(teacher);

        return "redirect:/DanhSachGiaoVien";
    }
    @PostMapping("/SuaNhanVien/{id}")
    public String CapNhatNhanVien(@PathVariable("id") Long id,
                                  @ModelAttribute Employees updatedEmployee) {
        Employees existingEmployee = entityManager.find(Employees.class, id);
        if (existingEmployee != null) {
            existingEmployee.setFirstName(updatedEmployee.getFirstName());
            existingEmployee.setLastName(updatedEmployee.getLastName());
            existingEmployee.setEmail(updatedEmployee.getEmail());
            existingEmployee.setPhoneNumber(updatedEmployee.getPhoneNumber());

            entityManager.merge(existingEmployee);
        }
        return "redirect:/DanhSachNhanVien";
    }

}
