package com.websecurityproject.web;

import com.websecurityproject.model.*;
import com.websecurityproject.model.enumerations.Role;
import com.websecurityproject.service.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Controller
public class GradeSystemController {



    private final StudentService studentService;
    private final ProfessorService professorService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final ExamService examService;



    public GradeSystemController(StudentService studentService, ProfessorService professorService, UserService userService, SubjectService subjectService, ExamService examService) {
        this.studentService = studentService;
        this.professorService = professorService;

        this.userService = userService;
        this.subjectService = subjectService;
        this.examService = examService;
    }

    @GetMapping("/")
    public String getFirstPage(HttpServletRequest request)
    {
        return "homepage";
    }

    @GetMapping("/home")
    public String getHomePage(HttpServletRequest request,Model model)
    {
        String username=request.getRemoteUser();
        User user=this.userService.findByUsername(username);
        Student student=new Student();
        if(user.getRole().equals(Role.ROLE_USER))
        {
            student=this.studentService.findByUsername(username);
        }
        model.addAttribute("student",student);
        model.addAttribute("user",user);
        return "index";
    }
    @GetMapping("/subjects")
    public String getSubjectsPage(HttpServletRequest request, Model model) {
        String username=request.getRemoteUser();

        User user=this.userService.findByUsername(username);
        List<Subject> subjectList=new ArrayList<>();
        if(user.getRole().equals(Role.ROLE_ADMIN)) {

            Professor professor=this.professorService.findByUsername(username);
            subjectList=professor.getSubjects();
            model.addAttribute("subjectList",subjectList);

        }
        else if(user.getRole().equals(Role.ROLE_USER))
        {
            Student student=this.studentService.findByUsername(username);
            subjectList= this.subjectService.findAllByStudent(student);
            model.addAttribute("subjectList",subjectList);

        }

        return "subjects";
    }


    @GetMapping("/preregister")
    public String getPreregisterPage()
    {
        return "preregister";
    }

