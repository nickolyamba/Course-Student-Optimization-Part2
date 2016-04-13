package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;

import java.util.List;
import java.util.Set;

/**
 * Created by dawu on 4/12/16.
 * Students can only take two courses until foundational done
 */
public class StudentCourseLimitConstraint extends BaseConstraint {

    private static final int FOUNDATIONAL_STUDENT = 1;

    @Override
    public void constrain(GRBModel model, GRBVar[][][][] grbVars, GRBVar X, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        for (int i = 0; i < getStudentSize(); i++) {
            for (int k = 0; k < getProfessorSize(); k++) {
                for (int z = 0; z < getTaSize(); z++) {
                    GRBLinExpr maxCourses = new GRBLinExpr();
                    for (int j = 0; j < getOfferingSize(); j++) {
                        maxCourses.addTerm(1, grbVars[i][j][k][z]);
                    }
                    String cname = "MAXCOURSES_Student="+i+"_Professor="+k+"_Ta="+z;
                    Student student = students.get(i);
                    Set<Course> coursesTaken = student.getCoursesTaken();
                    boolean foundationalRequirement = false;
                    int count = 0;
                    for (Course course : coursesTaken) {
                        // TODO: increment count if course is foundational
                        if (count == 2) {
                            foundationalRequirement = true;
                            break;
                        }
                    }
                    if (foundationalRequirement) {
                        model.addConstr(maxCourses, GRB.LESS_EQUAL, 2, cname);
                    } else {
                        model.addConstr(maxCourses, GRB.LESS_EQUAL, 3, cname);
                    }
                }
            }
        }

    }
}
