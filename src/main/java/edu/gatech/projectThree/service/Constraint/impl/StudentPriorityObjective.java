package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.Application;
import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
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
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings, GRBVar[] tasOfferings,
                          List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas,
                          List<TaOffering> taOfferings, Set<Preference> preferenceList) throws GRBException {

        int priority;
        int k = 0;
        // Create objective expression
        GRBLinExpr obj = new GRBLinExpr();
        for (Iterator<Preference> it = preferenceList.iterator(); it.hasNext();)
        {
            Preference preference = it.next();

            priority = preference.getPriority();
            obj.addTerm(priority, prefG[k]);

            //LOGGER.info("prefG[" + String.valueOf(preference.getId())+ "] added");
            k++;
        }

        model.setObjective(obj, GRB.MINIMIZE);
        model.update(); ///// ----------> remove in production

    }//constrain()
}//class