package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.datamodel.entity.*;
import edu.gatech.projectThree.repository.*;
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

        json.forEach((offeringId, stringArrayListMap) -> {
            try {
                Integer.parseInt(offeringId);
            } catch(NumberFormatException e) {
                // do stuff here because it isn't an offering Id but is a string
                System.out.println(offeringId);
                return;
            }
            Offering offering = offeringRepository.findOne(Long.valueOf(offeringId));
            stringArrayListMap.get("professors").forEach(profId -> {
                Professor professor = professorRepository.findOne(Long.valueOf(profId));
                professor.addOffering(offering, profRequest);
                professorRepository.save(professor);
            });
            stringArrayListMap.get("tas").forEach(taId -> {
                Ta ta = taRepository.findOne(Long.valueOf(taId));
                ta.addOffering(offering, taRequest);
                taRepository.save(ta);
            });
        });
        return "";
    }

    @RequestMapping(value = "/optimizations", method = RequestMethod.GET)
    public String optimizations(Model model) {
        CurrentSemester currSemester = currentSemesterRepository.findTopByOrderBySemesterIdDesc();
        // fetches all the Offerings...
        //List<Offering> allCurrentOfferings = offeringRepository.findBySemesterOrderByIdAsc(currSemester.getSemester());
        Set<Offering> allCurrentOfferings = currSemester.getSemester().getOfferings();

        OptimizedTime lastOptimized = optimizedTimeRepository.findTopByOrderByTimestampDesc();
        //LOGGER.info("!!!!!!OptimzedTime: " + lastOptimized.toString());
/*
        ArrayList<Preference> preferences = preferenceRepository.findLastOptimizedPreferencesByOffering();
        ArrayList<Offering> offerings = new ArrayList<Offering>();
        for (Preference preference : preferences) {
            offerings.add(preference.offering);
        }
*/

        ArrayList<PrintOffering> printOfferings = new ArrayList<PrintOffering>();

        for (Offering offering : allCurrentOfferings) {
            List<Student> students = new ArrayList<>();
            List<Ta> tas = new ArrayList<Ta>();
            List<Professor> profs = new ArrayList<Professor>();
            List<Demand> demand = new ArrayList<>();
            List<Preference> preferenceList = new ArrayList<>();
            // get Preferences
            Set<Preference> preferences = offering.getPreferences();
            //LOGGER.info("Offering: " + offering.getId());
            for(Preference preference :  preferences)
            {
                //LOGGER.info("Preference: " + preference.getId());
                /*
                if(preference.getOffering().getId() == 37)
                {
                    LOGGER.info("Preference: " + preference.getId());
                    LOGGER.info("Last: " + lastOptimized.toString());
                    LOGGER.info("Of37: " + preference.getOptimizedTime().toString());
                }*/

                if(preference.getOptimizedTime() == lastOptimized &&
                        preference.isAssigned())
                {
                    students.add(preference.getStudent());
                    preferenceList.add(preference);
                    //LOGGER.info("Preference: " + preference.getId());
                }

            }
            //LOGGER.info("\n");

            // get TAs
            Set<TaOffering> taOfferingPool = offering.getTaPool();
            for(TaOffering taOffering :  taOfferingPool)
            {
                if(taOffering.getOptimizedTime() == lastOptimized &&
                        taOffering.isAssigned())
                    tas.add(taOffering.getTa());
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
            //printOffering.setDemand(demand);

            //LOGGER.info(printOffering.toString());

        }//for

        model.addAttribute("currentOfferings", printOfferings);
        return "optimizations/index";
    }
}
