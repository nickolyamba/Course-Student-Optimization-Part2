package edu.gatech.projectThree;

import edu.gatech.projectThree.datamodel.entity.Semester;
import edu.gatech.projectThree.datamodel.entity.User;
import edu.gatech.projectThree.repository.CourseRepository;
import edu.gatech.projectThree.repository.OfferingRepository;
import edu.gatech.projectThree.repository.SemesterRepository;
import edu.gatech.projectThree.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;

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

    private OfferingRepository offeringRepository;
    private CourseRepository courseRepository;
    private SemesterRepository semesterRepository;
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    @Bean
    public CommandLineRunner demo() {

        return (args) -> {
            // save a couple of customers
            //ArrayList<Course> courses = courseRepository.findAll();
            ArrayList<Semester> semesters = semesterRepository.findAll();
            ArrayList<User> users = userRepository.findAll();

            //offeringRepository.save(new Offering("Jack", "Bauer"));

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
        };
    }
}
