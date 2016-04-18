package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.*;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface OfferingRepository extends CrudRepository<Offering, Long> {
    ArrayList<Offering> findAll();

    Offering findBySemesterAndCourse(Semester semester, Course course);

    ArrayList<Offering> findAllByOrderByIdAsc();
    //ArrayList<Offering> findBySemesterAndCourse(Semester semester, Course course);
    ArrayList<Offering> findBySemesterOrderByIdAsc(Semester semester);
    ArrayList<Offering> findDistinctByPreferencesIn(Collection<Preference> preferences);

}
