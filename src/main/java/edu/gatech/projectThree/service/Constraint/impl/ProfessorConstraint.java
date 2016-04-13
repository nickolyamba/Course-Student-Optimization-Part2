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
 * Created by dawu on 4/12/16.
 * At least one professor per offering
 */
@Component
public class ProfessorConstraint extends BaseConstraint {

    @Override
    public void constrain(GRBModel model, GRBVar[][][][] grbVars, GRBVar X, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        for (int i = 0; i < getStudentSize(); i++) {
            for (int z = 0; z < getTaSize(); z++) {
                for (int j = 0; j < getOfferingSize(); j++) {
                    GRBLinExpr minimumProfessor = new GRBLinExpr();
                    for (int k = 0; k < getProfessorSize(); k++) {
                        minimumProfessor.addTerm(1, grbVars[i][j][k][z]);
                    }
                    String cname = "ProfessorConstraint_Student=" + i + "_Offering=" + j + "_Ta=" + z;
                    model.addConstr(minimumProfessor, GRB.GREATER_EQUAL, 1, cname);
                }
            }
        }

    }
}
