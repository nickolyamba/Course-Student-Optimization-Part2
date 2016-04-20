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
 * Created by dawu on 4/5/16.
 * Total students taking course must be less than capacity
 */
@Component
public class StudentLimitConstraint extends BaseConstraint {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    @Override
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings,
                          GRBVar[] tasOfferings, List<Student> students, List<Offering> offerings,
                          List<Professor> professors, List<Ta> tas, List<TaOffering> taOfferings,
                          Set<Preference> preferenceList) throws GRBException {

        for(Offering offering : offerings)
        {
            int k = 0;
            GRBLinExpr studentLimit = new GRBLinExpr();
            for (Iterator<Preference> it = preferenceList.iterator(); it.hasNext();)
            {
                Preference preference = it.next();

                if(preference.getOffering() == offering)
                    studentLimit.addTerm(1, prefG[k]);
                k++;
            }

            String cname = "MAXSTUDENT_Offering=" + offering.getId();
            model.addConstr(studentLimit, GRB.LESS_EQUAL, offering.getCapacity(), cname);
        }//for offering


    }
}

