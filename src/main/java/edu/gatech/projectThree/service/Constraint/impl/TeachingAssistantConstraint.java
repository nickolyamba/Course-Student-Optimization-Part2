package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
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
public class TeachingAssistantConstraint extends BaseConstraint {

    private static final double  maxStudsPerTa = 50;  // adjust when needed
    private static final double  minStudsPerTa = 0;   // adjust when needed
    private static final int MAXIMUM_OFFERINGS_TAUGHT = 5;

    @Override
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings,
                          GRBVar[] tasOffG, List<Student> students, List<Offering> offerings,
                          List<Professor> professors, List<Ta> tas, List<TaOffering> taOfferings,
                          Set<Preference> preferenceList
                          ) throws GRBException {

         /********************************************************************************
         // Constraint: min*TA <= Studs <= max*TA
         // Number of TA per assigned Students is from min to max
         // Sum_(i): max*TA(i) <= Sum_(i): Stud(i) ; 1D: (offering j = const)
         *********************************************************************************/

        GRBLinExpr taMaxSumConstr;
        GRBLinExpr taMinSumConstr;
        GRBLinExpr studSumConstr;

        //for (int j = 0; j < offerings.size(); j++) {
        for(Offering offering : offerings)
        {

            taMaxSumConstr = new GRBLinExpr();
            taMinSumConstr = new GRBLinExpr();
            // if taOffering contains offering
            int k = 0;
            for (TaOffering taOffering : taOfferings)
            {
                if(taOffering.getOffering() == offering)
                {
                    taMaxSumConstr.addTerm(maxStudsPerTa, tasOffG[k]);
                    taMinSumConstr.addTerm(minStudsPerTa, tasOffG[k]);
                }
                k++;
            }

            //Stud1 + Stud2 + Stud3
            k = 0;
            studSumConstr = new GRBLinExpr();
            for (Iterator<Preference> it = preferenceList.iterator(); it.hasNext();)
            {
                Preference preference = it.next();

                if(preference.getOffering() == offering)
                    studSumConstr.addTerm(1, prefG[k]);
                k++;
            }//for studs

            String cMsxName = "TaMaxRatio_Offering=" + offering.getId();
            String cMinName = "TaMinRatio_Offering=" + offering.getId();
            model.addConstr(studSumConstr, GRB.LESS_EQUAL, taMaxSumConstr, cMsxName);
            model.addConstr(studSumConstr, GRB.GREATER_EQUAL, taMinSumConstr, cMinName);
        }//for offerings

        int k;
        for(Ta ta : tas)
        {
            GRBLinExpr taAssignment = new GRBLinExpr();
            k = 0;
            for(TaOffering taOffering : taOfferings)
            {
                if(taOffering.getTa() == ta)
                    taAssignment.addTerm(1, tasOffG[k]);
            }
            String cname = "TAASSIGNMENT_TA=" + ta.getId();
            model.addConstr(taAssignment, GRB.LESS_EQUAL, MAXIMUM_OFFERINGS_TAUGHT, cname);
        }//for

    }//constrain()
}