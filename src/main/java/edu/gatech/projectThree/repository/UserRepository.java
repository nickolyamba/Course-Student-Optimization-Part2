package edu.gatech.projectThree.repository;

import edu.gatech.projectThree.datamodel.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

/**
 * Created by Kolya on 4/2/2016.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    ArrayList<User> findAll();
    User findByUserName(String userName);
}
