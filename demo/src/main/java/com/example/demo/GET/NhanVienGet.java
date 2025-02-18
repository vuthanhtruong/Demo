package com.example.demo.GET;

import com.example.demo.OOP.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/")
@Transactional
public class NhanVienGet {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JavaMailSender mailSender; // Không khai báo lại ở nơi khác


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
    public String XoaGiaoVienCuaBan(@PathVariable String id, ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Teachers teachers = entityManager.find(Teachers.class, id);
        entityManager.remove(teachers);
        return "redirect:/DanhSachGiaoVienCuaBan";
    }
    @GetMapping("/XoaHocSinhCuaBan/{id}")
    public String XoaHocSinhCuaBan(@PathVariable String id, ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Students students = entityManager.find(Students.class, id);
        entityManager.remove(students);
        return "redirect:/DanhSachHocSinhCuaBan";
    }
    @GetMapping("/SuaGiaoVienCuaBan/{id}")
    public String SuaGiaoVienCuaBan(ModelMap model, @PathVariable("id") String id, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Teachers teachers=entityManager.find(Teachers.class, id);
        model.addAttribute("teachers", teachers);
        return "SuaGiaoVienCuaBan";
    }
    @GetMapping("/SuaHocSinhCuaBan/{id}")
    public String SuaHocSinhCuaBan(ModelMap model, @PathVariable("id") String id, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Students students=entityManager.find(Students.class, id);
        model.addAttribute("students", students);
        return "SuaHocSinhCuaBan";
    }
    @GetMapping("/DanhSachPhongHoc")
    public String DanhSachPhongHoc(ModelMap model, HttpSession session) {
        if (session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Lấy danh sách phòng học offline
        List<Rooms> offlineRooms = entityManager.createQuery("from Rooms", Rooms.class).getResultList();

        // Lấy danh sách phòng học online
        List<OnlineRooms> onlineRooms = entityManager.createQuery("from OnlineRooms", OnlineRooms.class).getResultList();


        model.addAttribute("rooms", offlineRooms);
        model.addAttribute("roomsonline", onlineRooms);
        return "DanhSachPhongHoc";
    }

    @GetMapping("/ThemPhongHoc")
    public String ThemPhongHoc(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        return "ThemPhongHoc";
    }
    @GetMapping("/ThemPhongHocOnline")
    public String ThemPhongHocOnline(ModelMap model, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        return "ThemPhongHocOnline";
    }
    @GetMapping("/SuaPhongHocOffline/{id}")
    public String SuaPhongHoc(ModelMap model, @PathVariable("id") String id, HttpSession session) {
        if(session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }
        Rooms room = entityManager.find(Rooms.class, id);

        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }
        model.addAttribute("room", room);

        return "SuaPhongHoc";
    }
    @GetMapping("/SuaPhongHocOnline/{id}")
    public String SuaPhongHocOnline(@PathVariable("id") String roomId, ModelMap model, HttpSession session) {
        if (session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Tìm phòng học online theo ID
        OnlineRooms room = entityManager.find(OnlineRooms.class, roomId);
        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }

        model.addAttribute("room", room);
        return "SuaPhongHocOnline";  // Trả về trang chỉnh sửa phòng online
    }

    @Transactional
    @GetMapping("/XoaPhongHocOffline/{id}")
    public String XoaPhongHocOffline(@PathVariable("id") String id) {
        Rooms room = entityManager.find(Rooms.class, id);

        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }

