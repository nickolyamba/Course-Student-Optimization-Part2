package edu.gatech.projectThree.datamodel.dao.impl;

import edu.gatech.projectThree.datamodel.dao.DataAccessObject;
import edu.gatech.projectThree.datamodel.entity.Student;
import org.springframework.stereotype.Component;

/**
 * Created by dawu on 3/18/16.
 */
@Component("studentDAO")
public class StudentDAO implements DataAccessObject<Student> {


    @Override
    public void insert(Student obj) {

    }

    @Override
    public Student update(Student obj) {
        return null;
    }

    @Override
    public void delete(Student obj) {

    }

    @Override
    public Student get(long index) {
        return null;
    }
}
