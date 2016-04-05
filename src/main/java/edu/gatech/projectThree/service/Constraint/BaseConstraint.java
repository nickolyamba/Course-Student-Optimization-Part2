package edu.gatech.projectThree.service.Constraint;

import gurobi.*;
/**
 * Created by dawu on 3/18/16.
 */
public abstract class BaseConstraint implements Constraint {


    int studentSize;
    int courseSize;
    int semesterSize;

    // template pattern add any universal constraint logic here
    @Override
    public void addConstraint(GRBModel model, GRBVar[][][] yijk, GRBVar X) throws GRBException {
        setStudentSize(yijk.length);
        setCourseSize(yijk[0].length);
        setSemesterSize(yijk[0][0].length);
        constrain(model, yijk, X);
    }

    // each constraint can override here and add to model
    public abstract void constrain(GRBModel model, GRBVar[][][] yijk, GRBVar X) throws GRBException;

    public int getStudentSize() {
        return studentSize;
    }

    public void setStudentSize(int studentSize) {
        this.studentSize = studentSize;
    }

    public int getCourseSize() {
        return courseSize;
    }

    public void setCourseSize(int courseSize) {
        this.courseSize = courseSize;
    }

    public int getSemesterSize() {
        return semesterSize;
    }

    public void setSemesterSize(int semesterSize) {
        this.semesterSize = semesterSize;
    }
}
