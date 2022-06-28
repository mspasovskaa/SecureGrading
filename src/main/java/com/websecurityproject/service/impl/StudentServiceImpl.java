package com.websecurityproject.service.impl;

import com.websecurityproject.model.Professor;
import com.websecurityproject.model.Student;
import com.websecurityproject.model.Subject;
import com.websecurityproject.model.enumerations.Role;
import com.websecurityproject.model.exceptions.StudentNotFoundException;
import com.websecurityproject.model.exceptions.SubjectNotFoundException;
import com.websecurityproject.repository.ExamRepository;
import com.websecurityproject.repository.ProfessorRepository;
import com.websecurityproject.repository.StudentRepository;
import com.websecurityproject.repository.SubjectRepository;
import com.websecurityproject.service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubjectRepository subjectRepository;
    private final ProfessorRepository professorRepository;

    public StudentServiceImpl(StudentRepository studentRepository, ExamRepository examRepository, PasswordEncoder passwordEncoder, SubjectRepository subjectRepository, ProfessorRepository professorRepository) {
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        this.passwordEncoder = passwordEncoder;
        this.subjectRepository = subjectRepository;
        this.professorRepository = professorRepository;
    }

    @Override
    public List<Student> findAll() {
        return this.studentRepository.findAll();
    }

    @Override
    public Student findById(Long id) {
        return this.studentRepository.findById(id).orElseThrow(()->new StudentNotFoundException());
    }

    @Override
    public Student add(String name, String lastname, String email, String username, String password, Role role, int index, int numberCredits) {
        Student student=new Student(name,lastname,email,username,passwordEncoder.encode(password),role,index,numberCredits);
        return this.studentRepository.save(student);
    }

    @Override
    public Student update(Long id, String name, String lastname, String email, String username, String password, Role role, int index, int numberCredits) {

        Student student=this.findById(id);
        student.setEmail(email);
        student.setLastname(lastname);
        student.setName(name);
        student.setUsername(username);
        student.setIndex(index);
        student.setPassword(passwordEncoder.encode(password));
        student.setNumberCredits(numberCredits);
        student.setRole(role);
        return this.studentRepository.save(student);
    }

    @Override
    public List<Student> findAllBySubject(List<Long> subjectIds) {
        List<Subject> subjects=this.subjectRepository.findAllById(subjectIds);
        List<Student> students=new ArrayList<>();


        for (Subject s:subjects
             ) {
            students.addAll(s.getStudents());
        }
        return students;
    }

    @Override
    public Student findByUsername(String username) {
        return this.studentRepository.findByUsername(username);
    }


    @Override
    public void delete(Long id, Professor professor,Long subjectId) {
        Student student=this.findById(id);
        professor.getStudents().remove(student);
        Subject subject=this.subjectRepository.findById(subjectId).orElseThrow(()->new SubjectNotFoundException());
        subject.getStudents().remove(student);
        this.subjectRepository.save(subject);
        this.professorRepository.save(professor);

    }
}
