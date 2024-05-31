package com.yxy.studyroomserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "study_rooms")
public class StudyRoomModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String location;
    private boolean isBooked;

    private String introduction; // 新增字段
    private String image;        // 新增字段
    @Transient
    private int studentId;


    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentModel student;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public StudentModel getStudent() {
        return student;
    }

    public void setStudent(StudentModel student) {
        this.student = student;
    }

    public int getStudentId() {
        return student != null ? student.getStudentId() : studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return "StudyRoomModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", isBooked=" + isBooked +
                ", introduction='" + introduction + '\'' +
                ", image='" + image + '\'' +
                ", student=" + student +
                ", studentId=" + getStudentId() +
                '}';
    }

}
