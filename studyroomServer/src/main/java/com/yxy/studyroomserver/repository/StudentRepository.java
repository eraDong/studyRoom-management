package com.yxy.studyroomserver.repository;

import com.yxy.studyroomserver.model.StudentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentModel, Integer> {
    StudentModel findByUsername(String username);

    boolean existsByUsername(String username);
}
