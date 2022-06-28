package com.websecurityproject.repository;

import com.websecurityproject.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
Student findByUsername(String username);
}
