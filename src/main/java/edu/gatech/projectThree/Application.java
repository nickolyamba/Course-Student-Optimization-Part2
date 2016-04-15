package edu.gatech.projectThree;

import edu.gatech.projectThree.datamodel.entity.Course;
import edu.gatech.projectThree.datamodel.entity.Semester;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.datamodel.entity.User;
import edu.gatech.projectThree.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawu on 3/18/16.
 */
@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private StudentRepository studRepository;
    private OfferingRepository offeringRepository;
    private CourseRepository courseRepository;
    private SemesterRepository semesterRepository;
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setStudentRepository(StudentRepository studRepository) {
        this.studRepository = studRepository;
    }

    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) {
        this.offeringRepository = offeringRepository;
    }

    @Autowired
    public void setCourseRepository(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Autowired
    public void setSemesterRepository(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    //https://spring.io/guides/gs/accessing-data-jpa/
    @Transactional
    @Bean
    public CommandLineRunner demo() {

        return (args) -> {
            // save a couple of customers
            //ArrayList<Course> courses = courseRepository.findAll();
            ArrayList<Semester> semesters = semesterRepository.findAll();
            ArrayList<User> users = userRepository.findAll();
            ArrayList<Course> courses = courseRepository.findAll();
            List<Student> students = studRepository.findAll();

            //offeringRepository.save(new Offering("Jack", "Bauer"));

            LOGGER.info("Studs found with findAll():");
            LOGGER.info("-------------------------------");
            for (Student student : students) {
                LOGGER.info(student.toString());
            }
            LOGGER.info("");


            // fetch all semesters
            log.info("Semesters found with findAll():");
            log.info("-------------------------------");
            for (Semester semester : semesters) {
                log.info(semester.toString());
            }
            log.info("");

            // fetch all users
            log.info("Users found with findAll():");
            log.info("-------------------------------");
            for (User user : users) {
                log.info(user.toString());
            }
            log.info("");

            // fetch all users
            log.info("Courses found with findAll():");
            log.info("-------------------------------");
            for (Course course : courses) {
                log.info(course.toString());
            }
            log.info("");


            log.info("Prereqs found with findAll():");
            log.info("-------------------------------");
            for (Course course : courses) {
                if(!course.getPrereqs().isEmpty())
                {
                    log.info("Course id: " + String.valueOf(course.getId()));
                    for (Course cour : course.getPrereqs())
                    {
                        log.info("Prereq id: " + String.valueOf(cour.getId()));
                    }
                }
            }
            log.info("");


/*
            //-----------Populate Offering --------------
            for (Semester semester : semesters)
            {
                for (Course course : courses)
                {
                    if(semester.getSeason() == Season.FALL)
                    {
                        if(course.isFall_term())
                            offeringRepository.save(new Offering(semester, course));
                    }

                    else if(semester.getSeason() == Season.SPRING)
                    {
                        if(course.isSpring_term())
                            offeringRepository.save(new Offering(semester, course));
                    }

                    else if(semester.getSeason() == Season.SUMMER)
                    {
                        if(course.isSummer_term())
                            offeringRepository.save(new Offering(semester, course));
                    }
                }
            }
*/

        };
    }
}
