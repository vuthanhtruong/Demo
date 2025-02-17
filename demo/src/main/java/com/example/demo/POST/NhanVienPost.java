package com.example.demo.POST;

import com.example.demo.OOP.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/")
@Transactional
public class NhanVienPost {

    @PersistenceContext
    private EntityManager entityManager;

    @PostMapping("/DangKyNhanVien")
    public String DangKyNhanVien(@RequestParam String EmployeeID, @RequestParam String FirstName,
                                 @RequestParam String LastName, @RequestParam String Email,@RequestParam String PhoneNumber,
                                 @RequestParam String Password) {
        Admin admin = entityManager.find(Admin.class, 1);

        Employees employees = new Employees();
        employees.setEmployeeID("EMP"+EmployeeID);
        employees.setFirstName(FirstName);
        employees.setLastName(LastName);
        employees.setEmail(Email);
        employees.setPassword(Password);
        employees.setPhoneNumber(PhoneNumber);
        employees.setAdminID(admin);

        entityManager.persist(employees);

        return "redirect:/DangNhapNhanVien";
    }
    @PostMapping("/DangNhapNhanVien")
    public String DangNhapNhanVien(@RequestParam("EmployeeID") String employeeID,
                                   @RequestParam("Password") String password,
                                   HttpSession session,
                                   ModelMap model) {
        try {
            Employees employee = entityManager.createQuery(
                            "SELECT e FROM Employees e WHERE e.employeeID = :employeeID", Employees.class)
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
            @RequestParam("teacherID") String teacherID,
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
            @RequestParam("studentID") String studentID,
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

        Admin admin = employee.getAdminID();

        Students existingStudent = entityManager.find(Students.class, studentID);
        if (existingStudent != null) {
            model.addAttribute("error", "Mã học sinh đã tồn tại!");
            return "ThemHocSinhCuaBan";
        }

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
    public String SuaGiaoVienCuaBan(@RequestParam("teacherID") String id,
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
    public String SuaHocSinhCuaBan(@RequestParam("studentID") String studentID,
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
        String employeeID = (String) session.getAttribute("EmployeeID");
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
    @PostMapping("/ThemPhongHoc")
    public String ThemPhongHoc(@RequestParam("roomId") String roomId,
                               @RequestParam("roomName") String roomName,
                               HttpSession session) {
        // Lấy thông tin nhân viên từ session
        Employees loggedInEmployee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        if (loggedInEmployee == null) {
            return "redirect:/DangNhapNhanVien"; // Nếu chưa đăng nhập, quay về trang login
        }

        Rooms newRoom = new Rooms();
        newRoom.setRoomName(roomName);
        newRoom.setEmployee(loggedInEmployee);
        entityManager.persist(newRoom);

        return "redirect:/DanhSachPhongHoc"; // Quay về danh sách phòng học
    }
    @PostMapping("/ThemPhongHocOnline")
    public String LuuPhongHocOnline(
            @RequestParam("roomId") String roomId,
            @RequestParam("roomName") String roomName,
            @RequestParam("status") Boolean status,
            HttpSession session) {

        // Kiểm tra đăng nhập
        if (session.getAttribute("EmployeeID") == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Lấy thông tin nhân viên từ session
        String employeeId = (String) session.getAttribute("EmployeeID");
        Employees employee = entityManager.find(Employees.class, employeeId);
        if (employee == null) {
            return "redirect:/ThemPhongHocOnline?error=invalid_employee";
        }

        // Kiểm tra ID phòng có tồn tại không
        OnlineRooms existingRoom = entityManager.find(OnlineRooms.class, roomId);
        if (existingRoom != null) {
            return "redirect:/ThemPhongHocOnline?error=room_exists";
        }

        // Tạo phòng học online mới
        OnlineRooms newRoom = new OnlineRooms();
        newRoom.setRoomId(roomId);
        newRoom.setRoomName(roomName);
        newRoom.setStatus(status);
        newRoom.setEmployee(employee);

        // Lưu vào database
        entityManager.persist(newRoom);

        return "redirect:/DanhSachPhongHoc?success=added";
    }


    @PostMapping("/SuaPhongHocOffline")
    public String CapNhatPhongHoc(@RequestParam("roomId") String roomId,
                                  @RequestParam("roomName") String roomName,
                                  HttpSession session) {
        // Lấy thông tin phòng từ database
        Rooms room = entityManager.find(Rooms.class, roomId);
        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }

        // Kiểm tra nhân viên đang đăng nhập
        Employees loggedInEmployee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        if (loggedInEmployee == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Cập nhật thông tin phòng học
        room.setRoomName(roomName);
        room.setEmployee(loggedInEmployee);
        entityManager.merge(room);  // Lưu thay đổi

        return "redirect:/DanhSachPhongHoc?success=RoomUpdated";
    }
    @PostMapping("/SuaPhongHocOnline")
    public String CapNhatPhongHocOnline(@RequestParam("roomId") String roomId,
                                        @RequestParam("roomName") String roomName,
                                        HttpSession session) {
        OnlineRooms room = entityManager.find(OnlineRooms.class, roomId);
        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }

        Employees loggedInEmployee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        if (loggedInEmployee == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Cập nhật thông tin phòng học online
        room.setRoomName(roomName);
        room.setEmployee(loggedInEmployee);
        entityManager.merge(room);  // Lưu thay đổi

        return "redirect:/DanhSachPhongHoc?success=RoomUpdated";
    }

    @PostMapping("/ThemGiaoVienVaoLop")
    @Transactional
    public String ThemGiaoVienVaoLop(@RequestParam String roomId, @RequestParam List<String> teacherIds) {
        for (String teacherId : teacherIds) {
            // Kiểm tra xem giáo viên đã được thêm vào lớp này chưa
            Long count = entityManager.createQuery(
                            "SELECT COUNT(cd) FROM ClassroomDetails cd WHERE cd.roomId = :roomId AND cd.memberId = :teacherId",
                            Long.class)
                    .setParameter("roomId", roomId)
                    .setParameter("teacherId", teacherId)
                    .getSingleResult();

            if (count == 0) {
                ClassroomDetails classroomDetail = new ClassroomDetails(roomId, teacherId);
                entityManager.persist(classroomDetail);
            }
        }
        return "redirect:/ChiTietLopHoc/" + roomId + "?success=updated";
    }
    @PostMapping("/ThemHocSinhVaoLop")
    @Transactional
    public String ThemHocSinhVaoLop(@RequestParam String roomId, @RequestParam List<String> studentIds) {
        for (String studentId : studentIds) {
            // Kiểm tra xem học sinh đã có trong lớp hay chưa
            Long count = entityManager.createQuery(
                            "SELECT COUNT(cd) FROM ClassroomDetails cd WHERE cd.roomId = :roomId AND cd.memberId = :studentId",
                            Long.class)
                    .setParameter("roomId", roomId)
                    .setParameter("studentId", studentId)
                    .getSingleResult();

            if (count == 0) {
                ClassroomDetails classroomDetail = new ClassroomDetails(roomId, studentId);
                entityManager.persist(classroomDetail);
            }
        }
        return "redirect:/ChiTietLopHoc/" + roomId + "?success=updated";
    }
    @PostMapping("/CapNhatLichTrinh")
    @Transactional
    public String capNhatLichTrinh(
            @RequestParam("roomId") String roomId,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime thoiGianBatDau = LocalDateTime.parse(startTime, formatter);
            LocalDateTime thoiGianKetThuc = LocalDateTime.parse(endTime, formatter);

            Object room = entityManager.find(Rooms.class, roomId);
            if (room == null) {
                room = entityManager.find(OnlineRooms.class, roomId);
            }

            if (room instanceof Rooms) {
                ((Rooms) room).setStartTime(thoiGianBatDau);
                ((Rooms) room).setEndTime(thoiGianKetThuc);
            } else if (room instanceof OnlineRooms) {
                ((OnlineRooms) room).setStartTime(thoiGianBatDau);
                ((OnlineRooms) room).setEndTime(thoiGianKetThuc);
            } else {
                return "redirect:/BoTriLopHoc?error=notfound";
            }

            entityManager.merge(room);
            return "redirect:/BoTriLopHoc?success=updated";

        } catch (DateTimeParseException e) {
            return "redirect:/BoTriLopHoc?error=invalid_datetime";
        }
    }
    @PostMapping("/CapNhatDiaChi")
    @Transactional
    public String capNhatDiaChi(
            @RequestParam("roomId") String roomId,
            @RequestParam("roomAddress") String diaChiMoi) {

        Rooms room = entityManager.find(Rooms.class, roomId);
        if (room != null) {
            room.setAddress(diaChiMoi);
            entityManager.merge(room);
            return "redirect:/BoTriLopHoc?success=updated";
        } else {
            return "redirect:/BoTriLopHoc?error=notfound";
        }
    }

    // Cập nhật link phòng học Online
    @PostMapping("/CapNhatLink")
    @Transactional
    public String capNhatLink(
            @RequestParam("roomId") String roomId,
            @RequestParam("roomLink") String linkMoi) {

        OnlineRooms room = entityManager.find(OnlineRooms.class, roomId);
        if (room != null) {
            room.setLink(linkMoi);
            entityManager.merge(room);
            return "redirect:/BoTriLopHoc?success=updated";
        } else {
            return "redirect:/BoTriLopHoc?error=notfound";
        }
    }


}
