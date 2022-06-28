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
public class Student extends User{

    private int index;
    private int numberCredits;



    public Student(String name, String lastname, String email, String username, String password, Role role, int index, int numberCredits) {
        super(name, lastname, email, username, password,role);
        this.index = index;
        this.numberCredits = numberCredits;
    }

    public Student() {

    }
}
