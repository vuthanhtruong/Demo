package com.example.demo.POST;
import com.example.demo.OOP.*;
import com.mysql.cj.protocol.Message;
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

import java.time.LocalDateTime;

@Controller
@RequestMapping("/")
@Transactional
public class GiaoVienPost {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/DangKyGiaoVien")
    public String dangKyGiaoVien(@RequestParam("EmployeeID") String employeeID,
                                 @RequestParam("TeacherID") String teacherID,
                                 @RequestParam("FirstName") String firstName,
                                 @RequestParam("LastName") String lastName,
                                 @RequestParam("Email") String email,
                                 @RequestParam("PhoneNumber") String phoneNumber,
                                 @RequestParam(value = "MisID", required = false) String misID,
                                 @RequestParam("Password") String password,
                                 @RequestParam("ConfirmPassword") String confirmPassword) {

        // Kiểm tra TeacherID có bắt đầu bằng "TEA" không
        if (!teacherID.startsWith("TEA")) {
            return "redirect:/DangKyGiaoVien?error=invalidTeacherID";
        }

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

        // Tạo giáo viên mới
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

    @PostMapping("/DangNhapGiaoVien")
    public String DangNhapGiaoVien(@RequestParam("TeacherID") String teacherID,
                                   @RequestParam("Password") String password,
                                   ModelMap model,
                                   HttpSession session) {
        try {
            Teachers teacher = entityManager.createQuery(
                            "SELECT t FROM Teachers t WHERE t.teacherID = :teacherID", Teachers.class)
                    .setParameter("teacherID", teacherID)
                    .getSingleResult();

            if (teacher != null && teacher.getPassword().equals(password)) {
                session.setAttribute("TeacherID", teacher.getTeacherID()); // ✅ Lưu ID vào session
                return "redirect:/TrangChuGiaoVien"; // ✅ Đăng nhập thành công
            } else {
                model.addAttribute("error", "Mã giáo viên hoặc mật khẩu không đúng!");
                return "DangNhapGiaoVien"; // ❌ Không dùng redirect để giữ thông báo lỗi
            }
        } catch (NoResultException e) {
            model.addAttribute("error", "Mã giáo viên không tồn tại!");
            return "DangNhapGiaoVien";
        }
    }
    @PostMapping("/ChiTietTinNhanCuaGiaoVien")
    public String handleMessage(@RequestParam("studentID") String studentID,
                                @RequestParam("teacherID") String teacherID,
                                @RequestParam("messageText") String messageText,
                                ModelMap model) {

        // Tạo tin nhắn với các ID của học sinh và giáo viên
        Messages message = new Messages();
        message.setSenderID(teacherID);   // Giáo viên gửi
        message.setRecipientID(studentID); // Học sinh nhận
        message.setText(messageText);
        message.setDatetime(LocalDateTime.now());

        // Lưu tin nhắn vào cơ sở dữ liệu
        entityManager.persist(message);

        // Chuyển hướng đến trang chi tiết tin nhắn của học sinh
        return "redirect:/ChiTietTinNhanCuaGiaoVien/" + studentID;
    }



}

