package edu.gatech.projectThree.service.Constraint;

import edu.gatech.projectThree.datamodel.entity.*;
import gurobi.*;

import java.util.List;

/**
 * Created by dawu on 3/18/16.
 */
public abstract class BaseConstraint implements Constraint {


    int studentSize;
    int offeringSize;
    int professorSize;
    int taSize;

    // template pattern add any universal constraint logic here
    @Override
    public void addConstraint(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings, GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas, List<Preference> preferences) throws GRBException {
        constrain(model, studentsOfferings, professorsOfferings, tasOfferings, obj, students, offerings, professors, tas, preferences);
    }

    // each constraint can override here and add to model
    public abstract void constrain(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings,
                                   GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students, List<Offering> offerings,
                                   List<Professor> professors, List<Ta> tas, List<Preference> preferences) throws GRBException;

}
