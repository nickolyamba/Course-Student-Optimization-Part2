package edu.gatech.projectThree.datamodel.dao.impl;

import edu.gatech.projectThree.datamodel.dao.DataAccessObject;
import edu.gatech.projectThree.datamodel.entity.Professor;
import org.springframework.stereotype.Component;

/**
 * Created by dawu on 3/18/16.
 */
@Component("professorDAO")
public class ProfessorDAO implements DataAccessObject<Professor>{
    @Override
    public void insert(Professor obj) {

    }

    @Override
    public Professor update(Professor obj) {
        return null;
    }

    @Override
    public void delete(Professor obj) {

    }

    @Override
    public Professor get(long index) {
        return null;
    }
}
