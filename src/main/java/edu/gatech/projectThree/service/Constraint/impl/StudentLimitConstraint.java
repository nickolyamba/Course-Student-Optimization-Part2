package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
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
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings,
                          GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings,
                          List<Professor> professors, List<Ta> tas, List<Preference> preferenceList) throws GRBException {
        for (int j = 0; j < offerings.size(); j++) {
            GRBLinExpr studentLimit = new GRBLinExpr();
            for (int i = 0; i < students.size(); i++) {
                //check if stud has Preference for this course
                //Set<Preference> preferences = students.get(i).getPreferences();
                for(Preference preference : preferenceList)
                {
                    if (preference.getStudent().getId() == students.get(i).getId() &&
                            preference.getOffering().getId() == offerings.get(j).getId())
                    {
                        studentLimit.addTerm(1, studentsOfferings[i][j]);
                    }

                }
                String cname = "STUDENTLIMIT_Offering=" + offerings.get(j).getId();
                model.addConstr(studentLimit, GRB.LESS_EQUAL, offerings.get(j).getCapacity(), cname);
            }
        }
    }
}

