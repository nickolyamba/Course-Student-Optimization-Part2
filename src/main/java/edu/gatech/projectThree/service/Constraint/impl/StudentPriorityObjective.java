package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.Application;
import edu.gatech.projectThree.constants.Seniority;
import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import gurobi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private GlobalState state;

    @Autowired
    public void setState(GlobalState state) {
        this.state = state;
    }

    // It would be great to have a slider UI to allow change of the coefficient by an admin
    // via GlobalState, or database entity (preferable)

    // value of 0.9 ensures that in case when 2 students
    // have the same priorities and only one can get into
    // the class, the one who have this course as a
    // Concentration course, gets into the class
    // Source: https://piazza.com/class/ij4blvpmdri3ou?cid=740
    private static final double CONCENTRATION_K = 1.0;

    // Coefficients for optimization based on Seniority and GPA
    private static final int SENIORITY_K = Seniority.POSTDOC.ordinal() + 1;
    private static final double GPA_K = 4.0;
    // Constraint that accounts for GPA and Seniority
    //obj.addTerm(CONCENTRATION_K*priority*(SENIORITY_K - seniority.ordinal())*(GPA_K - gpa), prefG[k]);

    // Minimization of TAs contribution coefficient
    private double TA_MIN_COEFF = 2.5; //2.4; // > 2.5 goes to the TA side on custom data sample

    @Override
    public void constrain(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings, GRBVar[] tasOfferings,
                          List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas,
                          List<TaOffering> taOfferings, List<ProfessorOffering> profOfferings, List<Preference> preferences)
                          throws GRBException {
        LOGGER.info("Config: " + state.getConfig());
        TA_MIN_COEFF = state.getConfig().getTaCoeff();
        int priority;
        int k = 0;
        // Create objective expression
        GRBLinExpr obj = new GRBLinExpr();
        Seniority seniority;
        double gpa;

        if (state.getConfig().isGpa() && state.getConfig().isSeniority())
        {
            //LOGGER.info("BOTH ARE TRUE");
            for (Preference preference : preferences)
            {
                priority = preference.getPriority();
                Student student = preference.getStudent();

                seniority = student.getSeniority();
                gpa = student.getGpa();

                Specialization specialization = student.getSpecialization();
                Course course = preference.getOffering().getCourse();

                if (specialization.getCourses().contains(course))
                    obj.addTerm(CONCENTRATION_K*priority*(SENIORITY_K - seniority.ordinal())*(GPA_K - gpa), prefG[k]);
                else
                    obj.addTerm(priority*(SENIORITY_K - seniority.ordinal())*(GPA_K - gpa), prefG[k]);

                //LOGGER.info("prefG[" + String.valueOf(preference.getId())+ "] added");
                k++;
            }//for
        }//if

        else if(state.getConfig().isGpa())
        {
            //LOGGER.info("isGpa() is TRUE");
            for (Preference preference : preferences)
            {
                priority = preference.getPriority();
                Student student = preference.getStudent();

                seniority = student.getSeniority();
                gpa = student.getGpa();

                Specialization specialization = student.getSpecialization();
                Course course = preference.getOffering().getCourse();

                if (specialization.getCourses().contains(course))
                    obj.addTerm(CONCENTRATION_K*priority*(GPA_K - gpa), prefG[k]);
                else
                    obj.addTerm(priority*(GPA_K - gpa), prefG[k]);

                //LOGGER.info("prefG[" + String.valueOf(preference.getId())+ "] added");
                k++;
            }//for
        }

        else if(state.getConfig().isSeniority())
        {
            //LOGGER.info("isSeniority() is TRUE");
            for (Preference preference : preferences)
            {
                priority = preference.getPriority();
                Student student = preference.getStudent();

                seniority = student.getSeniority();
                gpa = student.getGpa();

                Specialization specialization = student.getSpecialization();
                Course course = preference.getOffering().getCourse();

                if (specialization.getCourses().contains(course))
                    obj.addTerm(CONCENTRATION_K*priority*(SENIORITY_K - seniority.ordinal()), prefG[k]);
                else
                    obj.addTerm(priority*(SENIORITY_K - seniority.ordinal()), prefG[k]);
                //LOGGER.info("prefG[" + String.valueOf(preference.getId())+ "] added");
                k++;
            }//for
        }

        // Both False
        else
        {   //LOGGER.info("BOTH ARE FALSE");
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
            }//for
        }//else


        // Minimize number of TAs
        for(Offering offering : offerings) {
            // if taOffering contains offering
            k = 0;
            for (TaOffering taOffering : taOfferings) {
                if (taOffering.getOffering() == offering) {
                    obj.addTerm(TA_MIN_COEFF, tasOfferings[k]);
                }
                k++;
            }
        }

        model.setObjective(obj, GRB.MINIMIZE);
        model.update(); ///// ----------> remove in production

    }//constrain()
}//class