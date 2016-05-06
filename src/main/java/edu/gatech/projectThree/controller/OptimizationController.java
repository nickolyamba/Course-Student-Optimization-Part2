package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.repository.*;
import edu.gatech.projectThree.service.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by pjreed on 3/28/16.
 */
@Controller
public class OptimizationController {
    private CurrentSemesterRepository currentSemesterRepository;
    private TaRepository taRepository;
    private ProfessorRepository professorRepository;
    private OfferingRepository offeringRepository;
    private TaRequestRepository taRequestRepository;
    private ProfRequestRepository profRequestRepository;
    private PreferenceRepository preferenceRepository;
    private OptimizedTimeRepository optimizedTimeRepository;
    private GlobalState state;
    private Scheduler scheduler;
    private ConfigRepository configRepository;
    private DemandRepository demandRepository;
    private ProfessorOfferingRepository profOfferingRepository;
    private TaOfferingRepository taOfferingRepository;


    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizationController.class);

    @Autowired
    public void setCurrentSemesterRepository(CurrentSemesterRepository currentSemesterRepository) { this.currentSemesterRepository = currentSemesterRepository; }

    @Autowired
    public void setTaRepository(TaRepository taRepository) { this.taRepository = taRepository; }

    @Autowired
    public void setProfessorRepository(ProfessorRepository professorRepository) { this.professorRepository = professorRepository; }

    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) { this.offeringRepository = offeringRepository; }

    @Autowired
    public void setTaRequestRepository(TaRequestRepository taRequestRepository) {
        this.taRequestRepository = taRequestRepository;
    }

    @Autowired
    public void setProfRequestRepository(ProfRequestRepository profRequestRepository) {
        this.profRequestRepository = profRequestRepository;
    }

    @Autowired
    public void setPreferenceRepository(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Autowired
    public void setOptimizedTimeRepository(OptimizedTimeRepository optimizedTimeRepository) {
        this.optimizedTimeRepository = optimizedTimeRepository;
    }

    @Autowired
    public void setState(GlobalState state) {
        this.state = state;
    }

    @Autowired
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Autowired
    public void setConfigRepository(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Autowired
    public void setDemandRepository(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    @Autowired
    public void setProfOfferingRepository(ProfessorOfferingRepository profOfferingRepository) {
        this.profOfferingRepository = profOfferingRepository;
    }

    @Autowired
    public void setTaOfferingRepository(TaOfferingRepository taOfferingRepository) {
        this.taOfferingRepository = taOfferingRepository;
    }

    @RequestMapping(value = "/optimizations/new", method = RequestMethod.GET)
    public String newOptimization(Model model) {
        Semester semester = currentSemesterRepository.findTopByOrderBySemesterIdDesc().getSemester();
        ArrayList<Ta> tas = taRepository.findAll();
        ArrayList<Professor> professors = professorRepository.findAll();
        ArrayList<Offering> offerings = offeringRepository.findBySemesterOrderByIdAsc(semester);

        model.addAttribute("tas", tas);
        model.addAttribute("professors", professors);
        model.addAttribute("offerings", offerings);

        return "optimizations/new";
    }

    @RequestMapping(value = "/optimizations/new", method = RequestMethod.POST)
    @ResponseBody
    public String newOptimizationPost(@RequestBody Map<String, Map<String, ArrayList<String>>> json) {
        TaRequest taRequest = new TaRequest();
        taRequestRepository.save(taRequest);

        ProfRequest profRequest = new ProfRequest();
        profRequestRepository.save(profRequest);

        // ----------------- Optimization Configs ---------------------\\
        Config config = configRepository.findTopByOrderByIdDesc();
        ArrayList<String> configs = json.get("includeGpa").get("configs");
        //LOGGER.info(configs.toString());

        double taFactor = 0;
        int minTa = 1, maxTa = 50;
        //GPA
        if(configs.get(0).equals("true"))
            config.setGpa(true);
        else
            config.setGpa(false);

        //Seniority
        if(configs.get(1).equals("true"))
            config.setSeniority(true);
        else
            config.setSeniority(false);

        //TA factor
        try {
            taFactor = Double.parseDouble(configs.get(2));
        }
        catch(NumberFormatException e) {
            System.out.print(e.getMessage());
        }

        // minTA and maxTA
        try {
            minTa = Integer.parseInt(configs.get(3));
        }
        catch(NumberFormatException e) {
            System.out.print(e.getMessage());
        }

        try {
            maxTa = Integer.parseInt(configs.get(4));
        }
        catch(NumberFormatException e) {
            System.out.print(e.getMessage());
        }

        config.setTaCoeff(taFactor);
        config.setMinTa(minTa);
        config.setMaxTa(maxTa);
        //LOGGER.info(config.toString());
        //set the State
        state.setConfig(config);

        json.forEach((offeringId, stringArrayListMap) -> {
            try {
                Integer.parseInt(offeringId);
            } catch(NumberFormatException e) {
                // do stuff here because it isn't an offering Id but is a string
                //System.out.println(offeringId);
                return;
            }
            Offering offering = offeringRepository.findOne(Long.valueOf(offeringId));
            stringArrayListMap.get("professors").forEach(profId -> {
                Professor professor = professorRepository.findOne(Long.valueOf(profId));
                ProfessorOffering professorOffering = new ProfessorOffering(professor, offering, profRequest);

                offering.addProfOffering(professorOffering);
                professor.addProfessorPool(professorOffering);

                professorRepository.save(professor);
                profOfferingRepository.save(professorOffering);
                offeringRepository.save(offering);
            });
            stringArrayListMap.get("tas").forEach(taId -> {
                Ta ta = taRepository.findOne(Long.valueOf(taId));
                TaOffering taOffering = new TaOffering(ta, offering, taRequest);

                offering.addTaOffering(taOffering);
                ta.addTaOffering(taOffering);

                //ta.addOffering(offering, taRequest);
                taOfferingRepository.save(taOffering);
                taRepository.save(ta);
                offeringRepository.save(offering);
            });
        });

        scheduler.schedule(); // <---- uncomment!!!

        return "";
    }

    @RequestMapping(value = "/optimizations", method = RequestMethod.GET)
    public String optimizations(Model model) {
        CurrentSemester currSemester = currentSemesterRepository.findTopByOrderBySemesterIdDesc();
        List<Offering> allCurrentOfferings = offeringRepository.findBySemesterOrderByIdAsc(currSemester.getSemester()); //currSemester.getSemester().getOfferings();
        OptimizedTime lastOptimized = optimizedTimeRepository.findTopByOrderByTimestampDesc();

        ArrayList<PrintOffering> printOfferings = new ArrayList<PrintOffering>();

        double gpa=0.0;
        double seniority = 0;
        double studTaRatio;
        int studCounter = 0;
        int taCounter = 0;
        int priority = 0;
        double gpaPrior = 0;
        double seniorPrior = 0;

        for (Offering offering : allCurrentOfferings) {
            List<Student> students = new ArrayList<>();
            List<Ta> tas = new ArrayList<Ta>();
            List<Professor> profs = new ArrayList<Professor>();
            List<Preference> preferenceList = new ArrayList<>();
            Demand demand = new Demand(offering);
            //Map<String, Integer> demand = new HashMap<String, Integer>();
            int totalDemand = 0;
            // get Preferences


            Set<Preference> preferences = offering.getPreferences();
            demand.getDemandMap().put("total", preferences.size());
            //LOGGER.info("Offering: " + offering.getId());
            for(Preference preference :  preferences)
            {
                //LOGGER.info("Preference: " + preference.getId());
                if(preference.getOptimizedTime() == lastOptimized &&
                        preference.isAssigned())
                {
                    students.add(preference.getStudent());
                    preferenceList.add(preference);

                    gpa += preference.getStudent().getGpa();
                    seniority += preference.getStudent().getSeniority().ordinal();
                    studCounter++;
                    priority += preference.getPriority();
                    //LOGGER.info("Preference: " + preference.getId());
                }
            }
            gpaPrior += gpa*priority;
            seniorPrior += seniority*priority;
            //LOGGER.info("\n");

            // get TAs
            Set<TaOffering> taOfferingPool = offering.getTaPool();
            for(TaOffering taOffering :  taOfferingPool)
            {
                if(taOffering.getOptimizedTime() == lastOptimized &&
                        taOffering.isAssigned())
                {
                    tas.add(taOffering.getTa());
                    taCounter++;
                }
                    
            }


            // get Profs
            Set<ProfessorOffering> profOfferingPool = offering.getProfPool();
            for(ProfessorOffering profOffering :  profOfferingPool)
            {
                if(profOffering.getOptimizedTime() == lastOptimized &&
                        profOffering.isAssigned())
                    profs.add(profOffering.getProfessor());
            }

            PrintOffering printOffering = new PrintOffering();

            printOffering.setOffering(offering);
            printOffering.setTas(tas);
            printOffering.setProfessors(profs);
            printOffering.setStudents(students);
            printOffering.setCapacity(offering.getCapacity());
            printOffering.setPreferences(preferenceList);

            printOfferings.add(printOffering);
            printOffering.setDemand(demand);
            demandRepository.save(demand);
            //LOGGER.info(printOffering.toString());
        }//for

        gpa = gpaPrior/allCurrentOfferings.size();
        seniority = seniorPrior/allCurrentOfferings.size();
        studTaRatio = (double)studCounter/taCounter;

        model.addAttribute("currentOfferings", printOfferings);
        model.addAttribute("config", state.getConfig());
        /* Uncomment for testing
        model.addAttribute("gpa", gpa);
        model.addAttribute("seniority", seniority);
        model.addAttribute("studTaRatio", studTaRatio);
        */
        
        return "optimizations/index";
    }


    @RequestMapping(value = "/optimizations/renew", method = RequestMethod.POST)
    @ResponseBody
    public String renewOptimizationPost(@RequestBody Map<String, Map<String, ArrayList<String>>> json) {

        // ----------------- Optimization Configs ---------------------\\
        Config config = configRepository.findTopByOrderByIdDesc();
        ArrayList<String> configs = json.get("includeGpa").get("configs");
        //LOGGER.info(configs.toString());

        double taFactor = 0;
        int minTa = 1, maxTa = 50;
        //GPA
        if(configs.get(0).equals("true"))
            config.setGpa(true);
        else
            config.setGpa(false);

        //Seniority
        if(configs.get(1).equals("true"))
            config.setSeniority(true);
        else
            config.setSeniority(false);

        //TA factor
        try {
            taFactor = Double.parseDouble(configs.get(2));
        }
        catch(NumberFormatException e) {
            System.out.print(e.getMessage());
        }

        // minTA and maxTA
        try {
            minTa = Integer.parseInt(configs.get(3));
        }
        catch(NumberFormatException e) {
            System.out.print(e.getMessage());
        }

        try {
            maxTa = Integer.parseInt(configs.get(4));
        }
        catch(NumberFormatException e) {
            System.out.print(e.getMessage());
        }

        config.setTaCoeff(taFactor);
        config.setMinTa(minTa);
        config.setMaxTa(maxTa);
        //LOGGER.info(config.toString());
        //set the State
        state.setConfig(config);

        scheduler.schedule();

        return "";
    }
}
