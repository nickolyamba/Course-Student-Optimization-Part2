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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        int priority = 0;
        long offeringID = 0;
        Set<Long> offeringIDs;
        Set<Preference> preferences;

        for(int i = 0; i < students.size(); i++) {
            preferences = students.get(i).getPreferences();

            // Get Set of offering IDs in Student's Preference set
            offeringIDs = new HashSet<Long>();
            for(Preference preference : preferences)
                offeringIDs.add(preference.getOffering().getId());

            for(int j = 0; j < offerings.size(); j++) {
                // Get current offeringID
                offeringID = offerings.get(j).getId();
                // If this offering in the set of student's preferences, addTerm
                if(offeringIDs.contains(offeringID)) //omit if offering is not in preferences
                {
                    for (Preference preference : preferences)
                    {
                        //if(preference.getOptimizedTime() != null)
                        //    continue;
                        // find preference that contains the given offering
                        if(preference.getOffering().getId() == offeringID)
                        {
                            priority = preference.getPriority();
                            obj.addTerm(priority, studentsOfferings[i][j]);

                            LOGGER.info("stud_offer["+ String.valueOf(students.get(i).getId()) +"]"+
                                    "["+ String.valueOf(offerings.get(j).getId()) + "]=" +
                                    String.valueOf(studentsOfferings[i][j])+ "  added");
                        }
                    }//for
                }//if
            }//for j
        }// for i

        /*
        *     1 OF_35_ST_1 + 1 OF_35_ST_2 + 0 OF_35_ST_3 +
              2 OF_36_ST_1 + 2 OF_36_ST_2 + 0 OF_36_ST_3 +
              3 OF_37_ST_1 + 3 OF_37_ST_2 + 0 OF_37_ST_3 +
              4 OF_38_ST_1 + 4 OF_38_ST_2 + 0 OF_38_ST_3 +
              5 OF_39_ST_1 +                0 OF_39_ST_3 +
              0 OF_40_ST_1 + 0 OF_40_ST_2 + 1 OF_40_ST_3
        * */

        for(int i = 0; i < students.size(); i++) {
            for(int j = 0; j < offerings.size(); j++) {
                //check each preference if it contains (i, j) pair
                for(Preference preference : preferenceList)
                {
                    if(preference.getOptimizedTime() != null)
                        continue;
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