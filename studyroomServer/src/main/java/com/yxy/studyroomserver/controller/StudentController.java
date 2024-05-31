package com.yxy.studyroomserver.controller;

import com.yxy.studyroomserver.model.StudentModel;
import com.yxy.studyroomserver.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public StudentModel createStudent(
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam boolean isAdmin) {

        return studentService.addStudent(name, username, password, isAdmin);
    }

    @GetMapping("/{id}")
    public StudentModel getStudent(@PathVariable int id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public StudentModel updateStudent(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam boolean isAdmin) {

        return studentService.updateStudent(id, name, username, password, isAdmin);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
    }

    @GetMapping
    public List<StudentModel> getAllStudents() {
        return studentService.getAllStudents();
    }
}
