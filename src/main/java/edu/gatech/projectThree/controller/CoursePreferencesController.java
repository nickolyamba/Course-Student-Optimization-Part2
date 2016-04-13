package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.datamodel.entity.Course;
import edu.gatech.projectThree.datamodel.entity.Semester;
import edu.gatech.projectThree.repository.CourseRepository;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.repository.SemesterRepository;
import edu.gatech.projectThree.repository.StudentRepository;
import edu.gatech.projectThree.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by pjreed on 4/7/16.
 */
@Controller
public class CoursePreferencesController {
    private CourseRepository courseRepository;
    private StudentRepository studRepository;
    private SemesterRepository semesterRepository;

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setStudRepository(StudentRepository studRepository) {
        this.studRepository = studRepository;
    }

    @Autowired
    public void setSemesterRepository(SemesterRepository semesterRepository) { this.semesterRepository = semesterRepository; }

    private static final Logger LOGGER = LoggerFactory.getLogger(CoursePreferencesController.class);

    @RequestMapping(value = "/course_preferences/edit", method = RequestMethod.GET)
    public String editCoursePreferences(Model model, Authentication authentication) {

        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        Student currentStudent = studRepository.findByUserName(currentUser.getUsername());
        ArrayList<Semester> semesters = semesterRepository.findAll();


        model.addAttribute(
                "coursesNotTaken",
                currentStudent.getCoursesNotTaken(courseRepository.findAll())
        );
        model.addAttribute("semesters", semesters);
        return "course_preferences/edit";
    }
}
