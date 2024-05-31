package com.yxy.studyroomserver.controller;

import com.yxy.studyroomserver.model.StudentModel;
import com.yxy.studyroomserver.service.StudentService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        StudentModel student = studentService.login(username, password);
        if (student != null) {
            // 用户登录成功，生成JWT令牌
            String token = generateToken(student); // 生成令牌
            String response = String.format(
                    "{\"token\": \"%s\", \"username\": \"%s\", \"isAdmin\": %b, \"studentId\": %d}",
                    token, student.getUsername(), student.isAdmin(), student.getStudentId()
            );
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            // 登录失败
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"用户名或密码错误！\"}");
        }
    }

    private SecretKey generateKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
    // 生成JWT令牌的方法
    private String generateToken(StudentModel student) {
        SecretKey key = generateKey();
        return Jwts.builder()
                .setSubject(student.getUsername())
                .claim("studentId", student.getStudentId())
                .signWith(key)
                .compact();
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam boolean isAdmin
    ) {
        // 检查用户名是否已经存在
        if (studentService.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"用户名已存在！\"}");
        }

        // 创建新用户
        StudentModel studentModel = new StudentModel();
        studentModel.setName(name);
        studentModel.setUsername(username);
        studentModel.setPassword(password);
        studentModel.setAdmin(isAdmin);

        StudentModel createdStudent = studentService.register(studentModel);
        if (createdStudent != null) {
            return ResponseEntity.ok()
                    .body("{\"message\": \"用户注册成功！\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"用户注册失败！\"}");
        }
    }
}

