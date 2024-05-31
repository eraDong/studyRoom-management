package com.yxy.studyroomserver.service;

import com.yxy.studyroomserver.model.StudyRoomModel;
import com.yxy.studyroomserver.model.StudentModel;
import com.yxy.studyroomserver.repository.StudentRepository;
import com.yxy.studyroomserver.repository.StudyRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudyRoomService {

    @Autowired
    private StudyRoomRepository studyRoomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    // 创建一个新的自习室
    public StudyRoomModel addStudyRoom(String name, String location, boolean isBooked, int studentId, String introduction, String imagePath) {
        StudyRoomModel studyRoom = new StudyRoomModel();
        studyRoom.setName(name);
        studyRoom.setLocation(location);
        studyRoom.setBooked(isBooked);
        studyRoom.setIntroduction(introduction);
        studyRoom.setImage(imagePath);

        StudentModel student = studentService.getStudentById(studentId);
        studyRoom.setStudent(student);

        return studyRoomRepository.save(studyRoom);
    }

    // 根据 ID 获取自习室
    public StudyRoomModel getStudyRoomById(int id) {
        Optional<StudyRoomModel> studyRoom = studyRoomRepository.findById(id);
        return studyRoom.orElse(null);
    }

    // 更新自习室信息
    public StudyRoomModel updateStudyRoom(int id, String name, String location, boolean isBooked, Integer studentId, String introduction, String imagePath) {
        Optional<StudyRoomModel> existingRoom = studyRoomRepository.findById(id);
        if (existingRoom.isPresent()) {
            StudyRoomModel studyRoom = existingRoom.get();
            studyRoom.setName(name);
            studyRoom.setLocation(location);
            studyRoom.setBooked(isBooked);
            studyRoom.setIntroduction(introduction);
            studyRoom.setImage(imagePath);

            StudentModel student = studentService.getStudentById(studentId);
            studyRoom.setStudent(student);

            return studyRoomRepository.save(studyRoom);
        } else {
            return null; // or throw an exception
        }
    }

    // 删除自习室
    public void deleteStudyRoom(int id) {
        studyRoomRepository.deleteById(id);
    }

    // 获取所有自习室列表
    public List<StudyRoomModel> getAllStudyRooms() {
        return studyRoomRepository.findAll();
    }

    // 其他自习室相关操作...
    public StudyRoomModel reserveStudyRoom(int roomId, int studentId) throws Exception {
        Optional<StudyRoomModel> roomOpt = studyRoomRepository.findById(roomId);
        if (!roomOpt.isPresent()) {
            throw new Exception("Study Room not found");
        }

        StudyRoomModel room = roomOpt.get();
        Optional<StudentModel> studentOpt = studentRepository.findById(studentId);
        if (!studentOpt.isPresent()) {
            throw new Exception("Student not found");
        }

        StudentModel student = studentOpt.get();
        room.setStudent(student);
        room.setBooked(true);
        return studyRoomRepository.save(room);
    }
}
