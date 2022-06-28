package com.websecurityproject.service.impl;

import com.websecurityproject.model.Professor;
import com.websecurityproject.model.Student;
import com.websecurityproject.model.Subject;
import com.websecurityproject.model.User;
import com.websecurityproject.model.enumerations.Role;
import com.websecurityproject.model.exceptions.UserNotFoundException;
import com.websecurityproject.repository.SubjectRepository;
import com.websecurityproject.repository.UserRepository;
import com.websecurityproject.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl  implements UserService , UserDetailsService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public UserServiceImpl(UserRepository userRepository, SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=this.userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException());
        UserDetails userDetails=new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(), Stream.of(new SimpleGrantedAuthority(user.getRole().toString())).collect(Collectors.toList())
        );
        return userDetails;
    }


    @Override
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException());
    }

    @Override
    public User add(String name, String lastname, String email, String username, String password, Role role, List<Long> subjects, List<Student> students) {
       List<Subject> subjectList=new ArrayList<>();
        Professor professor=new Professor(name,lastname,email,username,password,role,subjectList,students);
        return null;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        //make everyone ROLE_USER
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            //anonymous inner type
            public String getAuthority() {
                return "ROLE_ADMIN";
            }
        };
        grantedAuthorities.add(grantedAuthority);
        return grantedAuthorities;
    }
    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public List<User> findAllStudents() {
        List<User> users=this.userRepository.findAll();
        List<User> students=new ArrayList<>();
        for (User u:users
             ) {
            if(u.getRole().equals(Role.ROLE_USER))
            {
                students.add(u);
            }

        }
        return students;
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(()->new UserNotFoundException());
    }

    @Override
    public User update(Long id, String name, String lastname, String email, String username) {
        User user=this.findById(id);
        user.setName(name);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setUsername(username);
        return this.userRepository.save(user);

    }
}
