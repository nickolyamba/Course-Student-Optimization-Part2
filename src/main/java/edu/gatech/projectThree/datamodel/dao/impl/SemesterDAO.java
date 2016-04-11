package edu.gatech.projectThree.datamodel.dao.impl;

import edu.gatech.projectThree.datamodel.dao.DataAccessObject;
import edu.gatech.projectThree.datamodel.entity.Semester;
import org.springframework.stereotype.Component;

/**
 * Created by dawu on 4/5/16.
 */
@Component("semesterDAO")
public class SemesterDAO implements DataAccessObject<Semester> {

    @Override
    public void insert(Semester obj) {

    }

    @Override
    public Semester update(Semester obj) {
        return null;
    }

    @Override
    public void delete(Semester obj) {

    }

    @Override
    public Semester get(long index) {
        return null;
    }

    @Override
    public Semester[] getAll() {
        return new Semester[0];
    }
}
