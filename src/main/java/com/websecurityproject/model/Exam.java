package com.websecurityproject.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int grade;
    private String session;
    private LocalDate date;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Subject subject;


    public Exam(){}

    public Exam(int grade, String session, LocalDate date, Student student, Subject subject) {
        this.grade = grade;
        this.session = session;
        this.date = date;
        this.student = student;
        this.subject = subject;
    }
}
