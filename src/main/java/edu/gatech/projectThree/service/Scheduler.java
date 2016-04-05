package edu.gatech.projectThree.service;

import edu.gatech.projectThree.datamodel.dao.impl.CourseDAO;
import edu.gatech.projectThree.datamodel.dao.impl.OfferingDAO;
import edu.gatech.projectThree.datamodel.dao.impl.ProfessorDAO;
import edu.gatech.projectThree.datamodel.dao.impl.StudentDAO;
import edu.gatech.projectThree.datamodel.entity.Course;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.service.Constraint.Constraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gurobi.*;

import java.util.Arrays;

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

    public double schedule(Constraint[] constraints) {
        double result = 0;
        try {
            GRBEnv env = new GRBEnv("mip1.log");
            env.set(GRB.IntParam.LogToConsole, 0);
            final GRBModel model = new GRBModel(env);

            Student[] students = studentDAO.getAll();
            Course[] courses = courseDAO.getAll();
            Professor[] professors = professorDAO.getAll();

            GRBVar[][][] yijk = new GRBVar[students.length][courses.length][professors.length];
            // initialize variables here

            Arrays.stream(constraints)
                .forEach(constraint -> {
                    try {
                        constraint.addConstraint(model, yijk);
                    } catch (GRBException e) {
                        e.printStackTrace();
                    }
                });

            model.optimize();
            result = model.get(GRB.DoubleAttr.ObjVal);
        } catch (GRBException e) {
            e.printStackTrace();
        }

        return result;
    }

}
