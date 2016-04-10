package edu.gatech.projectThree.service;

import edu.gatech.projectThree.datamodel.dao.impl.*;
import edu.gatech.projectThree.datamodel.entity.Course;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Semester;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.service.Constraint.Constraint;
import gurobi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by dawu on 3/18/16.
 */
@Service("scheduler")
public class Scheduler{

    @Autowired
    @Qualifier("studentDAO")
    StudentDAO studentDAO;

    @Autowired
    @Qualifier("courseDAO")
    CourseDAO courseDAO;

    @Autowired
    @Qualifier("professorDAO")
    ProfessorDAO professorDAO;
    
    @Autowired
    @Qualifier("offeringDAO")
    OfferingDAO offeringDAO;

    @Autowired
    @Qualifier("semesterDAO")
    SemesterDAO semesterDAO;

    @Autowired
    private ApplicationContext context;

    /* http://stackoverflow.com/questions/21553120/how-does-applicationcontextaware-work-in-spring
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }*/


    // TODO: Redesign constraint template to not use multi-dimensional arrays
    public double schedule() {
        double result = 0;
        try {
            GRBEnv env = new GRBEnv("mip1.log");
            env.set(GRB.IntParam.LogToConsole, 1);
            GRBModel model = new GRBModel(env);

            Student[] students = studentDAO.getAll();
            Course[] courses = courseDAO.getAll();
            Professor[] professors = professorDAO.getAll();
            Semester[] semesters = semesterDAO.getAll();

            GRBVar[][][] yijk = new GRBVar[students.length][courses.length][semesters.length];
            // initialize variables here
            for (int i = 0; i < students.length; i++) {
                for (int j = 0; j < courses.length; j++) {
                    for (int k = 0; k < semesters.length; k++) {
                        GRBVar grbVar = model.addVar(0, 1, 0.0, GRB.BINARY, "");
                        yijk[i][j][k] = grbVar;
                    }
                }
            }

            GRBVar X = model.addVar(0, GRB.INFINITY, 0, GRB.CONTINUOUS, "X");
            model.update();
            GRBLinExpr obj = new GRBLinExpr();
            obj.addTerm(1, X);
            model.setObjective(obj);

            Collection<Constraint> constraints = context.getBeansOfType(Constraint.class).values();

            for (Constraint constraint : constraints) {
                constraint.addConstraint(model, yijk, X);
            }

            model.optimize();
            result = model.get(GRB.DoubleAttr.ObjVal);
        } catch (GRBException e) {
            e.printStackTrace();
        }

        return result;
    }

}
