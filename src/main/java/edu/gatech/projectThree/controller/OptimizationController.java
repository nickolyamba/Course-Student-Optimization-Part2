package edu.gatech.projectThree.controller;

import edu.gatech.projectThree.datamodel.entity.Offering;
import edu.gatech.projectThree.datamodel.entity.Professor;
import edu.gatech.projectThree.datamodel.entity.Semester;
import edu.gatech.projectThree.datamodel.entity.Ta;
import edu.gatech.projectThree.repository.CurrentSemesterRepository;
import edu.gatech.projectThree.repository.OfferingRepository;
import edu.gatech.projectThree.repository.ProfessorRepository;
import edu.gatech.projectThree.repository.TaRepository;
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

/**
 * Created by pjreed on 3/28/16.
 */
@Controller
public class OptimizationController {
    private CurrentSemesterRepository currentSemesterRepository;
    private TaRepository taRepository;
    private ProfessorRepository professorRepository;
    private OfferingRepository offeringRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizationController.class);

    @Autowired
    public void setCurrentSemesterRepository(CurrentSemesterRepository currentSemesterRepository) { this.currentSemesterRepository = currentSemesterRepository; }

    @Autowired
    public void setTaRepository(TaRepository taRepository) { this.taRepository = taRepository; }

    @Autowired
    public void setProfessorRepository(ProfessorRepository professorRepository) { this.professorRepository = professorRepository; }

    @Autowired
    public void setOfferingRepository(OfferingRepository offeringRepository) { this.offeringRepository = offeringRepository; }

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
        json.forEach((offeringId, stringArrayListMap) -> {
            Offering offering = offeringRepository.findOne(Long.valueOf(offeringId));
            stringArrayListMap.get("professors").forEach(profId -> {
                Professor professor = professorRepository.findOne(Long.valueOf(profId));
                professor.addOffering(offering);
                professorRepository.save(professor);
            });
            stringArrayListMap.get("tas").forEach(taId -> {
                Ta ta = taRepository.findOne(Long.valueOf(taId));
                ta.addOffering(offering);
                taRepository.save(ta);
            });
        });
        return "";
    }
}
