package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.Application;
import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
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
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        int priority = 0;
        long offeringID = 0;
        Set<Long> offeringIDs;
        Set<Preference> preferences;

        for(int i = 0; i < students.size(); i++) {
            preferences = students.get(i).getPreferences();
  /*          if(preferences.isEmpty()) //// if student have no preferernces
                continue;

            // fetch all pref
            LOGGER.info("Preference found with findAll():");
            LOGGER.info("-------------------------------");
            for (Preference preference : preferences) {
                LOGGER.info(preference.toString());
            }
            LOGGER.info("");
*/
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
                        // find preference that contains the given offering
                        if(preference.getOffering().getId() == offeringID)
                        {
                            priority = preference.getPriority();
                            obj.addTerm(priority, studentsOfferings[i][j]);
                        }
                    }//for
                }//if
            }//for j
        }// for i

    }
}