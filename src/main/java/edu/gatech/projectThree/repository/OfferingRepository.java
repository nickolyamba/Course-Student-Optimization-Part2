package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Course;
import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Semester;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface OfferingRepository extends CrudRepository<Offering, Long> {
    ArrayList<Offering> findAll();
    ArrayList<Offering> findBySemesterAndCourse(Semester semester, Course course);
    ArrayList<Offering> findBySemester(Semester semester);
}
