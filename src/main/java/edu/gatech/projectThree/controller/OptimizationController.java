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

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
        Set<Offering> allCurrentOfferings = currSemester.getSemester().getOfferings();
        model.addAttribute("currentOfferings", allCurrentOfferings);
        return "optimizations/index";
    }
}
