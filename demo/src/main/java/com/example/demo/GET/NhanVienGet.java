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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;

@Controller
@RequestMapping("/")
@Transactional
public class NhanVienGet {
    @PersistenceContext
    private EntityManager entityManager;
    @GetMapping("/DangKyNhanVien")
    public String DangKyNhanVien() {
        return "DangKyNhanVien";
    }
    @GetMapping("/TrangChuNhanVien")
    public String TrangChuNhanVien(ModelMap model, HttpSession session) {
        Employees employee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        model.addAttribute("employee", employee);
        return "TrangChuNhanVien";
    }
    @GetMapping("/DangXuatNhanVien")
    public String DangXuatGiaoVien(HttpSession session) {
        session.invalidate();
        return "redirect:/DangNhapNhanVien";
    }
    @GetMapping("/DanhSachGiaoVienCuaBan")
    public String DanhSachGiaoVienCuaBan(ModelMap model) {
        List<Teachers> teachers = entityManager.createQuery("from Teachers").getResultList();
        model.addAttribute("teachers", teachers);
        return "DanhSachGiaoVienCuaBan";
    }
    @GetMapping("/ThemGiaoVienCuaBan")
    public String ThemGiaoVienCuaBan(HttpSession session, ModelMap model) {
        return "ThemGiaoVienCuaBan";
    }
    @GetMapping("/DanhSachHocSinhCuaBan")
    public String DanhSachHocSinhCuaBan(ModelMap model) {
        List<Students> students = entityManager.createQuery("from Students ").getResultList();
        model.addAttribute("students", students);
        return "DanhSachHocSinhCuaBan";
    }
    @GetMapping("/ThemHocSinhCuaBan")
    public String ThemHocSinhCuaBan(ModelMap model) {
        return "ThemHocSinhCuaBan";
    }
    @GetMapping("/DanhSachNguoiDungHeThong")
    public String DanhSachNguoiDungHeThong(HttpSession session, ModelMap model) {
        List<Employees> employee = entityManager.createQuery("from Employees ").getResultList();
        List<Teachers> teachers = entityManager.createQuery("from Teachers ").getResultList();
        List<Students> students = entityManager.createQuery("from Students ").getResultList();
        Employees employee1 = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        Admin admin = employee1.getAdminID();
        model.addAttribute("employee", employee);
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        model.addAttribute("employee", employee);
        model.addAttribute("admin", admin);
        return "DanhSachNguoiDungHeThong";
    }
    @GetMapping("/XoaGiaoVienCuaBan/{id}")
    public String XoaGiaoVienCuaBan(@PathVariable int id) {
        Teachers teachers = entityManager.find(Teachers.class, id);
        entityManager.remove(teachers);
        return "redirect:/DanhSachGiaoVienCuaBan";
    }
    @GetMapping("/XoaHocSinhCuaBan/{id}")
    public String XoaHocSinhCuaBan(@PathVariable int id) {
        Students students = entityManager.find(Students.class, id);
        entityManager.remove(students);
        return "redirect:/DanhSachHocSinhCuaBan";
    }
    @GetMapping("/SuaGiaoVienCuaBan/{id}")
    public String SuaGiaoVienCuaBan(ModelMap model, @PathVariable("id") long id) {
        Teachers teachers=entityManager.find(Teachers.class, id);
        model.addAttribute("teachers", teachers);
        return "SuaGiaoVienCuaBan";
    }
    @GetMapping("/SuaHocSinhCuaBan/{id}")
    public String SuaHocSinhCuaBan(ModelMap model, @PathVariable("id") long id) {
        Students students=entityManager.find(Students.class, id);
        model.addAttribute("students", students);
        return "SuaHocSinhCuaBan";
    }



}
