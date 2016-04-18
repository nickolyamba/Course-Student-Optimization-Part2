package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.datamodel.entity.Ta;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dawu on 4/12/16.
 * Require N teaching assistants for every M student capacity in course
 * Each TA can only be assigned to a single course
 */
@Component
public class TeachingAssistantConstraint extends BaseConstraint {
    private static final double N = 1;
    private static final double M = 100;
    private static final int MAXIMUM_OFFERINGS_TAUGHT = 5;


    @Override
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        /*
        // N teaching assistants for every M student capacity
        for (int j = 0; j < offerings.size(); j++) {
            GRBLinExpr taRatio = new GRBLinExpr();
            for (int i = 0; i < tas.size(); i++) {
                Set<TaOffering> taOfferings = tas.get(i).getTaOfferings();
                for(TaOffering taOffering : taOfferings)
                {
                    if(taOffering.getOffering().getId() == offerings.get(j).getId())
                        taRatio.addTerm(1, tasOfferings[i][j]);
                }

            }
            double ratio = (offerings.get(j).getCapacity()) * (N / M);
            String cname = "TARATIO_Offering=" + j;
            model.addConstr(taRatio, GRB.GREATER_EQUAL, ratio, cname);
        }

        // Each TA can only be assigned once
        for (int i = 0; i < tas.size(); i++) {
            GRBLinExpr taAssignment = new GRBLinExpr();
            for (int j = 0; j < offerings.size(); j++) {
                taAssignment.addTerm(1, tasOfferings[i][j]);
            }
            String cname = "TAASSIGNMENT_TA=" + i;
            model.addConstr(taAssignment, GRB.LESS_EQUAL, MAXIMUM_OFFERINGS_TAUGHT, cname);
        }
        */
    }
}