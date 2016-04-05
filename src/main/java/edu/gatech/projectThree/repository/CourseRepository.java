package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Course;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface CourseRepository extends CrudRepository<Course, Integer> {

}