    @GetMapping("/logProf")
    public String getLoginProf(HttpServletRequest request,Principal principal)
    {


        UserDetails currentUser
                = (UserDetails) ((Authentication) principal).getPrincipal();
        User user=this.userService.findByUsername(currentUser.getUsername());
        if(user!=null) {
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), userService.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
            return "redirect:/home";
        }
        return "redirect:/";
    }

    @GetMapping("/logStudent")
    public String getLogStudent()
    {
        return "redirect:/login";

    }
    @PostMapping("/register1")
    public String getRegisterPage(@RequestParam String role,Model model)
    {
        if(role.equals("USER"))
        {
            return "registeruser";
        }
        else if(role.equals("ADMIN"))
        {
            List<Subject> subjects=this.subjectService.findAll();
            model.addAttribute("subjects",subjects);
            return "registerprof";
        }
        return "preregister";
    }

    @PostMapping("/registerprof")
    public String registerProf(@RequestParam String name,
                               @RequestParam String lastname,
                               @RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam List<Long> subject,
                               Model model,HttpServletRequest request){
        String[] subjectsList = request.getParameterValues("subject");
        List<Long> sub = new ArrayList<>();
        for (int i = 0; i < subjectsList.length; i++) {
            sub.add(Long.parseLong(subjectsList[i]));
        }
        List<Student> students=this.studentService.findAllBySubject(sub);
        this.professorService.add(name,lastname,email,username,password,Role.ROLE_ADMIN,sub,students);
        return "redirect:/login";


    }


    @PostMapping("/registeruser")
    public String registerUser(@RequestParam String name,
                               @RequestParam String lastname,
                               @RequestParam String email,
                               @RequestParam String username,
                               @RequestParam String password)
    {
        int numberofstudents=220000+this.studentService.findAll().size() + 1;
        Student student=this.studentService.add(name,lastname,email,username,password,Role.ROLE_USER,numberofstudents,0);


        return "redirect:/login";
    }
    @DeleteMapping("/deletesubject/{id}")
    public String deleteSubject(@PathVariable Long id,HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        Professor professor=this.professorService.findByUsername(username);
        this.subjectService.delete(id,professor);
        return "redirect:/subjects";
    }

    @DeleteMapping("/deletestudent/{id}")
    public String deleteStudent(@PathVariable Long id,@RequestParam Long subject,HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        Professor professor=this.professorService.findByUsername(username);
        this.studentService.delete(id,professor,subject);
        return "redirect:/subjects";
    }
    @GetMapping("/students/{id}")
    public String getStudents(@PathVariable Long id, HttpServletRequest request, Model model)
    {

        String username=request.getRemoteUser();
        Professor professor=this.professorService.findByUsername(username);

        Subject subject=this.subjectService.findById(id);
        List<Student> students=subject.getStudents();


        model.addAttribute("subject",subject);
        model.addAttribute("students",students);
        return "students";
    }

    @PostMapping("/examEntry/{id}")
    public String setExam(@PathVariable Long id, @RequestParam Long subject,Model model)
    {
        Student student=this.studentService.findById(id);
        Subject subject1=this.subjectService.findById(subject);
        model.addAttribute("student",student);
        model.addAttribute("subject",subject1);
        return "exam";
    }

    @GetMapping("/add-subject")
    public String getAddSubjectPage(Model model,HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        Professor professor=this.professorService.findByUsername(username);
        List<Subject> alreadyAdded=professor.getSubjects();
        List<Subject> subjects=this.subjectService.findAll();
        for (Subject s:
             alreadyAdded) {
            if(subjects.contains(s))
            {
                subjects.remove(s);
            }
        }
        model.addAttribute("subjects",subjects);
        return "addsubject";
    }
    @PostMapping("/addSubjects")
    public String addSubjects(@RequestParam List<Long> subject,
                               Model model,HttpServletRequest request){
        String[] subjectsList = request.getParameterValues("subject");
        List<Long> sub = new ArrayList<>();
        for (int i = 0; i < subjectsList.length; i++) {
            sub.add(Long.parseLong(subjectsList[i]));
        }
        String username=request.getRemoteUser();
        Professor professor=this.professorService.findByUsername(username);
        this.professorService.addSubjects(professor.getId(),sub);
        return "redirect:/subjects";
    }

    @GetMapping("/add-student/{id}")
    public String getAddStudentPage(@PathVariable Long id,Model model, HttpServletRequest request)
    {
        Subject subject=this.subjectService.findById(id);
        List<Student> alreadyAdded=subject.getStudents();
        List<User> students=this.userService.findAllStudents();
        List<User> leftStudents=new ArrayList<>();
        for (User u: students
             ) {
            for (Student s:alreadyAdded
                 ) {
                if(s.getUsername().equals(u.getUsername()))
                {
                   leftStudents.add(u);
                }
            }
        }
        students.removeAll(leftStudents);
        model.addAttribute("subject",subject);
        model.addAttribute("students",students);
        return "add-students";
    }

    @PostMapping("/addStudents")
    public String addStudents(@RequestParam List<Long> student,@RequestParam Long subject,
                              Model model,HttpServletRequest request){
        String[] studentList = request.getParameterValues("student");
        List<Long> stu = new ArrayList<>();
        for (int i = 0; i < studentList.length; i++) {
            stu.add(Long.parseLong(studentList[i]));
        }
        String username=request.getRemoteUser();
        Professor professor=this.professorService.findByUsername(username);
        this.professorService.addStudents(professor.getId(),stu);
        this.subjectService.addStudents(subject,stu);
        return "redirect:/subjects";
    }
    @GetMapping("/exams")
    public String getExams(HttpServletRequest request,Model model)
    {
        String username=request.getRemoteUser();
        Student student=this.studentService.findByUsername(username);
        System.out.println(student.getRole());

        List<Exam> exams=this.examService.findAllForStudent(student);
        model.addAttribute("exams",exams);
        return "exams";
    }

    @PostMapping("/examFinal")
    public String setExamGrade(@RequestParam Long studentId,
                               @RequestParam Long subjectId,
                               @RequestParam String grade,
                               @RequestParam String date,
                               @RequestParam String session)
    {

     int grade1=Integer.valueOf(grade);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date1 = LocalDate.parse(date, dateFormat);
     this.examService.add(grade1,session,date1,studentId,subjectId);
        return "redirect:/subjects";
    }

    @PostMapping("/edit-acc/{id}")
    public String showEditUser(@PathVariable Long id, Model model) {
        User user = this.userService.findById(id);
        model.addAttribute("user", user);
        return "edit-info";
    }

    @PostMapping("/user/{id}")
    public String updateUser(@PathVariable Long id, @RequestParam String name,@RequestParam String lastname,
                             @RequestParam String email,@RequestParam String username, Model model,HttpServletRequest request) {
        User user = this.userService.findById(id);
        model.addAttribute("user", user);
        this.userService.update(id, name, lastname,email,username);
        return "redirect:/home";
    }
}
