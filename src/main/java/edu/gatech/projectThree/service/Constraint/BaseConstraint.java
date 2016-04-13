package edu.gatech.projectThree.service.Constraint;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.datamodel.entity.Ta;
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
    public void addConstraint(GRBModel model, GRBVar[][][][] grbVars, GRBVar X, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException {
        setStudentSize(grbVars.length);
        setOfferingSize(grbVars[0].length);
        setProfessorSize(grbVars[0][0].length);
        setTaSize(grbVars[0][0][0].length);
        constrain(model, grbVars, X, students, offerings, professors, tas);
    }

    // each constraint can override here and add to model
    public abstract void constrain(GRBModel model, GRBVar[][][][] grbVars, GRBVar X, List<Student> students, List<Offering> offerings, List<Professor> professors, List<Ta> tas) throws GRBException;

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
