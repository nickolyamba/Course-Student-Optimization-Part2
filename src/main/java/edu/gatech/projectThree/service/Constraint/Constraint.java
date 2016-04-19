package edu.gatech.projectThree.service.Constraint;

import edu.gatech.projectThree.datamodel.entity.*;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

import java.util.List;

/**
 * Created by dawu on 3/18/16.
 */
public interface Constraint {
    void addConstraint(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings,
                       GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students,
                       List<Offering> offerings, List<Professor> professors, List<Ta> tas,
                       List<Preference> preferences) throws GRBException;
}
