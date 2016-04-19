package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dawu on 4/19/16.
 */
@Component
public class ConcentrationConstraint extends BaseConstraint {

    @Override
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        for (int i = 0; i < students.size(); i++) {
            GRBLinExpr concentration = new GRBLinExpr();
            for (int j = 0; j < offerings.size(); j++) {
                Student student = students.get(i);
                Course course = offerings.get(j).getCourse();

                Specialization specialization = student.getSpecialization();
                if (specialization.getCourses().contains(course)) {
                    // Do something with constraint here
                }
            }
        }
    }
}
