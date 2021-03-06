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

import java.util.*;

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
    private PreferenceRepository preferenceRepository;
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
    public void setPreferenceRepository(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
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
            env.set(GRB.IntParam.LogToConsole, 0);
            GRBModel model = new GRBModel(env);

            // get current semester
            //CurrentSemester currSemester = currentSemesterRepository.findTopByOrderBySemesterIdDesc();
            CurrentSemester currSemester = state.getCurrentSemObject();
            //can't fetch from state - returns lazyException. when accessing Preferences in a offering object
            //List<Offering> offerings = new ArrayList<>(state.getOfferings());
            List<Offering> offerings = offeringRepository.findBySemesterOrderByIdAsc(currSemester.getSemester());
            List<Long> offeringIDs = new ArrayList<Long>();

            // offerings that has no preferences and populate offeringIDs
            for (Iterator<Offering> it = offerings.iterator(); it.hasNext();)
            {
                Offering offering = it.next();
                offeringIDs.add(offering.getId());
                //if(offering.getPreferences().isEmpty())
                //    it.remove();
            }

            List<Request> requests = requestRepository.findLastReqestsByStudent();

            // get association classes corresponding to Offerings
            List<Preference> preferences = prefRepository.findByOfferingInAndRequestInOrderByIdAsc(offerings, requests);
            List<ProfessorOffering> profOfferings = profOfferingRepository.findLastProfOfferings(offeringIDs);
            List<TaOffering> taOfferings = taOfferingRepository.findLastTaOfferings(offeringIDs);

            // get Studs, Profs, and Tas
            List<Student> students = studRepository.findDistinctByPreferencesInAndRequestsInOrderByIdAsc(preferences, requests);
            List<Professor> professors = profRepository.findDistinctByProfOfferingsInOrderByIdAsc(profOfferings);
            List<Ta> tas = taRepository.findDistinctByTaOfferingsInOrderByIdAsc(taOfferings);

            //showLogs(requests, preferences, students, offerings, professors, tas);

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
                //LOGGER.info("prefG[" + String.valueOf(preference.getId())+ "]");
                k++;
            }
            model.update();

            k = 0;
            for (TaOffering taOffering : taOfferings)
            {
                gvar_name = "TA_"+ taOffering.getId();

                taG[k] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                //LOGGER.info("taG[" + String.valueOf(taOffering.getId())+ "]");
                k++;
            }


            k = 0;
            for (ProfessorOffering profOffering : profOfferings)
            {
                gvar_name = "PROF_"+ profOffering.getId();// preference

                profG[k] = model.addVar(0, 1, 0.0, GRB.BINARY, gvar_name);
                //LOGGER.info("profG[" + String.valueOf(profOffering.getId())+ "]");
                k++;
            }

            model.update();

            // get all constraints
            Collection<Constraint> constraints = context.getBeansOfType(Constraint.class).values();

            for (Constraint constraint : constraints) {
                constraint.addConstraint(model, prefG, profG, taG,
                                        students, offerings, professors,
                                        tas, taOfferings, profOfferings,
                                                            preferences);
            }

            //model.update();//                              ------> comment out for production
            //model.write("constraints.lp"); // constraints  ------> comment out for production
            model.optimize();

            //model.computeIIS();
            //model.write("infeasible.ilp");

            //model.write("solution.sol"); // solution       ------> comment out for production

            double[] prefArray = model.get(GRB.DoubleAttr.X, prefG);
            double[] taOfferArray = model.get(GRB.DoubleAttr.X, taG);
            double[] profOfferArray = model.get(GRB.DoubleAttr.X, profG);
            //double objectiveValue = model.get(GRB.DoubleAttr.ObjVal);

            //------------------------------------- Save results in the database -----------------------------------\\
            postResultsToDb(preferences, taOfferings, profOfferings, prefArray, taOfferArray, profOfferArray, currSemester);
            //showResultLogs(preferences, taOfferings, profOfferings, prefArray, taOfferArray, profOfferArray);

            // Dispose of model and environment
            model.dispose();
            env.dispose();

        } catch (GRBException e) {
            e.printStackTrace();
            //e.
            LOGGER.info("Gurobi Exception: " + e.getErrorCode() + " " +
                    e.getMessage());
        }

        return result;
    }

    @Transactional
    private void postResultsToDb(List<Preference> preferences, List<TaOffering> taOfferings, List<ProfessorOffering> profOfferings,
                                 double [] prefArray, double[] taOfferArray, double[] profOfferArray, CurrentSemester currSem){

        OptimizedTime timestamp = new OptimizedTime(currSem.getSemester());
        optimizedTimeRepository.save(timestamp);

        //----------------------- Update Preferences ----------------------\\
        int k = 0;
        Student student;
        Set<Course> coursesTaken;
        Set<Course> prereqCourses;
        String issueMsg = "", recommendMsg = "";
        boolean isPrereqIssue = false;
        for (Preference preference : preferences)
        {
            //LOGGER.info(preference.toString());
            //
            if(prefArray[k] > 0)
            {
                coursesTaken = preference.getStudent().getCoursesTaken();
                prereqCourses = preference.getOffering().getCourse().getPrereqs();

                for(Course prereq : prereqCourses)
                {
                    if(!coursesTaken.contains(prereq))
                    {
                        issueMsg += "Prereq required: "+ "CS " +  prereq.getCourse_num() + " " + prereq.getCourse_name();
                        isPrereqIssue = true;
                    }
                    /*else
                    {
                        LOGGER.info("Stud" + preference.getStudent().getId() + " " + " Course: " +
                                preference.getOffering().getCourse().getId() + " " + "Prereq " + prereq.getId() + " satisfied");
                    }*/
                }
                recommendMsg = (isPrereqIssue) ? "You've being assigned, but " + issueMsg : "You've being assigned";
                //if(isPrereqIssue)
                //    LOGGER.info( "Course: " + preference.getOffering().getCourse().getId() + " " +  recommendMsg);
                preference.setAssigned(true);
                preference.setRecommend(recommendMsg);
                preference.setOptimizedTime(timestamp);
                issueMsg = "";
                recommendMsg = "";
                isPrereqIssue = false;

            }//if

            else
            {
                preference.setAssigned(false);
                preference.setRecommend("Didn't get in class");
                preference.setOptimizedTime(timestamp);
            }
            k++;

            preferenceRepository.save(preference);
            offeringRepository.save(preference.getOffering());

            //LOGGER.info(" Contains or not?"+String.valueOf(preference.getOffering().getPreferences().contains(preference)));
        }//for

        //----------------------- Update TaOfferings -------------------\\
        k = 0;
        for (TaOffering taOffering : taOfferings)
        {
            //LOGGER.info(preference.toString());
            if(taOfferArray[k] > 0)
            {
                taOffering.setAssigned(true);
                taOffering.setOptimizedTime(timestamp);
            }

            else
            {
                taOffering.setAssigned(false);
                taOffering.setOptimizedTime(timestamp);
            }
            k++;
            taOfferingRepository.save(taOffering);
            offeringRepository.save(taOffering.getOffering());
        }

        //----------------------- Update ProfessorOfferings -------------------\\
        k = 0;
        for (ProfessorOffering profOffering : profOfferings)
        {
            //LOGGER.info(preference.toString());
            if(profOfferArray[k] > 0)
            {
                profOffering.setAssigned(true);
                profOffering.setOptimizedTime(timestamp);
            }

            else
            {
                profOffering.setAssigned(false);
                profOffering.setOptimizedTime(timestamp);
            }
            k++;
            profOfferingRepository.save(profOffering);
            offeringRepository.save(profOffering.getOffering());
        }

    }//postResultsToDb()


    private void showLogs(List<Request> requests, List<Preference> preferences, List<Student> students,
                          List<Offering> offerings, List<Professor> professors, List<Ta> tas){
        LOGGER.info("Offerings requested:");
        LOGGER.info("-------------------------------");
        for (Offering offering : offerings) {
            LOGGER.info(String.valueOf(offering.getId()));

            LOGGER.info("Preferences:");
            for (Preference preference : offering.getPreferences())
                LOGGER.info(String.valueOf(preference.getId()));
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
            LOGGER.info(String.valueOf(preference.getId()));
        }
        LOGGER.info("");

        LOGGER.info("Studs:");
        LOGGER.info("-------------------------------");
        for (Student student : students) {
            LOGGER.info("stud["+String.valueOf(student.getId())+"]");
            /*for (Preference preference : student.getPreferences())
                LOGGER.info("priority = "+ String.valueOf(preference.getPriority())+
                        "  offering=" + String.valueOf(preference.getOffering().getId()));
            */
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

    private void showResultLogs(List<Preference> preferences, List<TaOffering> taOfferings, List<ProfessorOffering> professorOfferings,
                                 double[] prefArray, double[] taOfferArray, double[] profOfferArray){

        LOGGER.info("Students assigned:                      priority");
        LOGGER.info("------------------------------------------------");
        int k = 0;
        for (Preference preference : preferences)
        {
            if (prefArray[k] > 0)
                LOGGER.info("prefG["+ String.valueOf(preference.getId()) +"] = " + prefArray[k] +
                                    "\t\tstud["+ String.valueOf(preference.getStudent().getId()) +"]"+
                                    "["+ String.valueOf(preference.getOffering().getId()) + "]=" +
                                    String.valueOf(prefArray[k]) + "\t\t" + preference.getPriority());
            k++;
        }

        LOGGER.info("TAs assigned:");
        LOGGER.info("-------------------------------");
        k = 0;
        for (TaOffering taOffering : taOfferings)
        {
            if (taOfferArray[k] > 0)
                LOGGER.info("taOffer["+ String.valueOf(taOffering.getId()) +"] = " + taOfferArray[k] +
                        "\t\tta["+ String.valueOf(taOffering.getTa().getId()) +"]"+
                        "["+ String.valueOf(taOffering.getOffering().getId()) + "]=" +
                        String.valueOf(taOfferArray[k]));
            k++;
        }

        LOGGER.info("Professors assigned:");
        LOGGER.info("-------------------------------");
        k = 0;
        for (ProfessorOffering profOffering : professorOfferings)
        {
            if (profOfferArray[k] > 0)
                LOGGER.info("profOffer["+ String.valueOf(profOffering.getId()) +"] = " + profOfferArray[k] +
                        "\t\tprof["+ String.valueOf(profOffering.getProfessor().getId()) +"]"+
                        "["+ String.valueOf(profOffering.getOffering().getId()) + "]=" +
                        String.valueOf(profOfferArray[k]));
            k++;
        }
    }

}// class