        try {
            // Xóa tất cả ClassroomDetails liên quan đến phòng học này
            int deletedDetails = entityManager.createQuery("DELETE FROM ClassroomDetails c WHERE c.roomId = :roomId")
                    .setParameter("roomId", id)
                    .executeUpdate();

            System.out.println("Deleted " + deletedDetails + " ClassroomDetails records.");

            // Xóa phòng học
            entityManager.remove(room);

            return "redirect:/DanhSachPhongHoc?success=RoomDeleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/DanhSachPhongHoc?error=DeleteFailed";
        }
    }

    @Transactional
    @GetMapping("/XoaPhongHocOnline/{id}")
    public String XoaPhongHocOnline(@PathVariable("id") String id) {
        OnlineRooms room = entityManager.find(OnlineRooms.class, id);

        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }

        try {
            // Xóa tất cả ClassroomDetails liên quan đến phòng học online này
            int deletedDetails = entityManager.createQuery("DELETE FROM ClassroomDetails c WHERE c.roomId = :roomId")
                    .setParameter("roomId", id)
                    .executeUpdate();

            System.out.println("Deleted " + deletedDetails + " ClassroomDetails records.");

            // Xóa phòng học online
            entityManager.remove(room);

            return "redirect:/DanhSachPhongHoc?success=RoomDeleted";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/DanhSachPhongHoc?error=DeleteFailed";
        }
    }
    @GetMapping("/BoTriLopHoc")
    public String BoTriLopHoc(ModelMap model, HttpSession session) {
        if (session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Lấy danh sách phòng học offline
        List<Rooms> offlineRooms = entityManager.createQuery("FROM Rooms", Rooms.class).getResultList();

        // Lấy danh sách phòng học online
        List<OnlineRooms> onlineRooms = entityManager.createQuery("FROM OnlineRooms", OnlineRooms.class).getResultList();

        // Đưa từng danh sách vào model
        model.addAttribute("offlineRooms", offlineRooms);
        model.addAttribute("onlineRooms", onlineRooms);

        return "BoTriLopHoc";
    }
    @GetMapping("/ChiTietLopHoc/{id}")
    public String ChiTietLopHoc(ModelMap model, @PathVariable("id") String id, HttpSession session) {
        // Check if EmployeeID exists in session
        if (session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }

        boolean isOnline = id.startsWith("ONL");

        // Find the room based on type (Online or Offline)
        Object room;
        if (isOnline) {
            room = entityManager.find(OnlineRooms.class, id);
        } else {
            room = entityManager.find(Rooms.class, id);
        }

        // If the room doesn't exist, redirect to the list page with an error
        if (room == null) {
            return "redirect:/DanhSachLopHoc?error=notfound";
        }

        model.addAttribute("room", room);

        // Fetch classroom details (teacher and student assignments to this room)
        List<ClassroomDetails> classroomDetails = entityManager.createQuery(
                        "FROM ClassroomDetails WHERE roomId = :roomId", ClassroomDetails.class)
                .setParameter("roomId", id)
                .getResultList();

        // Lists to hold teacher and student IDs
        List<String> teacherIds = new ArrayList<>();
        List<String> studentIds = new ArrayList<>();

        // Iterate through classroom details and classify members as teacher or student
        for (ClassroomDetails detail : classroomDetails) {
            if (detail.getMemberId() != null) {
                if (detail.getMemberId().startsWith("TEA")) {
                    teacherIds.add(detail.getMemberId());
                } else if (detail.getMemberId().startsWith("STU")) {
                    studentIds.add(detail.getMemberId());
                }
            }
        }

        // Fetch teachers and students in the class by their IDs
        List<Teachers> teachersInClass = teacherIds.isEmpty() ? List.of() :
                entityManager.createQuery("FROM Teachers WHERE teacherID IN :teacherIds", Teachers.class)
                        .setParameter("teacherIds", teacherIds)
                        .getResultList();

        List<Students> studentsInClass = studentIds.isEmpty() ? List.of() :
                entityManager.createQuery("FROM Students WHERE studentID IN :studentIds", Students.class)
                        .setParameter("studentIds", studentIds)
                        .getResultList();

        // Fetch all teachers and students in the system
        List<Teachers> allTeachers = entityManager.createQuery("FROM Teachers", Teachers.class).getResultList();
        List<Students> allStudents = entityManager.createQuery("FROM Students", Students.class).getResultList();

        // Add attributes to model for the view
        model.addAttribute("teachersInClass", teachersInClass);
        model.addAttribute("studentsInClass", studentsInClass);
        model.addAttribute("allTeachers", allTeachers);
        model.addAttribute("allStudents", allStudents);
        model.addAttribute("classroomDetails", classroomDetails);

        return "ChiTietLopHoc";
    }

    @Transactional
    @GetMapping("/XoaGiaoVienTrongLop")
    public String XoaGiaoVienTrongLop(@RequestParam String teacherId, @RequestParam String roomId, HttpSession session) {
        if (session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Đảm bảo ID hợp lệ: TeacherID phải bắt đầu bằng "TEA"
        if (!teacherId.startsWith("TEA")) {
            return "redirect:/ChiTietLopHoc/" + roomId + "?error=InvalidTeacherID";
        }

        try {
            int deletedCount = entityManager.createQuery(
                            "DELETE FROM ClassroomDetails WHERE roomId = :roomId AND memberId = :teacherId")
                    .setParameter("roomId", roomId)
                    .setParameter("teacherId", teacherId)
                    .executeUpdate();
            if (deletedCount == 0) {
                return "redirect:/ChiTietLopHoc/" + roomId + "?error=TeacherNotFound";
            }

            return "redirect:/ChiTietLopHoc/" + roomId + "?success=TeacherRemoved";
        } catch (Exception e) {
            return "redirect:/ChiTietLopHoc/" + roomId + "?error=DeleteFailed";
        }
    }

    @Transactional
    @GetMapping("/XoaHocSinhTrongLop")
    public String XoaHocSinhTrongLop(@RequestParam String studentId, @RequestParam String roomId, HttpSession session) {
        if (session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Đảm bảo ID hợp lệ: StudentID phải bắt đầu bằng "STU"
        if (!studentId.startsWith("STU")) {
            return "redirect:/ChiTietLopHoc/" + roomId + "?error=InvalidStudentID";
        }

        try {
            int deletedCount = entityManager.createQuery(
                            "DELETE FROM ClassroomDetails WHERE roomId = :roomId AND memberId = :studentId")
                    .setParameter("roomId", roomId)
                    .setParameter("studentId", studentId)
                    .executeUpdate();

            if (deletedCount == 0) {
                return "redirect:/ChiTietLopHoc/" + roomId + "?error=StudentNotFound";
            }

            return "redirect:/ChiTietLopHoc/" + roomId + "?success=StudentRemoved";
        } catch (Exception e) {
            return "redirect:/ChiTietLopHoc/" + roomId + "?error=DeleteFailed";
        }
    }

    @GetMapping("/GuiThongBao/{id}")
    public String GuiThongBao(@PathVariable("id") String id, HttpSession session) {
        // Kiểm tra đăng nhập
        Employees employee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        if (employee == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Lấy danh sách thành viên và thông tin phòng học trong một truy vấn duy nhất
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT c.memberId, c.roomId, s.email, t.email, o.startTime, o.endTime, r.startTime, r.endTime " +
                        "FROM ClassroomDetails c " +
                        "LEFT JOIN Students s ON c.memberId = s.studentID " +
                        "LEFT JOIN Teachers t ON c.memberId = t.teacherID " +
                        "LEFT JOIN OnlineRooms o ON c.roomId = o.roomId " +
                        "LEFT JOIN Rooms r ON c.roomId = r.roomId " +
                        "WHERE c.roomId = :roomId",
                Object[].class
        );
        query.setParameter("roomId", id);

        for (Object[] result : query.getResultList()) {
            String memberId = (String) result[0];
            String roomId = (String) result[1];
            String studentEmail = (String) result[2];
            String teacherEmail = (String) result[3];
            LocalDateTime onlineStart = (LocalDateTime) result[4];
            LocalDateTime onlineEnd = (LocalDateTime) result[5];
            LocalDateTime roomStart = (LocalDateTime) result[6];
            LocalDateTime roomEnd = (LocalDateTime) result[7];

            // Xác định nội dung tin nhắn
            String messageContent;
            if (roomId.startsWith("ONL")) {
                messageContent = "Lịch trình học Online của bạn bắt đầu từ ngày " + onlineStart + " đến hết ngày " + onlineEnd;
            } else {
                messageContent = "Lịch trình học của bạn bắt đầu từ ngày " + roomStart + " đến hết ngày " + roomEnd;
            }

            // Xác định email người nhận
            String recipientEmail = (studentEmail != null) ? studentEmail : teacherEmail;
            if (recipientEmail != null) {
                sendNotification(memberId, roomId, messageContent, employee, recipientEmail);
            }
        }

        return "redirect:/BoTriLopHoc";
    }

    private void sendNotification(String memberId, String roomId, String message, Employees sender, String email) {
        ScheduleNotifications scheduleNotifications = new ScheduleNotifications();
        scheduleNotifications.setMember(memberId);
        scheduleNotifications.setRoomId(roomId);
        scheduleNotifications.setMessage(message);
        scheduleNotifications.setSender(sender);
        entityManager.persist(scheduleNotifications);

        sendEmail(email, "Thông báo lịch trình học", message);
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

}
