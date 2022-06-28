package com.websecurityproject.repository;

import com.websecurityproject.model.Exam;
import com.websecurityproject.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam,Long> {
List<Exam> findAllByStudent(Student student);
}
