package edu.gatech.projectThree.service.Constraint;

import edu.gatech.projectThree.datamodel.entity.*;
import gurobi.GRBException;
import gurobi.GRBModel;
import gurobi.GRBVar;

import java.util.List;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
public interface Constraint {
    void addConstraint(GRBModel model, GRBVar[] prefG, GRBVar[] professorsOfferings,
                       GRBVar[] tasOfferings, List<Student> students,
                       List<Offering> offerings, List<Professor> professors, List<Ta> tas,
                       List<TaOffering> taOfferings,
                       Set<Preference> preferences) throws GRBException;
}
