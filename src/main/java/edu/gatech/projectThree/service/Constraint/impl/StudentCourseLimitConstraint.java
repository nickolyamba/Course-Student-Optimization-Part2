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
    public void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
       for (int i = 0; i < students.size(); i++) {
           //if(students.get(i).getPreferences().isEmpty())//if no preferences by student
           //    continue;//
           GRBLinExpr maxCourses = new GRBLinExpr();
           for (int j = 0; j < offerings.size(); j++) {
               if(offerings.get(j).getPreferences().isEmpty()) //if none signed up for course
                   continue;//
               maxCourses.addTerm(1, studentsOfferings[i][j]);
           }
           String cname = "MAXCOURSES_Student=" + i;
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
           if (foundationalRequirement) {
               model.addConstr(maxCourses, GRB.EQUAL, 3, cname);
           } else {
               model.addConstr(maxCourses, GRB.EQUAL, 2, cname);
           }
       }
    }
}
