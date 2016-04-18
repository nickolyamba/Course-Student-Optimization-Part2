package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Created by dawu on 4/5/16.
 * Total students taking course must be less than capacity
 */
@Component
public class StudentLimitConstraint extends BaseConstraint {

    @Override
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        for (int j = 0; j < offerings.size(); j++) {
            GRBLinExpr studentLimit = new GRBLinExpr();
            for (int i = 0; i < students.size(); i++) {
                //check if stud has Preference for this course
                Set<Preference> preferences = students.get(i).getPreferences();
                for(Preference preference : preferences)
                {
                    if(preference.getOffering().getId() == offerings.get(j).getId())
                        studentLimit.addTerm(1, studentsOfferings[i][j]);
                }

            }
            String cname = "STUDENTLIMIT_Offering=" + j;
            model.addConstr(studentLimit, GRB.LESS_EQUAL, offerings.get(j).getCapacity(), cname);
        }
    }
}
