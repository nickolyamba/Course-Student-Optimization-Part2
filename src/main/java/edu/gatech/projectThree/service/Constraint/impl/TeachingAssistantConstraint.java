package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.Application;
import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * Created by dawu on 4/12/16.
 * Require N teaching assistants for every M student capacity in course
 * Each TA can only be assigned to a single course
 */
@Component
public class TeachingAssistantConstraint extends BaseConstraint {
    private GlobalState state;
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static double  maxStudsPerTa;// 50;  // adjust when needed
    private static double  minStudsPerTa;// 1;   // adjust when needed
    private static int MAXIMUM_OFFERINGS_TAUGHT = 5;


    @Autowired
    public void setState(GlobalState state) {
        this.state = state;
    }

    @Override
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings,
                          GRBVar[] tasOffG, List<Student> students, List<Offering> offerings,
                          List<Professor> professors, List<Ta> tas, List<TaOffering> taOfferings,
                          List<ProfessorOffering> profOfferings, List<Preference> preferences
                          ) throws GRBException {

        maxStudsPerTa = state.getConfig().getMaxTa();
        minStudsPerTa = state.getConfig().getMinTa();
        //LOGGER.info("minTA:" + String.valueOf(minStudsPerTa)+ "  maxTA: " + String.valueOf(maxStudsPerTa));

        /********************************************************************************
        // Constraint: min*TA <= Studs <= max*TA
        // Number of TA per assigned Students is from min to max
        // Sum_(i): max*TA(i) <= Sum_(i): Stud(i) ; 1D: (offering j = const)
        *********************************************************************************/
        GRBLinExpr taMaxSumConstr;
        GRBLinExpr taMinSumConstr;
        GRBLinExpr studSumConstr;

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
            for (Iterator<Preference> it = preferences.iterator(); it.hasNext();)
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

        /********************************************************************************
         // Constraint: ta = const: OFF1 + OFF2 + OFF3... <= MAXIMUM_OFFERINGS_TAUGHT
         // Each TA can only be assigned to a single course
         *********************************************************************************/
        int k;
        for(Ta ta : tas)
        {
            GRBLinExpr taAssignment = new GRBLinExpr();
            k = 0;
            for(TaOffering taOffering : taOfferings)
            {
                if(taOffering.getTa() == ta)
                    taAssignment.addTerm(1, tasOffG[k]);
                k++;
            }
            String cname = "TAASSIGNMENT_TA=" + ta.getId();
            model.addConstr(taAssignment, GRB.LESS_EQUAL, MAXIMUM_OFFERINGS_TAUGHT, cname);
        }//for

    }//constrain()
}