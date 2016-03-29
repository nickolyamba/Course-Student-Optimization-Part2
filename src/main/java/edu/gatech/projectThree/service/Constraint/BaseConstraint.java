package edu.gatech.projectThree.service.Constraint;

import gurobi.*;
/**
 * Created by dawu on 3/18/16.
 */
public abstract class BaseConstraint implements Constraint {

    // template pattern add any universal constraint logic here
    @Override
    public void addConstraint(GRBModel model, GRBVar[][][] yijk) throws GRBException {
        constrain(model, yijk);
    }

    // each constraint can override here and add to model
    public abstract void constrain(GRBModel model, GRBVar[][][] yijk);
}
