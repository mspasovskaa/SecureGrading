package com.websecurityproject.model;

import com.websecurityproject.model.enumerations.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "security_users")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    protected String lastname;
    protected String email;
    protected String username;
    protected String password;
    protected Role role;


    public User(){}

    public User(String name, String lastname, String email, String username, String password,Role role) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role=role;
    }
}
