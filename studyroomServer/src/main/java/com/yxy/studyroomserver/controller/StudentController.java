package com.yxy.studyroomserver.controller;

import com.yxy.studyroomserver.model.StudentModel;
import com.yxy.studyroomserver.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam boolean isAdmin) {
        try {
            StudentModel student = studentService.addStudent(name, username, password, isAdmin);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            throw new RuntimeException("创建学生失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable int id) {
        try {
            StudentModel student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            throw new RuntimeException("学生未找到: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam boolean isAdmin) {
        try {
            StudentModel student = studentService.updateStudent(id, name, username, password, isAdmin);
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            throw new RuntimeException("更新学生信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok("删除学生成功");
        } catch (Exception e) {
            throw new RuntimeException("删除学生失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<StudentModel> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            throw new RuntimeException("获取学生列表失败: " + e.getMessage());
        }
    }
}
