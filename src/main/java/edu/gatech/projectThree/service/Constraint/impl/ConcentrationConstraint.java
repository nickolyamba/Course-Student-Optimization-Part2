package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dawu on 4/19/16.
 */
@Component
public class ConcentrationConstraint extends BaseConstraint {

    @Override
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings, GRBVar[] tasOfferings, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas, List<TaOffering> taOfferings, List<ProfessorOffering> profOfferings, List<Preference> preferences) throws GRBException {

        for (Student student : students) {
            GRBLinExpr concentration = new GRBLinExpr();
            Specialization specialization = student.getSpecialization();
            boolean isConcentration = false;
            for (int j = 0; j < preferences.size(); j++) {
                Preference preference = preferences.get(j);
                Course course = preference.getOffering().getCourse();

                if (preference.getStudent() == student &&
                    specialization.getCourses().contains(course)) {
                    concentration.addTerm(1, prefG[j]);
                    isConcentration = true;
                }
            }
            //if student doesn't have any preference with the Concentration,
            // don't addConstr; Otherwise, get CONCENTRATION_Student=3: = 1 (infeasible)
            if(isConcentration)
            {
                String cname = "CONCENTRATION_Student=" + student.getId();
                model.addConstr(concentration, GRB.EQUAL, 1, cname);
            }
        }

    }
}
