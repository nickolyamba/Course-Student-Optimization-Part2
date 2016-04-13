package edu.gatech.projectThree.service;

import edu.gatech.projectThree.datamodel.dao.impl.*;
import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Student;
import edu.gatech.projectThree.datamodel.entity.Ta;
import edu.gatech.projectThree.repository.OfferingRepository;
import edu.gatech.projectThree.repository.ProfessorRepository;
import edu.gatech.projectThree.repository.StudentRepository;
import edu.gatech.projectThree.repository.TaRepository;
import edu.gatech.projectThree.service.Constraint.Constraint;
import gurobi.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private StudentRepository studRepository;
    private OfferingRepository offeringRepository;
    private ProfessorRepository profRepository;
    private TaRepository taRepository;

    @Autowired
    private ApplicationContext context;

    //http://stackoverflow.com/questions/21553120/how-does-applicationcontextaware-work-in-spring
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Autowired
    public void setStudentRepository(StudentRepository studRepository) {
        this.studRepository = studRepository;
    }

    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) {
        this.offeringRepository = offeringRepository;
    }

    @Autowired
    public void setProfRepository(ProfessorRepository profRepository) {
        this.profRepository = profRepository;
    }

    @Autowired
    public void setTaRepository(TaRepository taRepository) {
        this.taRepository = taRepository;
    }

    public double schedule() {
        double result = 0;
        try {
            GRBEnv env = new GRBEnv("mip1.log");
            env.set(GRB.IntParam.LogToConsole, 1);
            GRBModel model = new GRBModel(env);

            List<Student> students = studRepository.findAll();

            List<Offering> offerings = offeringRepository.findAll();

            List<Professor> professors = profRepository.findAll();

            List<Ta> tas = taRepository.findAll();


            GRBVar[][][][] grbVars = new GRBVar[students.size()][offerings.size()][professors.size()][tas.size()];
            // initialize gurobi variables variables here
            for (int i = 0; i < students.size(); i++) {
                for (int j = 0; j < offerings.size(); j++) {
                    for (int k = 0; k < professors.size(); k++) {
                        for (int z = 0; z < tas.size(); z++) {
                            GRBVar grbVar = model.addVar(0, 1, 0.0, GRB.BINARY, "");
                            grbVars[i][j][k][z] = grbVar;
                        }
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
                constraint.addConstraint(model, grbVars, X, students, offerings, professors, tas);
            }

            model.optimize();
            result = model.get(GRB.DoubleAttr.ObjVal);
        } catch (GRBException e) {
            e.printStackTrace();
        }

        return result;
    }

}
