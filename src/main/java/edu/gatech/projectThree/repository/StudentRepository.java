package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.Preference;
import edu.gatech.projectThree.datamodel.entity.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface StudentRepository extends CrudRepository<Student, Long> {
    ArrayList<Student> findAll();
    ArrayList<Student> findAllByOrderByIdAsc();
    Student findByUserName(String userName);

    //@Query("SELECT DISTINCT * FROM Customer c")
    ArrayList<Student> findDistinctByPreferencesInOrderByIdAsc(Collection<Preference> preferences);
}
