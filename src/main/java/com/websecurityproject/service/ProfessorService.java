package com.websecurityproject.service;

import com.websecurityproject.model.Professor;
import com.websecurityproject.model.Student;
import com.websecurityproject.model.Subject;
import com.websecurityproject.model.enumerations.Role;

import java.util.List;

public interface ProfessorService {
    List<Professor> findAll();
    Professor findById(Long id);
    Professor add(String name, String lastname, String email, String username, String password, Role role, List<Long> subjects,List<Student> students);
    Professor update(Long id,String name, String lastname, String email, String username, String password,Role role,List<Long> subjectIds,List<Long> studentIds);
    Professor findByUsername(String username);
    void addSubjects(Long profId,List<Long> subjectIds);
    void addSubject(Long profId,Subject subject);
    Professor addStudents(Long profId,List<Long> studentIds);
}
