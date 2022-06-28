package com.websecurityproject.service;


import com.websecurityproject.model.Student;
import com.websecurityproject.model.User;
import com.websecurityproject.model.enumerations.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;

public interface UserService {
    Collection<GrantedAuthority> getAuthorities();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    User findByUsername(String username);
    User add(String name, String lastname, String email, String username, String password, Role role, List<Long> subjects, List<Student> students);
    List<User> findAll();
    List<User> findAllStudents();
    User findById(Long id);
    User update(Long id,String name,String lastname,String email,String username);
}
