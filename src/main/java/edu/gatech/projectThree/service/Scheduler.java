package edu.gatech.projectThree.service;

import edu.gatech.projectThree.datamodel.dao.impl.CourseDAO;
import edu.gatech.projectThree.datamodel.dao.impl.OfferingDAO;
import edu.gatech.projectThree.datamodel.dao.impl.ProfessorDAO;
import edu.gatech.projectThree.datamodel.dao.impl.StudentDAO;
import edu.gatech.projectThree.datamodel.entity.Course;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.repository.StudentRepository;
import edu.gatech.projectThree.service.Constraint.Constraint;
import gurobi.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by dawu on 3/18/16.
 */
@Service("scheduler")
public class Scheduler {

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

    private StudentRepository studRepository;

    @Autowired
    public void setStudentRepository(StudentRepository studRepository) {
        this.studRepository = studRepository;
    }

    public double schedule(Constraint[] constraints) {
        double result = 0;
        try {
            GRBEnv env = new GRBEnv("mip1.log");
            //env.set(GRB.IntParam.LogToConsole, 1);
            final GRBModel model = new GRBModel(env);

            //Student[] students = //studentDAO.getAll();
            ArrayList<Student> studs = studRepository.findAll();
            Student[] students = new Student[studs.size()];
            students = studs.toArray(students);

            Course[] courses = courseDAO.getAll();
            Professor[] professors = professorDAO.getAll(); // Teacher? I've seen in piazza someone

            GRBVar[][][] yijk = new GRBVar[students.length][courses.length][professors.length];
            // initialize variables here

            for (Constraint constraint : constraints) {
                constraint.addConstraint(model, yijk);
            }

            model.optimize();
            result = model.get(GRB.DoubleAttr.ObjVal);
        } catch (GRBException e) {
            e.printStackTrace();
        }

        return result;
    }

}
