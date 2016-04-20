package edu.gatech.projectThree.service.Constraint;

import edu.gatech.projectThree.datamodel.entity.*;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

import java.util.List;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
public interface Constraint {
    void addConstraint(GRBModel model, GRBVar[][] studentsOfferings, GRBVar[][] professorsOfferings,
                       GRBVar[][] tasOfferings, GRBLinExpr obj, List<Student> students,
                       List<Offering> offerings, List<Professor> professors, List<Ta> tas,
                       Set<Preference> preferences) throws GRBException;
}
