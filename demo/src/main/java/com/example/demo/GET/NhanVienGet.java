package com.example.demo.GET;

import com.example.demo.OOP.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
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
    public String DanhSachGiaoVienCuaBan(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        List<Teachers> teachers = entityManager.createQuery("from Teachers").getResultList();
        model.addAttribute("teachers", teachers);
        return "DanhSachGiaoVienCuaBan";
    }
    @GetMapping("/ThemGiaoVienCuaBan")
    public String ThemGiaoVienCuaBan(HttpSession session, ModelMap model) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        return "ThemGiaoVienCuaBan";
    }
    @GetMapping("/DanhSachHocSinhCuaBan")
    public String DanhSachHocSinhCuaBan(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        List<Students> students = entityManager.createQuery("from Students ").getResultList();
        model.addAttribute("students", students);
        return "DanhSachHocSinhCuaBan";
    }
    @GetMapping("/ThemHocSinhCuaBan")
    public String ThemHocSinhCuaBan(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        return "ThemHocSinhCuaBan";
    }
    @GetMapping("/DanhSachNguoiDungHeThong")
    public String DanhSachNguoiDungHeThong(HttpSession session, ModelMap model) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
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
    public String XoaGiaoVienCuaBan(@PathVariable int id, ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Teachers teachers = entityManager.find(Teachers.class, id);
        entityManager.remove(teachers);
        return "redirect:/DanhSachGiaoVienCuaBan";
    }
    @GetMapping("/XoaHocSinhCuaBan/{id}")
    public String XoaHocSinhCuaBan(@PathVariable int id, ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Students students = entityManager.find(Students.class, id);
        entityManager.remove(students);
        return "redirect:/DanhSachHocSinhCuaBan";
    }
    @GetMapping("/SuaGiaoVienCuaBan/{id}")
    public String SuaGiaoVienCuaBan(ModelMap model, @PathVariable("id") long id, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Teachers teachers=entityManager.find(Teachers.class, id);
        model.addAttribute("teachers", teachers);
        return "SuaGiaoVienCuaBan";
    }
    @GetMapping("/SuaHocSinhCuaBan/{id}")
    public String SuaHocSinhCuaBan(ModelMap model, @PathVariable("id") long id, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Students students=entityManager.find(Students.class, id);
        model.addAttribute("students", students);
        return "SuaHocSinhCuaBan";
    }
    @GetMapping("/DanhSachPhongHoc")
    public String DanhSachPhongHoc(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        List<Rooms> rooms = entityManager.createQuery("from Rooms").getResultList();
        model.addAttribute("rooms", rooms);
        return "DanhSachPhongHoc";
    }
    @GetMapping("/ThemLoaiPhongHoc")
    public String ThemLoaiPhongHoc(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        List<RoomTypes> roomTypes = entityManager.createQuery("from RoomTypes ").getResultList();
        model.addAttribute("rooms", roomTypes);
        return "ThemLoaiPhongHoc";
    }
    @GetMapping("/ThemPhongHoc")
    public String ThemPhongHoc(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        List<RoomTypes> roomTypes = entityManager.createQuery("from RoomTypes ").getResultList();
        model.addAttribute("roomTypes", roomTypes);
        return "ThemPhongHoc";
    }
    @GetMapping("/SuaPhongHoc/{id}")
    public String SuaPhongHoc(ModelMap model, @PathVariable("id") long id, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Rooms room = entityManager.find(Rooms.class, id);

        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }

        List<RoomTypes> roomTypes = entityManager.createQuery("from RoomTypes", RoomTypes.class).getResultList();

        model.addAttribute("room", room);
        model.addAttribute("roomTypes", roomTypes);

        return "SuaPhongHoc";
    }

    @GetMapping("/XoaLoaiPhongHoc/{id}")
    @Transactional
    public String XoaLoaiPhongHoc(@PathVariable("id") long id, ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        // Tìm loại phòng học cần xóa
        RoomTypes roomType = entityManager.find(RoomTypes.class, id);
        if (roomType == null) {
            return "redirect:/ThemLoaiPhongHoc";
        }

        // Cập nhật các phòng học có loại phòng này thành NULL
        entityManager.createQuery("UPDATE Rooms r SET r.roomTypeID = NULL WHERE r.roomTypeID = :roomType")
                .setParameter("roomType", roomType)
                .executeUpdate();

        // Xóa loại phòng sau khi cập nhật
        entityManager.remove(roomType);

        return "redirect:/ThemLoaiPhongHoc";
    }
    @GetMapping("/SuaLoaiPhongHoc/{id}")
    public String SuaLoaiPhongHoc(ModelMap model, @PathVariable("id") long id, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        RoomTypes roomType = entityManager.find(RoomTypes.class, id);
        model.addAttribute("roomType", roomType);
        return "SuaLoaiPhongHoc";
    }
    @GetMapping("/XoaPhongHoc/{id}")
    public String XoaPhongHoc(ModelMap model, @PathVariable("id") long id) {
        Rooms room = entityManager.find(Rooms.class, id);
        List<ClassroomDetails> existingDetails = entityManager.createQuery(
                        "FROM ClassroomDetails WHERE room.roomId = :roomId", ClassroomDetails.class)
                .setParameter("roomId", id)
                .getResultList();
        for (ClassroomDetails detail : existingDetails) {
            entityManager.remove(detail);
        }
        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }

        try {
            entityManager.remove(room);
            return "redirect:/DanhSachPhongHoc?success=RoomDeleted";
        } catch (Exception e) {
            return "redirect:/DanhSachPhongHoc?error=DeleteFailed";
        }

    }

    @GetMapping("/BoTriLopHoc")
    public String BoTriLopHoc(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        List<Rooms> rooms = entityManager.createQuery("from Rooms").getResultList();
        model.addAttribute("rooms", rooms);
        return "BoTriLopHoc";
    }
    @GetMapping("/ChiTietLopHoc/{id}")
    public String ChiTietLopHoc(ModelMap model, @PathVariable("id") int id, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Rooms room = entityManager.find(Rooms.class, id);
        model.addAttribute("room", room);

        List<Teachers> teachers = entityManager.createQuery("from Teachers", Teachers.class).getResultList();
        List<Students> students = entityManager.createQuery("from Students", Students.class).getResultList();

        List<ClassroomDetails> classroomDetails = entityManager.createQuery(
                        "from ClassroomDetails where room.roomId = :roomId", ClassroomDetails.class)
                .setParameter("roomId", id)
                .getResultList();
        Set<Teachers> uniqueTeachers = new LinkedHashSet<>();
        Set<Students> uniqueStudents = new LinkedHashSet<>();

        for (ClassroomDetails detail : classroomDetails) {
            if (detail.getTeacher() != null) {
                uniqueTeachers.add(detail.getTeacher());
            }
            if (detail.getStudent() != null) {
                uniqueStudents.add(detail.getStudent());
            }
        }

        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        model.addAttribute("classroomDetails", classroomDetails);
        model.addAttribute("uniqueTeachers", uniqueTeachers);
        model.addAttribute("uniqueStudents", uniqueStudents);

        return "ChiTietLopHoc";
    }
    @GetMapping("/XoaGiaoVienTrongLop")
    public String XoaGiaoVienTrongLop(@RequestParam Long teacherId, @RequestParam Long roomId, ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        List<ClassroomDetails> details = entityManager.createQuery(
                        "FROM ClassroomDetails WHERE room.roomId = :roomId AND teacher.teacherID = :teacherId", ClassroomDetails.class)
                .setParameter("roomId", roomId)
                .setParameter("teacherId", teacherId)
                .getResultList();

        for (ClassroomDetails detail : details) {
            entityManager.remove(detail);
        }

        return "redirect:/ChiTietLopHoc/" + roomId + "?success=deleted";
    }
    @GetMapping("/XoaHocSinhTrongLop")
    public String XoaHocSinhTrongLop(@RequestParam Long studentId, @RequestParam Long roomId, ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        List<ClassroomDetails> details = entityManager.createQuery(
                        "FROM ClassroomDetails WHERE room.roomId = :roomId AND student.studentID = :studentId", ClassroomDetails.class)
                .setParameter("roomId", roomId)
                .setParameter("studentId", studentId)
                .getResultList();

        for (ClassroomDetails detail : details) {
            entityManager.remove(detail);
        }

        return "redirect:/ChiTietLopHoc/" + roomId + "?success=deleted";
    }
    @PostMapping("/CapNhatLichTrinh")
    public String capNhatLichTrinh(@RequestParam("roomId") Long roomId,
                                   @RequestParam("startTime") String startTimeStr,
                                   @RequestParam("endTime") String endTimeStr,
                                   RedirectAttributes redirectAttributes, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        try {
            Rooms room = entityManager.find(Rooms.class, roomId);
            if (room == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng!");
                return "redirect:/BoTriLopHoc";
            }

            LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr);

            if (endTime.isBefore(startTime)) {
                redirectAttributes.addFlashAttribute("error", "Thời gian kết thúc không thể trước thời gian bắt đầu.");
                return "redirect:/BoTriLopHoc";
            }

            room.setStartTime(startTime);
            room.setEndTime(endTime);
            entityManager.merge(room); // Cập nhật dữ liệu

            redirectAttributes.addFlashAttribute("success", "Lịch trình được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật lịch trình.");
        }
        return "redirect:/BoTriLopHoc";
    }

}
