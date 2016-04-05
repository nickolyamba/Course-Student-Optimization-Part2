package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import edu.gatech.projectThree.service.Constraint.Constraint;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * Created by dawu on 3/19/16.
 */
public class ExampleConstraint extends BaseConstraint implements Constraint {

    @Override
    public void constrain(GRBModel model, GRBVar[][][] yijk) {

    }
}
