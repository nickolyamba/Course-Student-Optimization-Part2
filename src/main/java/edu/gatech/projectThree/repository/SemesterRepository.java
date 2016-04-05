package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Semester;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Kolya on 3/31/2016.
 */
public interface SemesterRepository extends CrudRepository<Semester, Integer>{
    List<Semester> findByYear(String year);
}
