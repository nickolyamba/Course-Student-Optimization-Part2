package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Request;
import edu.gatech.projectThree.datamodel.entity.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface RequestRepository extends CrudRepository<Request, Long> {
    ArrayList<Request> findAll();
    ArrayList<Request> findByStudent(Student student);
}
