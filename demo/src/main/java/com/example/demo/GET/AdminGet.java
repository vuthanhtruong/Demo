package com.example.demo.GET;

import com.example.demo.OOP.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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


}
