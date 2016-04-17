package edu.gatech.projectThree.service;

import edu.gatech.projectThree.Application;
import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.repository.*;
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
import java.util.Iterator;
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
    private PreferenceRepository prefRepository;
    private CurrentSemesterRepository currentSemesterRepository;
    private ProfessorOfferingRepository profOfferingRepository;
    private TaOfferingRepository taOfferingRepository;

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

    @Autowired
    public void setPrefRepository(PreferenceRepository prefRepository) {
        this.prefRepository = prefRepository;
    }

    @Autowired
    public void setCurrentSemesterRepository(CurrentSemesterRepository currentSemesterRepository) {
        this.currentSemesterRepository = currentSemesterRepository;
    }

    @Autowired
    public void setProfessorOffering(ProfessorOfferingRepository profOfferingRepository) {
        this.profOfferingRepository = profOfferingRepository;
    }

    @Autowired
    public void setTaOffering(TaOfferingRepository taOfferingRepository) {
        this.taOfferingRepository = taOfferingRepository;
    }

    @Transactional
    public double schedule() {
        double result = 0;
        try {
            GRBEnv env = new GRBEnv("mip1.log");
            env.set(GRB.IntParam.LogToConsole, 1);
            GRBModel model = new GRBModel(env);

            // get current semester
            CurrentSemester currSemester = currentSemesterRepository.findTopByOrderBySemesterIdDesc();
            List<Offering> offerings = offeringRepository.findBySemester(currSemester.getSemester());

            // remove offerings that has no preferences
            for (Iterator<Offering> it = offerings.iterator(); it.hasNext();)
            {
                Offering offering = it.next();
                if(offering.getPreferences().isEmpty())
                    it.remove();
            }

            // get association classes corresponding to Offerings
            List<Preference> preferences = prefRepository.findByOfferingIn(offerings);
            List<ProfessorOffering> profOfferings = profOfferingRepository.findByOfferingIn(offerings);
            List<TaOffering> taOfferings = taOfferingRepository.findByOfferingIn(offerings);

            // get Studs, Profs, and Tas
            List<Student> students = studRepository.findDistinctByPreferencesIn(preferences);
            List<Professor> professors = profRepository.findDistinctByProfOfferingsIn(profOfferings);
            List<Ta> tas = taRepository.findDistinctByTaOfferingsIn(taOfferings);

            LOGGER.info("Offerings requested:");
            LOGGER.info("-------------------------------");
            for (Offering offering : offerings) {
                LOGGER.info(String.valueOf(offering.getId()));
            }
            LOGGER.info("");

            LOGGER.info("Studs found with findByPreferenceIn():");
            LOGGER.info("-------------------------------");
            for (Student student : students) {
                LOGGER.info("stud["+String.valueOf(student.getId())+"] preferences:");
                for (Preference preference : student.getPreferences())
                    LOGGER.info("priority = "+ String.valueOf(preference.getPriority())+
                            "  offering=" + String.valueOf(preference.getOffering().getId()));
                LOGGER.info("\n");
            }
            LOGGER.info("");

            LOGGER.info("TAs requested:");
            LOGGER.info("-------------------------------");
            for (Ta ta : tas) {
                LOGGER.info(String.valueOf(ta.getId()));
            }
            LOGGER.info("");

            LOGGER.info("Profs requested:");
            LOGGER.info("-------------------------------");
            for (Professor prof : professors) {
                LOGGER.info(String.valueOf(prof.getId()));
            }
            LOGGER.info("");

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
                    gvar_name = "OF_" + String.valueOf(j) + // offering
                                "ST_" + String.valueOf(i);  // student

                    studentsOfferings[i][j] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                }

                for (int i = 0; i < professors.size(); i++) {
                    gvar_name = "OF_" + String.valueOf(j) + // offering
                                "PR_" + String.valueOf(i);  // professor
                    professorsOfferings[i][j] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                }

                for (int i = 0; i < tas.size(); i++) {
                    gvar_name = "OF_" + String.valueOf(j) + // offering
                                "TA_" + String.valueOf(i);  // ta
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

            double[][] stud_offer = model.get(GRB.DoubleAttr.X, studentsOfferings);
            double[][] prof_offer = model.get(GRB.DoubleAttr.X, professorsOfferings);
            double[][] ta_offer = model.get(GRB.DoubleAttr.X, tasOfferings);

            LOGGER.info("Students assigned:");
            LOGGER.info("-------------------------------");
            for (int i = 0; i < students.size(); i++) {
                for (int j = 0; j < offerings.size(); j++) {
                    //if(stud_offer[i][j] > 0)
                        LOGGER.info("stud_offer["+ String.valueOf(students.get(i).getId()) +"]"+
                                    "["+ String.valueOf(offerings.get(j).getId()) + "]=" +
                                    String.valueOf(stud_offer[i][j]));
                }
                LOGGER.info("\n");
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
