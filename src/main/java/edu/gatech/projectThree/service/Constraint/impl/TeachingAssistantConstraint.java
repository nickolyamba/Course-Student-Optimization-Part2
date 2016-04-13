package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dawu on 4/12/16.
 * Require N teaching assistants for every M student capacity in course
 * Each TA can only be assigned to a single course
 */
@Component
public class TeachingAssistantConstraint extends BaseConstraint {
    private static final int N = 1;
    private static final int M = 30;
    @Override
    public void constrain(GRBModel model, GRBVar[][][][] grbVars, GRBVar X, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        for (int i = 0; i < getStudentSize(); i++) {
            for (int k = 0; k < getProfessorSize(); k++) {
                for (int j = 0; j < getOfferingSize(); j++) {
                    Course course = offerings.get(j).getCourse();
                    GRBLinExpr taRatio = new GRBLinExpr();
                    for (int z = 0; z < getTaSize(); z++) {
                        taRatio.addTerm(1, grbVars[i][j][k][z]);
                    }
                    String cname = "TARATIO_Student=" + i + "_Offering=" + j + "_Professor" + k;
                    int ratio = M / N; // TODO: multiply by course limit
                    model.addConstr(taRatio, GRB.GREATER_EQUAL, ratio,cname);
                }
            }
        }

        // Each TA can only be assigned to a single course
        for (int i = 0; i < getStudentSize(); i++) {
            for (int k = 0; k < getProfessorSize(); k++) {
                for (int j = 0; j < getOfferingSize(); j++) {
                    GRBLinExpr taRatio = new GRBLinExpr();
                    for (int z = 0; z < getTaSize(); z++) {
                        taRatio.addTerm(1, grbVars[i][j][k][z]);
                    }
                    String cname = "TARATIO_Student=" + i + "_Offering=" + j + "_Professor" + k;
                    model.addConstr(taRatio, GRB.EQUAL, 1, cname);
                }
            }
        }

    }
}
