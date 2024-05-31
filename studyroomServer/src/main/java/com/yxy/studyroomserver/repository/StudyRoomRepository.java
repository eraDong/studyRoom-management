package com.yxy.studyroomserver.repository;

import com.yxy.studyroomserver.model.StudyRoomModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRoomRepository extends JpaRepository<StudyRoomModel, Integer> {
    List<StudyRoomModel> findByStudent_StudentId(int studentId);
}
