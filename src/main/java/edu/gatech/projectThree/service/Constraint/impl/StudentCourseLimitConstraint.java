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
 * Students can only take two courses until foundational done
 */
@Component
public class StudentCourseLimitConstraint extends BaseConstraint {

    private static final int FOUNDATIONAL_STUDENT = 1;


    @Override
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings, GRBVar[] tasOfferings,
                          List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas,
                          List<TaOffering> taOfferings, List<ProfessorOffering> profOfferings, List<Preference> preferenceList)
                          throws GRBException {

        for(Student student : students)
        {
            GRBLinExpr maxCourses = new GRBLinExpr();
            int k = 0;
            for (Iterator<Preference> it = preferenceList.iterator(); it.hasNext();)
            {
                Preference preference = it.next();
                if(preference.getStudent() == student)
                    maxCourses.addTerm(1, prefG[k]);

                k++;
            }//for

            // Find if foundational courses are taken
            //Student student = preference.getStudent();
            Set<Course> coursesTaken = student.getCoursesTaken();
            boolean foundationalRequirement = false;
            int count = 0;
            for (Course course : coursesTaken) {
                if (course.isFoundational()) {
                    count++;
                    if (count >= 2) {
                        foundationalRequirement = true;
                        break;
                    }
                }
            }

            String cname = "MAXCOURSES_Student=" + student.getId();
            if (foundationalRequirement) {
                model.addConstr(maxCourses, GRB.EQUAL, // in case # preferences < 3
                        Math.min(3, student.getPreferences().size()), cname);
            } else {
                model.addConstr(maxCourses, GRB.EQUAL, // in case # preferences < 2
                        Math.min(2, student.getPreferences().size()), cname);
            }
        }//for stud
    }
}