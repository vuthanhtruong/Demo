package com.example.demo.POST;

import com.example.demo.OOP.Admin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
