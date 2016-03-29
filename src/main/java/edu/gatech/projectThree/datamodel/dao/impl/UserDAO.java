package edu.gatech.projectThree.datamodel.dao.impl;

import edu.gatech.projectThree.datamodel.dao.DataAccessObject;
import edu.gatech.projectThree.datamodel.entity.User;
import org.springframework.stereotype.Component;

/**
 * Created by dawu on 3/18/16.
 */
@Component("userDAO")
public class UserDAO implements DataAccessObject<User>{

    @Override
    public void insert(User obj) {

    }

    @Override
    public User update(User obj) {
        return null;
    }

    @Override
    public void delete(User obj) {

    }

    @Override
    public User get(long index) {
        return null;
    }

    @Override
    public User[] getAll() {
        return null;
    }
}
