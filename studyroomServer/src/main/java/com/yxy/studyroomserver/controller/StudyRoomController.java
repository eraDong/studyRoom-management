package com.yxy.studyroomserver.controller;

import com.yxy.studyroomserver.model.StudyRoomModel;
import com.yxy.studyroomserver.service.StudyRoomService;
import com.yxy.studyroomserver.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studyrooms")
public class StudyRoomController {

    @Autowired
    private StudyRoomService studyRoomService;

    @Autowired
    private StudentService studentService;

    static class ReservationRequest {
        private int studentId;

        // Getters and Setters
        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudyRoom(
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam boolean isBooked,
            @RequestParam int studentId,
            @RequestParam String introduction,
            @RequestParam String image) {
        try {
            StudyRoomModel studyRoom = studyRoomService.addStudyRoom(name, location, isBooked, studentId, introduction, image);
            return ResponseEntity.ok(studyRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("创建自习室失败: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudyRoom(@PathVariable int id) {
        try {
            StudyRoomModel studyRoom = studyRoomService.getStudyRoomById(id);
            return ResponseEntity.ok(studyRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("自习室未找到: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudyRoom(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam boolean isBooked,
            @RequestParam Integer studentId,
            @RequestParam String introduction,
            @RequestParam String image) {
        try {
            StudyRoomModel studyRoom = studyRoomService.updateStudyRoom(id, name, location, isBooked, studentId, introduction, image);
            return ResponseEntity.ok(studyRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("更新自习室信息失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudyRoom(@PathVariable int id) {
        try {
            studyRoomService.deleteStudyRoom(id);
            return ResponseEntity.ok(new ErrorResponse("删除自习室成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("删除自习室失败: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllStudyRooms() {
        try {
            List<StudyRoomModel> studyRooms = studyRoomService.getAllStudyRooms();
            return ResponseEntity.ok(studyRooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("获取自习室列表失败: " + e.getMessage()));
        }
    }

    @PostMapping("/reserve/{id}")
    public ResponseEntity<?> reserveStudyRoom(@PathVariable int id, @RequestBody ReservationRequest request) {
        try {
            StudyRoomModel studyRoom = studyRoomService.reserveStudyRoom(id, request.getStudentId());
            return ResponseEntity.ok(studyRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("预订自习室失败: " + e.getMessage()));
        }
    }

    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
