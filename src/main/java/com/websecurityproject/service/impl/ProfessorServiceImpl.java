package com.websecurityproject.service.impl;

import com.websecurityproject.model.Professor;
import com.websecurityproject.model.Student;
import com.websecurityproject.model.Subject;
import com.websecurityproject.model.enumerations.Role;
import com.websecurityproject.model.exceptions.ProfessorNotFoundException;
import com.websecurityproject.model.exceptions.SubjectNotFoundException;
import com.websecurityproject.repository.ProfessorRepository;
import com.websecurityproject.repository.StudentRepository;
import com.websecurityproject.repository.SubjectRepository;
import com.websecurityproject.repository.UserRepository;
import com.websecurityproject.service.ProfessorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ProfessorServiceImpl(ProfessorRepository professorRepository, StudentRepository studentRepository, SubjectRepository subjectRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public List<Professor> findAll() {
        return this.professorRepository.findAll();
    }

    @Override
    public Professor findById(Long id) {
        return this.professorRepository.findById(id).orElseThrow(()->new ProfessorNotFoundException());
    }

    @Override
    public Professor add(String name, String lastname, String email, String username, String password, Role role, List<Long> subjects,List<Student> students) {
        List<Subject> subjectsList = this.subjectRepository.findAllById(subjects);
        Professor professor=new Professor(name,lastname,email,username,passwordEncoder.encode(password),role,subjectsList,students);
        return this.professorRepository.save(professor);
    }


    @Override
    public Professor update(Long id, String name, String lastname, String email, String username, String password,Role role,List<Long> subjectIds,List<Long> studentIds) {
        List<Subject> subjectList=this.subjectRepository.findAllById(subjectIds);
        List<Student> studentList=this.studentRepository.findAllById(studentIds);
        Professor professor=this.findById(id);
        professor.setEmail(email);
        professor.setLastname(lastname);
        professor.setName(name);
        professor.setUsername(username);
        professor.setPassword(passwordEncoder.encode(password));
        professor.setRole(role);
        professor.setStudents(studentList);
        professor.setSubjects(subjectList);
        return this.professorRepository.save(professor);
    }

    @Override
    public Professor findByUsername(String username) {
        Professor professor=this.professorRepository.findByUsername(username);
        return professor;
    }

    @Override
    public void addSubjects(Long profId, List<Long> subjectIds) {
        Professor professor=this.findById(profId);
        List<Subject> subjectsList = this.subjectRepository.findAllById(subjectIds);
        List<Subject> alreadyAdded=professor.getSubjects();
        alreadyAdded.addAll(subjectsList);
        professor.setSubjects(alreadyAdded);
        this.professorRepository.save(professor);
    }

    @Override
    public void addSubject(Long profId, Subject subject) {
        Professor professor=this.findById(profId);
        List<Subject> subjectList=professor.getSubjects();
        subjectList.add(subject);
        professor.setSubjects(subjectList);
        Subject subject1=this.subjectRepository.findById(subject.getId()).orElseThrow(()->new SubjectNotFoundException());
        List<Professor> professors=subject1.getProfessors();
        subject1.setProfessors(professors);
        this.subjectRepository.save(subject1);
        this.professorRepository.save(professor);
    }

    @Override
    public Professor addStudents(Long profId, List<Long> studentIds) {
        Professor professor=this.findById(profId);
        List<Student> studentsList = this.studentRepository.findAllById(studentIds);
        List<Student> alreadyAdded=professor.getStudents();
        alreadyAdded.addAll(studentsList);
        professor.setStudents(alreadyAdded);
        return this.professorRepository.save(professor);
    }


}
