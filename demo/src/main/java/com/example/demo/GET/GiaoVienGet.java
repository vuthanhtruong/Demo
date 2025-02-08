package com.example.demo.GET;

import com.example.demo.OOP.Employees;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@Transactional
public class GiaoVienGet {

    // Thêm EntityManager, không dùng SessionFactory nữa
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/DangKyGiaoVien")
    public String DangKyGiaoVien(ModelMap model) {
        // Sử dụng EntityManager để thực thi truy vấn
        List<Employees> employees = entityManager.createQuery("from Employees", Employees.class).getResultList();
        model.addAttribute("employees", employees);
        return "DangKyGiaoVien";
    }
}
