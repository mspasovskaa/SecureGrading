package com.websecurityproject.repository;

import com.websecurityproject.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Long> {
    Professor findByUsername(String username);

}
