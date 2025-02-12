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
    @PostMapping("/ThemLoaiPhongHoc")
    @Transactional
    public String ThemLoaiPhongHoc(@RequestParam("roomTypeId") Long roomTypeId,
                                   @RequestParam("roomTypeId") String roomTypeName,HttpSession session) {
        // Tìm Employee theo ID
        Employees employee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        if (employee == null) {
            return "redirect:/ThemLoaiPhongHoc?error=EmployeeNotFound";
        }
        RoomTypes roomType = new RoomTypes();
        roomType.setRoomTypeId(roomTypeId);
        roomType.setRoomTypeName(roomTypeName);
        roomType.setEmployeeID(employee);

        entityManager.persist(roomType);

        return "redirect:/ThemLoaiPhongHoc?success=updated";
    }
    @PostMapping("/ThemPhongHoc")
    public String ThemPhongHoc(@RequestParam("roomId") Long roomId,
                               @RequestParam("roomName") String roomName,
                               @RequestParam("roomTypeId") Long roomTypeId,
                               HttpSession session) {
        // Lấy thông tin nhân viên từ session
        Employees loggedInEmployee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        RoomTypes roomType1 = entityManager.find(RoomTypes.class, roomTypeId);
        if (loggedInEmployee == null) {
            return "redirect:/DangNhapNhanVien"; // Nếu chưa đăng nhập, quay về trang login
        }

        // Tìm loại phòng theo ID
        RoomTypes roomType = entityManager.find(RoomTypes.class, roomTypeId);
        if (roomType == null) {
            return "redirect:/ThemPhongHoc?error=InvalidRoomType";
        }

        Rooms newRoom = new Rooms();
        newRoom.setRoomName(roomName);
        newRoom.setRoomId(roomId);
        newRoom.setEmployeeID(loggedInEmployee);
        newRoom.setRoomTypeID(roomType1);
        entityManager.persist(newRoom);

        return "redirect:/DanhSachPhongHoc"; // Quay về danh sách phòng học
    }
    @PostMapping("/SuaPhongHoc")
    public String CapNhatPhongHoc(@RequestParam("roomId") Long roomId,
                                  @RequestParam("roomName") String roomName,
                                  @RequestParam("roomTypeId") Long roomTypeId,
                                  HttpSession session) {
        // Lấy thông tin phòng từ database
        Rooms room = entityManager.find(Rooms.class, roomId);
        if (room == null) {
            return "redirect:/DanhSachPhongHoc?error=RoomNotFound";
        }

        // Kiểm tra xem loại phòng có tồn tại không
        RoomTypes roomType = entityManager.find(RoomTypes.class, roomTypeId);
        if (roomType == null) {
            return "redirect:/SuaPhongHoc/" + roomId + "?error=InvalidRoomType";
        }

        // Kiểm tra nhân viên đang đăng nhập
        Employees loggedInEmployee = entityManager.find(Employees.class, session.getAttribute("EmployeeID"));
        if (loggedInEmployee == null) {
            return "redirect:/DangNhapNhanVien";
        }

        // Cập nhật thông tin phòng học
        room.setRoomName(roomName);
        room.setRoomTypeID(roomType);
        room.setEmployeeID(loggedInEmployee);
        entityManager.merge(room);  // Lưu thay đổi

        return "redirect:/DanhSachPhongHoc?success=RoomUpdated";
    }
    @PostMapping("/SuaLoaiPhongHoc")
    @Transactional
    public String updateRoomType(@ModelAttribute RoomTypes updatedRoomType) {
        RoomTypes existingRoomType = entityManager.find(RoomTypes.class, updatedRoomType.getRoomTypeId());

        if (existingRoomType == null) {
            return "redirect:/ThemLoaiPhongHoc?error=RoomTypeNotFound";
        }

        existingRoomType.setRoomTypeName(updatedRoomType.getRoomTypeName());


        entityManager.merge(existingRoomType);

        return "redirect:/ThemLoaiPhongHoc";
    }
    @PostMapping("/ThemGiaoVienVaoLop")
    public String ThemGiaoVienVaoLop(@RequestParam Long roomId, @RequestParam List<Long> teacherIds) {
        Rooms room = entityManager.find(Rooms.class, roomId);

        for (Long teacherId : teacherIds) {
            Teachers teacher = entityManager.find(Teachers.class, teacherId);

            if (teacher != null) {
                // Kiểm tra xem giáo viên đã có ClassroomDetails trong phòng học này chưa
                List<ClassroomDetails> existingDetails = entityManager.createQuery(
                                "FROM ClassroomDetails WHERE room.roomId = :roomId AND teacher.teacherID = :teacherId", ClassroomDetails.class)
                        .setParameter("roomId", roomId)
                        .setParameter("teacherId", teacherId)
                        .getResultList();

                // Nếu chưa có, tạo mới ClassroomDetails
                if (existingDetails.isEmpty()) {
                    ClassroomDetails classroomDetail = new ClassroomDetails();
                    classroomDetail.setRoom(room);
                    classroomDetail.setTeacher(teacher);
                    classroomDetail.setStudent(null);
                    entityManager.persist(classroomDetail);
                }
            }
        }
        return "redirect:/ChiTietLopHoc/" + roomId + "?success=updated";
    }


    @PostMapping("/ThemHocSinhVaoLop")
    public String ThemHocSinhVaoLop(@RequestParam Long roomId, @RequestParam List<Long> studentIds) {
        Rooms room = entityManager.find(Rooms.class, roomId);

        for (Long studentId : studentIds) {
            Students student = entityManager.find(Students.class, studentId);

            if (student != null) {
                // Kiểm tra xem học sinh đã có ClassroomDetails trong phòng học này chưa
                List<ClassroomDetails> existingDetails = entityManager.createQuery(
                                "FROM ClassroomDetails WHERE room.roomId = :roomId AND student.studentID = :studentId", ClassroomDetails.class)
                        .setParameter("roomId", roomId)
                        .setParameter("studentId", studentId)
                        .getResultList();

                // Nếu chưa có, tạo mới ClassroomDetails
                if (existingDetails.isEmpty()) {
                    ClassroomDetails classroomDetail = new ClassroomDetails();
                    classroomDetail.setRoom(room);
                    classroomDetail.setTeacher(null); // Nếu không có giáo viên, có thể bỏ qua
                    classroomDetail.setStudent(student);
                    entityManager.persist(classroomDetail);
                }
            }
        }
        return "redirect:/ChiTietLopHoc/" + roomId + "?success=updated";
    }



}
