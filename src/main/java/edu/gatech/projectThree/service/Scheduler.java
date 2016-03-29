package edu.gatech.projectThree.service;

import edu.gatech.projectThree.datamodel.dao.impl.CourseDAO;
import edu.gatech.projectThree.datamodel.dao.impl.OfferingDAO;
import edu.gatech.projectThree.datamodel.dao.impl.ProfessorDAO;
import edu.gatech.projectThree.datamodel.dao.impl.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gurobi.*;

/**
 * Created by dawu on 3/18/16.
 */
@Service("scheduler")
public class Scheduler {

    @Autowired
    @Qualifier("studentDAO")
    StudentDAO studentDAO;

    @Autowired
    @Qualifier("courseDAO")
    CourseDAO courseDAO;

    @Autowired
    @Qualifier("professorDAO")
    ProfessorDAO professorDAO;
    
    @Autowired
    @Qualifier("offeringDAO")
    OfferingDAO offeringDAO;
}