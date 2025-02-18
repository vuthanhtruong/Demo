package com.example.demo.GET;

import com.example.demo.OOP.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
@Transactional
public class GiaoVienGet {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/DangKyGiaoVien")
    public String DangKyGiaoVien(ModelMap model) {
        // Sử dụng EntityManager để thực thi truy vấn
        List<Employees> employees = entityManager.createQuery("from Employees", Employees.class).getResultList();
        model.addAttribute("employees", employees);
        return "DangKyGiaoVien";
    }
    @GetMapping("/TrangChuGiaoVien")
    public String DangNhapGiaoVien(ModelMap model, HttpSession session) {
        if(session.getAttribute("TeacherID") == null) {
            return "DangNhapGiaoVien";
        }
        Teachers teacher = entityManager.find(Teachers.class, session.getAttribute("TeacherID"));
        model.addAttribute("teacher", teacher);
        return "TrangChuGiaoVien";
    }
    @GetMapping("/DangXuatGiaoVien")
    public String DangXuatGiaoVien(HttpSession session) {
        session.invalidate();
        return "redirect:/DangNhapGiaoVien";
    }
    @GetMapping("/DanhSachLopHocGiaoVien")
    public String DanhSachLopHocGiaoVien(HttpSession session, ModelMap model) {

        List<ClassroomDetails> classroomDetails = entityManager.createQuery("from ClassroomDetails where  memberId =: memberId", ClassroomDetails.class).
                setParameter("memberId", session.getAttribute("TeacherID")).
                getResultList();

        List<Rooms> rooms=new ArrayList<>();
        List<OnlineRooms> onlineRooms =new  ArrayList<>();

        for (ClassroomDetails classroomDetail : classroomDetails) {
            if(classroomDetail.getRoomId().startsWith("ONL")){
                OnlineRooms onlineRoom = entityManager.find(OnlineRooms.class, classroomDetail.getRoomId());
                onlineRooms.add(onlineRoom);
            }
            else{
                Rooms Room = entityManager.find(Rooms.class, classroomDetail.getRoomId());
                rooms.add(Room);
            }
        }
        model.addAttribute("classroomDetails", classroomDetails);
        model.addAttribute("rooms", rooms);
        return "DanhSachLopHocGiaoVien";
    }
    @GetMapping("ChiTietLopHocGiaoVien/{id}")
    public String ChiTietLopHocGiaoVien(@PathVariable String id, HttpSession session, ModelMap model) {
        Object room = null;
        if (id.startsWith("ONL")) {
            room = entityManager.find(OnlineRooms.class, id);
        } else {
            room = entityManager.find(Rooms.class, id);
        }
        model.addAttribute("room", room);
        return "ChiTietLopHocGiaoVien";
    }

    @GetMapping("/ThanhVienTrongLopGiaoVien/{id}")
    public String ThanhVienTrongLopHoc(HttpSession session, @PathVariable String id, ModelMap model) {
        List<ClassroomDetails> classroomDetails = entityManager.createQuery("from ClassroomDetails where roomId=:roomId", ClassroomDetails.class)
                .setParameter("roomId", id)
                .getResultList();

        List<Students> students = new ArrayList<>();
        List<Teachers> teachers = new ArrayList<>();
        for (ClassroomDetails classroomDetail : classroomDetails) {
            if (classroomDetail.getMemberId().startsWith("STU")) {
                students.add(entityManager.find(Students.class, classroomDetail.getMemberId()));
            } else {
                teachers.add(entityManager.find(Teachers.class, classroomDetail.getMemberId()));
            }
        }

        // Log the list of students for debugging
        System.out.println("Students List: " + students);

        model.addAttribute("students", students);
        model.addAttribute("teachers", teachers);
        return "ThanhVienTrongLopGiaoVien";
    }

    @GetMapping("/TinNhanCuaGiaoVien")
    public String TinNhanCuaGiaoVien(HttpSession session, ModelMap model) {
        String teacherID = session.getAttribute("TeacherID").toString();

        // Truy vấn các tin nhắn mà giáo viên có ID là teacherID (bao gồm người gửi và người nhận)
        List<Messages> messages = entityManager.createQuery(
                        "from Messages m where m.senderID = :teacherID or m.recipientID = :teacherID", Messages.class)
                .setParameter("teacherID", teacherID)
                .getResultList();

        // Duyệt qua các tin nhắn và tạo danh sách những người đã nhắn tin (bao gồm người gửi và người nhận)
        Set<String> contactIDs = new HashSet<>();
        for (Messages message : messages) {
            if (!message.getSenderID().equals(teacherID)) {
                contactIDs.add(message.getSenderID());  // Thêm người gửi vào danh sách nếu không phải là giáo viên
            }
            if (!message.getRecipientID().equals(teacherID)) {
                contactIDs.add(message.getRecipientID());  // Thêm người nhận vào danh sách nếu không phải là giáo viên
            }
        }

        model.addAttribute("contactIDs", contactIDs);  // Danh sách các người đã nhắn tin
        return "TinNhanCuaGiaoVien";  // Trả về tên view
    }


    @GetMapping("/ChiTietTinNhanCuaGiaoVien/{id}")
    public String ChiTietTinNhanCuaGiaoVien(HttpSession session, ModelMap model, @PathVariable String id) {
        // Lấy thông tin học sinh từ database bằng studentID (id trong URL)
        Students student = entityManager.find(Students.class, id);

        // Lấy thông tin giáo viên từ session
        String teacherID = session.getAttribute("TeacherID").toString();
        Teachers teacher = entityManager.find(Teachers.class, teacherID);

        // Truy vấn các tin nhắn của giáo viên và học sinh, sắp xếp theo thời gian
        List<Messages> messages = entityManager.createQuery(
                        "from Messages m where " +
                                "(m.senderID = :teacherID and m.recipientID = :studentID) " +
                                "or (m.senderID = :studentID and m.recipientID = :teacherID) " +
                                "order by m.datetime asc", Messages.class)
                .setParameter("teacherID", teacherID)
                .setParameter("studentID", id)
                .getResultList();


        // Thêm đối tượng student, teacher và messages vào model
        model.addAttribute("student", student);
        model.addAttribute("teacher", teacher);
        model.addAttribute("messages", messages);

        // Trả về view "ChiTietTinNhanCuaGiaoVien"
        return "ChiTietTinNhanCuaGiaoVien";
    }



}
