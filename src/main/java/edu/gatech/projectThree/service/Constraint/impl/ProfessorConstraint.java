package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Created by dawu on 4/12/16.
 * At least one professor per offering
 * Each professor can only teach one offering
 */
@Component
public class ProfessorConstraint extends BaseConstraint {

    private static final int MINIMUM_PROFESSORS = 1;
    private static final int MAXIMUM_OFFERINGS_TAUGHT = 5;

    @Override
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings,
                          GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings,
                          List<Professor> professors, List<Ta> tas, Set<Preference> preferences) throws GRBException {

        /*
        // At least one professor per offering
        for (int j = 0; j < offerings.size(); j++) {
            if(offerings.get(j).getPreferences().isEmpty())
                continue;// if no demand for course
            GRBLinExpr minProfessor = new GRBLinExpr();
            for (int i = 0; i < professors.size(); i++) {
                minProfessor.addTerm(1, professorsOfferings[i][j]);
            }
            String cname = "MINPROFESSOR_Offering=" + j;
            model.addConstr(minProfessor, GRB.GREATER_EQUAL, MINIMUM_PROFESSORS, cname);
        }

        // Professor can only each one offering
        for (int i = 0; i < professors.size(); i++) {
            GRBLinExpr maxOfferingsTaught = new GRBLinExpr();
            for (int j = 0; j < offerings.size(); j++) {
                maxOfferingsTaught.addTerm(1, professorsOfferings[i][j]);
            }
            String cname = "MAXOFFERINGSTAUGHT_Professor=" + i;
            model.addConstr(maxOfferingsTaught, GRB.LESS_EQUAL, MAXIMUM_OFFERINGS_TAUGHT, cname);
        }

        */
    }
}
