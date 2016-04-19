package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.Application;
import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dawu on 4/12/16.
 * Require N teaching assistants for every M student capacity in course
 * Each TA can only be assigned to a single course
 */
@Component
public class StudentPriorityObjective extends BaseConstraint {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Override
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBLinExpr obj,
                          List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas, List<Preference> preferenceList) throws GRBException {

        int priority;
        for(int i = 0; i < students.size(); i++) {
            for(int j = 0; j < offerings.size(); j++) {
                //check each preference if it contains (i, j) pair
                for(Preference preference : preferenceList)
                {
                    //if(preference.getOptimizedTime() != null)
                    //    continue;
                    if (preference.getStudent().getId() == students.get(i).getId() &&
                            preference.getOffering().getId() == offerings.get(j).getId())
                    {

                        priority = preference.getPriority();
                        obj.addTerm(priority, studentsOfferings[i][j]);

                        LOGGER.info("stud_offer["+ String.valueOf(students.get(i).getId()) +"]"+
                                "["+ String.valueOf(offerings.get(j).getId()) + "]=" +
                                String.valueOf(studentsOfferings[i][j])+ "  added");
                    }
                }

            }//for j
        }// for i

    }//constrain()
}//class