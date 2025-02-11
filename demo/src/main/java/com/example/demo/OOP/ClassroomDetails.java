package com.example.demo.OOP;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ClassroomDetails")
public class ClassroomDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ClassroomDetailsID")
    private Long classroomDetailsId;

    @ManyToOne
    @JoinColumn(name = "RoomID", nullable = false, foreignKey = @ForeignKey(name = "FK_Classroom_Room"))
    private Rooms room;

    @ManyToOne
    @JoinColumn(name = "TeacherID", nullable = true, foreignKey = @ForeignKey(name = "FK_Classroom_Teacher"))
    private Teachers teacher;

    @ManyToOne
    @JoinColumn(name = "StudentID", foreignKey = @ForeignKey(name = "FK_Classroom_Student"))
    private Students student;



    // Getters và Setters
    public Long getClassroomDetailsId() {
        return classroomDetailsId;
    }

    public void setClassroomDetailsId(Long classroomDetailsId) {
        this.classroomDetailsId = classroomDetailsId;
    }

    public Rooms getRoom() {
        return room;
    }

    public void setRoom(Rooms room) {
        this.room = room;
    }

    public Teachers getTeacher() {
        return teacher;
    }

    public void setTeacher(Teachers teacher) {
        this.teacher = teacher;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }
}
