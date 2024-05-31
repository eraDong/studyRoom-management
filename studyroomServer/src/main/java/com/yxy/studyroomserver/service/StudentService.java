package com.yxy.studyroomserver.service;

import com.yxy.studyroomserver.model.StudentModel;
import com.yxy.studyroomserver.model.StudyRoomModel;
import com.yxy.studyroomserver.repository.StudentRepository;
import com.yxy.studyroomserver.repository.StudyRoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudyRoomRepository studyRoomRepository;

    // 创建新的学生
    public StudentModel addStudent(String name, String username, String password, boolean isAdmin) {
        StudentModel student = new StudentModel();
        student.setName(name);
        student.setUsername(username);
        student.setPassword(password);
        student.setAdmin(isAdmin);
        return studentRepository.save(student);
    }

    // 根据 ID 获取学生
    public StudentModel getStudentById(int id) {
        Optional<StudentModel> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    // 更新学生信息
    public StudentModel updateStudent(int id, String name, String username, String password, boolean isAdmin) {
        Optional<StudentModel> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            StudentModel student = existingStudent.get();
            student.setName(name);
            student.setUsername(username);
            student.setPassword(password);
            student.setAdmin(isAdmin);
            return studentRepository.save(student);
        } else {
            return null; // or throw an exception
        }
    }

    // 删除学生
    @Transactional
    public void deleteStudent(int id) {
        // 获取与该学生相关联的自习室记录
        List<StudyRoomModel> studyRooms = studyRoomRepository.findByStudent_StudentId(id);

        // 将这些自习室记录中的 studentId 置为 null
        for (StudyRoomModel studyRoom : studyRooms) {
            studyRoom.setStudent(null);
        }

        // 更新这些自习室记录到数据库中
        studyRoomRepository.saveAll(studyRooms);

        // 删除学生
        studentRepository.deleteById(id);
    }

    // 获取所有学生列表
    public List<StudentModel> getAllStudents() {
        return studentRepository.findAll();
    }



    // 其他学生相关操作...
    @Transactional
    public StudentModel login(String username, String password) {
        StudentModel student = studentRepository.findByUsername(username);
        if (student != null && student.getPassword().equals(password)) {
            return student;
        }
        return null; // 如果用户名或密码不匹配，返回null
    }

    public StudentModel register(StudentModel studentModel) {
        // 保存用户信息
        return studentRepository.save(studentModel);
    }

    public boolean existsByUsername(String username) {
        // 检查用户名是否已经存在
        return studentRepository.existsByUsername(username);
    }

}
