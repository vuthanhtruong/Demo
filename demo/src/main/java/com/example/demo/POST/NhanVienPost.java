package com.example.demo.POST;

import com.example.demo.OOP.Admin;
import com.example.demo.OOP.Employees;
import com.example.demo.OOP.Students;
import com.example.demo.OOP.Teachers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    @PostMapping("/DangNhapNhanVien")
    public String DangNhapNhanVien(@RequestParam("EmployeeID") Long employeeID,
                                   @RequestParam("Password") String password,
                                   HttpSession session,
                                   ModelMap model) {
        try {
            Employees employee = entityManager.createQuery(
                            "SELECT e FROM Employees e WHERE e.EmployeeID = :employeeID", Employees.class)
                    .setParameter("employeeID", employeeID)
                    .getSingleResult();

            if (employee != null && employee.getPassword().equals(password)) {
                session.setAttribute("EmployeeID", employee.getEmployeeID());
                return "redirect:/TrangChuNhanVien";
            } else {
                model.addAttribute("error", "Mã nhân viên hoặc mật khẩu không đúng!");
                return "redirect:/DangNhapNhanVien";
            }
        } catch (NoResultException e) {
            model.addAttribute("error", "Mã nhân viên không tồn tại!");
            return "redirect:/DangNhapNhanVien";
        }
    }

    @PostMapping("/ThemGiaoVienCuaBan")
    public String themGiaoVienCuaBan(
            @RequestParam("teacherID") Long teacherID,
            @RequestParam("password") String password,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "misID", required = false) String misID,
            HttpSession session,
            ModelMap model) {

        if (session.getAttribute("EmployeeID") == null) {
            model.addAttribute("error", "Bạn chưa đăng nhập hoặc không có quyền thêm giáo viên.");
            return "ThemGiaoVienCuaBan";
        }

        Employees employee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        if (employee == null) {
            model.addAttribute("error", "Nhân viên không hợp lệ.");
            return "ThemGiaoVienCuaBan";
        }

        Admin admin = employee.getAdminID();
        if (admin == null) {
            model.addAttribute("error", "Không thể xác định Admin.");
            return "ThemGiaoVienCuaBan";
        }

        // Kiểm tra trùng lặp email
        if (!entityManager.createQuery("SELECT COUNT(t) FROM Teachers t WHERE t.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult().equals(0L)) {
            model.addAttribute("error", "Email đã tồn tại.");
            return "ThemGiaoVienCuaBan";
        }

        // Kiểm tra trùng lặp TeacherID
        if (entityManager.find(Teachers.class, teacherID) != null) {
            model.addAttribute("error", "TeacherID đã tồn tại.");
            return "ThemGiaoVienCuaBan";
        }

        Teachers teachers = new Teachers();
        teachers.setTeacherID(teacherID);
        teachers.setFirstName(firstName);
        teachers.setLastName(lastName);
        teachers.setEmail(email);
        teachers.setPhoneNumber(phoneNumber);
        teachers.setMisID(misID);
        teachers.setPassword(password);
        teachers.setEmployee(employee);
        teachers.setAdmin(admin);

        entityManager.persist(teachers);
        return "redirect:/DanhSachGiaoVienCuaBan";
    }
    @PostMapping("/ThemHocSinhCuaBan")
    public String ThemHocSinhCuaBan(
            @RequestParam("studentID") Long studentID,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "misID", required = false) String misId,
            HttpSession session,
            ModelMap model) {

        // Kiểm tra Employee từ session
        Object employeeID = session.getAttribute("EmployeeID");
        if (employeeID == null) {
            model.addAttribute("error", "Bạn chưa đăng nhập!");
            return "ThemHocSinhCuaBan"; // Quay lại form với thông báo lỗi
        }

        Employees employee = entityManager.find(Employees.class, employeeID);
        if (employee == null) {
            model.addAttribute("error", "Không tìm thấy nhân viên với ID: " + employeeID);
            return "ThemHocSinhCuaBan";
        }

        Admin admin = employee.getAdminID(); // Lấy Admin của Employee

        // Kiểm tra trùng lặp StudentID
        Students existingStudent = entityManager.find(Students.class, studentID);
        if (existingStudent != null) {
            model.addAttribute("error", "Mã học sinh đã tồn tại!");
            return "ThemHocSinhCuaBan";
        }

        // Kiểm tra trùng lặp Email
        List<Students> studentsWithEmail = entityManager.createQuery(
                        "SELECT s FROM Students s WHERE s.email = :email", Students.class)
                .setParameter("email", email)
                .getResultList();
        if (!studentsWithEmail.isEmpty()) {
            model.addAttribute("error", "Email này đã được sử dụng!");
            return "ThemHocSinhCuaBan";
        }
        Students student = new Students();
        student.setStudentID(studentID);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setPassword(password); // Lưu mật khẩu đã mã hóa
        student.setMisId(misId);
        student.setEmployee(employee);
        student.setAdmin(admin);

        // Lưu vào database
        entityManager.persist(student);

        return "redirect:/DanhSachHocSinhCuaBan";
    }
    @PostMapping("/SuaGiaoVienCuaBan")
    public String SuaGiaoVienCuaBan(@RequestParam("teacherID") Long id,
                                    @RequestParam("email") String email,
                                    @RequestParam("phoneNumber") String phoneNumber,
                                    @RequestParam("lastName") String lastName,
                                    @RequestParam("firstName") String firstName) {
        Teachers teacher = entityManager.find(Teachers.class, id);
        if (teacher != null) {
            teacher.setEmail(email);
            teacher.setPhoneNumber(phoneNumber);
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            entityManager.merge(teacher);
        }
        return "redirect:/DanhSachGiaoVienCuaBan";
    }
    @PostMapping("/SuaHocSinhCuaBan")
    public String SuaHocSinhCuaBan(@RequestParam("studentID") Long studentID,
                                   @RequestParam("firstName") String firstName,
                                   @RequestParam("lastName") String lastName,
                                   @RequestParam("email") String email,
                                   @RequestParam("phoneNumber") String phoneNumber,
                                   @RequestParam(value = "misId", required = false) String misId,
                                   HttpSession session) {
        // Tìm học sinh theo ID
        Students student = entityManager.find(Students.class, studentID);

        if (student == null) {
            return "redirect:/DanhSachHocSinhCuaBan?error=notfound";
        }

        // Kiểm tra quyền chỉnh sửa (giáo viên phải là người phụ trách)
        Long employeeID = (Long) session.getAttribute("EmployeeID");
        if (employeeID == null || !employeeID.equals(student.getEmployee().getEmployeeID())) {
            return "redirect:/DanhSachHocSinhCuaBan?error=unauthorized";
        }

        // Cập nhật thông tin học sinh
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhoneNumber(phoneNumber);
        student.setMisId(misId);

        entityManager.merge(student);  // Lưu vào database

        return "redirect:/DanhSachHocSinhCuaBan?success=updated";
    }




}
