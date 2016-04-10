package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

/**
 * Created by dawu on 4/5/16.
 */
@Component
public class StudentLimitConstraint extends BaseConstraint {

    @Override
    public void constrain(GRBModel model, GRBVar[][][][] yijk, GRBVar X) throws GRBException {
        for (int j = 0; j < getOfferingSize(); j++) {
            for (int k = 0; k < getProfessorSize(); k++) {
                for (int z = 0; z < getTaSize(); z++)
                {
                    GRBLinExpr total = new GRBLinExpr();
                    for (int i = 0; i < getStudentSize(); i++) {
                        total.addTerm(1, yijk[i][j][k][z]);
                    }
                    String cname = "StudentLimitConstraint_Course=" + j + "_Professor=" + k + "_Ta=" + z;
                    model.addConstr(total, GRB.LESS_EQUAL, X, cname);
                }
            }
        }
    }
}
