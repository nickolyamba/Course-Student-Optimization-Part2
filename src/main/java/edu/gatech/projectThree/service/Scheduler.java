package edu.gatech.projectThree.service;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.repository.OfferingRepository;
import edu.gatech.projectThree.repository.ProfessorRepository;
import edu.gatech.projectThree.repository.StudentRepository;
import edu.gatech.projectThree.repository.TaRepository;
import edu.gatech.projectThree.service.Constraint.Constraint;
import gurobi.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Service("scheduler")
public class Scheduler{

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

            // initialize gurobi variables variables here
            GRBVar[][] studentsOfferings = new GRBVar[students.size()][offerings.size()];
            GRBVar[][] professorsOfferings = new GRBVar[professors.size()][offerings.size()];
            GRBVar[][] tasOfferings = new GRBVar[tas.size()][offerings.size()];

            // Objective var
            GRBVar X = model.addVar(0, GRB.INFINITY, 0, GRB.CONTINUOUS, "X");

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

            // set objective
            GRBLinExpr obj = new GRBLinExpr();
            int priority = 0;
            Set<Preference> preferences;

            for(int i = 0; i < students.size(); i++) {
                preferences = students.get(i).getPreferences();
                for(int j = 0; j < offerings.size(); j++) {

                    priority = 5;
                    obj.addTerm(priority, studentsOfferings[i][j]);
                }
            }

            model.setObjective(obj, GRB.MINIMIZE);

            // get all constraints
            Collection<Constraint> constraints = context.getBeansOfType(Constraint.class).values();

            for (Constraint constraint : constraints) {
                constraint.addConstraint(model, studentsOfferings, professorsOfferings, tasOfferings, X, students, offerings, professors, tas);
            }

            model.optimize();
            result = model.get(GRB.DoubleAttr.ObjVal);

            // Dispose of model and environment
            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            e.printStackTrace();
        }

        return result;
    }

}
