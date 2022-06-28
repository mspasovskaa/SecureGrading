package com.websecurityproject.service.impl;

import com.websecurityproject.model.Professor;
import com.websecurityproject.model.Student;
import com.websecurityproject.model.Subject;
import com.websecurityproject.model.exceptions.SubjectNotFoundException;
import com.websecurityproject.repository.ProfessorRepository;
import com.websecurityproject.repository.StudentRepository;
import com.websecurityproject.repository.SubjectRepository;
import com.websecurityproject.repository.UserRepository;
import com.websecurityproject.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final UserRepository userRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, StudentRepository studentRepository, ProfessorRepository professorRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Subject> findAll() {
        return this.subjectRepository.findAll();
    }

    @Override
    public Subject findById(Long id) {
        return this.subjectRepository.findById(id).orElseThrow(()->new SubjectNotFoundException());
    }

    @Override
    public Subject add(String name, int credit,String semester) {
        List<Professor> professors=new ArrayList<>();
        List<Student> students=new ArrayList<>();
        Subject subject=new Subject(name,credit,semester,students,professors);
        return this.subjectRepository.save(subject);
    }

    @Override
    public Subject update(Long id, String name, int credit,String semester, List<Long> studentsIds, List<Long> professorsIds) {
        List<Professor> professors=this.professorRepository.findAllById(professorsIds);
        List<Student> students=this.studentRepository.findAllById(studentsIds);
        Subject subject=this.findById(id);
        subject.setCredit(credit);
        subject.setName(name);
        subject.setStudents(students);
        subject.setProfessors(professors);
        subject.setSemester(semester);
        return this.subjectRepository.save(subject);
    }



    @Override
    public List<Subject> findAllByStudent(Student student) {
        List<Subject> subjectList=this.subjectRepository.findAll();
        List<Subject> subjects=new ArrayList<>();
        for (Subject s:subjectList
        ) {
            if(s.getStudents().contains(student))
            {
                subjects.add(s);
            }
        }
        return subjects;
    }



    @Override
    public void delete(Long id,Professor professor) {
        Subject subject=this.findById(id);
        professor.getSubjects().remove(subject);
        this.professorRepository.save(professor);
        subject.getProfessors().remove(professor);
        this.subjectRepository.save(subject);
    }

    @Override
    public Subject addStudents(Long subjectId, List<Long> studentIds) {
        Subject subject=this.findById(subjectId);
        List<Student> students=this.studentRepository.findAllById(studentIds);
        List<Student> alreadyAdded=subject.getStudents();

        alreadyAdded.addAll(students);
        subject.setStudents(alreadyAdded);
        return this.subjectRepository.save(subject);
    }
}
