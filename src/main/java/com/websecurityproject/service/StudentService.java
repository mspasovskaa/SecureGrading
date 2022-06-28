package com.websecurityproject.service;

import com.websecurityproject.model.Professor;
import com.websecurityproject.model.Student;
import com.websecurityproject.model.enumerations.Role;

import java.util.List;

public interface StudentService {
    List<Student> findAll();
    Student findById(Long id);
    Student add(String name, String lastname, String email, String username, String password, Role role, int index, int numberCredits);
    Student update(Long id,String name, String lastname, String email, String username, String password, Role role, int index, int numberCredits);
    List<Student> findAllBySubject(List<Long> subject);
    Student findByUsername(String username);
    void delete(Long id,Professor professor,Long subjectId);
}
