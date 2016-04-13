package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.datamodel.entity.Ta;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dawu on 4/5/16.
 * Total students taking course must be less than capacity
 */
@Component
public class StudentLimitConstraint extends BaseConstraint {


    @Override
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBVar X, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        for (int j = 0; j < offerings.size(); j++) {
            GRBLinExpr studentLimit = new GRBLinExpr();
            for (int i = 0; i < students.size(); i++) {
                studentLimit.addTerm(1, studentsOfferings[i][j]);
            }
            String cname = "STUDENTLIMIT_Offering=" + j;
            model.addConstr(studentLimit, GRB.LESS_EQUAL, offerings.get(j).getCapacity(), cname);
        }
    }
}
