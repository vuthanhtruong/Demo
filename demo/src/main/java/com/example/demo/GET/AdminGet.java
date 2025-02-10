package com.example.demo.GET;

import com.example.demo.OOP.Admin;
import com.example.demo.OOP.Employees;
import com.example.demo.OOP.Students;
import com.example.demo.OOP.Teachers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@Transactional
public class AdminGet {
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/TrangChuAdmin")
    public String TrangChuAdmin(HttpSession session, ModelMap model) {
        Admin admin = entityManager.find(Admin.class, session.getAttribute("AdminID"));
        model.addAttribute("admin",admin);
        return "TrangChuAdmin";
    }

    @GetMapping("/DangXuatAdmin")
    public String DangXuatAdmin(HttpSession session) {
        session.invalidate(); // Hủy toàn bộ session
        return "redirect:/DangNhapAdmin";
    }
    @GetMapping("/DanhSachGiaoVien")
    public String DanhSachGiaoVien(ModelMap model) {
        List<Teachers> teachers = entityManager.createQuery("from Teachers", Teachers.class).getResultList();
        model.addAttribute("teachers",teachers);
        return "DanhSachGiaoVien";
    }
    @GetMapping("/ThemGiaoVien")
    public String ThemGiaoVien(ModelMap model) {
        List<Employees> employees = entityManager.createQuery("from Employees", Employees.class).getResultList();
        model.addAttribute("employees",employees);
        return "ThemGiaoVien";
    }
    @GetMapping("/DanhSachHocSinh")
    public String DanhSachHocSinh(ModelMap model) {
        List<Students> students = entityManager.createQuery("from Students", Students.class).getResultList();
        model.addAttribute("students",students);
        return "DanhSachHocSinh";
    }
    @GetMapping("/ThemHocSinh")
    public String ThemHocSinh(ModelMap model) {
        List<Employees> employees = entityManager.createQuery("from Employees", Employees.class).getResultList();
        model.addAttribute("employees",employees);
        return "ThemHocSinh";
    }
    @GetMapping("DanhSachNhanVien")
    public String DanhSachNhanVien(ModelMap model) {
        List<Employees> employees = entityManager.createQuery("from Employees", Employees.class).getResultList();
        model.addAttribute("employees",employees);
        return "DanhSachNhanVien";
    }

    @GetMapping("/ThemNhanVien")
    public String ThemNhanVien() {
        return "ThemNhanVien";
    }
    @GetMapping("/XoaGiaoVien/{id}")
    public String XoaGiaoVien(@PathVariable("id") long id) {
        Teachers teacher = entityManager.find(Teachers.class, id);
        if (teacher != null) {
            entityManager.remove(teacher);
        }
        return "redirect:/DanhSachGiaoVien";
    }
    @GetMapping("/XoaHocSinh/{id}")
    public String XoaHocSinh(@PathVariable("id") long id) {
        Students student = entityManager.find(Students.class, id);
        entityManager.remove(student);
        return "redirect:/DanhSachHocSinh";
    }
    @Transactional
    @GetMapping("/XoaNhanVien/{id}")
    public String XoaNhanVien(@PathVariable("id") Long id) {
        Employees employee = entityManager.find(Employees.class, id);
        if (employee != null) {
            // Cập nhật tất cả Students có EmployeeID = id thành null
            entityManager.createQuery("UPDATE Students s SET s.employee = NULL WHERE s.employee.EmployeeID = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            entityManager.createQuery("UPDATE Teachers s SET s.employee = NULL WHERE s.employee.EmployeeID = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            // Sau đó xóa nhân viên
            entityManager.remove(employee);
        }
        return "redirect:/DanhSachNhanVien";
    }
    @GetMapping("/SuaHocSinh/{id}")
    public String SuaHocSinh(ModelMap model, @PathVariable("id") Long id) {
        Students student = entityManager.find(Students.class, id);
        List<Employees> employees=entityManager.createQuery("from Employees", Employees.class).getResultList();
        model.addAttribute("student",student);
        model.addAttribute("employees",employees);
        return "SuaHocSinh";
    }
    @GetMapping("/SuaGiaoVien/{id}")
    public String SuaGiaoVien(ModelMap model, @PathVariable("id") Long id) {
        List<Employees> employees=entityManager.createQuery("from Employees", Employees.class).getResultList();
        Teachers teachers = entityManager.find(Teachers.class, id);
        model.addAttribute("teachers",teachers);
        model.addAttribute("employees",employees);
        return "SuaGiaoVien";
    }
    @GetMapping("/SuaNhanVien/{id}")
    public String SuaAdmin(ModelMap model, @PathVariable("id") Long id) {
        Employees employee = entityManager.find(Employees.class, id);
        model.addAttribute("employees",employee);
        return "SuaNhanVien";
    }
}
