package edu.gatech.projectThree.service.Constraint;

import gurobi.*;
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
    public void addConstraint(GRBModel model, GRBVar[][][][] yijk, GRBVar X) throws GRBException {
        setStudentSize(yijk.length);
        setOfferingSize(yijk[0].length);
        setProfessorSize(yijk[0][0].length);
        setTaSize(yijk[0][0][0].length);
        constrain(model, yijk, X);
    }

    // each constraint can override here and add to model
    public abstract void constrain(GRBModel model, GRBVar[][][][] yijk, GRBVar X) throws GRBException;

    public int getStudentSize() {
        return studentSize;
    }

    public void setStudentSize(int studentSize) {
        this.studentSize = studentSize;
    }

    public int getOfferingSize() {
        return offeringSize;
    }

    public void setOfferingSize(int courseSize) {
        this.offeringSize = courseSize;
    }

    public int getProfessorSize() {
        return professorSize;
    }

    public void setProfessorSize(int professorSize) {
        this.professorSize = professorSize;
    }

    public int getTaSize() {
        return taSize;
    }

    public void setTaSize(int taSize) {
        this.taSize = taSize;
    }
}
