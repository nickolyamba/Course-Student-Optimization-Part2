package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

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
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings,
                          GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings,
                          List<Professor> professors, List<Ta> tas, Set<Preference> preferenceList) throws GRBException {

         /********************************************************************************
         // Constraint: min*TA <= Studs <= max*TA
         // Number of TA per assigned Students is from min to max
         // Sum_(i): max*TA(i) <= Sum_(i): Stud(i) ; 1D: (offering j = const)
         *********************************************************************************/
        GRBLinExpr taMaxSumConstr;
        GRBLinExpr taMinSumConstr;
        GRBLinExpr studSumConstr;
        for (int j = 0; j < offerings.size(); j++) {

            taMaxSumConstr = new GRBLinExpr(); taMinSumConstr = new GRBLinExpr();
            for (int i = 0; i < tas.size(); i++) {
                //check if ta is in TaPool for this course
                Set<TaOffering> taOfferings = tas.get(i).getTaOfferings();
                for(TaOffering taOffering : taOfferings)
                {
                    if(taOffering.getOffering().getId() == offerings.get(j).getId())
                    {
                        taMaxSumConstr.addTerm(maxStudsPerTa, tasOfferings[i][j]);
                        taMinSumConstr.addTerm(minStudsPerTa, tasOfferings[i][j]);
                    }
                }
            }//for i, tas

            //Stud1 + Stud2 + Stud3
            studSumConstr = new GRBLinExpr();
            for (int i = 0; i < students.size(); i++) {
                //check if stud has Preference for this course
                //Set<Preference> preferences = students.get(i).getPreferences();
                for(Preference preference : preferenceList)
                {
                    if (preference.getStudent().getId() == students.get(i).getId() &&
                            preference.getOffering().getId() == offerings.get(j).getId())
                    {
                        studSumConstr.addTerm(1, studentsOfferings[i][j]);
                    }
                }
            }//for i ,studs

            String cMsxName = "TaMaxRatio_Offering=" + offerings.get(j).getId();
            String cMinName = "TaMinRatio_Offering=" + offerings.get(j).getId();
            model.addConstr(studSumConstr, GRB.LESS_EQUAL, taMaxSumConstr, cMsxName);
            model.addConstr(studSumConstr, GRB.GREATER_EQUAL, taMinSumConstr, cMinName);
        }//for offerings


        // Each TA can only be assigned once
        for (int i = 0; i < tas.size(); i++) {
            GRBLinExpr taAssignment = new GRBLinExpr();
            for (int j = 0; j < offerings.size(); j++) {
                taAssignment.addTerm(1, tasOfferings[i][j]);
            }
            String cname = "TAASSIGNMENT_TA=" + tas.get(i).getId();
            model.addConstr(taAssignment, GRB.LESS_EQUAL, MAXIMUM_OFFERINGS_TAUGHT, cname);
        }//for
    }
}