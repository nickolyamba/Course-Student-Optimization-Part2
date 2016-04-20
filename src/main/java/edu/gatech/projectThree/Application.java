package edu.gatech.projectThree;

import edu.gatech.projectThree.repository.*;
import edu.gatech.projectThree.service.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dawu on 3/18/16.
 */
@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private StudentRepository studRepository;
    private OfferingRepository offeringRepository;
    private CourseRepository courseRepository;
    private SemesterRepository semesterRepository;
    private UserRepository userRepository;
    private Scheduler scheduler;

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

    @Autowired
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    //https://spring.io/guides/gs/accessing-data-jpa/
    @Transactional
    @Bean
    public CommandLineRunner demo() {
        scheduler.schedule(); // for testing
        return (args) -> {
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
