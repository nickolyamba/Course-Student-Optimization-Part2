package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by dawu on 4/19/16.
 */
@Component
public class ConcentrationConstraint extends BaseConstraint {

    @Override
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        for (int i = 0; i < students.size(); i++) {
            GRBLinExpr concentration = new GRBLinExpr();
            for (int j = 0; j < offerings.size(); j++) {
                Student student = students.get(i);
                Course course = offerings.get(j).getCourse();
                concentration.addTerm(1, studentsOfferings[i][j]);

                Specialization specialization = student.getSpecialization();
                Set<Course> preferredCourses = student.getPreferences().stream().map(Preference::getOffering).map(Offering::getCourse).collect(Collectors.toSet());
                if (specialization.getCourses().contains(course) && preferredCourses.contains(course)) {
                    String cname = "CONCENTRATION_Student=" + i + "_Offering=" + j;
                    model.addConstr(concentration, GRB.EQUAL, 1, cname);
                }
            }
        }
    }
}
