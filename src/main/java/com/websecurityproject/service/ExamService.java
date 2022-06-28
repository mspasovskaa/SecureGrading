package com.websecurityproject.service;

import com.websecurityproject.model.*;

import java.time.LocalDate;
import java.util.List;

public interface ExamService {
    List<Exam> findAll();
    Exam findById(Long id);
    Exam add(int grade, String session, LocalDate date, Long studentId,  Long subjectId);
    Exam update(Long id, int grade, String session, LocalDate date, Long studentId, Long subjectId);
    List<Exam> findAllForStudent(Student student);
}
