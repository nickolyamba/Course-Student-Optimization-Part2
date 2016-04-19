package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.springframework.stereotype.Component;

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
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings,
                          GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings,
                          List<Professor> professors, List<Ta> tas, List<Preference> preferenceList) throws GRBException {
       for (int i = 0; i < students.size(); i++) {
           GRBLinExpr maxCourses = new GRBLinExpr();
           for (int j = 0; j < offerings.size(); j++) {

               //check if stud has Preference for this course
               //Set<Preference> preferences = offerings.get(j).getPreferences();
               for(Preference preference : preferenceList)
               {
                   if(preference.getStudent().getId() == students.get(i).getId() &&
                           preference.getOffering().getId() == offerings.get(j).getId())
                       maxCourses.addTerm(1, studentsOfferings[i][j]);
               }
           }

           // Find if foundational courses are taken
           Student student = students.get(i);
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

           String cname = "MAXCOURSES_Student=" + students.get(i).getId();
           if (foundationalRequirement) {
               model.addConstr(maxCourses, GRB.EQUAL, // in case # preferences < 3
                       Math.min(3, student.getPreferences().size()), cname);
           } else {
               model.addConstr(maxCourses, GRB.EQUAL, // in case # preferences < 2
                       Math.min(2, student.getPreferences().size()), cname);
           }
       }
    }
}