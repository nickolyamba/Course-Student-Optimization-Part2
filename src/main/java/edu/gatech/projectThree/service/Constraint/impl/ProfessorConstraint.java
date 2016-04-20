package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * Created by dawu on 4/12/16.
 * At least one professor per offering
 * Each professor can only teach one offering
 */
@Component
public class ProfessorConstraint extends BaseConstraint {

    private static final int MAX_PROFESSORS = 2;
    private static final int MAXIMUM_OFFERINGS_TAUGHT = 5;
    private static final int MAX_PROF_RATIO = 1000000;

    /********************************************************************************
     // Constraint: offering = const: PROF1 + PROF2 + PROF3... <= MAXIMUM_OFFERINGS_TAUGHT
     // Each course can include at least MINIMUM_PROFESSORS
     *********************************************************************************/
    @Override
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] profG, GRBVar[] tasOfferings,
                          List<Student> students, List<Offering> offerings,
                          List<Professor> professors, List<Ta> tas, List<TaOffering> taOfferings,
                          List<ProfessorOffering> profOfferings, List<Preference> preferences) throws GRBException {

        for(Offering offering : offerings)
        {
            GRBLinExpr minProfessor = new GRBLinExpr();
            int k = 0;
            for (ProfessorOffering profOffering : profOfferings)
            {
                if(profOffering.getOffering() == offering)
                {
                    minProfessor.addTerm(1, profG[k]);
                }
                k++;
            }
            String cname = "MINPROF_OFFER=" + offering.getId();
            model.addConstr(minProfessor, GRB.LESS_EQUAL, MAX_PROFESSORS, cname);
        }


        /********************************************************************************
         // Constraint: prof = const: OFF1 + OFF2 + OFF3... <= MAXIMUM_OFFERINGS_TAUGHT
         // Each Prof MAXIMUM_OFFERINGS_TAUGHT
         *********************************************************************************/
        int k;
        for(Professor professor : professors)
        {
            GRBLinExpr maxOfferingsTaught = new GRBLinExpr();
            k = 0;
            for(ProfessorOffering profOffering : profOfferings)
            {
                if(profOffering.getProfessor() == professor)
                    maxOfferingsTaught.addTerm(1, profG[k]);
                k++;
            }
            String cname = "MAXTAUGHT_PROF=" + professor.getId();
            model.addConstr(maxOfferingsTaught, GRB.LESS_EQUAL, MAXIMUM_OFFERINGS_TAUGHT, cname);
        }//for


        /********************************************************************************
         // Constraint: Sum  Professor * MAX_PROF_RATIO >= Sum Studs => Sum Professor
         // Number of Studs more tham number of Profs in a offering (if no stud, no prof)
         *********************************************************************************/
        GRBLinExpr profMinSumConstr;
        GRBLinExpr profMaxSumConstr;
        GRBLinExpr studSumConstr;

        for(Offering offering : offerings)
        {
            profMinSumConstr = new GRBLinExpr();
            profMaxSumConstr = new GRBLinExpr();
            // if ProfOffering contains offering
            k = 0;
            for (ProfessorOffering profOffering : profOfferings)
            {
                if(profOffering.getOffering() == offering)
                {
                    profMinSumConstr.addTerm(1, profG[k]);
                    profMaxSumConstr.addTerm(MAX_PROF_RATIO, profG[k]);
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

            String cMinName = "ST_MIN_PROF_Offering=" + offering.getId();
            String cMaxName = "ST_MAX_PROF_Offering=" + offering.getId();
            model.addConstr(studSumConstr, GRB.GREATER_EQUAL, profMinSumConstr, cMinName);
            model.addConstr(studSumConstr, GRB.LESS_EQUAL, profMaxSumConstr, cMaxName);
        }//for offerings

    }
}
