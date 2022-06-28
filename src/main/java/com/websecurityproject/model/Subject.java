package com.websecurityproject.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int credit;
    private String semester;
    @ManyToMany
    private List<Student> students;
    @ManyToMany(mappedBy = "subjects")
    private  List<Professor> professors;



    public Subject(){}

    public Subject(String name, int credit, String semester, List<Student> students, List<Professor> professors) {
        this.name = name;
        this.credit = credit;
        this.semester = semester;
        this.students = students;
        this.professors = professors;
    }
}
