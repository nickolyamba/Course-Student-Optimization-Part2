package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.datamodel.entity.Course;
import edu.gatech.projectThree.repository.CourseRepository;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by pjreed on 4/7/16.
 */
@Controller
public class CoursePreferencesController {
    private CourseRepository courseRepository;
    private StudentRepository studRepository;

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setStudRepository(StudentRepository studRepository) {
        this.studRepository = studRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CoursePreferencesController.class);

    @RequestMapping(value = "/course_preferences/edit", method = RequestMethod.GET)
    public String editCoursePreferences(Model model) {

        Iterable<Course> courseList = courseRepository.findAll();
        Iterable<Student> students = studRepository.findAll();


        model.addAttribute("allCourses", courseList);
        model.addAttribute("students", students);
        return "course_preferences/edit";
    }
}
