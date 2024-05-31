package com.yxy.studyroomserver.controller;

import com.yxy.studyroomserver.model.StudyRoomModel;
import com.yxy.studyroomserver.service.StudyRoomService;
import com.yxy.studyroomserver.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public StudyRoomModel createStudyRoom(
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam boolean isBooked,
            @RequestParam int studentId,
            @RequestParam String introduction,
            @RequestParam String image) {

        return studyRoomService.addStudyRoom(name, location, isBooked, studentId, introduction, image);
    }

    @GetMapping("/{id}")
    public StudyRoomModel getStudyRoom(@PathVariable int id) {
        return studyRoomService.getStudyRoomById(id);
    }

    @PutMapping("/{id}")
    public StudyRoomModel updateStudyRoom(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam boolean isBooked,
            @RequestParam Integer studentId,
            @RequestParam String introduction,
            @RequestParam String image) {

        return studyRoomService.updateStudyRoom(id, name, location, isBooked, studentId, introduction, image);
    }

    @DeleteMapping("/{id}")
    public void deleteStudyRoom(@PathVariable int id) {
        studyRoomService.deleteStudyRoom(id);
    }

    @GetMapping
    public List<StudyRoomModel> getAllStudyRooms() {
        return studyRoomService.getAllStudyRooms();
    }

    @PostMapping("/reserve/{id}")
    public ResponseEntity<StudyRoomModel> reserveStudyRoom(@PathVariable int id, @RequestBody ReservationRequest request) {
        try {
            StudyRoomModel studyRoom = studyRoomService.reserveStudyRoom(id, request.getStudentId());
            return ResponseEntity.ok(studyRoom);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
