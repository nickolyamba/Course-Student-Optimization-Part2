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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    private OptimizedTimeRepository optimizedTimeRepository;
    private RequestRepository requestRepository;
    private GlobalState state;

    @Autowired
    private ApplicationContext context;

    //http://stackoverflow.com/questions/21553120/how-does-applicationcontextaware-work-in-spring
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Autowired
    public void setStudRepository(StudentRepository studRepository) {
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
    public void setProfOfferingRepository(ProfessorOfferingRepository profOfferingRepository) {
        this.profOfferingRepository = profOfferingRepository;
    }

    @Autowired
    public void setTaOfferingRepository(TaOfferingRepository taOfferingRepository) {
        this.taOfferingRepository = taOfferingRepository;
    }

    @Autowired
    public void setOptimizedTimeRepository(OptimizedTimeRepository optimizedTimeRepository) {
        this.optimizedTimeRepository = optimizedTimeRepository;
    }

    @Autowired
    public void setRequestRepository(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Autowired
    public void setState(GlobalState state) {
        this.state = state;
    }

    @Transactional
    @Async
    public double schedule() {
        double result = 0;
        try {
            GRBEnv env = new GRBEnv("mip1.log");
            env.set(GRB.IntParam.LogToConsole, 1);
            GRBModel model = new GRBModel(env);

            // get current semester
            // CurrentSemester currSemester = currentSemesterRepository.findTopByOrderBySemesterIdDesc();
            CurrentSemester currSemester = state.getCurrentSemObject();
            //can't fetch from state - returns lazyException. when accessing Preferences in a offering object
            List<Offering> offerings = offeringRepository.findBySemesterOrderByIdAsc(currSemester.getSemester());

            // remove offerings that has no preferences
            for (Iterator<Offering> it = offerings.iterator(); it.hasNext();)
            {

                Offering offering = (Offering)it.next();
                if(offering.getPreferences().isEmpty())
                    it.remove();
            }

            List<Request> requests = requestRepository.findLastReuestsByStudent();

            // get association classes corresponding to Offerings
            Set<Preference> preferences = prefRepository.findByOfferingInAndRequestIn(offerings, requests);
            List<ProfessorOffering> profOfferings = profOfferingRepository.findByOfferingIn(offerings);
            List<TaOffering> taOfferings = taOfferingRepository.findByOfferingIn(offerings);

            // get Studs, Profs, and Tas
            List<Student> students = studRepository.findDistinctByPreferencesInAndRequestsInOrderByIdAsc(preferences, requests);
            List<Professor> professors = profRepository.findDistinctByProfOfferingsInOrderByIdAsc(profOfferings);
            List<Ta> tas = taRepository.findDistinctByTaOfferingsInOrderByIdAsc(taOfferings);

            showLogs(requests, preferences, students, offerings, professors, tas);

            // initialize gurobi variables variables here
            GRBVar[] profG = new GRBVar[profOfferings.size()];
            GRBVar[] prefG = new GRBVar[preferences.size()];
            GRBVar[] taG = new GRBVar[taOfferings.size()];

            String gvar_name;

            int k = 0;
            for (Preference preference : preferences)
            {
                gvar_name = "PREF_"+ preference.getId();// preference

                prefG[k] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                LOGGER.info("prefG[" + String.valueOf(preference.getId())+ "]");
                k++;
            }
            model.update();

            k = 0;
            for (TaOffering taOffering : taOfferings)
            {
                gvar_name = "TA_"+ taOffering.getId();

                taG[k] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                LOGGER.info("taG[" + String.valueOf(taOffering.getId())+ "]");
                k++;
            }
            model.update();

            k = 0;
            for (ProfessorOffering profOffering : profOfferings)
            {
                gvar_name = "PROF_"+ profOffering.getId();// preference

                profG[k] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                LOGGER.info("profG[" + String.valueOf(profOffering.getId())+ "]");
                k++;
            }
            
            // get all constraints
            Collection<Constraint> constraints = context.getBeansOfType(Constraint.class).values();

            for (Constraint constraint : constraints) {
                constraint.addConstraint(model, prefG, profG, taG,
                                        students, offerings, professors, tas, taOfferings, preferences);
            }

            model.update();//                              ------> comment out for production
            model.write("constraints.lp"); // constraints  ------> comment out for production
            model.optimize();

            // model.computeIIS();
            // model.write("infeasible.ilp");

            model.write("solution.sol"); // solution       ------> comment out for production

            double[] prefArray = model.get(GRB.DoubleAttr.X, prefG);
            double[] taOfferArray = model.get(GRB.DoubleAttr.X, taG);
            double[] profOfferArray = model.get(GRB.DoubleAttr.X, profG);
            double objectiveValue = model.get(GRB.DoubleAttr.ObjVal);

            // save results in the database
            //updatePreferences(preferences, students, offerings, stud_offer, currSemester);

            /*
            LOGGER.info("Profs assigned:");
            LOGGER.info("-------------------------------");
            for (int i = 0; i < professors.size(); i++) {
                for (int j = 0; j < offerings.size(); j++) {
                    if(prof_offer[i][j] > 0)
                    LOGGER.info("prof_offer["+ String.valueOf(professors.get(i).getId()) +"]"+
                            "["+ String.valueOf(offerings.get(j).getId()) + "]=" +
                            String.valueOf(prof_offer[i][j]));
                }
                LOGGER.info("\n");
            }
            LOGGER.info("");

            */

            /*
            LOGGER.info("TAs assigned:");
            LOGGER.info("-------------------------------");
            for (int i = 0; i < tas.size(); i++) {
                for (int j = 0; j < offerings.size(); j++) {
                    if(ta_offer[i][j] > 0)
                    LOGGER.info("ta_offer["+ String.valueOf(tas.get(i).getId()) +"]"+
                            "["+ String.valueOf(offerings.get(j).getId()) + "]=" +
                            String.valueOf(ta_offer[i][j]));
                }
                LOGGER.info("\n");
            }
            LOGGER.info("");
            */

            /*
            LOGGER.info("Students assigned:");
            LOGGER.info("-------------------------------");
            for (int i = 0; i < students.size(); i++) {
                for (int j = 0; j < offerings.size(); j++) {
                    if(stud_offer[i][j] > 0)
                        LOGGER.info("stud_offer["+ String.valueOf(students.get(i).getId()) +"]"+
                                    "["+ String.valueOf(offerings.get(j).getId()) + "]=" +
                                    String.valueOf(stud_offer[i][j]));
                }
                LOGGER.info("\n");
            }*/

            k = 0;
            for (Iterator<Preference> it = preferences.iterator(); it.hasNext();)
            {
                Preference preference = it.next();
                //if(prefArray[k] > 0)
                    LOGGER.info("prefG["+ String.valueOf(preference.getId()) +"] = " + prefArray[k] +
                                    "   stud["+ String.valueOf(preference.getStudent().getId()) +"]"+
                                    "["+ String.valueOf(preference.getOffering().getId()) + "]=" +
                                    String.valueOf(prefArray[k]));
                k++;
            }


            // Dispose of model and environment
            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Transactional
    private void updatePreferences(List<Preference> preferences, List<Student> students,
                                   List<Offering> offerings, double [][] stud_offer, CurrentSemester currSem){

        OptimizedTime timestamp = new OptimizedTime(currSem.getSemester());
        optimizedTimeRepository.save(timestamp);
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < offerings.size(); j++) {
                //if(stud_offer[i][j] > 0)
                {
                    /*
                    LOGGER.info("stud_offer["+ String.valueOf(students.get(i).getId()) +"]"+
                            "["+ String.valueOf(offerings.get(j).getId()) + "]=" +
                            String.valueOf(stud_offer[i][j]));
                    */
                    long student_id = students.get(i).getId();
                    long offering_id = offerings.get(j).getId();

                    // find corresponding to student and offering preference
                    for(Preference preference : preferences)
                    {
                        if(preference.getStudent().getId() == student_id &&
                           preference.getOffering().getId() == offering_id)
                        {
                            LOGGER.info(preference.toString());
                            if(stud_offer[i][j] > 0)
                            {
                                preference.setAssigned(true);
                                preference.setRecommend("You've being assigned");
                                preference.setOptimizedTime(timestamp);
                            }

                            else
                            {
                                preference.setAssigned(false);
                                preference.setRecommend("Didn't get in class");
                                preference.setOptimizedTime(timestamp);
                            }

                        }//if
                    }//for
                }//if
            }//for j
        }//for i
    }//updatePreferences()

    private void showLogs(List<Request> requests, Set<Preference> preferences, List<Student> students,
                          List<Offering> offerings, List<Professor> professors, List<Ta> tas){
        LOGGER.info("Offerings requested:");
        LOGGER.info("-------------------------------");
        for (Offering offering : offerings) {
            LOGGER.info(String.valueOf(offering.getId()));
        }
        LOGGER.info("");

        LOGGER.info("Last Requests requested:");
        LOGGER.info("-------------------------------");
        for (Request request : requests) {
            LOGGER.info(request.toString());
        }
        LOGGER.info("");

        LOGGER.info("!!!Preferences!!! requested:");
        LOGGER.info("-------------------------------");
        for (Preference preference : preferences) {
            LOGGER.info(preference.toString());
        }
        LOGGER.info("");

        LOGGER.info("Studs and Preferences:");
        LOGGER.info("-------------------------------");
        for (Student student : students) {
            LOGGER.info("stud["+String.valueOf(student.getId())+"] preferences:");
            /*for (Preference preference : student.getPreferences())
                LOGGER.info("priority = "+ String.valueOf(preference.getPriority())+
                        "  offering=" + String.valueOf(preference.getOffering().getId()));
            */LOGGER.info("\n");
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
    }//showLogs()


    /* Better way to schedule, but requires refactoring of all constraints
            for (Preference preference : preferences) {

                int i = (int)preference.getStudent().getId();
                int j = (int)preference.getOffering().getId();
                gvar_name = "OF_" + String.valueOf(offerings.get(j).getId()) + // offering
                        "_ST_" + String.valueOf(students.get(i).getId());  // student

                studentsOfferings[i][j] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
            }
    */

}// class
