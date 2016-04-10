package edu.gatech.projectThree.service.Constraint;

import gurobi.GRBException;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * Created by dawu on 3/18/16.
 */
public interface Constraint {
    void addConstraint(GRBModel model, GRBVar[][][][] yijk, GRBVar X) throws GRBException;
}
