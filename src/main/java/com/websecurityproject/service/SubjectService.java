package com.websecurityproject.service;

import com.websecurityproject.model.Professor;
import com.websecurityproject.model.Student;
import com.websecurityproject.model.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAll();
    Subject findById(Long id);
    Subject add(String name, int credit,String semester);
    Subject update(Long id,String name,int credit,String semester,List<Long> studentsIds, List<Long> professorsIds);
    List<Subject> findAllByStudent(Student student);
    void delete(Long id,Professor professor);
    Subject addStudents(Long subjectId,List<Long> studentIds);


}
