package edu.gatech.projectThree.datamodel.dao.impl;

import edu.gatech.projectThree.datamodel.dao.DataAccessObject;
import edu.gatech.projectThree.datamodel.entity.Course;
import org.springframework.stereotype.Component;

/**
 * Created by dawu on 3/18/16.
 */
@Component("courseDAO")
public class CourseDAO implements DataAccessObject<Course> {

    @Override
    public void insert(Course obj) {

    }

    @Override
    public Course update(Course obj) {
        return null;
    }

    @Override
    public void delete(Course obj) {

    }

    @Override
    public Course get(long index) {
        return null;
    }
}
