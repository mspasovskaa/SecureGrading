package com.websecurityproject.model;

import com.websecurityproject.model.enumerations.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Professor extends User{

    @ManyToMany
    private List<Subject> subjects;

    @ManyToMany
    private List<Student> students;

    public Professor(String name, String lastname, String email, String username, String password, Role role, List<Subject> subjects, List<Student> students) {
        super(name, lastname, email, username, password,role);
        this.subjects = subjects;
        this.students=students;
    }


    public Professor() {

    }
}
