package com.websecurityproject.service.impl;

import com.websecurityproject.model.*;
import com.websecurityproject.model.exceptions.ExamNotFoundException;
import com.websecurityproject.model.exceptions.StudentNotFoundException;
import com.websecurityproject.model.exceptions.SubjectNotFoundException;
import com.websecurityproject.repository.ExamRepository;
import com.websecurityproject.repository.ProfessorRepository;
import com.websecurityproject.repository.StudentRepository;
import com.websecurityproject.repository.SubjectRepository;
import com.websecurityproject.service.ExamService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;

    public ExamServiceImpl(ExamRepository examRepository, StudentRepository studentRepository, ProfessorRepository professorRepository, SubjectRepository subjectRepository) {
        this.examRepository = examRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Exam> findAll() {
        return this.examRepository.findAll();
    }

    @Override
    public Exam findById(Long id) {
        return this.examRepository.findById(id).orElseThrow(()->new ExamNotFoundException());
    }

    @Override
    public Exam add(int grade, String session, LocalDate date, Long studentId, Long subjectId) {
        Student student=this.studentRepository.findById(studentId).orElseThrow(()->new StudentNotFoundException());
        Subject subject=this.subjectRepository.findById(subjectId).orElseThrow(()->new SubjectNotFoundException());
        Exam exam=new Exam(grade,session,date,student,subject);
        int credits=student.getNumberCredits()+subject.getCredit();
        student.setNumberCredits(credits);
        this.studentRepository.save(student);
        return this.examRepository.save(exam);
    }

    @Override
    public Exam update(Long id, int grade, String session, LocalDate date,Long studentId, Long subjectId) {
        Exam exam=this.findById(id);
        Student student=this.studentRepository.findById(studentId).orElseThrow(()->new StudentNotFoundException());
        Subject subject=this.subjectRepository.findById(subjectId).orElseThrow(()->new SubjectNotFoundException());
        exam.setGrade(grade);
        exam.setDate(date);
        exam.setSession(session);
        exam.setStudent(student);
        exam.setSubject(subject);
        int credits=student.getNumberCredits()+subject.getCredit();
        student.setNumberCredits(credits);
        this.studentRepository.save(student);
        return this.examRepository.save(exam);
    }

    @Override
    public List<Exam> findAllForStudent(Student student) {

        return this.examRepository.findAllByStudent(student);
    }
}
