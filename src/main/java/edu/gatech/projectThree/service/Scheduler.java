package edu.gatech.projectThree.service;

import edu.gatech.projectThree.Application;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by dawu on 3/18/16.
 */
@Service("scheduler")
public class Scheduler{
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

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

    @Transactional
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

            // initialize gurobi variables variables here
            GRBVar[][] studentsOfferings = new GRBVar[students.size()][offerings.size()];
            GRBVar[][] professorsOfferings = new GRBVar[professors.size()][offerings.size()];
            GRBVar[][] tasOfferings = new GRBVar[tas.size()][offerings.size()];

            // Create objective expression
            GRBLinExpr obj = new GRBLinExpr();

            // Name to identify Gurobi variables for optimization analysis
            String gvar_name = "";

            for (int j = 0; j < offerings.size(); j++) {
                for (int i = 0; i < students.size(); i++) {
                    gvar_name = "OF_" + String.valueOf(j+1) + // offering
                                "ST_" + String.valueOf(i+1);  // student

                    studentsOfferings[i][j] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                }

                for (int i = 0; i < professors.size(); i++) {
                    gvar_name = "OF_" + String.valueOf(j+1) + // offering
                                "PR_" + String.valueOf(i+1);  // professor
                    professorsOfferings[i][j] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                }

                for (int i = 0; i < tas.size(); i++) {
                    gvar_name = "OF_" + String.valueOf(j+1) + // offering
                                "TA_" + String.valueOf(i+1);  // ta
                    tasOfferings[i][j] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                }
            }

            model.update();

            model.setObjective(obj, GRB.MINIMIZE);

            // get all constraints
            Collection<Constraint> constraints = context.getBeansOfType(Constraint.class).values();

            for (Constraint constraint : constraints) {
                constraint.addConstraint(model, studentsOfferings, professorsOfferings, tasOfferings, obj, students, offerings, professors, tas);
            }

            model.update();// for obj

            model.optimize();

            double[][] stud_offer = model.get(GRB.DoubleAttr.X, studentsOfferings);// not sure if it's correct source
            double[][] prof_offer = model.get(GRB.DoubleAttr.X, professorsOfferings);
            double[][] ta_offer = model.get(GRB.DoubleAttr.X, tasOfferings);

            LOGGER.info("Students assigned:");
            LOGGER.info("-------------------------------");
            for (int j = 0; j < offerings.size(); j++) {
                for (int i = 0; i < students.size(); i++) {
                    if(stud_offer[i][j] > 0)
                        LOGGER.info("off_stud["+ String.valueOf(i) +"]"+"["+ String.valueOf(j) + "]=" + String.valueOf(stud_offer[i][j]));
                }
            }
            LOGGER.info("");

            // Dispose of model and environment
            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            e.printStackTrace();
        }

        return result;
    }

}
