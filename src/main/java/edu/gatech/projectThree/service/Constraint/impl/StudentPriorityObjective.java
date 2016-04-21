package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.Application;
import edu.gatech.projectThree.constants.Seniority;
import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dawu on 4/12/16.
 * Require N teaching assistants for every M student capacity in course
 * Each TA can only be assigned to a single course
 */
@Component
public class StudentPriorityObjective extends BaseConstraint {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    // value of 0.9 ensures that in case when 2 students
    // have the same priorities and only one can get into
    // the class, the one who have this course as a
    // Concentration course, gets into the class
    // Source: https://piazza.com/class/ij4blvpmdri3ou?cid=740
    private static final double CONCENTRATION_K = 0.9;

    // Coefficients for optimization based on Seniority and GPA
    private static final int SENIORITY_K = Seniority.POSTDOC.ordinal() + 1;
    private static final double GPA_K = 4.0;
    // Constraint that also accounts for GPA and Seniority
    //obj.addTerm(CONCENTRATION_K*priority*(SENIORITY_K - seniority.ordinal())*(GPA_K - gpa), prefG[k]);

    @Override
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings, GRBVar[] tasOfferings,
                          List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas,
                          List<TaOffering> taOfferings, List<ProfessorOffering> profOfferings, List<Preference> preferences)
                          throws GRBException {

        int priority;
        int k = 0;
        // Create objective expression
        GRBLinExpr obj = new GRBLinExpr();
        Seniority seniority;
        double gpa;
        for (Preference preference : preferences)
        {
            priority = preference.getPriority();
            Student student = preference.getStudent();

            seniority = student.getSeniority();
            gpa = student.getGpa();

            Specialization specialization = student.getSpecialization();
            Course course = preference.getOffering().getCourse();

            if (specialization.getCourses().contains(course))
                obj.addTerm(CONCENTRATION_K*priority, prefG[k]);
            else
                obj.addTerm(priority, prefG[k]);
            //LOGGER.info("prefG[" + String.valueOf(preference.getId())+ "] added");
            k++;
        }

        model.setObjective(obj, GRB.MINIMIZE);
        model.update(); ///// ----------> remove in production

    }//constrain()
}//class