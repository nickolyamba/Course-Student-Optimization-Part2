package edu.gatech.projectThree.service.Constraint.impl;

import edu.gatech.projectThree.datamodel.entity.Course;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.service.Constraint.BaseConstraint;
import edu.gatech.projectThree.service.Constraint.Constraint;
import gurobi.GRBModel;
import gurobi.GRBVar;

/**
 * Created by dawu on 3/19/16.
 */
public class ExampleConstraint extends BaseConstraint implements Constraint {

    @Override
    public void constrain(GRBVar[][][] yijk) {

    }
}
